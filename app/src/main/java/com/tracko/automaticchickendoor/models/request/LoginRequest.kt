package com.tracko.automaticchickendoor.models.request

import android.content.Context
import com.tracko.automaticchickendoor.R

data class LoginRequest(
    val email: String,
    val password: String,
)

fun LoginRequest.validate(context: Context): LoginValidationResult {
    if (email.isBlank()) {
        return LoginValidationResult.Error(
            context.getString(R.string.email),
            context.getString(R.string.email_error)
        )
    }

    if (password.isBlank()) {
        return LoginValidationResult.Error(
            context.getString(R.string.password),
            context.getString(R.string.password_error)
        )
    }

    return LoginValidationResult.Success
}

sealed class LoginValidationResult {
    object Success : LoginValidationResult()
    data class Error(val fieldName:String,val message: String) : LoginValidationResult()
}
