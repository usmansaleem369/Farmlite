package com.tracko.automaticchickendoor.ui

import android.app.Activity
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothDevice
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.tracko.automaticchickendoor.adapters.BleDeviceAdapter
import com.tracko.automaticchickendoor.databinding.ActivityScanBleactivityBinding
import com.tracko.automaticchickendoor.databinding.WifiCredentialsPopupBinding
import com.tracko.automaticchickendoor.util.FarmLiteUtil
import com.tracko.automaticchickendoor.util.SharedPreferencesHelper
import com.tracko.automaticchickendoor.viewmodel.BleViewModel
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class ScanBLEActivity : AppCompatActivity() {
    
    @Inject
    lateinit var sharedPreferencesHelper: SharedPreferencesHelper
    private lateinit var binding: ActivityScanBleactivityBinding
    private val bleViewModel: BleViewModel by viewModels()
    private lateinit var wifiCredentialDialog: AlertDialog
    private val bluetoothAdapter: BluetoothAdapter? = BluetoothAdapter.getDefaultAdapter()
    
    private val enableBluetoothLauncher = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            Toast.makeText(this, "Bluetooth enabled", Toast.LENGTH_SHORT).show()
            binding.switchBluetooth.isChecked = true
            bleViewModel.startScanning()
        } else {
            Toast.makeText(this, "Bluetooth not enabled", Toast.LENGTH_SHORT).show()
            binding.switchBluetooth.isChecked = false
        }
    }
    
    private val bluetoothReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            if (intent?.action == BluetoothAdapter.ACTION_STATE_CHANGED) {
                when (intent.getIntExtra(BluetoothAdapter.EXTRA_STATE, BluetoothAdapter.ERROR)) {
                    BluetoothAdapter.STATE_OFF -> binding.switchBluetooth.isChecked = false
                    BluetoothAdapter.STATE_ON -> binding.switchBluetooth.isChecked = true
                }
            }
        }
    }
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityScanBleactivityBinding.inflate(layoutInflater)
        setContentView(binding.root)
        
        if (FarmLiteUtil.hasBluetoothPermissions(this)) {
           // FarmLiteUtil.checkAndEnableBluetooth(this)
        } else {
            FarmLiteUtil.requestBluetoothPermissions(this)
        }
        
        initUiAndListeners()
    }
    
    private fun initUiAndListeners() {
        // --- Bluetooth switch handling ---
        binding.switchBluetooth.isChecked = bluetoothAdapter?.isEnabled == true
        
        binding.switchBluetooth.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                if (bluetoothAdapter?.isEnabled == false) {
                    val enableBtIntent = Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE)
                    enableBluetoothLauncher.launch(enableBtIntent)
                }
            } else {
                bluetoothAdapter?.disable()
                bleViewModel.stopScanning()
            }
        }
        
        // --- Existing BLE logic ---
        bleViewModel.startScanning()
        
        bleViewModel.devicesList.observe(this) { devices ->
            if (devices.isEmpty()) {
                Toast.makeText(this, "No devices found", Toast.LENGTH_SHORT).show()
            } else {
                Log.e("Device found", devices.toString())
                binding.rvBleDevices.apply {
                    layoutManager = LinearLayoutManager(this@ScanBLEActivity)
                    adapter = BleDeviceAdapter(devices) { device ->
                        showCustomDialog(device)
                    }
                }
            }
        }
        
        bleViewModel.credentialsSet.observe(this) {
            if (::wifiCredentialDialog.isInitialized) {
                wifiCredentialDialog.dismiss()
                if (it.first) {
                    val intent = Intent(this, DeviceDetailActivity::class.java)
                    startActivity(intent)
                    finish()
                } else {
                    Toast.makeText(
                        this,
                        "Not able to connect to device",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        }
    }
    
    private fun showCustomDialog(device: BluetoothDevice) {
        val dialogViewBinding = WifiCredentialsPopupBinding.inflate(layoutInflater)
        
        wifiCredentialDialog = MaterialAlertDialogBuilder(this)
            .setView(dialogViewBinding.root)
            .setCancelable(true)
            .create()
        
        dialogViewBinding.btnSetCredentials.setOnClickListener {
            val ssid = dialogViewBinding.etSsid.text.toString()
            val password = dialogViewBinding.etPassword.text.toString()
            if (ssid.isEmpty()) {
                dialogViewBinding.ssidTextField.error = "Enter wifi ssid"
            } else if (password.isEmpty()) {
                dialogViewBinding.passwordTextField.error = "Enter wifi password"
            } else {
                bleViewModel.connectToDevice(device, ssid, password)
            }
        }
        wifiCredentialDialog.show()
    }
    
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        
        FarmLiteUtil.onRequestPermissionsResult(
            requestCode, grantResults,
            onBluetoothPermissionGranted = {
                initUiAndListeners()
            },
            onBluetoothPermissionDenied = {
                Toast.makeText(
                    this,
                    "Bluetooth permissions denied. Cannot scan devices.",
                    Toast.LENGTH_LONG
                ).show()
            }
        )
    }
    
    override fun onResume() {
        super.onResume()
        binding.switchBluetooth.isChecked = bluetoothAdapter?.isEnabled == true
        registerReceiver(bluetoothReceiver, IntentFilter(BluetoothAdapter.ACTION_STATE_CHANGED))
    }
    
    override fun onPause() {
        super.onPause()
        unregisterReceiver(bluetoothReceiver)
    }
    
    override fun onStop() {
        super.onStop()
        // bleViewModel.stopScanning()
    }
    
    override fun onDestroy() {
        super.onDestroy()
        // bleViewModel.stopScanning()
    }
}
