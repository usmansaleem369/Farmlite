package com.tracko.automaticchickendoor.ui

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.WindowCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.amplifyframework.core.Amplify
import com.tracko.automaticchickendoor.R
import com.tracko.automaticchickendoor.adapters.DevicesListAdapter
import com.tracko.automaticchickendoor.databinding.ActivityMainBinding
import com.tracko.automaticchickendoor.util.FarmLiteUtil
import com.tracko.automaticchickendoor.util.SharedPreferencesHelper
import com.tracko.automaticchickendoor.viewmodel.BleViewModel
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    @Inject
    lateinit var sharedPreferencesHelper: SharedPreferencesHelper
    private lateinit var binding: ActivityMainBinding
    private val bleViewModel: BleViewModel by viewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        
        if (FarmLiteUtil.hasBluetoothPermissions(this)) {
            FarmLiteUtil.checkAndEnableBluetooth(this)
            initUiAndListeners()
        } else {
            FarmLiteUtil.requestBluetoothPermissions(this)
        }
        //Not in initUiAndListeners function to match functionality
        binding.drawerItems.llLogout.setOnClickListener {
            signOut()
        }
        binding.drawerItems.llConnectToFarmliteDoor.setOnClickListener {
            val intent = Intent(this, ScanBLEActivity::class.java)
            startActivity(intent)
        }
        
        sharedPreferencesHelper.chickenDoorDevices?.let {
            binding.rvBluetooth.apply {
                layoutManager = LinearLayoutManager(this@MainActivity)
                adapter = DevicesListAdapter(it) { device ->
                    bleViewModel.setEspMacAddress(device.espMac)
                    bleViewModel.connectToBleDeviceByMacAddress(device.bleMac)
                    val intent = Intent(this@MainActivity, DeviceDetailActivity::class.java)
                    startActivity(intent)
                }
            }
        }
    }
    
    private fun initUiAndListeners() {
        binding.btnAddDevices.setOnClickListener {
            val intent = Intent(this, ScanBLEActivity::class.java)
            startActivity(intent)
        }
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
    
    private fun signOut() {
        Amplify.Auth.signOut { signOutResult ->
            sharedPreferencesHelper.isUserLogin = false
            val intent = Intent(this, SignInActivity::class.java)
            startActivity(intent)
            finish()
        }
    }
    
    override fun onStop() {
        super.onStop()
    }
    
    override fun onDestroy() {
        super.onDestroy()
    }
}
