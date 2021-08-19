package com.yusufgokmenarisoy.foodorder.util

import java.util.regex.Pattern

class Common {

    companion object {

        fun validateEmail(email: String): Boolean {
            return email.isNotEmpty() && android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()
        }

        fun CharSequence.validatePassword(): Boolean {
            val passwordPattern = "^(?=.*[0-9])(?=.*[A-Z])(?=.*[@#$%^&+=!])(?=\\S+$).{4,}$"
            val pattern = Pattern.compile(passwordPattern)
            return pattern.matcher(this).matches()
        }
    }
}