package com.example.kopkarrsui.presentation.screen.admin
import com.example.kopkarrsui.ui.theme.*

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Assessment
import androidx.compose.material.icons.filled.Backup
import androidx.compose.material.icons.filled.Group
import androidx.compose.material.icons.filled.PieChart
import androidx.compose.material.icons.filled.Receipt
import androidx.compose.material.icons.filled.Restore
import androidx.compose.material.icons.filled.Logout
import androidx.compose.material.icons.filled.Security
import androidx.compose.material.icons.filled.TrendingUp
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.kopkarrsui.presentation.viewmodel.MemberViewModel

@Composable
fun AdminDashboardScreen(
    onNavigateToAuditLog: () -> Unit = {},
    onNavigateToBackup: () -> Unit = {},
    onLogout: () -> Unit = {},
    viewModel: MemberViewModel = hiltViewModel()
) {
    LazyColumn(
        modifier = Modifier.fillMaxSize().padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        // Header
        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = KopkarBlue)
            ) {
                Row(
                    modifier = Modifier.padding(20.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        Text("Dashboard Admin", fontSize = TextSize.s20, fontWeight = FontWeight.Bold, color = Color.White)
                        Text("Panel pengurus koperasi", fontSize = TextSize.s12, color = Color.White.copy(alpha = 0.7f))
                    }
                    Icon(Icons.Filled.Security, contentDescription = null, tint = Color.White, modifier = Modifier.size(40.dp))
                    Spacer(modifier = Modifier.width(12.dp))
                    IconButton(onClick = {
                        viewModel.logout()
                        onLogout()
                    }) {
                        Icon(Icons.Filled.Logout, contentDescription = "Keluar", tint = Color.White)
                    }
                }
            }
        }

        // Stat cards
        item {
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                AdminStatCard("Anggota", "47", Icons.Filled.Group, KopkarGreen, Modifier.weight(1f))
                AdminStatCard("Transaksi", "156", Icons.Filled.Receipt, KopkarBlue, Modifier.weight(1f))
            }
        }
        item {
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                AdminStatCard("SHU Total", "Rp 68jt", Icons.Filled.PieChart, KopkarPurple, Modifier.weight(1f))
                AdminStatCard("Aset", "Rp 500jt", Icons.Filled.TrendingUp, KopkarTeal, Modifier.weight(1f))
            }
        }

        // Quick actions
        item {
            Card(modifier = Modifier.fillMaxWidth(), colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceContainerLow), shape = RoundedCornerShape(12.dp)) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text("Aksi Cepat", fontSize = TextSize.s14, fontWeight = FontWeight.Bold)
                    Spacer(modifier = Modifier.height(8.dp))
                    AdminActionButton("Audit Trail", "Lihat log semua perubahan data", Icons.Filled.Assessment, KopkarOrange, onNavigateToAuditLog)
                    AdminActionButton("Backup Database", "Cadangkan database lokal", Icons.Filled.Backup, KopkarGreen, onNavigateToBackup)
                    AdminActionButton("Restore Database", "Pulihkan dari backup", Icons.Filled.Restore, KopkarBlue, onNavigateToBackup)
                }
            }
        }

        // Recent activity
        item {
            Card(modifier = Modifier.fillMaxWidth(), colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceContainerLow), shape = RoundedCornerShape(12.dp)) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text("Aktivitas Terakhir", fontSize = TextSize.s14, fontWeight = FontWeight.Bold)
                    Spacer(modifier = Modifier.height(8.dp))
                    listOf(
                        "Budi Santoso setor tabungan wajib" to "2 jam lalu",
                        "Sistem hitung SHU 2025" to "1 hari lalu",
                        "Dewi Lestari daftar jadi anggota" to "3 hari lalu",
                        "Backup database otomatis" to "7 hari lalu"
                    ).forEach { (action, time) ->
                        Row(modifier = Modifier.fillMaxWidth().padding(vertical = 6.dp), horizontalArrangement = Arrangement.SpaceBetween) {
                            Text(action, fontSize = TextSize.s12, modifier = Modifier.weight(1f))
                            Text(time, fontSize = TextSize.s11, color = MaterialTheme.colorScheme.onSurfaceVariant)
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun AdminStatCard(title: String, value: String, icon: ImageVector, color: Color, modifier: Modifier = Modifier) {
    Card(modifier = modifier, colors = CardDefaults.cardColors(containerColor = color.copy(alpha = 0.08f)), shape = RoundedCornerShape(12.dp)) {
        Column(modifier = Modifier.padding(12.dp)) {
            Icon(icon, contentDescription = null, tint = color, modifier = Modifier.size(24.dp))
            Spacer(modifier = Modifier.height(8.dp))
            Text(value, fontSize = TextSize.s18, fontWeight = FontWeight.Bold, color = color)
            Text(title, fontSize = TextSize.s11, color = MaterialTheme.colorScheme.onSurfaceVariant)
        }
    }
}

@Composable
private fun AdminActionButton(title: String, subtitle: String, icon: ImageVector, color: Color, onClick: () -> Unit) {
    OutlinedButton(onClick = onClick, modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp)) {
        Icon(icon, contentDescription = null, tint = color, modifier = Modifier.size(20.dp))
        Spacer(modifier = Modifier.width(12.dp))
        Column(modifier = Modifier.weight(1f)) {
            Text(title, fontSize = TextSize.s13, fontWeight = FontWeight.Medium)
            Text(subtitle, fontSize = TextSize.s11, color = MaterialTheme.colorScheme.onSurfaceVariant)
        }
    }
}
