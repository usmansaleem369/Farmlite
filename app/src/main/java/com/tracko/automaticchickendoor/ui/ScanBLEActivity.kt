package com.tracko.automaticchickendoor.ui

import android.bluetooth.BluetoothDevice
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.amplifyframework.core.Amplify
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.tracko.automaticchickendoor.R
import com.tracko.automaticchickendoor.adapters.BleDeviceAdapter
import com.tracko.automaticchickendoor.databinding.ActivityMainBinding
import com.tracko.automaticchickendoor.databinding.ActivityScanBleactivityBinding
import com.tracko.automaticchickendoor.databinding.WifiCredentialsPopupBinding
import com.tracko.automaticchickendoor.ui.MainActivity
import com.tracko.automaticchickendoor.util.FarmLiteUtil
import com.tracko.automaticchickendoor.util.SharedPreferencesHelper
import com.tracko.automaticchickendoor.viewmodel.BleViewModel
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject
import kotlin.getValue

@AndroidEntryPoint
class ScanBLEActivity : AppCompatActivity() {
    @Inject
    lateinit var sharedPreferencesHelper: SharedPreferencesHelper
    private lateinit var binding: ActivityScanBleactivityBinding
    private val bleViewModel: BleViewModel by viewModels()
    private lateinit var wifiCredentialDialog : AlertDialog
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        
        binding = ActivityScanBleactivityBinding.inflate(layoutInflater)
        setContentView(binding.root)
        
        if (FarmLiteUtil.hasBluetoothPermissions(this)) {
            FarmLiteUtil.checkAndEnableBluetooth(this)
            initUiAndListeners()
        } else {
            FarmLiteUtil.requestBluetoothPermissions(this)
        }
        
      
    }
    
    private fun initUiAndListeners() {
        binding.lottieAnimationView.playAnimation()
        bleViewModel.startScanning()
        bleViewModel.devicesList.observe(this) { devices ->
            binding.lottieAnimationView.cancelAnimation()
            binding.lottieAnimationView.visibility = View.GONE
            if (devices.isEmpty()) {
                Toast.makeText(this, "No devices found", Toast.LENGTH_SHORT).show()
            } else {
                Log.e("Device found", devices.toString())
                binding.rvBluetooth.apply {
                    layoutManager = LinearLayoutManager(this@ScanBLEActivity)
                    adapter = BleDeviceAdapter(devices) { device ->
                        showCustomDialog(device)
                    }
                }
            }
        }
        
        
        
        bleViewModel.credentialsSet.observe(this){
            if (::wifiCredentialDialog.isInitialized) {
                wifiCredentialDialog.dismiss()
                if (it.first) {
                    val intent = Intent(this,DeviceDetailActivity::class.java)
                    startActivity(intent)
                    finish()
                }else {
                    Toast.makeText(this,"Not able to connect to device",Toast.LENGTH_SHORT).show()
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
    
    override fun onStop() {
        super.onStop()
       // bleViewModel.stopScanning()
    }
    
    override fun onDestroy() {
        super.onDestroy()
      //  bleViewModel.stopScanning()
    }
}
