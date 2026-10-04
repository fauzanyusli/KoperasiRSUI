package com.example.kopkarrsui.presentation.screen.savings
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDownward
import androidx.compose.material.icons.filled.ArrowUpward
import androidx.compose.material.icons.filled.Calculate
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableDoubleStateOf
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
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.kopkarrsui.data.local.entity.SavingsAccount
import com.example.kopkarrsui.presentation.component.EmptyState
import com.example.kopkarrsui.presentation.viewmodel.SavingsViewModel
import com.example.kopkarrsui.util.formatRupiah

// ═══ SIMPANAN TAB CONTENT ═══

@Composable
internal fun SimpananPokok() {
    var showOk by remember { mutableStateOf(false) }
    val paid = true

    LazyColumn(
        modifier = Modifier.fillMaxSize().padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        item { InfoCard("Simpanan Pokok", "Simpanan satu kali saat menjadi anggota", Icons.Filled.Calculate, KopkarBlue) }
        item {
            DetailCard(
                listOf("Jumlah" to "Rp 100.000", "Status" to if (paid) "Lunas" else "Belum Bayar", "Tanggal" to "15 Jan 2020", "Bunga" to "Tidak ada"),
                if (paid) KopkarGreen else KopkarRed
            )
        }
        if (!paid) {
            item { PrimaryButton("Bayar Simpanan Pokok", Icons.Filled.Calculate, KopkarBlue) { showOk = true } }
        } else {
            item { StatusCard("Simpanan Pokok Lunas", "Anda sudah membayar simpanan pokok", KopkarGreen) }
        }
    }
    if (showOk) SuccessDialog("Simpanan pokok Rp 100.000 telah terbayar.") { showOk = false }
}

@Composable
internal fun SimpananWajib(vm: SavingsViewModel) {
    val accounts by vm.accounts.collectAsStateWithLifecycle()
    val acc = accounts.firstOrNull { it.jenis == SavingsAccount.SavingsType.WAJIB }
    val saldo = acc?.saldo ?: 0L
    val progress = (saldo.toFloat() / 10_000_000L).coerceIn(0f, 1f)

    LazyColumn(modifier = Modifier.fillMaxSize().padding(16.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
        item {
            Card(modifier = Modifier.fillMaxWidth(), shape = RoundedCornerShape(16.dp), colors = CardDefaults.cardColors(containerColor = KopkarGreen)) {
                Column(modifier = Modifier.padding(20.dp)) {
                    Text("Simpanan Wajib", fontSize = TextSize.s14, color = Color.White.copy(alpha = 0.8f))
                    Text(vm.formatRupiah(saldo), fontSize = TextSize.s28, fontWeight = FontWeight.Bold, color = Color.White)
                    Spacer(modifier = Modifier.height(12.dp))
                    LinearProgressIndicator(progress = { progress }, modifier = Modifier.fillMaxWidth().height(8.dp).clip(RoundedCornerShape(4.dp)), color = Color.White, trackColor = Color.White.copy(alpha = 0.3f))
                    Spacer(modifier = Modifier.height(4.dp))
                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                        Text("Target: Rp 10.000.000", fontSize = TextSize.s11, color = Color.White.copy(alpha = 0.7f))
                        Text("${(progress * 100).toInt()}%", fontSize = TextSize.s11, color = Color.White, fontWeight = FontWeight.Bold)
                    }
                }
            }
        }
        item { InfoDetailCard(listOf("Bunga/Tahun" to "3%", "Setoran Min" to "Rp 100.000/bulan", "Perhitungan" to "Bulanan, masuk ke saldo", "Penarikan" to "Bisa kapan saja")) }
        item { DualButton("Setor", KopkarGreen, "Tarik", KopkarRed) }
    }
}

@Composable
internal fun SimpananSukarela(vm: SavingsViewModel) {
    val accounts by vm.accounts.collectAsStateWithLifecycle()
    val acc = accounts.firstOrNull { it.jenis == SavingsAccount.SavingsType.SUKARELA }
    val saldo = acc?.saldo ?: 0L

    LazyColumn(modifier = Modifier.fillMaxSize().padding(16.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
        item {
            Card(modifier = Modifier.fillMaxWidth(), shape = RoundedCornerShape(16.dp), colors = CardDefaults.cardColors(containerColor = KopkarPurple)) {
                Column(modifier = Modifier.padding(20.dp)) {
                    Text("Simpanan Sukarela", fontSize = TextSize.s14, color = Color.White.copy(alpha = 0.8f))
                    Text(vm.formatRupiah(saldo), fontSize = TextSize.s28, fontWeight = FontWeight.Bold, color = Color.White)
                    Spacer(modifier = Modifier.height(8.dp))
                    Text("Bunga 5% per tahun", fontSize = TextSize.s12, color = Color.White.copy(alpha = 0.7f))
                }
            }
        }
        item { InfoDetailCard(listOf("Bunga/Tahun" to "5%", "Setoran Min" to "Rp 50.000", "Perhitungan" to "Bulanan, masuk ke saldo", "Penarikan" to "Bisa kapan saja")) }
        item { DualButton("Setor", KopkarPurple, "Tarik", KopkarRed) }
    }
}

@Composable
internal fun RiwayatSimpanan() {
    val data = listOf(
        SavingsHistory(1, "Setor Simpanan Wajib", "Wajib", 500_000.0, "27 Sep 2025", true),
        SavingsHistory(2, "Setor Simpanan Sukarela", "Sukarela", 200_000.0, "25 Sep 2025", true),
        SavingsHistory(3, "Bunga Simpanan Wajib", "Wajib", 15_000.0, "01 Sep 2025", true),
        SavingsHistory(4, "Penarikan Sukarela", "Sukarela", -100_000.0, "20 Ags 2025", false)
    )

    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            FilterChip(selected = true, onClick = {}, label = { Text("Semua", fontSize = TextSize.s11) }, colors = FilterChipDefaults.filterChipColors(selectedContainerColor = MaterialTheme.colorScheme.primaryContainer))
            FilterChip(selected = false, onClick = {}, label = { Text("30 Hari", fontSize = TextSize.s11) })
        }
        Spacer(modifier = Modifier.height(12.dp))
        LazyColumn(verticalArrangement = Arrangement.spacedBy(8.dp)) {
            items(data) { h ->
                Card(modifier = Modifier.fillMaxWidth(), colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceContainerLow), shape = RoundedCornerShape(12.dp)) {
                    Row(modifier = Modifier.padding(12.dp), verticalAlignment = Alignment.CenterVertically) {
                        Box(modifier = Modifier.size(40.dp).background(if (h.isIncome) KopkarGreen.copy(alpha = 0.12f) else KopkarRed.copy(alpha = 0.12f), RoundedCornerShape(8.dp)), contentAlignment = Alignment.Center) {
                            Icon(if (h.isIncome) Icons.Filled.ArrowDownward else Icons.Filled.ArrowUpward, contentDescription = null, tint = if (h.isIncome) KopkarGreen else KopkarRed, modifier = Modifier.size(20.dp))
                        }
                        Spacer(modifier = Modifier.width(12.dp))
                        Column(modifier = Modifier.weight(1f)) {
                            Text(h.deskripsi, fontSize = TextSize.s13, fontWeight = FontWeight.Medium)
                            Text("${h.jenis} \u2022 ${h.tanggal}", fontSize = TextSize.s11, color = MaterialTheme.colorScheme.onSurfaceVariant)
                        }
                        Text("${if (h.isIncome) "+" else "-"}${formatRupiah(kotlin.math.abs(h.jumlah))}", fontSize = TextSize.s13, fontWeight = FontWeight.Bold, color = if (h.isIncome) KopkarGreen else KopkarRed)
                    }
                }
            }
        }
    }
}

@Composable
internal fun LaporanSimpanan() {
    var q by remember { mutableStateOf("") }
    val data = listOf(
        MemberSavings("KPR-001", "Budi Santoso", "Ketua", 2_500_000.0, 1_800_000.0, 3_200_000.0),
        MemberSavings("KPR-002", "Siti Rahayu", "Sekretaris", 1_800_000.0, 1_200_000.0, 2_500_000.0),
        MemberSavings("KPR-003", "Ahmad Fauzi", "Bendahara", 3_200_000.0, 2_100_000.0, 5_000_000.0)
    )
    val filtered = data.filter { it.nama.contains(q, ignoreCase = true) || it.noAnggota.contains(q, ignoreCase = true) }

    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        androidx.compose.material3.OutlinedTextField(value = q, onValueChange = { q = it }, modifier = Modifier.fillMaxWidth(), placeholder = { Text("Cari anggota...") }, singleLine = true, shape = RoundedCornerShape(12.dp))
        Spacer(modifier = Modifier.height(12.dp))
        Card(modifier = Modifier.fillMaxWidth(), colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primaryContainer), shape = RoundedCornerShape(12.dp)) {
            Row(modifier = Modifier.fillMaxWidth().padding(12.dp), horizontalArrangement = Arrangement.SpaceEvenly) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) { Text("Pokok", fontSize = TextSize.s11); Text(formatRupiah(data.sumOf { it.pokok }), fontSize = TextSize.s13, fontWeight = FontWeight.Bold) }
                Column(horizontalAlignment = Alignment.CenterHorizontally) { Text("Wajib", fontSize = TextSize.s11); Text(formatRupiah(data.sumOf { it.wajib }), fontSize = TextSize.s13, fontWeight = FontWeight.Bold) }
                Column(horizontalAlignment = Alignment.CenterHorizontally) { Text("Sukarela", fontSize = TextSize.s11); Text(formatRupiah(data.sumOf { it.sukarela }), fontSize = TextSize.s13, fontWeight = FontWeight.Bold) }
            }
        }
        Spacer(modifier = Modifier.height(12.dp))
        LazyColumn(verticalArrangement = Arrangement.spacedBy(8.dp)) {
            items(filtered) { m ->
                Card(modifier = Modifier.fillMaxWidth(), colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceContainerLow), shape = RoundedCornerShape(12.dp)) {
                    Column(modifier = Modifier.padding(12.dp)) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Box(modifier = Modifier.size(36.dp).background(KopkarBlue.copy(alpha = 0.12f), RoundedCornerShape(8.dp)), contentAlignment = Alignment.Center) {
                                Text(m.nama.take(2).uppercase(), fontSize = TextSize.s12, fontWeight = FontWeight.Bold, color = KopkarBlue)
                            }
                            Spacer(modifier = Modifier.width(10.dp))
                            Column(modifier = Modifier.weight(1f)) {
                                Text(m.nama, fontSize = TextSize.s13, fontWeight = FontWeight.Bold)
                                Text("${m.noAnggota} \u2022 ${m.jabatan}", fontSize = TextSize.s10, color = MaterialTheme.colorScheme.onSurfaceVariant)
                            }
                        }
                        Spacer(modifier = Modifier.height(8.dp))
                        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                            Column { Text("Pokok", fontSize = TextSize.s10, color = MaterialTheme.colorScheme.onSurfaceVariant); Text(formatRupiah(m.pokok), fontSize = TextSize.s12) }
                            Column { Text("Wajib", fontSize = TextSize.s10, color = MaterialTheme.colorScheme.onSurfaceVariant); Text(formatRupiah(m.wajib), fontSize = TextSize.s12) }
                            Column { Text("Sukarela", fontSize = TextSize.s10, color = MaterialTheme.colorScheme.onSurfaceVariant); Text(formatRupiah(m.sukarela), fontSize = TextSize.s12) }
                            Column(horizontalAlignment = Alignment.End) { Text("Total", fontSize = TextSize.s10, color = MaterialTheme.colorScheme.onSurfaceVariant); Text(formatRupiah(m.pokok + m.wajib + m.sukarela), fontSize = TextSize.s12, fontWeight = FontWeight.Bold, color = KopkarBlue) }
                        }
                    }
                }
            }
        }
    }
}

// ═══ PINJAMAN TAB CONTENT ═══

@Composable
internal fun LoanTypeSection(type: LoanType) {
    val loans = when (type) {
        LoanType.KONSUMTIF -> listOf(Loan(1, 1, "Budi Santoso", type, 5_000_000.0, 2_500_000.0, 437_500.0, "15 Jan 2025", "15 Jan 2026", Loan.LoanStatus.AKTIF, 12))
        LoanType.PRODUKTIF -> listOf(Loan(3, 1, "Budi Santoso", type, 20_000_000.0, 15_000_000.0, 916_667.0, "01 Mar 2025", "01 Mar 2027", Loan.LoanStatus.AKTIF, 24))
        LoanType.DARURAT -> listOf(Loan(4, 4, "Dewi Lestari", type, 3_000_000.0, 1_500_000.0, 525_000.0, "10 Sep 2025", "10 Mar 2026", Loan.LoanStatus.AKTIF, 6))
    }
    val total = loans.filter { it.status == Loan.LoanStatus.AKTIF }.sumOf { it.sisaBayar }
    val hc = when (type) { LoanType.KONSUMTIF -> KopkarBlue; LoanType.PRODUKTIF -> KopkarGreen; LoanType.DARURAT -> KopkarRed }

    LazyColumn(modifier = Modifier.fillMaxSize().padding(16.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
        item {
            Card(modifier = Modifier.fillMaxWidth(), shape = RoundedCornerShape(16.dp), colors = CardDefaults.cardColors(containerColor = hc)) {
                Column(modifier = Modifier.padding(20.dp)) {
                    Text(type.label, fontSize = TextSize.s18, fontWeight = FontWeight.Bold, color = Color.White)
                    Text(type.description, fontSize = TextSize.s12, color = Color.White.copy(alpha = 0.8f))
                    Spacer(modifier = Modifier.height(12.dp))
                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                        Column { Text("Maks", fontSize = TextSize.s11, color = Color.White.copy(alpha = 0.7f)); Text(formatRupiah(type.maxAmount), fontSize = TextSize.s14, fontWeight = FontWeight.Bold, color = Color.White) }
                        Column(horizontalAlignment = Alignment.End) { Text("Bunga/Tahun", fontSize = TextSize.s11, color = Color.White.copy(alpha = 0.7f)); Text("${type.bungaPersen}%", fontSize = TextSize.s14, fontWeight = FontWeight.Bold, color = Color.White) }
                    }
                    Spacer(modifier = Modifier.height(8.dp))
                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                        Column { Text("Tenor", fontSize = TextSize.s11, color = Color.White.copy(alpha = 0.7f)); Text("${type.tenorBulan} bulan", fontSize = TextSize.s14, fontWeight = FontWeight.Bold, color = Color.White) }
                        Column(horizontalAlignment = Alignment.End) { Text("Total Aktif", fontSize = TextSize.s11, color = Color.White.copy(alpha = 0.7f)); Text(formatRupiah(total), fontSize = TextSize.s14, fontWeight = FontWeight.Bold, color = Color.White) }
                    }
                }
            }
        }
        if (loans.isEmpty()) {
            item { EmptyState(icon = Icons.Filled.Calculate, title = "Belum Ada Pinjaman", message = "Ajukan ${type.label}", actionLabel = null, onAction = null) }
        } else {
            items(loans) { l -> LoanCard(l) }
        }
        item {
            androidx.compose.material3.OutlinedButton(onClick = {}, modifier = Modifier.fillMaxWidth(), shape = RoundedCornerShape(12.dp)) {
                Text("Ajukan ${type.label}", fontWeight = FontWeight.Medium)
            }
        }
    }
}

@Composable
internal fun SimulasiSection() {
    var jumlah by remember { mutableDoubleStateOf(0.0) }
    var tenor by remember { mutableIntStateOf(12) }
    var sel by remember { mutableIntStateOf(0) }
    val types = listOf(LoanType.KONSUMTIF, LoanType.PRODUKTIF, LoanType.DARURAT)
    val bunga = types[sel].bungaPersen
    val bpb = bunga / 100 / 12
    val cicilan = if (jumlah > 0 && tenor > 0) (jumlah + jumlah * bpb * tenor) / tenor else 0.0

    LazyColumn(modifier = Modifier.fillMaxSize().padding(16.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
        item { InfoCard("Simulasi Pinjaman", "Hitung estimasi cicilan", Icons.Filled.Calculate, MaterialTheme.colorScheme.primary) }
        item {
            Card(modifier = Modifier.fillMaxWidth(), colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceContainerLow), shape = RoundedCornerShape(12.dp)) {
                Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
                    Text("Jenis Pinjaman", fontSize = TextSize.s13, fontWeight = FontWeight.Medium)
                    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        types.forEachIndexed { i, t ->
                            FilterChip(selected = sel == i, onClick = { sel = i }, label = { Text(t.label.split(" ").last(), fontSize = TextSize.s11) }, colors = FilterChipDefaults.filterChipColors(selectedContainerColor = MaterialTheme.colorScheme.primaryContainer))
                        }
                    }
                    androidx.compose.material3.OutlinedTextField(value = if (jumlah > 0) jumlah.toLong().toString() else "", onValueChange = { jumlah = it.toDoubleOrNull() ?: 0.0 }, modifier = Modifier.fillMaxWidth(), label = { Text("Jumlah Pinjaman") }, prefix = { Text("Rp ") }, keyboardOptions = androidx.compose.foundation.text.KeyboardOptions(keyboardType = androidx.compose.ui.text.input.KeyboardType.Number), singleLine = true, shape = RoundedCornerShape(12.dp))
                    Text("Tenor: $tenor bulan", fontSize = TextSize.s13)
                    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        listOf(6, 12, 18, 24).forEach { t ->
                            FilterChip(selected = tenor == t, onClick = { tenor = t }, label = { Text("${t}bln", fontSize = TextSize.s11) })
                        }
                    }
                }
            }
        }
        if (jumlah > 0) {
            item {
                Card(modifier = Modifier.fillMaxWidth(), shape = RoundedCornerShape(12.dp), colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primary)) {
                    Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                        Text("Hasil Simulasi", fontSize = TextSize.s14, fontWeight = FontWeight.Bold, color = Color.White)
                        androidx.compose.material3.HorizontalDivider(color = Color.White.copy(alpha = 0.3f))
                        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) { Text("Pokok", fontSize = TextSize.s12, color = Color.White.copy(alpha = 0.8f)); Text(formatRupiah(jumlah), fontSize = TextSize.s12, color = Color.White) }
                        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) { Text("Bunga/Tahun", fontSize = TextSize.s12, color = Color.White.copy(alpha = 0.8f)); Text("$bunga%", fontSize = TextSize.s12, color = Color.White) }
                        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) { Text("Tenor", fontSize = TextSize.s12, color = Color.White.copy(alpha = 0.8f)); Text("$tenor bulan", fontSize = TextSize.s12, color = Color.White) }
                        androidx.compose.material3.HorizontalDivider(color = Color.White.copy(alpha = 0.3f))
                        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) { Text("Cicilan/Bulan", fontSize = TextSize.s13, fontWeight = FontWeight.Bold, color = Color.White); Text(formatRupiah(cicilan), fontSize = TextSize.s16, fontWeight = FontWeight.Bold, color = Color.White) }
                        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) { Text("Total Bayar", fontSize = TextSize.s12, color = Color.White.copy(alpha = 0.8f)); Text(formatRupiah(cicilan * tenor), fontSize = TextSize.s12, color = Color.White) }
                    }
                }
            }
            item {
                androidx.compose.material3.OutlinedButton(onClick = {}, modifier = Modifier.fillMaxWidth(), shape = RoundedCornerShape(12.dp)) {
                    Text("Ajukan Pinjaman Ini", fontWeight = FontWeight.Bold)
                }
            }
        }
    }
}

@Composable
internal fun CicilanOtomatis() {
    val data = listOf(
        CicilanItem(1, "Budi Santoso", "Konsumtif", 437_500.0, 5, 12, "Aktif", KopkarBlue),
        CicilanItem(3, "Budi Santoso", "Produktif", 916_667.0, 3, 24, "Aktif", KopkarGreen),
        CicilanItem(4, "Dewi Lestari", "Darurat", 525_000.0, 2, 6, "Aktif", KopkarRed)
    )

    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        Card(modifier = Modifier.fillMaxWidth(), colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primaryContainer), shape = RoundedCornerShape(12.dp)) {
            Row(modifier = Modifier.fillMaxWidth().padding(12.dp), horizontalArrangement = Arrangement.SpaceEvenly) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) { Text("${data.size}", fontSize = TextSize.s20, fontWeight = FontWeight.Bold); Text("Aktif", fontSize = TextSize.s11) }
                Column(horizontalAlignment = Alignment.CenterHorizontally) { Text(formatRupiah(data.sumOf { it.jumlahCicilan }), fontSize = TextSize.s14, fontWeight = FontWeight.Bold); Text("Total/Bulan", fontSize = TextSize.s11) }
            }
        }
        Spacer(modifier = Modifier.height(12.dp))
        LazyColumn(verticalArrangement = Arrangement.spacedBy(8.dp)) {
            items(data) { c ->
                Card(modifier = Modifier.fillMaxWidth(), colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceContainerLow), shape = RoundedCornerShape(12.dp)) {
                    Column(modifier = Modifier.padding(12.dp)) {
                        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
                            Column { Text(c.nama, fontSize = TextSize.s14, fontWeight = FontWeight.Bold); Text("Pinjaman ${c.jenis}", fontSize = TextSize.s11, color = MaterialTheme.colorScheme.onSurfaceVariant) }
                            Column(horizontalAlignment = Alignment.End) { Text(formatRupiah(c.jumlahCicilan), fontSize = TextSize.s14, fontWeight = FontWeight.Bold, color = c.color); Text("Per bulan", fontSize = TextSize.s10, color = MaterialTheme.colorScheme.onSurfaceVariant) }
                        }
                        Spacer(modifier = Modifier.height(8.dp))
                        LinearProgressIndicator(progress = { c.bulanDibayar.toFloat() / c.totalBulan }, modifier = Modifier.fillMaxWidth().height(6.dp).clip(RoundedCornerShape(3.dp)), color = c.color, trackColor = c.color.copy(alpha = 0.2f))
                        Spacer(modifier = Modifier.height(4.dp))
                        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                            Text("Bulan ${c.bulanDibayar}/${c.totalBulan}", fontSize = TextSize.s11, color = MaterialTheme.colorScheme.onSurfaceVariant)
                            Text("${((c.bulanDibayar.toFloat() / c.totalBulan) * 100).toInt()}%", fontSize = TextSize.s11, color = c.color, fontWeight = FontWeight.Medium)
                        }
                    }
                }
            }
        }
    }
}
