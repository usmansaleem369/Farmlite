package com.tracko.automaticchickendoor.ui

import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.ImageDecoder
import android.graphics.drawable.AnimatedImageDrawable
import android.graphics.drawable.AnimationDrawable
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.tracko.automaticchickendoor.R
import com.tracko.automaticchickendoor.databinding.ActivityDeviceDetailBinding
import com.tracko.automaticchickendoor.models.local.DeviceMacInfo
import com.tracko.automaticchickendoor.models.request.DoorOpenCloseRequest
import com.tracko.automaticchickendoor.util.SharedPreferencesHelper
import com.tracko.automaticchickendoor.viewmodel.BleViewModel
import com.tracko.automaticchickendoor.viewmodel.UserViewModel
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class DeviceDetailActivity : AppCompatActivity() {
    @Inject
    lateinit var sharedPreferencesHelper: SharedPreferencesHelper
    private val bleViewModel: BleViewModel by viewModels()
    private val userViewModel: UserViewModel by viewModels()
    private lateinit var binding: ActivityDeviceDetailBinding
    private var animationDrawable: AnimationDrawable? = null
    private var animatedImageDrawable: AnimatedImageDrawable? = null
    private var espMac: String = ""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDeviceDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initUiAndListeners()
        observeViewModel()
        bleViewModel.readMacAddress()
    }
    
    private fun initUiAndListeners() {
        binding.tvDeviceStatus.apply {
            text = "Online"
            background = ContextCompat.getDrawable(this@DeviceDetailActivity,R.drawable.textview_filled_online_rounded_bg)
        }
        if (sharedPreferencesHelper.isOpen) {
            binding.tvCurrentStatus.text = getString(R.string.open)
            startDoorAnimation(R.drawable.door_animation_open)
        } else {
            binding.tvCurrentStatus.text = getString(R.string.close)
            startDoorAnimation(R.drawable.door_animation_close)
        }
        
        binding.ivBackBtn.setOnClickListener {
            finish()
        }
        
        
        binding.btnSetSchedule.setOnClickListener {
            val intent = Intent(this@DeviceDetailActivity, SetScheduleActivity::class.java)
            startActivity(intent)
        }
        
        binding.btnOpenClose.setOnClickListener {
            if (sharedPreferencesHelper.isOpen) {
                userViewModel.doorOpenCloseApi(DoorOpenCloseRequest(espMac,"close"))
            } else {
                userViewModel.doorOpenCloseApi(DoorOpenCloseRequest(espMac,"open"))
            }
        }
        
    }
    
    @SuppressLint("MissingPermission")
    private fun observeViewModel(){
        userViewModel.isDoorCommandSentByApi.observe(this) {
            if (it.isSuccess){
                when(it.message){
                     "close"->{
                         binding.tvCurrentStatus.text = getString(R.string.close)
                         startDoorAnimation(R.drawable.door_animation_close)
                         sharedPreferencesHelper.isOpen = false
                     }
                    "open"->{
                        binding.tvCurrentStatus.text = getString(R.string.open)
                        startDoorAnimation(R.drawable.door_animation_open)
                        sharedPreferencesHelper.isOpen = true
                    }
                }
            } else {
                bleViewModel.sendDoorCommand(it.message)
                Toast.makeText(this@DeviceDetailActivity,"No internet connection. Trying with BLE", Toast.LENGTH_SHORT).show()
            }
        }
        
        bleViewModel.isDoorCommandSentByBle.observe(this) {
            if (it.isSuccess){
                when(it.message){
                    "close"->{
                        binding.tvCurrentStatus.text = getString(R.string.close)
                        startDoorAnimation(R.drawable.door_animation_close)
                        sharedPreferencesHelper.isOpen = false
                    }
                    "open"->{
                        binding.tvCurrentStatus.text = getString(R.string.open)
                        startDoorAnimation(R.drawable.door_animation_open)
                        sharedPreferencesHelper.isOpen = true
                    }
                }
            }  else {
                Toast.makeText(this@DeviceDetailActivity,"Device not connected. Please connect via BLE and try again", Toast.LENGTH_SHORT).show()
            }
        }
        bleViewModel.readMacAddress.observe(this) {
            it?.let {mac->
                espMac = mac
                val bleMac = bleViewModel.credentialsSet.value?.second?.address?:""
                val bleName = bleViewModel.credentialsSet.value?.second?.name?:""
                val device = DeviceMacInfo(name = bleName, bleMac = bleMac, espMac = espMac)
                addChickenDoorDevice(device)
            }
        }
    }
    
    fun addChickenDoorDevice(newDevice: DeviceMacInfo) {
        val currentList = sharedPreferencesHelper.chickenDoorDevices?.toMutableList() ?: mutableListOf()
        
        // Check if device already exists (by BLE MAC or ESP MAC)
        val exists = currentList.any {
            it.bleMac.equals(newDevice.bleMac, ignoreCase = true) ||
                    it.espMac.equals(newDevice.espMac, ignoreCase = true)
        }
        
        if (!exists) {
            currentList.add(newDevice)
            sharedPreferencesHelper.chickenDoorDevices = currentList
            Log.d("SharedPreferences", "New device added: $newDevice")
        } else {
            Log.d("SharedPreferences", "Device already exists: $newDevice")
        }
    }
    
    private fun startDoorAnimation(drawable: Int) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            val source = ImageDecoder.createSource(resources, drawable)
            animatedImageDrawable = ImageDecoder.decodeDrawable(source) as? AnimatedImageDrawable
            binding.ivDoor.setImageDrawable(animatedImageDrawable)
            animatedImageDrawable?.start()
        } else {
            animationDrawable = resources.getDrawable(drawable, null) as? AnimationDrawable
            binding.ivDoor.setImageDrawable(animationDrawable)
            animationDrawable?.start() // ✅ correct drawable here
        }
    }
}
