package com.example.kopkarrsui.util

import com.google.firebase.FirebaseApp

/**
 * Deteksi konfigurasi Firebase tanpa melempar crash.
 * google-services.json belum ada → FirebaseApp tidak ter-init → getInstance() lempar ISE.
 */
object FirebaseStatus {
    fun isConfigured(): Boolean = runCatching { FirebaseApp.getInstance() }.isSuccess
}
