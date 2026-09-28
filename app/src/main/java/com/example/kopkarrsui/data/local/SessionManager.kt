package com.example.kopkarrsui.data.local

import android.content.Context
import android.content.SharedPreferences
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SessionManager @Inject constructor(
    @ApplicationContext context: Context
) {
    private val prefs: SharedPreferences = context.getSharedPreferences("kopkar_session", Context.MODE_PRIVATE)

    companion object {
        private const val KEY_MEMBER_ID = "current_member_id"
        private const val KEY_IS_LOGGED_IN = "is_logged_in"
    }

    var currentMemberId: Long
        get() = prefs.getLong(KEY_MEMBER_ID, -1L)
        set(value) = prefs.edit().putLong(KEY_MEMBER_ID, value).apply()

    var isLoggedIn: Boolean
        get() = prefs.getBoolean(KEY_IS_LOGGED_IN, false)
        set(value) = prefs.edit().putBoolean(KEY_IS_LOGGED_IN, value).apply()

    fun saveSession(memberId: Long) {
        currentMemberId = memberId
        isLoggedIn = true
    }

    fun clearSession() {
        prefs.edit().clear().apply()
    }
}
