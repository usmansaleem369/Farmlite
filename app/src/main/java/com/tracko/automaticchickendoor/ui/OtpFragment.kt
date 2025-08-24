package com.tracko.automaticchickendoor.ui

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.tracko.automaticchickendoor.R
import com.tracko.automaticchickendoor.databinding.CustomDialogLayoutBinding
import com.tracko.automaticchickendoor.databinding.FragmentOtpBinding
import com.tracko.automaticchickendoor.viewmodel.UserViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class OtpFragment : BottomSheetDialogFragment() {
    private val userViewModel: UserViewModel by activityViewModels()
    
    private var _binding: FragmentOtpBinding? = null
    private val binding get() = _binding!!
    
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentOtpBinding.inflate(inflater, container, false)
        return binding.root
    }
    
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupOtpInputs()
        initUiAndListeners()
    }
    
    private fun initUiAndListeners(){
        binding.otp1.requestFocus()
        
        binding.btnVerifyOtp.setOnClickListener {
            val otp = getOtp()
            if (otp.length == 6) {
                userViewModel.verifyOtp(otp)
                binding.tvError.text = ""
            } else {
                binding.tvError.text = "Enter a valid 6-digit OTP"
                listOf(binding.otp1, binding.otp2, binding.otp3, binding.otp4, binding.otp5, binding.otp6)
                    .firstOrNull { it.text.isNullOrEmpty() }
                    ?.error = "Required"
            }
        }
        
        userViewModel.signUpData.observe(this){
            if (it.isSuccessful && it.otpVerified){
               showSuccessDialog()
            } else {
                binding.tvError.text = getString(R.string.invalid_code)
            }
        }
    }
    
    private fun getOtp(): String {
        return listOf(
            binding.otp1.text.toString(),
            binding.otp2.text.toString(),
            binding.otp3.text.toString(),
            binding.otp4.text.toString(),
            binding.otp5.text.toString(),
            binding.otp6.text.toString()
        ).joinToString("")
    }
    
    private fun showSuccessDialog() {
        val bindingDialog = CustomDialogLayoutBinding.inflate(layoutInflater)
        val dialog = android.app.Dialog(requireContext())
        dialog.setContentView(bindingDialog.root)
        dialog.setCancelable(false)
        dialog.window?.setBackgroundDrawableResource(android.R.color.transparent)
        bindingDialog.btnConfirm.setOnClickListener {
            dialog.dismiss()
            val intent = Intent(requireActivity(), SignInActivity::class.java)
            startActivity(intent)
            requireActivity().finish()
        }
        dialog.show()
    }
    
    
    private fun setupOtpInputs() {
        val otpFields = listOf(
            binding.otp1, binding.otp2, binding.otp3, binding.otp4,binding.otp5,binding.otp6
        )
        
        otpFields.forEachIndexed { index, editText ->
            editText.addTextChangedListener(object : TextWatcher {
                override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                    if (s?.length == 1 && index < otpFields.size - 1) {
                        otpFields[index + 1].requestFocus()
                    } else if (s.isNullOrEmpty() && index > 0) {
                        otpFields[index - 1].requestFocus()
                    }
                }
                override fun afterTextChanged(s: Editable?) {}
            })
        }
    }
    
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
    
    override fun onCreateDialog(savedInstanceState: Bundle?): BottomSheetDialog {
        val dialog = super.onCreateDialog(savedInstanceState) as BottomSheetDialog
        dialog.setCanceledOnTouchOutside(false)
        dialog.setOnCancelListener { /* do nothing */ }
        dialog.setOnShowListener { dlg ->
            val bottomSheetDialog = dlg as BottomSheetDialog
            val bottomSheet = bottomSheetDialog.findViewById<View>(
                com.google.android.material.R.id.design_bottom_sheet
            )
            bottomSheet?.let {
                val behavior = BottomSheetBehavior.from(it)
                behavior.isHideable = false
            }
        }
        
        return dialog
    }
}
