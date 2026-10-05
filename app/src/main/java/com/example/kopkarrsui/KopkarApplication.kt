package com.example.kopkarrsui

import android.app.Application
import com.example.kopkarrsui.data.local.Seeder
import dagger.hilt.android.HiltAndroidApp
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch

@HiltAndroidApp
class KopkarApplication : Application() {

    private val appScope = CoroutineScope(SupervisorJob() + Dispatchers.IO)

    override fun onCreate() {
        super.onCreate()
        // Seed data awal ke Firestore (sekali jalan, swallow error — offline pun app tetap jalan)
        appScope.launch { runCatching { Seeder.ensureSeeded() } }
    }
}
