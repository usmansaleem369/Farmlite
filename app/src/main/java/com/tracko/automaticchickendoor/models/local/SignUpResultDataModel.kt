package com.tracko.automaticchickendoor.models.local

data class SignUpResultDataModel(val message:String,val isSuccessful:Boolean,val isManual:Boolean,var otpVerified:Boolean = false)
