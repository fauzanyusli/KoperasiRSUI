package com.example.kopkarrsui.util

import java.security.MessageDigest

object PasswordUtils {
    fun hashPin(pin: String): String {
        val digest = MessageDigest.getInstance("SHA-256")
        val hashBytes = digest.digest(pin.toByteArray())
        return hashBytes.joinToString("") { "%02x".format(it) }
    }

    fun verifyPin(pin: String, hash: String): Boolean {
        return hashPin(pin) == hash
    }
}
