package com.tracko.automaticchickendoor.ui

import android.annotation.SuppressLint
import android.app.TimePickerDialog
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.SeekBar
import android.widget.TextView
import android.widget.TimePicker
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.content.res.AppCompatResources
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.textfield.TextInputEditText
import com.tracko.automaticchickendoor.R
import com.tracko.automaticchickendoor.databinding.ActivitySetScheduleBinding
import com.tracko.automaticchickendoor.databinding.SchedulePopupBinding
import com.tracko.automaticchickendoor.util.DoorCommandMode
import com.tracko.automaticchickendoor.viewmodel.BleViewModel
import com.tracko.automaticchickendoor.viewmodel.UserViewModel
import dagger.hilt.android.AndroidEntryPoint
import java.util.Calendar

@AndroidEntryPoint
class SetScheduleActivity : AppCompatActivity() {
    private val userViewModel: UserViewModel by viewModels()
    private lateinit var binding: ActivitySetScheduleBinding
    private var gridItems = listOf<TextView>()
    private val bleViewModel: BleViewModel by viewModels()
    private var macAddress: String = ""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySetScheduleBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initUiAndListeners()
        observeViewModel()
    }
    
    private fun initUiAndListeners() {
        binding.ivBackBtn.setOnClickListener {
            finish()
        }
        
        gridItems = listOf(binding.itemTT, binding.itemTL, binding.itemLT, binding.itemLL)
        gridItems.forEach { item ->
            item.setOnClickListener {
                gridItems.forEach { otherItem ->
                    otherItem.setTextColor(resources.getColor(R.color.dark_text_color, null))
                    otherItem.background = AppCompatResources.getDrawable(this, R.drawable.grid_item_bg_unselected)
                }
                item.setTextColor(resources.getColor(R.color.white, null))
                it.background = AppCompatResources.getDrawable(this, R.drawable.grid_item_bg_selected)
                showCustomDialog(item.text.toString())
            }
        }
        
       
    }
    
    @SuppressLint("MissingPermission")
    private fun observeViewModel(){
        bleViewModel.readMacAddress.observe(this) {
            it?.let {mac->
                macAddress = mac
            }
            
        }
        userViewModel.isModeSetByApi.observe(this) {
            if (it.isSuccess){
                Toast.makeText(this@SetScheduleActivity,"Mode set successfully", Toast.LENGTH_SHORT).show()
                
            } else{
                Toast.makeText(this@SetScheduleActivity,"No internet connection. Trying with BLE", Toast.LENGTH_SHORT).show()
                bleViewModel.sendBleModeCommand(openTime = it.data.openTime, closeTime = it.data.closeTime, mode = it.data.mode)
            }
        }
        
        bleViewModel.isModeSetByBle.observe(this) {
            if (!it){
                Toast.makeText(this@SetScheduleActivity,"Device not connected. Please connect via BLE and try again", Toast.LENGTH_SHORT).show()
            }
        }
    }
    
    private fun showCustomDialog(type: String) {
        val dialogViewBinding = SchedulePopupBinding.inflate(layoutInflater)
        val dialog = MaterialAlertDialogBuilder(this)
            .setView(dialogViewBinding.root)
            .setCancelable(true)
            .create()
        val finalMode = when (type) {
            getString(R.string.t_l) -> {
                // For "t_l" mode, hide endTimeTextField and show closeIntensityGroup
                dialogViewBinding.endTimeTextField.visibility = View.GONE
                dialogViewBinding.startTimeTextField.visibility = View.VISIBLE
                dialogViewBinding.closeIntensityGroup.visibility = View.VISIBLE
                dialogViewBinding.openIntensityGroup.visibility = View.GONE
                DoorCommandMode.MODE_TL
            }
            
            getString(R.string.l_t) -> {
                // For "l_t" mode, hide startTimeTextField, show endTimeTextField and openIntensityGroup
                dialogViewBinding.startTimeTextField.visibility = View.GONE
                dialogViewBinding.endTimeTextField.visibility = View.VISIBLE
                dialogViewBinding.closeIntensityGroup.visibility = View.GONE
                dialogViewBinding.openIntensityGroup.visibility = View.VISIBLE
                DoorCommandMode.MODE_LT
            }
            
            getString(R.string.l_l) -> {
                // For "l_l" mode, hide both start and end time fields, show intensity groups if needed
                dialogViewBinding.startTimeTextField.visibility = View.GONE
                dialogViewBinding.endTimeTextField.visibility = View.GONE
                dialogViewBinding.closeIntensityGroup.visibility = View.VISIBLE
                dialogViewBinding.openIntensityGroup.visibility = View.VISIBLE
                DoorCommandMode.MODE_LL
            }
            
            else -> {
                // Default: show both time fields and intensity groups
                dialogViewBinding.startTimeTextField.visibility = View.VISIBLE
                dialogViewBinding.endTimeTextField.visibility = View.VISIBLE
                dialogViewBinding.closeIntensityGroup.visibility = View.GONE
                dialogViewBinding.openIntensityGroup.visibility = View.GONE
                DoorCommandMode.MODE_TT
            }
        }
        
        
        dialogViewBinding.etStartTime.setOnFocusChangeListener { _, hasFocus ->
            if (hasFocus) {
                openTimePicker(dialogViewBinding.etStartTime)
            }
        }
        
        dialogViewBinding.etEndTime.setOnFocusChangeListener { _, hasFocus ->
            if (hasFocus) {
                openTimePicker(dialogViewBinding.etEndTime)
            }
        }
        // Declare variables to hold SeekBar values
        var openIntensityValue = dialogViewBinding.seekBarOpenIntensity.progress
        var closeIntensityValue = dialogViewBinding.seekBarCloseIntensity.progress
        // Set listener on Open Intensity SeekBar
        dialogViewBinding.seekBarOpenIntensity.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                openIntensityValue = progress
            }
            
            override fun onStartTrackingTouch(seekBar: SeekBar?) {
                // Optional: do something on touch start
            }
            
            override fun onStopTrackingTouch(seekBar: SeekBar?) {
                // Optional: do something on touch stop
            }
        })
        // Set listener on Close Intensity SeekBar
        dialogViewBinding.seekBarCloseIntensity.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                closeIntensityValue = progress
            }
            
            override fun onStartTrackingTouch(seekBar: SeekBar?) {}
            override fun onStopTrackingTouch(seekBar: SeekBar?) {}
        })
        
        dialogViewBinding.btnSetSchedule.setOnClickListener {
            when (type) {
                getString(R.string.t_l) -> {
                    userViewModel.setSchedule(mac = macAddress, openTime = dialogViewBinding.etStartTime.text.toString(), closeTime = closeIntensityValue.toString(), mode = finalMode)
                }
                getString(R.string.l_t) -> {
                    userViewModel.setSchedule(mac = macAddress, openTime = openIntensityValue.toString(), closeTime = dialogViewBinding.etEndTime.text.toString(), mode = finalMode)
                }
                getString(R.string.l_l) -> {
                    userViewModel.setSchedule(mac = macAddress, openTime = openIntensityValue.toString(), closeTime = closeIntensityValue.toString(), mode = finalMode)
                }
                else -> {
                    userViewModel.setSchedule(mac = macAddress, openTime = dialogViewBinding.etStartTime.text.toString(), closeTime = dialogViewBinding.etEndTime.text.toString(), mode = finalMode)
                }
            }
            dialog.dismiss()
            resetGridItems()
        }
        dialog.show()
    }
    
    private fun openTimePicker(editText: TextInputEditText) {
        val calendar = Calendar.getInstance()
        val hour = calendar.get(Calendar.HOUR_OF_DAY)
        val minute = calendar.get(Calendar.MINUTE)
        val timePickerDialog = TimePickerDialog(
            this,
            R.style.CustomTimePickerDialog,
            { _: TimePicker, selectedHour: Int, selectedMinute: Int ->
                val formattedTime = String.format("%02d:%02d", selectedHour, selectedMinute)
                editText.setText(formattedTime)
            },
            hour,
            minute,
            false
        )
        
        timePickerDialog.show()
    }
    
    private fun resetGridItems() {
        gridItems.forEach { item ->
            item.setTextColor(resources.getColor(R.color.dark_text_color, null))
            item.background = AppCompatResources.getDrawable(this, R.drawable.grid_item_bg_unselected)
        }
    }
}
