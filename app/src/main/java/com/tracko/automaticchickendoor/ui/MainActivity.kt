package com.tracko.automaticchickendoor.ui

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.amplifyframework.api.graphql.GraphQLRequest
import com.amplifyframework.api.graphql.model.ModelQuery
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
       getDevicesByEmail("kamrans124@gmail.com")
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
        Log.i("Amplify", "Querying devices for email: $email")
        
        // Use list() because get() only retrieves a single record by ID.
        Amplify.API.query(
            ModelQuery.list(
                DevicesTable::class.java,
                DevicesTable.EMAIL.eq(email) // <-- filter by Email field
            ),
            { response ->
                if (response.hasData()) {
                    val devices = response.data.items.toList()
                    if (devices.isEmpty()) {
                        Log.i("Amplify", "No devices found for $email")
                    } else {
                        devices.forEach {
                            Log.i(
                                "Amplify",
                                "Device found: ${it.deviceName} (${it.macAddress})"
                            )
                        }
                        Log.i("Amplify", "Total devices: ${devices.size}")
                    }
                } else {
                    Log.i("Amplify", "No data found for $email")
                }
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
