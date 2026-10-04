package com.example.kopkarrsui.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.kopkarrsui.presentation.screen.auth.LoginScreen
import com.example.kopkarrsui.presentation.screen.admin.AdminDashboardScreen
import com.example.kopkarrsui.presentation.screen.admin.AuditLogScreen
import com.example.kopkarrsui.presentation.screen.admin.BackupRestoreScreen
import com.example.kopkarrsui.presentation.screen.rat.RATScreen
import com.example.kopkarrsui.presentation.screen.dashboard.DashboardScreen
import com.example.kopkarrsui.presentation.screen.dashboard.MemberManagementScreen
import com.example.kopkarrsui.presentation.screen.profile.ProfileScreen
import com.example.kopkarrsui.presentation.screen.savings.SavingsScreen
import com.example.kopkarrsui.presentation.screen.shu.SHUScreen
import com.example.kopkarrsui.presentation.screen.transaction.TransactionScreen

@Composable
fun AppNavHost(
    navController: NavHostController,
    modifier: Modifier = Modifier,
    startDestination: String = AppDestination.Dashboard.route
) {
    NavHost(navController, startDestination, modifier) {
        composable(AppDestination.AuthLogin.route) {
            LoginScreen(onLoginSuccess = { isAdmin ->
                navController.navigate(
                    if (isAdmin) AppDestination.AdminDashboard.route else AppDestination.Dashboard.route
                ) {
                    popUpTo(AppDestination.AuthLogin.route) { inclusive = true }
                }
            })
        }
        composable(AppDestination.Dashboard.route) {
            DashboardScreen(
                viewModel = hiltViewModel(),
                onNavigateToMemberManagement = { navController.navigate(AppDestination.MemberManagement.route) }
            )
        }
        composable(AppDestination.MemberManagement.route) {
            MemberManagementScreen(onBack = { navController.popBackStack() })
        }
        composable(AppDestination.Savings.route) {
            SavingsScreen(hiltViewModel())
        }
        composable(AppDestination.Transaction.route) {
            TransactionScreen(hiltViewModel())
        }
        composable(AppDestination.SHU.route) {
            SHUScreen(hiltViewModel())
        }
        composable(AppDestination.Profile.route) {
            ProfileScreen(
                viewModel = hiltViewModel(),
                onLogout = {
                    navController.navigate(AppDestination.AuthLogin.route) {
                        popUpTo(0) { inclusive = true }
                    }
                }
            )
        }
        composable(AppDestination.AdminDashboard.route) {
            AdminDashboardScreen(
                onNavigateToAuditLog = { navController.navigate(AppDestination.AuditLog.route) },
                onNavigateToBackup = { navController.navigate(AppDestination.BackupRestore.route) },
                onLogout = {
                    navController.navigate(AppDestination.AuthLogin.route) {
                        popUpTo(0) { inclusive = true }
                    }
                }
            )
        }
        composable(AppDestination.AuditLog.route) {
            AuditLogScreen()
        }
        composable(AppDestination.RAT.route) {
            RATScreen()
        }
        composable(AppDestination.BackupRestore.route) {
            BackupRestoreScreen(onBack = { navController.popBackStack() })
        }
    }
}

sealed class AppDestination(val route: String) {
    data object AuthLogin : AppDestination("auth/login")
    data object Dashboard : AppDestination("dashboard")
    data object Savings : AppDestination("savings")
    data object Transaction : AppDestination("transaction")
    data object SHU : AppDestination("shu")
    data object MemberManagement : AppDestination("member-management")
    data object Profile : AppDestination("profile")
    data object AdminDashboard : AppDestination("admin-dashboard")
    data object AuditLog : AppDestination("audit-log")
    data object RAT : AppDestination("rat")
    data object BackupRestore : AppDestination("backup-restore")
}
