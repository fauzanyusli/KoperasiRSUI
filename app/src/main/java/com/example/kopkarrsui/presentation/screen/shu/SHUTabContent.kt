package com.example.kopkarrsui.presentation.screen.shu

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
import androidx.compose.material.icons.filled.Assessment
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.filled.PieChart
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.kopkarrsui.presentation.viewmodel.SHUViewModel

// ═══ TAB 1: PERHITUNGAN SHU OTOMATIS ═══

@Composable
internal fun PerhitunganTab(viewModel: SHUViewModel) {
    val financialStatement by viewModel.financialStatement.collectAsStateWithLifecycle()
    val labaBersih = 85_000_000L // placeholder — from financialStatement later
    val shuTotal = 68_000_000L // placeholder: 80% laba bersih
    val cadangan = 12_240_000L // placeholder: 18% shu
    val shuDibagikan = 55_760_000L // placeholder: 82% shu

    LazyColumn(
        modifier = Modifier.fillMaxSize().padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        // Header card
        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = Color(0xFF1565C0))
            ) {
                Column(modifier = Modifier.padding(20.dp)) {
                    Text("Perhitungan SHU Otomatis", fontSize = 18.sp, fontWeight = FontWeight.Bold, color = Color.White)
                    Text("Tahun ${java.time.Year.now().value}", fontSize = 13.sp, color = Color.White.copy(alpha = 0.7f))
                    Spacer(modifier = Modifier.height(16.dp))
                    Text("SHU Total", fontSize = 12.sp, color = Color.White.copy(alpha = 0.7f))
                    Text(viewModel.formatRupiah(shuTotal), fontSize = 28.sp, fontWeight = FontWeight.Bold, color = Color.White)
                }
            }
        }

        // Alur perhitungan
        item {
            Card(modifier = Modifier.fillMaxWidth(), colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceContainerLow), shape = RoundedCornerShape(12.dp)) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text("Alur Perhitungan", fontSize = 14.sp, fontWeight = FontWeight.Bold)
                    Spacer(modifier = Modifier.height(12.dp))

                    CalculationStep("1", "Laba Bersih Tahunan", viewModel.formatRupiah(labaBersih), "Hasil usaha setelah dikurangi biaya operasional", Color(0xFF1565C0))
                    CalculationStep("2", "Persentase SHU ke Anggota", "80%", "Bagian laba bersih yang dialokasikan untuk anggota", Color(0xFFFF8F00))
                    CalculationStep("3", "SHU Total untuk Dibagikan", viewModel.formatRupiah(shuTotal), "Laba bersih \u00D7 persentase SHU", Color(0xFF2E7D32))
                    CalculationStep("4", "Cadangan (18%)", viewModel.formatRupiah(cadangan), "Untuk pengembangan koperasi", Color(0xFFC62828))
                    CalculationStep("5", "SHU Bersih ke Anggota", viewModel.formatRupiah(shuDibagikan), "SHU total - cadangan", Color(0xFF6A1B9A))
                }
            }
        }

        // Rumus (placeholder)
        item {
            Card(modifier = Modifier.fillMaxWidth(), colors = CardDefaults.cardColors(containerColor = Color(0xFFFF8F00).copy(alpha = 0.08f)), shape = RoundedCornerShape(12.dp)) {
                Row(modifier = Modifier.padding(16.dp), verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Filled.Info, contentDescription = null, tint = Color(0xFFFF8F00))
                    Spacer(modifier = Modifier.width(12.dp))
                    Column {
                        Text("Rumus Perhitungan", fontSize = 13.sp, fontWeight = FontWeight.Medium)
                        Text("Rumus detail akan ditambahkan setelah data dikirim.", fontSize = 11.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                    }
                }
            }
        }

        // Komponen pendukung
        item {
            Card(modifier = Modifier.fillMaxWidth(), colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceContainerLow), shape = RoundedCornerShape(12.dp)) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text("Komponen Perhitungan", fontSize = 14.sp, fontWeight = FontWeight.Bold)
                    Spacer(modifier = Modifier.height(8.dp))
                    listOf(
                        "Modal Sendiri" to viewModel.formatRupiah(250_000_000),
                        "Total Aset" to viewModel.formatRupiah(500_000_000),
                        "Sisa Hasil Usaha" to viewModel.formatRupiah(shuTotal),
                        "Jumlah Anggota Aktif" to "47 orang",
                        "SHU per Anggota (rata-rata)" to viewModel.formatRupiah(shuDibagikan / 47)
                    ).forEach { (label, value) ->
                        Row(modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp), horizontalArrangement = Arrangement.SpaceBetween) {
                            Text(label, fontSize = 12.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                            Text(value, fontSize = 12.sp, fontWeight = FontWeight.Medium)
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun CalculationStep(num: String, title: String, value: String, desc: String, color: Color) {
    Row(modifier = Modifier.padding(vertical = 6.dp), verticalAlignment = Alignment.Top) {
        Box(modifier = Modifier.size(28.dp).background(color, CircleShape), contentAlignment = Alignment.Center) {
            Text(num, fontSize = 12.sp, fontWeight = FontWeight.Bold, color = Color.White)
        }
        Spacer(modifier = Modifier.width(12.dp))
        Column(modifier = Modifier.weight(1f)) {
            Text(title, fontSize = 13.sp, fontWeight = FontWeight.Medium)
            Text(desc, fontSize = 11.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
        }
        Text(value, fontSize = 13.sp, fontWeight = FontWeight.Bold, color = color)
    }
}

// ═══ TAB 2: DISTRIBUSI PROPORSIONAL ═══

@Composable
internal fun DistribusiTab(viewModel: SHUViewModel) {
    val members = listOf(
        MemberSHUDistribution("KPR-001", "Budi Santoso", "Ketua", 3_200_000, 4.8, 2_500_000),
        MemberSHUDistribution("KPR-002", "Siti Rahayu", "Sekretaris", 2_800_000, 4.2, 1_800_000),
        MemberSHUDistribution("KPR-003", "Ahmad Fauzi", "Bendahara", 3_500_000, 5.3, 3_200_000),
        MemberSHUDistribution("KPR-004", "Dewi Lestari", "Anggota", 1_200_000, 1.8, 950_000),
        MemberSHUDistribution("KPR-005", "Rizky Pratama", "Anggota", 1_800_000, 2.7, 1_200_000)
    )
    val totalDibagikan = members.sumOf { it.jumlahSHU }
    var selectedFilter by remember { mutableIntStateOf(0) }

    LazyColumn(
        modifier = Modifier.fillMaxSize().padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        // Header
        item {
            Card(modifier = Modifier.fillMaxWidth(), shape = RoundedCornerShape(16.dp), colors = CardDefaults.cardColors(containerColor = Color(0xFF2E7D32))) {
                Column(modifier = Modifier.padding(20.dp)) {
                    Text("Distribusi Proporsional", fontSize = 18.sp, fontWeight = FontWeight.Bold, color = Color.White)
                    Text("Berdasarkan kontribusi anggota", fontSize = 12.sp, color = Color.White.copy(alpha = 0.7f))
                    Spacer(modifier = Modifier.height(12.dp))
                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                        Column { Text("Total Didistribusi", fontSize = 11.sp, color = Color.White.copy(alpha = 0.7f)); Text(viewModel.formatRupiah(totalDibagikan), fontSize = 16.sp, fontWeight = FontWeight.Bold, color = Color.White) }
                        Column(horizontalAlignment = Alignment.End) { Text("Anggota", fontSize = 11.sp, color = Color.White.copy(alpha = 0.7f)); Text("${members.size} orang", fontSize = 16.sp, fontWeight = FontWeight.Bold, color = Color.White) }
                    }
                }
            }
        }

        // Komponen distribusi
        item {
            Card(modifier = Modifier.fillMaxWidth(), colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceContainerLow), shape = RoundedCornerShape(12.dp)) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text("Komponen Distribusi", fontSize = 14.sp, fontWeight = FontWeight.Bold)
                    Spacer(modifier = Modifier.height(8.dp))
                    listOf(
                        "Simpanan Pokok" to "Bobot 10%",
                        "Simpanan Wajib" to "Bobot 40%",
                        "Simpanan Sukarela" to "Bobot 20%",
                        "Masa Keanggotaan" to "Bobot 15%",
                        "Frekuensi Transaksi" to "Bobot 15%"
                    ).forEach { (label, bobot) ->
                        Row(modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp), horizontalArrangement = Arrangement.SpaceBetween) {
                            Text(label, fontSize = 12.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                            Text(bobot, fontSize = 12.sp, fontWeight = FontWeight.Medium)
                        }
                    }
                }
            }
        }

        // Filter chips placeholder
        item {
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                listOf("Semua", "10 Besar", "Ketua/Bendahara").forEachIndexed { i, label ->
                    androidx.compose.material3.FilterChip(
                        selected = selectedFilter == i,
                        onClick = { selectedFilter = i },
                        label = { Text(label, fontSize = 11.sp) }
                    )
                }
            }
        }

        // Member distribution list
        items(members) { m ->
            Card(modifier = Modifier.fillMaxWidth(), colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceContainerLow), shape = RoundedCornerShape(12.dp)) {
                Column(modifier = Modifier.padding(12.dp)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Box(modifier = Modifier.size(40.dp).background(Color(0xFF2E7D32).copy(alpha = 0.12f), RoundedCornerShape(8.dp)), contentAlignment = Alignment.Center) {
                            Text(m.nama.take(2).uppercase(), fontSize = 12.sp, fontWeight = FontWeight.Bold, color = Color(0xFF2E7D32))
                        }
                        Spacer(modifier = Modifier.width(12.dp))
                        Column(modifier = Modifier.weight(1f)) {
                            Text(m.nama, fontSize = 14.sp, fontWeight = FontWeight.Bold)
                            Text("${m.noAnggota} \u2022 ${m.jabatan}", fontSize = 11.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                        }
                        Column(horizontalAlignment = Alignment.End) {
                            Text(viewModel.formatRupiah(m.jumlahSHU), fontSize = 14.sp, fontWeight = FontWeight.Bold, color = Color(0xFF2E7D32))
                            Text("${m.persenDistribusi}%", fontSize = 11.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                        }
                    }
                    Spacer(modifier = Modifier.height(8.dp))
                    LinearProgressIndicator(
                        progress = { (m.persenDistribusi.toFloat() / 100f).coerceIn(0f, 1f) },
                        modifier = Modifier.fillMaxWidth().height(6.dp).clip(RoundedCornerShape(3.dp)),
                        color = Color(0xFF2E7D32),
                        trackColor = Color(0xFF2E7D32).copy(alpha = 0.15f)
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text("Simpanan: ${viewModel.formatRupiah(m.totalSimpanan)}", fontSize = 11.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                }
            }
        }
    }
}

// ═══ TAB 3: RIWAYAT SHU PER TAHUN ═══

@Composable
internal fun RiwayatSHUTab(viewModel: SHUViewModel) {
    val riwayat = listOf(
        SHUYearly("2025", 68_000_000, 47, 1_446_808, "Dibagikan"),
        SHUYearly("2024", 55_000_000, 43, 1_279_069, "Dicairkan"),
        SHUYearly("2023", 42_000_000, 38, 1_105_263, "Dicairkan"),
        SHUYearly("2022", 35_000_000, 35, 1_000_000, "Dicairkan"),
        SHUYearly("2021", 28_000_000, 32, 875_000, "Dicairkan")
    )

    LazyColumn(
        modifier = Modifier.fillMaxSize().padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        // Summary card
        item {
            Card(modifier = Modifier.fillMaxWidth(), shape = RoundedCornerShape(16.dp), colors = CardDefaults.cardColors(containerColor = Color(0xFF6A1B9A))) {
                Column(modifier = Modifier.padding(20.dp)) {
                    Text("Riwayat SHU 5 Tahun", fontSize = 18.sp, fontWeight = FontWeight.Bold, color = Color.White)
                    Spacer(modifier = Modifier.height(12.dp))
                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                        Column { Text("Total SHU (5 thn)", fontSize = 11.sp, color = Color.White.copy(alpha = 0.7f)); Text(viewModel.formatRupiah(riwayat.sumOf { it.totalSHU }), fontSize = 16.sp, fontWeight = FontWeight.Bold, color = Color.White) }
                        Column(horizontalAlignment = Alignment.End) { Text("Rata-rata/Tahun", fontSize = 11.sp, color = Color.White.copy(alpha = 0.7f)); Text(viewModel.formatRupiah(riwayat.sumOf { it.totalSHU } / riwayat.size), fontSize = 16.sp, fontWeight = FontWeight.Bold, color = Color.White) }
                    }
                }
            }
        }

        // Chart placeholder
        item {
            Card(modifier = Modifier.fillMaxWidth(), colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceContainerLow), shape = RoundedCornerShape(12.dp)) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text("Tren SHU 5 Tahun Terakhir", fontSize = 14.sp, fontWeight = FontWeight.Bold)
                    Spacer(modifier = Modifier.height(12.dp))
                    val maxSHU = riwayat.maxOf { it.totalSHU }
                    riwayat.reversed().forEach { r ->
                        Row(modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp), verticalAlignment = Alignment.CenterVertically) {
                            Text(r.tahun, fontSize = 12.sp, modifier = Modifier.width(40.dp))
                            LinearProgressIndicator(
                                progress = { (r.totalSHU.toFloat() / maxSHU).coerceIn(0f, 1f) },
                                modifier = Modifier.weight(1f).height(16.dp).clip(RoundedCornerShape(4.dp)),
                                color = Color(0xFF6A1B9A),
                                trackColor = Color(0xFF6A1B9A).copy(alpha = 0.15f)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(viewModel.formatRupiah(r.totalSHU), fontSize = 11.sp, fontWeight = FontWeight.Medium, modifier = Modifier.width(100.dp), textAlign = TextAlign.End)
                        }
                    }
                }
            }
        }

        // Detail per tahun
        items(riwayat) { r ->
            val statusColor = if (r.status == "Dicairkan") Color(0xFF2E7D32) else Color(0xFFFF8F00)
            Card(modifier = Modifier.fillMaxWidth(), colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceContainerLow), shape = RoundedCornerShape(12.dp)) {
                Column(modifier = Modifier.padding(12.dp)) {
                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
                        Column {
                            Text("Tahun ${r.tahun}", fontSize = 16.sp, fontWeight = FontWeight.Bold)
                            Text("${r.jumlahAnggota} anggota", fontSize = 11.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                        }
                        Column(horizontalAlignment = Alignment.End) {
                            Text(viewModel.formatRupiah(r.totalSHU), fontSize = 16.sp, fontWeight = FontWeight.Bold, color = Color(0xFF6A1B9A))
                            Text(r.status, fontSize = 11.sp, color = statusColor)
                        }
                    }
                    Spacer(modifier = Modifier.height(8.dp))
                    HorizontalDivider()
                    Spacer(modifier = Modifier.height(8.dp))
                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                        Text("Rata-rata per anggota", fontSize = 12.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                        Text(viewModel.formatRupiah(r.rataRataPerAnggota), fontSize = 12.sp, fontWeight = FontWeight.Bold)
                    }
                }
            }
        }
    }
}

// ═══ TAB 4: LAPORAN DETAIL SHU ═══

@Composable
internal fun LaporanDetailTab(viewModel: SHUViewModel) {
    val mySHU by viewModel.mySHU.collectAsStateWithLifecycle()

    LazyColumn(
        modifier = Modifier.fillMaxSize().padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        // Header
        item {
            Card(modifier = Modifier.fillMaxWidth(), shape = RoundedCornerShape(16.dp), colors = CardDefaults.cardColors(containerColor = Color(0xFF1565C0))) {
                Column(modifier = Modifier.padding(20.dp)) {
                    Text("Laporan Detail SHU", fontSize = 18.sp, fontWeight = FontWeight.Bold, color = Color.White)
                    Text("Tahun ${java.time.Year.now().value} \u2022 Anggota: KPR-001", fontSize = 12.sp, color = Color.White.copy(alpha = 0.7f))
                }
            }
        }

        // Status
        item {
            val status = mySHU?.status
            val statusLabel = status?.label ?: "Belum Dihitung"
            val statusColor = when (status) {
                com.example.kopkarrsui.data.local.entity.SHUAllocation.SHUStatus.DIHITUNG -> Color(0xFFFF8F00)
                com.example.kopkarrsui.data.local.entity.SHUAllocation.SHUStatus.DIBAGIKAN -> Color(0xFF2E7D32)
                com.example.kopkarrsui.data.local.entity.SHUAllocation.SHUStatus.DICAIRKAN -> Color(0xFF1565C0)
                else -> Color.Gray
            }
            Card(modifier = Modifier.fillMaxWidth(), colors = CardDefaults.cardColors(containerColor = statusColor.copy(alpha = 0.08f)), shape = RoundedCornerShape(12.dp)) {
                Row(modifier = Modifier.padding(16.dp), verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Filled.CheckCircle, contentDescription = null, tint = statusColor)
                    Spacer(modifier = Modifier.width(12.dp))
                    Column { Text("Status SHU", fontSize = 12.sp, color = MaterialTheme.colorScheme.onSurfaceVariant); Text(statusLabel, fontSize = 14.sp, fontWeight = FontWeight.Bold, color = statusColor) }
                }
            }
        }

        // Detail perhitungan untuk anggota ini
        item {
            Card(modifier = Modifier.fillMaxWidth(), colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceContainerLow), shape = RoundedCornerShape(12.dp)) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text("Detail Perhitungan SHU Anda", fontSize = 14.sp, fontWeight = FontWeight.Bold)
                    Spacer(modifier = Modifier.height(12.dp))
                    listOf(
                        "Simpanan Pokok" to "Rp 100.000",
                        "Simpanan Wajib (total)" to "Rp 2.500.000",
                        "Simpanan Sukarela (total)" to "Rp 3.200.000",
                        "Masa Keanggotaan" to "5 tahun",
                        "Total Transaksi" to "120 transaksi",
                        "Kontribusi terhadap SHU" to "4.8%"
                    ).forEach { (label, value) ->
                        Row(modifier = Modifier.fillMaxWidth().padding(vertical = 6.dp), horizontalArrangement = Arrangement.SpaceBetween) {
                            Text(label, fontSize = 12.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                            Text(value, fontSize = 12.sp, fontWeight = FontWeight.Medium)
                        }
                        HorizontalDivider()
                    }
                    Spacer(modifier = Modifier.height(8.dp))
                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                        Text("SHU Anda", fontSize = 14.sp, fontWeight = FontWeight.Bold, color = Color(0xFF1565C0))
                        Text(viewModel.formatRupiah(mySHU?.jumlah ?: 3_200_000), fontSize = 16.sp, fontWeight = FontWeight.Bold, color = Color(0xFF1565C0))
                    }
                }
            }
        }

        // Timeline
        item {
            Card(modifier = Modifier.fillMaxWidth(), colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceContainerLow), shape = RoundedCornerShape(12.dp)) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text("Timeline", fontSize = 14.sp, fontWeight = FontWeight.Bold)
                    Spacer(modifier = Modifier.height(8.dp))
                    TimelineItem("Perhitungan SHU", "15 Jan 2025", true)
                    TimelineItem("Pengumuman di RAT", "20 Jan 2025", true)
                    TimelineItem("Distribusi ke Anggota", "25 Jan 2025", false)
                    TimelineItem("Pencairan ke Tabungan", "01 Feb 2025", false)
                }
            }
        }
    }
}

@Composable
private fun TimelineItem(title: String, date: String, done: Boolean) {
    Row(modifier = Modifier.padding(vertical = 4.dp), verticalAlignment = Alignment.CenterVertically) {
        Box(modifier = Modifier.size(12.dp).background(if (done) Color(0xFF2E7D32) else Color.Gray.copy(alpha = 0.3f), CircleShape))
        Spacer(modifier = Modifier.width(12.dp))
        Column(modifier = Modifier.weight(1f)) { Text(title, fontSize = 12.sp, fontWeight = FontWeight.Medium); Text(date, fontSize = 11.sp, color = MaterialTheme.colorScheme.onSurfaceVariant) }
        if (done) Icon(Icons.Filled.CheckCircle, contentDescription = null, tint = Color(0xFF2E7D32), modifier = Modifier.size(16.dp))
    }
}

// ═══ TAB 5: TRANSPARANSI PEMBAGIAN ═══

@Composable
internal fun TransparansiTab(viewModel: SHUViewModel) {
    val data = listOf(
        TransparansiItem("Total Laba Bersih", "85.000.000", "Hasil usaha setelah biaya operasional"),
        TransparansiItem("Bagian Cadangan", "12.240.000", "18% dari SHU untuk pengembangan koperasi"),
        TransparansiItem("Bagian Anggota", "55.760.000", "82% dari SHU didistribusikan ke anggota"),
        TransparansiItem("Dasar Pembagian", "Proporsional", "Berdasarkan simpanan + masa keanggotaan"),
        TransparansiItem("Proses", "Otomatis", "Dihitung otomatis oleh sistem koperasi"),
        TransparansiItem("Pengawasan", "Dewan Pengawas", "Diuji dan disetujui sebelum distribusi")
    )

    LazyColumn(
        modifier = Modifier.fillMaxSize().padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        // Header
        item {
            Card(modifier = Modifier.fillMaxWidth(), shape = RoundedCornerShape(16.dp), colors = CardDefaults.cardColors(containerColor = Color(0xFF00897B))) {
                Column(modifier = Modifier.padding(20.dp)) {
                    Text("Transparansi Pembagian SHU", fontSize = 18.sp, fontWeight = FontWeight.Bold, color = Color.White)
                    Text("Hak Anda untuk mengetahui", fontSize = 12.sp, color = Color.White.copy(alpha = 0.7f))
                }
            }
        }

        // Pie chart placeholder
        item {
            Card(modifier = Modifier.fillMaxWidth(), colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceContainerLow), shape = RoundedCornerShape(12.dp)) {
                Column(modifier = Modifier.padding(16.dp), horizontalAlignment = Alignment.CenterHorizontally) {
                    Text("Alokasi SHU", fontSize = 14.sp, fontWeight = FontWeight.Bold)
                    Spacer(modifier = Modifier.height(12.dp))
                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceEvenly) {
                        PieLegend(Color(0xFF2E7D32), "Anggota", "82%")
                        PieLegend(Color(0xFFFF8F00), "Cadangan", "18%")
                    }
                    Spacer(modifier = Modifier.height(8.dp))
                    // Simple bar representation
                    Row(modifier = Modifier.fillMaxWidth().height(24.dp).clip(RoundedCornerShape(12.dp))) {
                        Box(modifier = Modifier.weight(82f).background(Color(0xFF2E7D32)))
                        Box(modifier = Modifier.weight(18f).background(Color(0xFFFF8F00)))
                    }
                }
            }
        }

        // Detail items
        items(data) { item ->
            Card(modifier = Modifier.fillMaxWidth(), colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceContainerLow), shape = RoundedCornerShape(12.dp)) {
                Column(modifier = Modifier.padding(12.dp)) {
                    Text(item.judul, fontSize = 14.sp, fontWeight = FontWeight.Bold)
                    Text(item.nilai, fontSize = 16.sp, fontWeight = FontWeight.Bold, color = Color(0xFF00897B))
                    Text(item.keterangan, fontSize = 11.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                }
            }
        }

        // Disclaimer
        item {
            Card(modifier = Modifier.fillMaxWidth(), colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceContainerLow), shape = RoundedCornerShape(12.dp)) {
                Row(modifier = Modifier.padding(12.dp), verticalAlignment = Alignment.Top) {
                    Icon(Icons.Filled.Info, contentDescription = null, tint = MaterialTheme.colorScheme.onSurfaceVariant, modifier = Modifier.size(20.dp))
                    Spacer(modifier = Modifier.width(8.dp))
                    Column {
                        Text("Catatan", fontSize = 12.sp, fontWeight = FontWeight.Bold)
                        Text("Data perhitungan bersifat sementara dan akan diperbarui setelah data rumin dikirim. Semua anggota berhak melihat detail pembagian SHU.", fontSize = 11.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                    }
                }
            }
        }
    }
}

@Composable
private fun PieLegend(color: Color, label: String, pct: String) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Box(modifier = Modifier.size(12.dp).background(color, RoundedCornerShape(3.dp)))
        Spacer(modifier = Modifier.width(6.dp))
        Text("$label ($pct)", fontSize = 12.sp, fontWeight = FontWeight.Medium)
    }
}

// ═══ DATA MODELS ═══

private data class MemberSHUDistribution(val noAnggota: String, val nama: String, val jabatan: String, val jumlahSHU: Long, val persenDistribusi: Double, val totalSimpanan: Long)
private data class SHUYearly(val tahun: String, val totalSHU: Long, val jumlahAnggota: Int, val rataRataPerAnggota: Long, val status: String)
private data class TransparansiItem(val judul: String, val nilai: String, val keterangan: String)
