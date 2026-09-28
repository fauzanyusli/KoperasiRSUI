package com.example.kopkarrsui.presentation.screen.dashboard

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
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
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountBalanceWallet
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.AddCircle
import androidx.compose.material.icons.filled.Assessment
import androidx.compose.material.icons.filled.ArrowDownward
import androidx.compose.material.icons.filled.ArrowUpward
import androidx.compose.material.icons.filled.Call
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Group
import androidx.compose.material.icons.filled.People
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material.icons.filled.ReceiptLong
import androidx.compose.material.icons.filled.RemoveCircle
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.TrendingUp
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.kopkarrsui.data.local.entity.Member
import com.example.kopkarrsui.data.local.entity.Transaction
import com.example.kopkarrsui.presentation.component.ErrorState
import com.example.kopkarrsui.presentation.component.LoadingOverlay
import com.example.kopkarrsui.presentation.component.StatCard
import com.example.kopkarrsui.presentation.viewmodel.DashboardViewModel

// ─── Data class untuk anggota ───
data class MemberUI(
    val id: Long = 0,
    val noAnggota: String = "",
    val nama: String = "",
    val jabatan: String = "",
    val noTelp: String = "",
    val status: Member.MemberStatus = Member.MemberStatus.AKTIF,
    val totalTabungan: Double = 0.0,
    val totalPinjaman: Double = 0.0,
    val tanggalGabung: String = ""
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DashboardScreen(viewModel: DashboardViewModel = viewModel(), onNavigateToMemberManagement: () -> Unit = {}) {
    val member by viewModel.member.collectAsStateWithLifecycle()
    val totalSaldo by viewModel.totalSaldo.collectAsStateWithLifecycle()
    val totalPoin by viewModel.totalPoin.collectAsStateWithLifecycle()
    val latestSHU by viewModel.latestSHU.collectAsStateWithLifecycle()
    val recentTransactions by viewModel.recentTransactions.collectAsStateWithLifecycle()
    val isLoading by viewModel.isLoading.collectAsStateWithLifecycle()
    val error by viewModel.error.collectAsStateWithLifecycle()

    var searchQuery by remember { mutableStateOf("") }
    var showMemberList by remember { mutableStateOf(true) }

    // Sample data anggota - remember agar tidak recreate setiap recomposition
    val sampleMembers = remember {
        listOf(
            MemberUI(1, "KPR-001", "Budi Santoso", "Ketua", "081234567890", Member.MemberStatus.AKTIF, 2_500_000.0, 5_000_000.0, "2020-01-15"),
            MemberUI(2, "KPR-002", "Siti Rahayu", "Sekretaris", "081234567891", Member.MemberStatus.AKTIF, 1_800_000.0, 3_000_000.0, "2020-03-20"),
            MemberUI(3, "KPR-003", "Ahmad Fauzi", "Bendahara", "081234567892", Member.MemberStatus.AKTIF, 3_200_000.0, 10_000_000.0, "2020-06-10"),
            MemberUI(4, "KPR-004", "Dewi Lestari", "Anggota", "081234567893", Member.MemberStatus.AKTIF, 950_000.0, 0.0, "2021-01-05"),
            MemberUI(5, "KPR-005", "Rizky Pratama", "Anggota", "081234567894", Member.MemberStatus.NONAKTIF, 1_200_000.0, 2_000_000.0, "2021-06-15")
        )
    }

    val filteredMembers = remember(searchQuery) {
        sampleMembers.filter {
            it.nama.contains(searchQuery, ignoreCase = true) ||
                    it.noAnggota.contains(searchQuery, ignoreCase = true) ||
                    it.jabatan.contains(searchQuery, ignoreCase = true)
        }
    }

    Box(modifier = Modifier.fillMaxSize()) {
        LazyColumn(modifier = Modifier.fillMaxWidth()) {
            // Header
            item {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(MaterialTheme.colorScheme.primaryContainer)
                        .padding(16.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column {
                            Text("Selamat datang,", fontSize = 12.sp, color = MaterialTheme.colorScheme.onPrimaryContainer.copy(alpha = 0.8f))
                            Text(member?.nama?.split(" ")?.firstOrNull() ?: "Anggota", fontSize = 20.sp, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.onPrimaryContainer)
                            Text("No. ${member?.noAnggota ?: "-"}", fontSize = 11.sp, color = MaterialTheme.colorScheme.onPrimaryContainer.copy(alpha = 0.7f))
                        }
                        Box(
                            modifier = Modifier.size(56.dp).background(MaterialTheme.colorScheme.primary, CircleShape),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(Icons.Filled.AccountBalanceWallet, contentDescription = "", tint = MaterialTheme.colorScheme.onPrimary, modifier = Modifier.size(28.dp))
                        }
                    }
                }
            }

            // Error
            item {
                error?.let { msg ->
                    ErrorState(message = msg, onRetry = { viewModel.loadDashboardData(1) })
                }
            }

            // Stat Cards
            if (!isLoading) {
                item {
                    Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
                        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                            StatCard(title = "Total Tabungan", value = viewModel.formatRupiah(totalSaldo), icon = Icons.Filled.AccountBalanceWallet, iconColor = Color(0xFF2E7D32), modifier = Modifier.weight(1f))
                            StatCard(title = "Total Poin", value = viewModel.formatPoin(totalPoin), icon = Icons.Filled.TrendingUp, iconColor = Color(0xFFFF8F00), modifier = Modifier.weight(1f))
                        }
                        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                            StatCard(title = "SHU Tahun Ini", value = latestSHU?.let { viewModel.formatRupiah(it.jumlah) } ?: "Belum dihitung", icon = Icons.Filled.Assessment, iconColor = Color(0xFF1565C0), modifier = Modifier.weight(1f))
                            StatCard(title = "Anggota Aktif", value = "${sampleMembers.count { it.status == Member.MemberStatus.AKTIF }}", icon = Icons.Filled.People, iconColor = Color(0xFF6A1B9A), modifier = Modifier.weight(1f))
                        }
                    }
                }

                // Quick Actions
                item {
                    Column(modifier = Modifier.padding(horizontal = 16.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                        Text("Aksi Cepat", fontSize = 16.sp, fontWeight = FontWeight.Bold)
                        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                            QuickAction("Setor Tabungan", Icons.Filled.AddCircle, Color(0xFF2E7D32), Modifier.weight(1f))
                            QuickAction("Tarik Tabungan", Icons.Filled.RemoveCircle, Color(0xFFC62828), Modifier.weight(1f))
                            QuickAction("Mutasi", Icons.Filled.Assessment, MaterialTheme.colorScheme.primary, Modifier.weight(1f))
                        }
                    }
                }

                // Recent Transactions
                item {
                    Spacer(modifier = Modifier.height(16.dp))
                    Column(modifier = Modifier.padding(horizontal = 16.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                        Text("Transaksi Terbaru", fontSize = 16.sp, fontWeight = FontWeight.Bold)
                        if (recentTransactions.isEmpty()) {
                            Card(
                                modifier = Modifier.fillMaxWidth(),
                                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceContainerLow)
                            ) {
                                Box(modifier = Modifier.fillMaxWidth().padding(32.dp), contentAlignment = Alignment.Center) {
                                    Text("Belum ada transaksi", fontSize = 14.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                                }
                            }
                        }
                    }
                }

                // Transaction Items
                items(recentTransactions, key = { it.id }) { tx ->
                    TransactionItem(tx, viewModel)
                }

                // ═══════════════════════════════════════════
                // MANAJEMEN ANGGOTA
                // ═══════════════════════════════════════════
                item {
                    Spacer(modifier = Modifier.height(24.dp))
                    Column(
                        modifier = Modifier.padding(horizontal = 16.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(
                                    Icons.Filled.Group,
                                    contentDescription = null,
                                    tint = MaterialTheme.colorScheme.primary,
                                    modifier = Modifier.size(24.dp)
                                )
                                Spacer(modifier = Modifier.width(8.dp))
                                Text("Manajemen Anggota", fontSize = 16.sp, fontWeight = FontWeight.Bold)
                            }
                            Text("${sampleMembers.size} orang", fontSize = 12.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                        }
                    }
                }

                // Search Bar
                item {
                    OutlinedTextField(
                        value = searchQuery,
                        onValueChange = { searchQuery = it },
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp, vertical = 8.dp),
                        placeholder = { Text("Cari nama, no. anggota, atau jabatan...") },
                        leadingIcon = { Icon(Icons.Filled.Search, contentDescription = null) },
                        singleLine = true,
                        shape = RoundedCornerShape(12.dp),
                        colors = OutlinedTextFieldDefaults.colors(
                            unfocusedContainerColor = MaterialTheme.colorScheme.surfaceContainerLow,
                            focusedContainerColor = MaterialTheme.colorScheme.surfaceContainerLow
                        )
                    )
                }

                // Add Member Button
                item {
                    OutlinedButton(
                        onClick = onNavigateToMemberManagement,
                        modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp),
                        colors = ButtonDefaults.outlinedButtonColors(containerColor = MaterialTheme.colorScheme.primaryContainer, contentColor = MaterialTheme.colorScheme.primary),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Icon(Icons.Filled.Add, contentDescription = null)
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("Manajemen Anggota", fontWeight = FontWeight.Medium)
                    }
                }

                // Member List
                if (filteredMembers.isEmpty()) {
                    item {
                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 16.dp, vertical = 8.dp),
                            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceContainerLow)
                        ) {
                            Box(modifier = Modifier.fillMaxWidth().padding(32.dp), contentAlignment = Alignment.Center) {
                                Text("Tidak ada anggota ditemukan", fontSize = 14.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                            }
                        }
                    }
                } else {
                    items(filteredMembers, key = { it.id }) { memberUI ->
                        MemberCard(memberUI)
                    }
                }

                // Bottom Spacer
                item {
                    Spacer(modifier = Modifier.height(16.dp))
                }
            }
        }

        if (isLoading) LoadingOverlay("Memuat dashboard...")
    }
}

@Composable
private fun MemberCard(member: MemberUI) {
    val statusColor = when (member.status) {
        Member.MemberStatus.AKTIF -> Color(0xFF2E7D32)
        Member.MemberStatus.NONAKTIF -> Color(0xFFC62828)
        Member.MemberStatus.KELUAR -> Color(0xFF9E9E9E)
    }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 4.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceContainerLow),
        shape = RoundedCornerShape(12.dp)
    ) {
        Column(modifier = Modifier.padding(12.dp)) {
            // Header: Avatar + Name + Status
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Avatar
                Box(
                    modifier = Modifier
                        .size(48.dp)
                        .background(statusColor.copy(alpha = 0.15f), CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        member.nama.take(2).uppercase(),
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        color = statusColor
                    )
                }

                Spacer(modifier = Modifier.width(12.dp))

                // Name + Info
                Column(modifier = Modifier.weight(1f)) {
                    Text(member.nama, fontSize = 14.sp, fontWeight = FontWeight.Bold, maxLines = 1, overflow = TextOverflow.Ellipsis)
                    Text("${member.noAnggota} • ${member.jabatan}", fontSize = 11.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Box(
                            modifier = Modifier
                                .size(8.dp)
                                .background(statusColor, CircleShape)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(member.status.value.replaceFirstChar { it.uppercase() }, fontSize = 10.sp, color = statusColor)
                    }
                }

                // Actions
                Row {
                    IconButton(onClick = { }, modifier = Modifier.size(32.dp)) {
                        Icon(Icons.Filled.Phone, contentDescription = "Telepon", tint = MaterialTheme.colorScheme.primary, modifier = Modifier.size(18.dp))
                    }
                    IconButton(onClick = { }, modifier = Modifier.size(32.dp)) {
                        Icon(Icons.Filled.Edit, contentDescription = "Edit", tint = MaterialTheme.colorScheme.onSurfaceVariant, modifier = Modifier.size(18.dp))
                    }
                }
            }

            // Financial Summary
            Spacer(modifier = Modifier.height(8.dp))
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(MaterialTheme.colorScheme.surface, RoundedCornerShape(8.dp))
                    .padding(8.dp),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text("Tabungan", fontSize = 10.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                    Text(formatRupiahShort(member.totalTabungan), fontSize = 12.sp, fontWeight = FontWeight.Bold, color = Color(0xFF2E7D32))
                }
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text("Pinjaman", fontSize = 10.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                    Text(formatRupiahShort(member.totalPinjaman), fontSize = 12.sp, fontWeight = FontWeight.Bold, color = Color(0xFFC62828))
                }
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text("Gabung", fontSize = 10.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                    Text(member.tanggalGabung.take(7), fontSize = 12.sp, fontWeight = FontWeight.Medium)
                }
            }
        }
    }
}

@Composable
private fun QuickAction(label: String, icon: androidx.compose.ui.graphics.vector.ImageVector, color: Color, modifier: Modifier = Modifier) {
    OutlinedButton(
        onClick = {},
        modifier = modifier.height(80.dp),
        colors = ButtonDefaults.outlinedButtonColors(containerColor = color.copy(alpha = 0.05f), contentColor = color),
        shape = RoundedCornerShape(12.dp)
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Icon(icon, contentDescription = "", tint = color, modifier = Modifier.size(24.dp))
            Spacer(modifier = Modifier.height(4.dp))
            Text(label, fontSize = 10.sp, fontWeight = FontWeight.Medium, textAlign = TextAlign.Center)
        }
    }
}

@Composable
private fun TransactionItem(transaction: Transaction, viewModel: DashboardViewModel) {
    val isIncome = transaction.jumlah > 0
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 4.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceContainerLow)
    ) {
        Row(modifier = Modifier.padding(12.dp), verticalAlignment = Alignment.CenterVertically) {
            Box(
                modifier = Modifier.size(40.dp).background(if (isIncome) Color(0xFF2E7D32).copy(alpha = 0.12f) else Color(0xFFC62828).copy(alpha = 0.12f), CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = when (transaction.tipe) {
                        Transaction.TransactionType.BELANJA -> Icons.Filled.ReceiptLong
                        Transaction.TransactionType.SETORAN_TABUNGAN -> Icons.Filled.ArrowDownward
                        Transaction.TransactionType.PENARIKAN_TABUNGAN -> Icons.Filled.ArrowUpward
                        else -> Icons.Filled.ReceiptLong
                    },
                    contentDescription = "",
                    tint = if (isIncome) Color(0xFF2E7D32) else Color(0xFFC62828),
                    modifier = Modifier.size(20.dp)
                )
            }
            Spacer(modifier = Modifier.width(12.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(transaction.keterangan ?: transaction.tipe.label, fontSize = 14.sp, fontWeight = FontWeight.Medium)
                Text(transaction.tipe.label, fontSize = 11.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
            }
            Text(
                "${if (isIncome) "+" else "-"}${viewModel.formatRupiah(transaction.jumlah)}",
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold,
                color = if (isIncome) Color(0xFF2E7D32) else Color(0xFFC62828)
            )
        }
    }
}

// Helper function
private fun formatRupiahShort(amount: Double): String {
    return when {
        amount >= 1_000_000 -> "Rp ${(amount / 1_000_000).toString().take(4)}jt"
        amount >= 1_000 -> "Rp ${(amount / 1_000).toString().take(4)}rb"
        else -> "Rp ${amount.toLong()}"
    }
}
