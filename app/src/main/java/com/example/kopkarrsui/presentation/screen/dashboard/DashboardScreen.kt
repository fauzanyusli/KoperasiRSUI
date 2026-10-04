package com.example.kopkarrsui.presentation.screen.dashboard
import com.example.kopkarrsui.ui.theme.*

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
import androidx.compose.material.icons.filled.Assessment
import androidx.compose.material.icons.filled.ArrowDownward
import androidx.compose.material.icons.filled.ArrowUpward
import androidx.compose.material.icons.filled.Group
import androidx.compose.material.icons.filled.People
import androidx.compose.material.icons.filled.ReceiptLong
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
import com.example.kopkarrsui.util.formatRupiahShort

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
                            Text("Selamat datang,", fontSize = TextSize.s12, color = MaterialTheme.colorScheme.onPrimaryContainer.copy(alpha = 0.8f))
                            Text(member?.nama?.split(" ")?.firstOrNull() ?: "Anggota", fontSize = TextSize.s20, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.onPrimaryContainer)
                            Text("No. ${member?.noAnggota ?: "-"}", fontSize = TextSize.s11, color = MaterialTheme.colorScheme.onPrimaryContainer.copy(alpha = 0.7f))
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
                            StatCard(title = "Total Tabungan", value = viewModel.formatRupiah(totalSaldo), icon = Icons.Filled.AccountBalanceWallet, iconColor = KopkarGreen, modifier = Modifier.weight(1f))
                            StatCard(title = "Total Poin", value = viewModel.formatPoin(totalPoin), icon = Icons.Filled.TrendingUp, iconColor = KopkarOrange, modifier = Modifier.weight(1f))
                        }
                        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                            StatCard(title = "SHU Tahun Ini", value = latestSHU?.let { viewModel.formatRupiah(it.jumlah) } ?: "Belum dihitung", icon = Icons.Filled.Assessment, iconColor = KopkarBlue, modifier = Modifier.weight(1f))
                            StatCard(title = "Anggota Aktif", value = "${sampleMembers.count { it.status == Member.MemberStatus.AKTIF }}", icon = Icons.Filled.People, iconColor = KopkarPurple, modifier = Modifier.weight(1f))
                        }
                    }
                }

                // Recent Transactions
                item {
                    Spacer(modifier = Modifier.height(16.dp))
                    Column(modifier = Modifier.padding(horizontal = 16.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                        Text("Transaksi Terbaru", fontSize = TextSize.s16, fontWeight = FontWeight.Bold)
                        if (recentTransactions.isEmpty()) {
                            Card(
                                modifier = Modifier.fillMaxWidth(),
                                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceContainerLow)
                            ) {
                                Box(modifier = Modifier.fillMaxWidth().padding(32.dp), contentAlignment = Alignment.Center) {
                                    Text("Belum ada transaksi", fontSize = TextSize.s14, color = MaterialTheme.colorScheme.onSurfaceVariant)
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
                                Text("Manajemen Anggota", fontSize = TextSize.s16, fontWeight = FontWeight.Bold)
                            }
                            Text("${sampleMembers.size} orang", fontSize = TextSize.s12, color = MaterialTheme.colorScheme.onSurfaceVariant)
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
                                Text("Tidak ada anggota ditemukan", fontSize = TextSize.s14, color = MaterialTheme.colorScheme.onSurfaceVariant)
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
        Member.MemberStatus.AKTIF -> KopkarGreen
        Member.MemberStatus.NONAKTIF -> KopkarRed
        Member.MemberStatus.KELUAR -> KopkarGray
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
                        fontSize = TextSize.s16,
                        fontWeight = FontWeight.Bold,
                        color = statusColor
                    )
                }

                Spacer(modifier = Modifier.width(12.dp))

                // Name + Info
                Column(modifier = Modifier.weight(1f)) {
                    Text(member.nama, fontSize = TextSize.s14, fontWeight = FontWeight.Bold, maxLines = 1, overflow = TextOverflow.Ellipsis)
                    Text("${member.noAnggota} • ${member.jabatan}", fontSize = TextSize.s11, color = MaterialTheme.colorScheme.onSurfaceVariant)
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Box(
                            modifier = Modifier
                                .size(8.dp)
                                .background(statusColor, CircleShape)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(member.status.value.replaceFirstChar { it.uppercase() }, fontSize = TextSize.s10, color = statusColor)
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
                    Text("Tabungan", fontSize = TextSize.s10, color = MaterialTheme.colorScheme.onSurfaceVariant)
                    Text(formatRupiahShort(member.totalTabungan), fontSize = TextSize.s12, fontWeight = FontWeight.Bold, color = KopkarGreen)
                }
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text("Pinjaman", fontSize = TextSize.s10, color = MaterialTheme.colorScheme.onSurfaceVariant)
                    Text(formatRupiahShort(member.totalPinjaman), fontSize = TextSize.s12, fontWeight = FontWeight.Bold, color = KopkarRed)
                }
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text("Gabung", fontSize = TextSize.s10, color = MaterialTheme.colorScheme.onSurfaceVariant)
                    Text(member.tanggalGabung.take(7), fontSize = TextSize.s12, fontWeight = FontWeight.Medium)
                }
            }
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
                modifier = Modifier.size(40.dp).background(if (isIncome) KopkarGreen.copy(alpha = 0.12f) else KopkarRed.copy(alpha = 0.12f), CircleShape),
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
                    tint = if (isIncome) KopkarGreen else KopkarRed,
                    modifier = Modifier.size(20.dp)
                )
            }
            Spacer(modifier = Modifier.width(12.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(transaction.keterangan ?: transaction.tipe.label, fontSize = TextSize.s14, fontWeight = FontWeight.Medium)
                Text(transaction.tipe.label, fontSize = TextSize.s11, color = MaterialTheme.colorScheme.onSurfaceVariant)
            }
            Text(
                "${if (isIncome) "+" else "-"}${viewModel.formatRupiah(transaction.jumlah)}",
                fontSize = TextSize.s14,
                fontWeight = FontWeight.Bold,
                color = if (isIncome) KopkarGreen else KopkarRed
            )
        }
    }
}
