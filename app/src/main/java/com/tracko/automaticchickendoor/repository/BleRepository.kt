package com.tracko.automaticchickendoor.repository

import android.annotation.SuppressLint
import android.bluetooth.*
import android.bluetooth.le.*
import android.content.Context
import android.os.Handler
import android.os.Looper
import android.os.ParcelUuid
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.tracko.automaticchickendoor.models.local.DoorCommandResultLocal
import com.tracko.automaticchickendoor.util.DoorCommandMode
import com.tracko.automaticchickendoor.util.DOOR_UUID
import com.tracko.automaticchickendoor.util.MAC_UUID
import com.tracko.automaticchickendoor.util.MODE_UUID
import com.tracko.automaticchickendoor.util.SERVICE_UUID
import com.tracko.automaticchickendoor.util.WIFI_UUID
import javax.inject.Inject
import javax.inject.Singleton

@SuppressLint("MissingPermission")
@Singleton
class BleRepository @Inject constructor(private val context: Context) {
    
    private val bluetoothAdapter: BluetoothAdapter? by lazy {
        val bluetoothManager =
            context.getSystemService(Context.BLUETOOTH_SERVICE) as BluetoothManager
        bluetoothManager.adapter
    }
    var connectedBleDevice: BluetoothDevice? = null
    private val _devicesList = MutableLiveData<List<BluetoothDevice>>()
    val devicesList: LiveData<List<BluetoothDevice>> get() = _devicesList

    private val _scanResults = mutableSetOf<BluetoothDevice>()
    private val handler = Handler(Looper.getMainLooper())

    private var bleScanner: BluetoothLeScanner? = null
    private var bluetoothGatt: BluetoothGatt? = null

    // Store the SSID and password for later use
    private var ssid: String? = null
    private var password: String? = null
    
    private val _credentialsStatus = MutableLiveData<Pair<Boolean, BluetoothDevice?>>()
    val credentialsStatus: LiveData<Pair<Boolean, BluetoothDevice?>> get() = _credentialsStatus
    
    private val _isDeviceConnected = MutableLiveData<Boolean>()
    val isDeviceConnected : LiveData<Boolean> get() = _isDeviceConnected
    
    private val _readMacAddress = MutableLiveData<String?>()
    val readMacAddress: LiveData<String?> get() = _readMacAddress
    
    var doorCommand : String = ""
    private val _isDoorCommandSentByBle = MutableLiveData<DoorCommandResultLocal>()
    val isDoorCommandSentByBle: LiveData<DoorCommandResultLocal> get() = _isDoorCommandSentByBle
    
    private val _isModeSetByBle = MutableLiveData<Boolean>()
    val isModeSetByBle: LiveData<Boolean> get() = _isModeSetByBle
    
    
    init {
        if (bluetoothAdapter == null /*|| !bluetoothAdapter.isEnabled*/) {
            Log.e("BLE", "Bluetooth is not available or not enabled")
        }
    }

    @SuppressLint("MissingPermission")
    fun startScanning() {
        bleScanner = bluetoothAdapter?.bluetoothLeScanner
        _scanResults.clear()
       /* val serviceFilter = ScanFilter.Builder()
            .setServiceUuid(ParcelUuid(SERVICE_UUID))
            .build()*/
        val scanSettings = ScanSettings.Builder()
            .setScanMode(ScanSettings.SCAN_MODE_LOW_LATENCY)
            .build()
        bleScanner?.startScan(listOf(/*serviceFilter*/), scanSettings, scanCallback)
        handler.postDelayed({ stopScanning() }, 60000)
    }

    @SuppressLint("MissingPermission")
    fun stopScanning() {
        bleScanner?.stopScan(scanCallback)
    }
    
    private val scanCallback = object : ScanCallback() {
        @SuppressLint("MissingPermission")
        override fun onScanResult(callbackType: Int, result: ScanResult) {
            val device = result.device
            Log.e("Discovered", device.address.toString())
            if (_scanResults.add(device)) {
                _devicesList.postValue(_scanResults.toList())
            }
        }

        override fun onScanFailed(errorCode: Int) {
            super.onScanFailed(errorCode)
            Log.e("BLE", "Scan failed with error: $errorCode")
        }
    }

    @SuppressLint("MissingPermission")
    fun connectAndSetCredentials(device: BluetoothDevice, ssid: String, password: String) {
        this.ssid = ssid
        this.password = password
        connectedBleDevice = device
        bluetoothGatt = device.connectGatt(context, false, gattCallback)
    }
    
    @SuppressLint("MissingPermission")
    fun connectToDevice(device: BluetoothDevice) {
        this.ssid = null
        this.password = null
        connectedBleDevice = device
        bluetoothGatt = device.connectGatt(context, false, gattCallback)
    }

    private val gattCallback = object : BluetoothGattCallback() {
        
        override fun onConnectionStateChange(gatt: BluetoothGatt?, status: Int, newState: Int) {
            if (newState == BluetoothProfile.STATE_CONNECTED) {
                Log.d("BLE", "Connected to GATT server")
                gatt?.discoverServices()
            } else if (newState == BluetoothProfile.STATE_DISCONNECTED) {
                Log.d("BLE", "Disconnected from GATT server")
                gatt?.device?.connectGatt(context, false, this)
                Log.d("BLE", "Attempting to reconnect...")
            }
        }
        
        override fun onServicesDiscovered(gatt: BluetoothGatt, status: Int) {
            if (status == BluetoothGatt.GATT_SUCCESS) {
                val ssid = this@BleRepository.ssid
                val password = this@BleRepository.password
                _isDeviceConnected.postValue(true)
                if (ssid != null && password != null) {
                    sendWifiCredentials(gatt, ssid, password)
                } else {
                    Log.e("BLE", "SSID or password is null")
                }
            } else {
                Log.e("BLE", "Service discovery failed")
            }
        }
        
        override fun onCharacteristicWrite(
            gatt: BluetoothGatt?, characteristic: BluetoothGattCharacteristic, status: Int
        ) {
            if (status == BluetoothGatt.GATT_SUCCESS) {
                when (characteristic.uuid) {
                    WIFI_UUID -> {
                        _credentialsStatus.postValue(Pair(true,connectedBleDevice))
                    }
                    DOOR_UUID ->{
                        _isDoorCommandSentByBle.postValue(DoorCommandResultLocal(true,doorCommand))
                    }
                    MODE_UUID->{
                        _isModeSetByBle.postValue(true)
                    }
                }
            } else {
                when (characteristic.uuid) {
                    WIFI_UUID -> {
                        _credentialsStatus.postValue(Pair(false,null))
                    }
                    DOOR_UUID ->{
                        _isDoorCommandSentByBle.postValue(DoorCommandResultLocal(false,doorCommand))
                    }
                    MODE_UUID->{
                        _isModeSetByBle.postValue(false)
                    }
                }
            }
        }
        
        override fun onCharacteristicRead(
            gatt: BluetoothGatt,
            characteristic: BluetoothGattCharacteristic,
            status: Int
        ) {
            if (status == BluetoothGatt.GATT_SUCCESS) {
                when (characteristic.uuid) {
                    MAC_UUID -> {
                        val macBytes = characteristic.value
                        val macString = macBytes?.toString(Charsets.UTF_8) ?: ""
                        _readMacAddress.postValue(macString)
                    }
                }
            } else {
                Log.e("BLE", "Characteristic read failed with status: $status")
            }
        }
    }

    @SuppressLint("MissingPermission")
    fun sendWifiCredentials(gatt: BluetoothGatt?, ssid: String, password: String) {
        val service = gatt?.getService(SERVICE_UUID)
        if (service != null) {
            val characteristic = service.getCharacteristic(WIFI_UUID)
            if (characteristic != null) {
                val wifiData = "$ssid;$password".toByteArray(/*Charsets.UTF_8*/)
                Log.d("BLE", "Data length: ${wifiData.size}")
                characteristic.value = wifiData
                characteristic.writeType = BluetoothGattCharacteristic.WRITE_TYPE_NO_RESPONSE
                gatt.writeCharacteristic(characteristic)
            } else {
                _credentialsStatus.postValue(Pair(false,null))
            }
        } else {
            _credentialsStatus.postValue(Pair(false,null))
        }
    }
    
    @SuppressLint("MissingPermission")
    fun sendBleModeCommand(mode: DoorCommandMode, openTime: String, closeTime: String) {
        val service = bluetoothGatt?.getService(SERVICE_UUID)
        if (service != null) {
            val modeCharacteristic = service.getCharacteristic(MODE_UUID)
            if (modeCharacteristic != null) {
                val modeData = mode.formatCommand(openTime, closeTime).toByteArray(Charsets.UTF_8)
                modeCharacteristic.value = modeData
                modeCharacteristic.writeType = BluetoothGattCharacteristic.WRITE_TYPE_NO_RESPONSE
                bluetoothGatt?.writeCharacteristic(modeCharacteristic)
            } else {
                _isModeSetByBle.postValue(false)
                Log.e("BLE", "Mode characteristic not found with UUID: $MODE_UUID")
            }
        } else {
            _isModeSetByBle.postValue(false)
            Log.e("BLE", "Service with UUID $SERVICE_UUID not found")
        }
    }
    
    @SuppressLint("MissingPermission")
    fun sendDoorCommand(command: String) {
        doorCommand = command
        val service = bluetoothGatt?.getService(SERVICE_UUID)
        if (service != null) {
            val doorCharacteristic = service.getCharacteristic(DOOR_UUID)
            if (doorCharacteristic != null) {
                val commandData = command.toByteArray()
                doorCharacteristic.value = commandData
                doorCharacteristic.writeType = BluetoothGattCharacteristic.WRITE_TYPE_NO_RESPONSE
                bluetoothGatt?.writeCharacteristic(doorCharacteristic)

            } else {
                _isDoorCommandSentByBle.postValue(DoorCommandResultLocal(false,doorCommand))
                Log.e("BLE", "Door characteristic not found with UUID: $DOOR_UUID")
            }
        } else {
            _isDoorCommandSentByBle.postValue(DoorCommandResultLocal(false,doorCommand))
            Log.e("BLE", "Service with UUID $SERVICE_UUID not found")
        }
    }
    
    @SuppressLint("MissingPermission")
    fun connectToBleDeviceByMacAddress(macAddress: String) {
        val device = bluetoothAdapter?.getRemoteDevice(macAddress)
        if (device != null) {
            connectedBleDevice = device
            bluetoothGatt = device.connectGatt(context, false, gattCallback)
        } else {
            Log.e("BLE", "Device not found with MAC address: $macAddress")
        }
    }
    @SuppressLint("MissingPermission")
    fun readMacAddress() {
        val service = bluetoothGatt?.getService(SERVICE_UUID)
        if (service != null) {
            val macCharacteristic = service.getCharacteristic(MAC_UUID)
            if (macCharacteristic != null) {
                bluetoothGatt?.readCharacteristic(macCharacteristic) == true
            } else {
                Log.e("BLE", "MAC characteristic not found with UUID: $MAC_UUID")
            }
        } else {
            Log.e("BLE", "Service with UUID $SERVICE_UUID not found")
            _readMacAddress.postValue(null)
        }
    }
    
    fun setEspDeviceMac(mac: String){
        _readMacAddress.postValue(mac)
    }
    
    // Close the GATT connection when done
    fun closeConnection() {
        bluetoothGatt?.close()
    }
}
