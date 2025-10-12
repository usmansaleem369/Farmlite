package com.tracko.automaticchickendoor.ui

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.amplifyframework.api.graphql.model.ModelMutation
import com.amplifyframework.core.Amplify
import com.amplifyframework.datastore.generated.model.DevicesTable
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.tracko.automaticchickendoor.databinding.FragmentConfigureWifiBinding
import com.tracko.automaticchickendoor.databinding.WifiConnectionSuccessDialogBinding
import com.tracko.automaticchickendoor.util.SharedPreferencesHelper
import com.tracko.automaticchickendoor.viewmodel.BleViewModel
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject
import kotlin.getValue

@AndroidEntryPoint
class ConfigureWifiFragment : Fragment() {
    
    @Inject
    lateinit var sharedPreferencesHelper: SharedPreferencesHelper
    private val bleViewModel: BleViewModel by activityViewModels()
    
    // View binding variable
    private var _binding: FragmentConfigureWifiBinding? = null
    private val binding get() = _binding!!
    private lateinit var successDialog: AlertDialog
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentConfigureWifiBinding.inflate(inflater, container, false)
        return binding.root
    }
    
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initUiAndListeners()
        observeViewModel()
    }
    
    private fun observeViewModel() {
        bleViewModel.credentialsSet.observe(requireActivity()) {
            if (it.first) {
                bleViewModel.readMacAddress()
                showSuccessDialog()
            } else {
                Toast.makeText(requireContext(), "Not able to connect to device", Toast.LENGTH_SHORT).show()
            }
        }
        
        bleViewModel.readMacAddress.observe(requireActivity()) {
            it?.let {mac->
                addDeviceToAWS(mac)
            }?:run {
                Toast.makeText(requireContext(), "Not a valid FarmLite Device. Try Again", Toast.LENGTH_SHORT).show()
            }
        }
    }
    
    private fun addDeviceToAWS(mac:String) {
        val model = DevicesTable.builder()
            .deviceName("My Device")
            .macAddress(mac)
            .status("Online")
            .email(sharedPreferencesHelper.email?:"")
            .build()
        
        Amplify.API.mutate(
            ModelMutation.create(model),
            { response ->
                if (response.hasErrors()) {
                    Log.e("Amplify", "Failed to add device: ${response.errors}")
                } else {
                    Log.i("Amplify", "Device added successfully: ${response.data.id}")
                }
            },
            { error ->
                Log.e("Amplify", "Error adding device", error)
            }
        )
    }
    
    private fun initUiAndListeners() {
        binding.main.setOnClickListener {
        }
        binding.ivBackBtn.setOnClickListener {
            parentFragmentManager.popBackStack()
        }
        
        binding.btnConnect.setOnClickListener {
            val ssid = binding.etWifiName.text.toString()
            val password = binding.etWifiPassword.text.toString()
            if (ssid.isEmpty()) {
                binding.tilWifiName.error = "Enter wifi ssid"
            } else if (password.isEmpty()) {
                binding.tilWifiPassword.error = "Enter wifi password"
            } else {
                bleViewModel.selectedBleDevice?.let { device ->
                    bleViewModel.connectAndSetCredentials(device, ssid, password)
                } ?: run {
                    Toast.makeText(requireContext(), "Something went wrong please try again", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }
    
    private fun showSuccessDialog() {
        val dialogViewBinding = WifiConnectionSuccessDialogBinding.inflate(layoutInflater)
        
        successDialog = MaterialAlertDialogBuilder(requireContext())
            .setView(dialogViewBinding.root)
            .setCancelable(true)
            .create()
        
        dialogViewBinding.btnContinue.setOnClickListener {
            val intent = Intent(requireActivity(), ProgramDoorActivity::class.java)
            startActivity(intent)
        }
        
        successDialog.show()
    }
    
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null // Prevent memory leaks
    }
}
