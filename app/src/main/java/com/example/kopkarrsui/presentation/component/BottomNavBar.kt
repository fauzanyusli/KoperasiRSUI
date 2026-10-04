@file:OptIn(ExperimentalMaterial3Api::class)

package com.example.kopkarrsui.presentation.component
import com.example.kopkarrsui.ui.theme.*

import androidx.compose.foundation.layout.RowScope
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.AccountBalanceWallet
import androidx.compose.material.icons.filled.Assessment
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.ReceiptLong
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.currentBackStackEntryAsState
import com.example.kopkarrsui.presentation.navigation.AppDestination

@Composable
fun KopkarBottomNavBar(navController: NavController) {
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    val items = listOf(
        NavItem(AppDestination.Dashboard, Icons.Filled.Home, "Beranda"),
        NavItem(AppDestination.Savings, Icons.Filled.AccountBalanceWallet, "Tabungan"),
        NavItem(AppDestination.Transaction, Icons.Filled.ReceiptLong, "Transaksi"),
        NavItem(AppDestination.SHU, Icons.Filled.Assessment, "SHU"),
        NavItem(AppDestination.Profile, Icons.Filled.Person, "Profil")
    )

    NavigationBar {
        items.forEach { item ->
            val selected = currentRoute?.startsWith(item.destination.route) == true
            NavigationBarItem(
                selected = selected,
                onClick = {
                    if (!selected) {
                        navController.navigate(item.destination.route) {
                            popUpTo(navController.graph.findStartDestination().id) {
                                saveState = true
                            }
                            launchSingleTop = true
                            restoreState = true
                        }
                    }
                },
                icon = { Icon(imageVector = item.icon, contentDescription = item.label) },
                label = {
                    Text(
                        text = item.label,
                        fontWeight = if (selected) FontWeight.Bold else FontWeight.Normal,
                        fontSize = TextSize.s12
                    )
                }
            )
        }
    }
}

@Composable
fun KopkarTopAppBar(
    title: String,
    showBack: Boolean = false,
    onBack: () -> Unit = {},
    actions: @Composable RowScope.() -> Unit = {}
) {
    TopAppBar(
        title = { Text(text = title, fontWeight = FontWeight.Medium) },
        navigationIcon = if (showBack) {
            {
                IconButton(onClick = onBack) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = "Kembali"
                    )
                }
            }
        } else {{}},
        actions = actions,
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = MaterialTheme.colorScheme.surface
        )
    )
}

private data class NavItem(
    val destination: AppDestination,
    val icon: androidx.compose.ui.graphics.vector.ImageVector,
    val label: String
)
