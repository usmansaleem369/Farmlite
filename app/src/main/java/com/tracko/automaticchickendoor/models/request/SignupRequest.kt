package com.tracko.automaticchickendoor.models.request

import android.content.Context
import android.util.Patterns
import com.tracko.automaticchickendoor.R

data class SignupRequest(
    val email: String,
    val password: String,
    val confirmPassword: String,
)

fun SignupRequest.validate(context: Context): SignupValidationResult {

    if (email.isBlank() || !Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
        return SignupValidationResult.Error(
            context.getString(R.string.email),
            context.getString(R.string.email_error)
        )
    }

    if (password.isBlank() && password.length >= 8) {
        return SignupValidationResult.Error(
            context.getString(R.string.password),
            context.getString(R.string.password_error)
        )
    }

    if (confirmPassword != password) {
        return SignupValidationResult.Error(
            context.getString(R.string.cpassword),
            context.getString(R.string.cpassword_error)
        )
    }

    return SignupValidationResult.Success
}

sealed class SignupValidationResult {
    object Success : SignupValidationResult()
    data class Error(val fieldName: String, val message: String) : SignupValidationResult()
}
