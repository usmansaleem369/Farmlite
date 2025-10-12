package com.tracko.automaticchickendoor.ui

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.amplifyframework.core.Amplify
import com.amplifyframework.datastore.generated.model.DevicesTable
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
     
        initUiAndListeners()
        //Not in initUiAndListeners function to match functionality
       getDevicesByEmail("usman.saleem369@gmail.com")
       /* sharedPreferencesHelper.chickenDoorDevices?.let {
            binding.rvBluetooth.apply {
                layoutManager = LinearLayoutManager(this@MainActivity)
                adapter = DevicesListAdapter(it) { device ->
                    bleViewModel.setEspMacAddress(device.espMac)
                    bleViewModel.connectToBleDeviceByMacAddress(device.bleMac)
                    val intent = Intent(this@MainActivity, DeviceDetailActivity::class.java)
                    startActivity(intent)
                }
            }
        }*/
    }
    
    fun getDevicesByEmail(email: String) {
        Amplify.DataStore.query(
            DevicesTable::class.java,
            DevicesTable.EMAIL.eq(email),
            { matches ->
                val deviceList = mutableListOf<DevicesTable>()
                while (matches.hasNext()) {
                    val device = matches.next()
                    deviceList.add(device)
                    Log.i("Amplify", "Device found: ${device.deviceName} (${device.macAddress})")
                }
                
                if (deviceList.isEmpty()) {
                    Log.i("Amplify", "No devices found for email: $email")
                } else {
                    Log.i("Amplify", "Found ${deviceList.size} devices for $email")
                }
                
                // You can now post deviceList to LiveData, RecyclerView, etc.
            },
            { error ->
                Log.e("Amplify", "Query failed", error)
            }
        )
    }
    
    private fun initUiAndListeners() {
        binding.homeContent.ivProfile.setOnClickListener {
            Log.e("Button","Clicked")
            if (binding.motionLayout.progress == 0f) {
                binding.motionLayout.transitionToEnd()   // open menu
            } else {
                binding.motionLayout.transitionToStart() // close menu
            }
        }
        
        binding.mainContent.setOnClickListener {
        
        }
        
        binding.homeContent.cvConnectToFarmlite.setOnClickListener {
            val intent = Intent(this,ScanBLEActivity::class.java)
            startActivity(intent)
        }
        
        binding.sideMenuContent.apply {
            btnEditProfile.setOnClickListener {
                Toast.makeText(this@MainActivity, "Edit Profile clicked", Toast.LENGTH_SHORT).show()
            }
            
            btnChangePassword.setOnClickListener {
                Toast.makeText(this@MainActivity, "Change Password clicked", Toast.LENGTH_SHORT).show()
            }
            
            btnNotificationSettings.setOnClickListener {
                Toast.makeText(this@MainActivity, "Notification Settings clicked", Toast.LENGTH_SHORT).show()
            }
            
            btnContactSupport.setOnClickListener {
                Toast.makeText(this@MainActivity, "Contact Support clicked", Toast.LENGTH_SHORT).show()
            }
            
            btnDeleteAccount.setOnClickListener {
                Toast.makeText(this@MainActivity, "Delete Account clicked", Toast.LENGTH_SHORT).show()
            }
            
            btnPrivacyPolicy.setOnClickListener {
                Toast.makeText(this@MainActivity, "Privacy & Policy clicked", Toast.LENGTH_SHORT).show()
            }
            
            btnTermsConditions.setOnClickListener {
                Toast.makeText(this@MainActivity, "Terms & Conditions clicked", Toast.LENGTH_SHORT).show()
            }
            
            btnAppSettings.setOnClickListener {
                Toast.makeText(this@MainActivity, "App Settings clicked", Toast.LENGTH_SHORT).show()
            }
            
            btnLogout.setOnClickListener {
                signOut()
                Toast.makeText(this@MainActivity, "Logged out", Toast.LENGTH_SHORT).show()
            }
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
