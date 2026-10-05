package com.example.kopkarrsui

import android.Manifest
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.kopkarrsui.presentation.component.KopkarBottomNavBar
import com.example.kopkarrsui.presentation.navigation.AppDestination
import com.example.kopkarrsui.presentation.navigation.AppNavHost
import com.example.kopkarrsui.ui.theme.KopkarRSUITheme
import com.example.kopkarrsui.util.FirebaseStatus
import com.example.kopkarrsui.util.NotificationHelper
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    private val requestPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        // Permission result handled silently
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        // Create notification channels
        NotificationHelper.createChannels(this)

        // Request notification permission for Android 13+
        requestNotificationPermission()

        setContent {
            KopkarRSUITheme {
                // Guard: tanpa google-services.json semua route yang inject Firebase crash
                val firebaseReady = remember { FirebaseStatus.isConfigured() }
                if (firebaseReady) {
                    val navController = rememberNavController()
                    val currentRoute by navController.currentBackStackEntryAsState()
                    val showBottomBar = currentRoute?.destination?.route != AppDestination.AuthLogin.route
                    Scaffold(
                        modifier = Modifier.fillMaxSize(),
                        bottomBar = { if (showBottomBar) KopkarBottomNavBar(navController = navController) }
                    ) { innerPadding ->
                        AppNavHost(
                            navController = navController,
                            modifier = Modifier.padding(innerPadding)
                        )
                    }
                } else {
                    MissingFirebaseScreen()
                }
            }
        }
    }

    @Composable
    private fun MissingFirebaseScreen() {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(24.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Firebase belum dikonfigurasi",
                style = MaterialTheme.typography.titleMedium
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "Taruh google-services.json dari Firebase console di folder app/ lalu build ulang.",
                style = MaterialTheme.typography.bodyMedium,
                textAlign = TextAlign.Center
            )
        }
    }

    private fun requestNotificationPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ContextCompat.checkSelfPermission(
                    this, Manifest.permission.POST_NOTIFICATIONS
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                requestPermissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
            }
        }
    }
}
