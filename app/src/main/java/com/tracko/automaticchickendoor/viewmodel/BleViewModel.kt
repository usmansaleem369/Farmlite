package com.tracko.automaticchickendoor.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import android.bluetooth.BluetoothDevice
import android.util.Log
import com.tracko.automaticchickendoor.repository.BleRepository
import com.tracko.automaticchickendoor.util.DoorCommandMode
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class BleViewModel @Inject constructor(
    private val bleRepository: BleRepository
) : ViewModel() {

    val devicesList: LiveData<List<BluetoothDevice>> = bleRepository.devicesList
    val credentialsSet = bleRepository.credentialsStatus
    val readMacAddress = bleRepository.readMacAddress
    val isDoorCommandSentByBle = bleRepository.isDoorCommandSentByBle
    val isModeSetByBle = bleRepository.isModeSetByBle
    fun startScanning() {
        bleRepository.startScanning()

    }

    fun stopScanning() {
        bleRepository.stopScanning()
    }

    fun connectToDevice(device: BluetoothDevice, ssid: String, password: String) {
        bleRepository.connectToDevice(device, ssid, password)
    }

    fun writeWifiCredentials(ssid: String, password: String) {
        //  repository.sendWifiCredentials(ssid,password)
    }

    fun sendDoorCommand(command: String){
        bleRepository.sendDoorCommand(command)
    }
    fun sendBleModeCommand(mode: DoorCommandMode, openTime: String, closeTime: String){
        Log.e("Mode In VM",openTime.toString())
        Log.e("Mode In VM",closeTime.toString())
        bleRepository.sendBleModeCommand(mode,openTime,closeTime)
    }
    fun readMacAddress(){
        bleRepository.readMacAddress()
    }
    
    fun connectToBleDeviceByMacAddress(macAddress: String) {
        bleRepository.connectToBleDeviceByMacAddress(macAddress)
    }
    
    fun setEspMacAddress(macAddress: String) {
           bleRepository.setEspDeviceMac(macAddress)
    }
}
