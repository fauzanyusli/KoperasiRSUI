package com.example.kopkarrsui.util

import org.mindrot.jbcrypt.BCrypt

object PasswordUtils {
    private const val BCRYPT_LOG_ROUNDS = 10

    fun hashPin(pin: String): String {
        return BCrypt.hashpw(pin, BCrypt.gensalt(BCRYPT_LOG_ROUNDS))
    }

    fun verifyPin(pin: String, hash: String): Boolean {
        return BCrypt.checkpw(pin, hash)
    }
}
