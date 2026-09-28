package com.example.kopkarrsui.presentation.screen.rat

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
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Description
import androidx.compose.material.icons.filled.HowToVote
import androidx.compose.material.icons.filled.Poll
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.TabRowDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun RATScreen() {
    var selectedTab by remember { mutableIntStateOf(0) }
    val tabs = listOf("Berita Acara", "Voting", "Rekomendasi")

    Column(modifier = Modifier.fillMaxSize()) {
        // Header
        Column(modifier = Modifier.fillMaxWidth().background(Color(0xFF00897B).copy(alpha = 0.1f)).padding(16.dp)) {
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
                Column {
                    Text("RAT 2025", fontSize = 20.sp, fontWeight = FontWeight.Bold, color = Color(0xFF00897B))
                    Text("Rapat Anggota Tahunan", fontSize = 14.sp, color = Color(0xFF00897B).copy(alpha = 0.8f))
                }
                Box(modifier = Modifier.size(56.dp).background(Color(0xFF00897B), CircleShape), contentAlignment = Alignment.Center) {
                    Icon(Icons.Filled.HowToVote, contentDescription = null, tint = Color.White)
                }
            }
        }

        // Tab row
        TabRow(
            selectedTabIndex = selectedTab
        ) {
            tabs.forEachIndexed { i, t ->
                Tab(
                    selected = selectedTab == i,
                    onClick = { selectedTab = i },
                    text = { Text(t, fontSize = 12.sp) }
                )
            }
        }

        when (selectedTab) {
            0 -> BeritaAcaraTab()
            1 -> VotingTab()
            2 -> RekomendasiTab()
        }
    }
}

@Composable
private fun BeritaAcaraTab() {
    val agenda = listOf(
        AgendaItem("1", "Pembukaan", "Ketua", "10:00 - 10:15", true),
        AgendaItem("2", "Laporan Pengurus", "Sekretaris", "10:15 - 10:45", true),
        AgendaItem("3", "Laporan Keuangan", "Bendahara", "10:45 - 11:15", true),
        AgendaItem("4", "Laporan SHU", "Bendahara", "11:15 - 11:30", false),
        AgendaItem("5", "Pemilihan Pengurus", "Ketua", "11:30 - 12:00", false),
        AgendaItem("6", "Penutupan", "Ketua", "12:00 - 12:15", false)
    )

    LazyColumn(modifier = Modifier.fillMaxSize().padding(16.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
        item {
            Card(modifier = Modifier.fillMaxWidth(), colors = CardDefaults.cardColors(containerColor = Color(0xFF00897B)), shape = RoundedCornerShape(12.dp)) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text("Berita Acara RAT 2025", fontSize = 16.sp, fontWeight = FontWeight.Bold, color = Color.White)
                    Text("20 Januari 2025 \u2022 Aula RS UI", fontSize = 12.sp, color = Color.White.copy(alpha = 0.7f))
                }
            }
        }

        item {
            Card(modifier = Modifier.fillMaxWidth(), colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceContainerLow), shape = RoundedCornerShape(12.dp)) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text("Ringkasan", fontSize = 14.sp, fontWeight = FontWeight.Bold)
                    Spacer(modifier = Modifier.height(8.dp))
                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) { Text("Hadir", fontSize = 12.sp); Text("42 dari 47 anggota", fontSize = 12.sp, fontWeight = FontWeight.Bold) }
                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) { Text("Quorum", fontSize = 12.sp); Text("Tercapai (89%)", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = Color(0xFF2E7D32)) }
                }
            }
        }

        items(agenda) { item ->
            Card(modifier = Modifier.fillMaxWidth(), colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceContainerLow), shape = RoundedCornerShape(12.dp)) {
                Row(modifier = Modifier.padding(12.dp), verticalAlignment = Alignment.CenterVertically) {
                    Box(modifier = Modifier.size(32.dp).background(if (item.selesai) Color(0xFF2E7D32) else Color.Gray.copy(alpha = 0.3f), CircleShape), contentAlignment = Alignment.Center) {
                        Text(item.no, fontSize = 12.sp, fontWeight = FontWeight.Bold, color = if (item.selesai) Color.White else Color.Gray)
                    }
                    Spacer(modifier = Modifier.width(12.dp))
                    Column(modifier = Modifier.weight(1f)) {
                        Text(item.judul, fontSize = 14.sp, fontWeight = FontWeight.Medium)
                        Text("${item.pemateri} \u2022 ${item.waktu}", fontSize = 11.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                    }
                    if (item.selesai) Icon(Icons.Filled.Check, contentDescription = null, tint = Color(0xFF2E7D32), modifier = Modifier.size(20.dp))
                }
            }
        }
    }
}

@Composable
private fun VotingTab() {
    val voting = listOf(
        VotingItem("Ketua Baru", listOf("Budi Santoso" to 28, "Ahmad Fauzi" to 14), true),
        VotingItem("Sekretaris Baru", listOf("Siti Rahayu" to 35, "Dewi Lestari" to 7), true),
        VotingItem("Setujui Laporan Keuangan", listOf("Setuju" to 40, "Tolak" to 2), false)
    )

    LazyColumn(modifier = Modifier.fillMaxSize().padding(16.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
        items(voting) { v ->
            Card(modifier = Modifier.fillMaxWidth(), colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceContainerLow), shape = RoundedCornerShape(12.dp)) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Filled.Poll, contentDescription = null, tint = Color(0xFF00897B))
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(v.judul, fontSize = 14.sp, fontWeight = FontWeight.Bold)
                    }
                    Spacer(modifier = Modifier.height(12.dp))
                    val total = v.options.sumOf { it.second }
                    v.options.forEach { (label, votes) ->
                        val pct = if (total > 0) votes.toFloat() / total else 0f
                        Row(modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp), verticalAlignment = Alignment.CenterVertically) {
                            Text(label, fontSize = 12.sp, modifier = Modifier.width(100.dp))
                            LinearProgressIndicator(progress = { pct }, modifier = Modifier.weight(1f).height(8.dp).clip(RoundedCornerShape(4.dp)), color = Color(0xFF00897B), trackColor = Color(0xFF00897B).copy(alpha = 0.15f))
                            Spacer(modifier = Modifier.width(8.dp))
                            Text("$votes", fontSize = 12.sp, fontWeight = FontWeight.Bold, modifier = Modifier.width(30.dp))
                        }
                    }
                    Spacer(modifier = Modifier.height(8.dp))
                    Text("Total suara: $total", fontSize = 11.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                }
            }
        }
    }
}

@Composable
private fun RekomendasiTab() {
    val items = listOf(
        "Tingkatkan layanan simpan pinjam dengan aplikasi digital",
        "Setiap anggota wajib setor minimal Rp 100.000/bulan",
        "SHU dibagikan setiap Januari setelah RAT",
        "Pengurus baru dilantik bulan Februari 2025"
    )

    LazyColumn(modifier = Modifier.fillMaxSize().padding(16.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
        item {
            Card(modifier = Modifier.fillMaxWidth(), colors = CardDefaults.cardColors(containerColor = Color(0xFF00897B)), shape = RoundedCornerShape(12.dp)) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text("Rekomendasi RAT 2025", fontSize = 16.sp, fontWeight = FontWeight.Bold, color = Color.White)
                    Text("Keputusan dan rekomendasi dari rapat", fontSize = 12.sp, color = Color.White.copy(alpha = 0.7f))
                }
            }
        }

        items(items) { item ->
            Card(modifier = Modifier.fillMaxWidth(), colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceContainerLow), shape = RoundedCornerShape(12.dp)) {
                Row(modifier = Modifier.padding(12.dp), verticalAlignment = Alignment.Top) {
                    Icon(Icons.Filled.Check, contentDescription = null, tint = Color(0xFF00897B), modifier = Modifier.size(20.dp))
                    Spacer(modifier = Modifier.width(12.dp))
                    Text(item, fontSize = 13.sp)
                }
            }
        }
    }
}

private data class AgendaItem(val no: String, val judul: String, val pemateri: String, val waktu: String, val selesai: Boolean)
private data class VotingItem(val judul: String, val options: List<Pair<String, Int>>, val selesai: Boolean)
