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
import androidx.compose.material.icons.filled.CardMembership
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Description
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.PersonAdd
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material.icons.filled.QrCode
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Assignment
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.Store
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.kopkarrsui.data.local.entity.Member

data class MenuItem(val id: Int, val title: String, val subtitle: String, val icon: ImageVector, val color: Color)

// ══════════════════════════════════════════════════════════════
// MAIN
// ══════════════════════════════════════════════════════════════

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MemberManagementScreen(onBack: () -> Unit) {
    var selectedSection by remember { mutableIntStateOf(0) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(when (selectedSection) {
                        1 -> "Pendaftaran Anggota"
                        2 -> "Profil Anggota"
                        3 -> "Kartu Anggota"
                        4 -> "Status Keanggotaan"
                        5 -> "Data Suplier"
                        else -> "Manajemen Anggota"
                    })
                },
                navigationIcon = {
                    IconButton(onClick = { if (selectedSection > 0) selectedSection = 0 else onBack() }) {
                        Icon(Icons.Filled.Close, contentDescription = "Kembali")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = MaterialTheme.colorScheme.primaryContainer, titleContentColor = MaterialTheme.colorScheme.onPrimaryContainer)
            )
        }
    ) { paddingValues ->
        Box(modifier = Modifier.padding(paddingValues)) {
            when (selectedSection) {
                0 -> MemberManagementHome(onNavigate = { selectedSection = it })
                1 -> RegistrationSection()
                2 -> ProfileSection()
                3 -> DigitalCardSection()
                4 -> MembershipStatusSection()
                5 -> SupplierSection()
            }
        }
    }
}

// ══════════════════════════════════════════════════════════════
// HOME
// ══════════════════════════════════════════════════════════════

@Composable
private fun MemberManagementHome(onNavigate: (Int) -> Unit) {
    val menuItems = listOf(
        MenuItem(1, "Pendaftaran Anggota Baru", "Daftarkan anggota baru ke koperasi", Icons.Filled.PersonAdd, Color(0xFF2E7D32)),
        MenuItem(2, "Profil Anggota Lengkap", "Lihat dan edit data profil anggota", Icons.Filled.Person, Color(0xFF1565C0)),
        MenuItem(3, "Kartu Anggota Digital", "Kartu identitas digital anggota", Icons.Filled.CardMembership, Color(0xFF6A1B9A)),
        MenuItem(4, "Status Keanggotaan", "Cek dan kelola status keanggotaan", Icons.Filled.Assignment, Color(0xFFE65100)),
        MenuItem(5, "Data Suplier", "Kelola daftar suplier koperasi", Icons.Filled.Store, Color(0xFF00695C))
    )

    LazyColumn(modifier = Modifier.fillMaxSize().padding(16.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
        item { Text("Pilih Fitur", fontSize = 16.sp, fontWeight = FontWeight.Bold) }
        items(menuItems) { item -> MenuCard(item = item, onClick = { onNavigate(item.id) }) }
        item { Spacer(modifier = Modifier.height(8.dp)); Text("Statistik Anggota", fontSize = 16.sp, fontWeight = FontWeight.Bold) }
        item { Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(12.dp)) { MiniStat("Total Anggota", "25", Color(0xFF1565C0), Modifier.weight(1f)); MiniStat("Aktif", "22", Color(0xFF2E7D32), Modifier.weight(1f)) } }
        item { Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(12.dp)) { MiniStat("Nonaktif", "2", Color(0xFFC62828), Modifier.weight(1f)); MiniStat("Keluar", "1", Color(0xFF9E9E9E), Modifier.weight(1f)) } }
    }
}

@Composable
private fun MenuCard(item: MenuItem, onClick: () -> Unit) {
    Card(onClick = onClick, modifier = Modifier.fillMaxWidth(), colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceContainerLow), shape = RoundedCornerShape(16.dp)) {
        Row(modifier = Modifier.fillMaxWidth().padding(16.dp), verticalAlignment = Alignment.CenterVertically) {
            Box(modifier = Modifier.size(48.dp).background(item.color.copy(alpha = 0.12f), RoundedCornerShape(12.dp)), contentAlignment = Alignment.Center) {
                Icon(item.icon, contentDescription = null, tint = item.color, modifier = Modifier.size(24.dp))
            }
            Spacer(modifier = Modifier.width(16.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(item.title, fontSize = 15.sp, fontWeight = FontWeight.Bold)
                Text(item.subtitle, fontSize = 12.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
            }
        }
    }
}

@Composable
private fun MiniStat(label: String, value: String, color: Color, modifier: Modifier = Modifier) {
    Card(modifier = modifier, colors = CardDefaults.cardColors(containerColor = color.copy(alpha = 0.08f)), shape = RoundedCornerShape(12.dp)) {
        Column(modifier = Modifier.fillMaxWidth().padding(12.dp), horizontalAlignment = Alignment.CenterHorizontally) {
            Text(value, fontSize = 24.sp, fontWeight = FontWeight.Bold, color = color)
            Text(label, fontSize = 11.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
        }
    }
}

// ══════════════════════════════════════════════════════════════
// SECTION 1: PENDAFTARAN
// ══════════════════════════════════════════════════════════════

@Composable
private fun RegistrationSection() {
    var noAnggota by remember { mutableStateOf("") }
    var nama by remember { mutableStateOf("") }
    var nik by remember { mutableStateOf("") }
    var noHp by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var alamat by remember { mutableStateOf("") }
    var jabatan by remember { mutableStateOf("Anggota") }
    var showSuccess by remember { mutableStateOf(false) }

    LazyColumn(modifier = Modifier.fillMaxSize().padding(16.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
        item {
            Card(modifier = Modifier.fillMaxWidth(), colors = CardDefaults.cardColors(containerColor = Color(0xFF2E7D32).copy(alpha = 0.08f)), shape = RoundedCornerShape(12.dp)) {
                Row(modifier = Modifier.padding(16.dp), verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Filled.PersonAdd, contentDescription = null, tint = Color(0xFF2E7D32), modifier = Modifier.size(32.dp))
                    Spacer(modifier = Modifier.width(12.dp))
                    Column { Text("Formulir Pendaftaran", fontSize = 16.sp, fontWeight = FontWeight.Bold); Text("Isi data diri anggota baru", fontSize = 12.sp, color = MaterialTheme.colorScheme.onSurfaceVariant) }
                }
            }
        }
        item { FormField("No. Anggota", noAnggota, { noAnggota = it }, "Contoh: KPR-026") }
        item { FormField("Nama Lengkap", nama, { nama = it }, "Masukkan nama sesuai KTP") }
        item { FormField("NIK (No. KTP)", nik, { nik = it }, "16 digit NIK") }
        item { FormField("No. Handphone", noHp, { noHp = it }, "08xxxxxxxxxx") }
        item { FormField("Email", email, { email = it }, "opsional") }
        item { FormField("Alamat", alamat, { alamat = it }, "Alamat lengkap") }
        item {
            Text("Jabatan", fontSize = 13.sp, fontWeight = FontWeight.Medium)
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                listOf("Anggota", "Ketua", "Sekretaris", "Bendahara").forEach { j ->
                    FilterChip(selected = jabatan == j, onClick = { jabatan = j }, label = { Text(j, fontSize = 12.sp) }, colors = FilterChipDefaults.filterChipColors(selectedContainerColor = MaterialTheme.colorScheme.primaryContainer))
                }
            }
        }
        item { FormField("Tanggal Gabung", "27 September 2025", { }, "Tanggal otomatis", enabled = false) }
        item { FormField("PIN Transaksi", "", { }, "6 digit angka", isPassword = true) }
        item {
            Spacer(modifier = Modifier.height(8.dp))
            OutlinedButton(onClick = { showSuccess = true }, modifier = Modifier.fillMaxWidth(), colors = ButtonDefaults.outlinedButtonColors(containerColor = Color(0xFF2E7D32), contentColor = Color.White), shape = RoundedCornerShape(12.dp)) {
                Icon(Icons.Filled.CheckCircle, contentDescription = null); Spacer(modifier = Modifier.width(8.dp)); Text("Daftarkan Anggota", fontWeight = FontWeight.Bold)
            }
        }
    }

    if (showSuccess) {
        AlertDialog(onDismissRequest = { showSuccess = false }, icon = { Icon(Icons.Filled.CheckCircle, contentDescription = null, tint = Color(0xFF2E7D32), modifier = Modifier.size(48.dp)) }, title = { Text("Pendaftaran Berhasil!", textAlign = TextAlign.Center) }, text = { Text("Anggota baru berhasil didaftarkan.", textAlign = TextAlign.Center) }, confirmButton = { TextButton(onClick = { showSuccess = false }) { Text("OK") } })
    }
}

@Composable
private fun FormField(label: String, value: String, onValueChange: (String) -> Unit, placeholder: String, enabled: Boolean = true, isPassword: Boolean = false) {
    Column {
        Text(label, fontSize = 13.sp, fontWeight = FontWeight.Medium)
        OutlinedTextField(value = value, onValueChange = onValueChange, modifier = Modifier.fillMaxWidth(), placeholder = { Text(placeholder, fontSize = 13.sp) }, singleLine = true, enabled = enabled, shape = RoundedCornerShape(12.dp), colors = OutlinedTextFieldDefaults.colors(unfocusedContainerColor = MaterialTheme.colorScheme.surfaceContainerLow))
    }
}

// ══════════════════════════════════════════════════════════════
// SECTION 2: PROFIL
// ══════════════════════════════════════════════════════════════

@Composable
private fun ProfileSection() {
    var searchQuery by remember { mutableStateOf("") }
    var selectedMember by remember { mutableStateOf<MemberUI?>(null) }
    val sampleMembers = remember {
        listOf(
            MemberUI(1, "KPR-001", "Budi Santoso", "Ketua", "081234567890", Member.MemberStatus.AKTIF, 2_500_000.0, 5_000_000.0, "15 Jan 2020"),
            MemberUI(2, "KPR-002", "Siti Rahayu", "Sekretaris", "081234567891", Member.MemberStatus.AKTIF, 1_800_000.0, 3_000_000.0, "20 Mar 2020"),
            MemberUI(3, "KPR-003", "Ahmad Fauzi", "Bendahara", "081234567892", Member.MemberStatus.AKTIF, 3_200_000.0, 10_000_000.0, "10 Jun 2020"),
            MemberUI(4, "KPR-004", "Dewi Lestari", "Anggota", "081234567893", Member.MemberStatus.AKTIF, 950_000.0, 0.0, "05 Jan 2021"),
            MemberUI(5, "KPR-005", "Rizky Pratama", "Anggota", "081234567894", Member.MemberStatus.NONAKTIF, 1_200_000.0, 2_000_000.0, "15 Jun 2021")
        )
    }
    val filtered = remember(searchQuery) { sampleMembers.filter { it.nama.contains(searchQuery, ignoreCase = true) || it.noAnggota.contains(searchQuery, ignoreCase = true) } }

    if (selectedMember != null) {
        MemberProfileDetail(member = selectedMember!!, onBack = { selectedMember = null })
    } else {
        Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
            OutlinedTextField(value = searchQuery, onValueChange = { searchQuery = it }, modifier = Modifier.fillMaxWidth(), placeholder = { Text("Cari anggota...") }, leadingIcon = { Icon(Icons.Filled.Search, contentDescription = null) }, singleLine = true, shape = RoundedCornerShape(12.dp), colors = OutlinedTextFieldDefaults.colors(unfocusedContainerColor = MaterialTheme.colorScheme.surfaceContainerLow))
            Spacer(modifier = Modifier.height(12.dp))
            LazyColumn(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                items(filtered, key = { it.id }) { member ->
                    Card(onClick = { selectedMember = member }, modifier = Modifier.fillMaxWidth(), colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceContainerLow), shape = RoundedCornerShape(12.dp)) {
                        Row(modifier = Modifier.padding(12.dp), verticalAlignment = Alignment.CenterVertically) {
                            Box(modifier = Modifier.size(44.dp).background(Color(0xFF1565C0).copy(alpha = 0.12f), CircleShape), contentAlignment = Alignment.Center) { Text(member.nama.take(2).uppercase(), fontSize = 14.sp, fontWeight = FontWeight.Bold, color = Color(0xFF1565C0)) }
                            Spacer(modifier = Modifier.width(12.dp))
                            Column(modifier = Modifier.weight(1f)) { Text(member.nama, fontSize = 14.sp, fontWeight = FontWeight.Bold); Text("${member.noAnggota} • ${member.jabatan}", fontSize = 11.sp, color = MaterialTheme.colorScheme.onSurfaceVariant) }
                            Icon(Icons.Filled.Visibility, contentDescription = null, tint = MaterialTheme.colorScheme.primary, modifier = Modifier.size(20.dp))
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun MemberProfileDetail(member: MemberUI, onBack: () -> Unit) {
    val statusColor = when (member.status) { Member.MemberStatus.AKTIF -> Color(0xFF2E7D32); Member.MemberStatus.NONAKTIF -> Color(0xFFC62828); Member.MemberStatus.KELUAR -> Color(0xFF9E9E9E) }
    LazyColumn(modifier = Modifier.fillMaxSize().padding(16.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
        item {
            Column(modifier = Modifier.fillMaxWidth(), horizontalAlignment = Alignment.CenterHorizontally) {
                Box(modifier = Modifier.size(80.dp).background(statusColor.copy(alpha = 0.15f), CircleShape), contentAlignment = Alignment.Center) { Text(member.nama.take(2).uppercase(), fontSize = 28.sp, fontWeight = FontWeight.Bold, color = statusColor) }
                Spacer(modifier = Modifier.height(8.dp))
                Text(member.nama, fontSize = 20.sp, fontWeight = FontWeight.Bold)
                Text(member.noAnggota, fontSize = 14.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                Spacer(modifier = Modifier.height(4.dp))
                Row(verticalAlignment = Alignment.CenterVertically) { Box(modifier = Modifier.size(10.dp).background(statusColor, CircleShape)); Spacer(modifier = Modifier.width(6.dp)); Text(member.status.value.replaceFirstChar { it.uppercase() }, fontSize = 13.sp, color = statusColor, fontWeight = FontWeight.Medium) }
            }
        }
        item { SectionTitle("Data Diri"); ProfileInfoRow("No. Anggota", member.noAnggota); ProfileInfoRow("Jabatan", member.jabatan); ProfileInfoRow("No. Handphone", member.noTelp); ProfileInfoRow("Tanggal Gabung", member.tanggalGabung) }
        item { SectionTitle("Data Keuangan"); Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(12.dp)) { FinancialCard("Total Tabungan", member.totalTabungan, Color(0xFF2E7D32), Modifier.weight(1f)); FinancialCard("Total Pinjaman", member.totalPinjaman, Color(0xFFC62828), Modifier.weight(1f)) } }
        item { SectionTitle("Aksi"); Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) { OutlinedButton(onClick = { }, modifier = Modifier.weight(1f), shape = RoundedCornerShape(12.dp)) { Icon(Icons.Filled.Edit, contentDescription = null, modifier = Modifier.size(16.dp)); Spacer(modifier = Modifier.width(4.dp)); Text("Edit", fontSize = 12.sp) }; OutlinedButton(onClick = { }, modifier = Modifier.weight(1f), shape = RoundedCornerShape(12.dp)) { Icon(Icons.Filled.Phone, contentDescription = null, modifier = Modifier.size(16.dp)); Spacer(modifier = Modifier.width(4.dp)); Text("Hubungi", fontSize = 12.sp) }; OutlinedButton(onClick = { }, modifier = Modifier.weight(1f), shape = RoundedCornerShape(12.dp)) { Icon(Icons.Filled.Description, contentDescription = null, modifier = Modifier.size(16.dp)); Spacer(modifier = Modifier.width(4.dp)); Text("Riwayat", fontSize = 12.sp) } } }
    }
}

// ══════════════════════════════════════════════════════════════
// SECTION 3: KARTU DIGITAL
// ══════════════════════════════════════════════════════════════

@Composable
private fun DigitalCardSection() {
    var selectedMember by remember { mutableStateOf<MemberUI?>(null) }
    val sampleMembers = remember {
        listOf(
            MemberUI(1, "KPR-001", "Budi Santoso", "Ketua", "081234567890", Member.MemberStatus.AKTIF, 2_500_000.0, 5_000_000.0, "15 Jan 2020"),
            MemberUI(2, "KPR-002", "Siti Rahayu", "Sekretaris", "081234567891", Member.MemberStatus.AKTIF, 1_800_000.0, 3_000_000.0, "20 Mar 2020"),
            MemberUI(3, "KPR-003", "Ahmad Fauzi", "Bendahara", "081234567892", Member.MemberStatus.AKTIF, 3_200_000.0, 10_000_000.0, "10 Jun 2020")
        )
    }
    if (selectedMember != null) {
        MemberDigitalCard(member = selectedMember!!, onBack = { selectedMember = null })
    } else {
        Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
            Text("Pilih Anggota", fontSize = 16.sp, fontWeight = FontWeight.Bold)
            Spacer(modifier = Modifier.height(12.dp))
            LazyColumn(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                items(sampleMembers) { member ->
                    Card(onClick = { selectedMember = member }, modifier = Modifier.fillMaxWidth(), colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceContainerLow), shape = RoundedCornerShape(12.dp)) {
                        Row(modifier = Modifier.padding(12.dp), verticalAlignment = Alignment.CenterVertically) {
                            Box(modifier = Modifier.size(44.dp).background(Color(0xFF6A1B9A).copy(alpha = 0.12f), CircleShape), contentAlignment = Alignment.Center) { Text(member.nama.take(2).uppercase(), fontSize = 14.sp, fontWeight = FontWeight.Bold, color = Color(0xFF6A1B9A)) }
                            Spacer(modifier = Modifier.width(12.dp))
                            Column(modifier = Modifier.weight(1f)) { Text(member.nama, fontSize = 14.sp, fontWeight = FontWeight.Bold); Text(member.noAnggota, fontSize = 11.sp, color = MaterialTheme.colorScheme.onSurfaceVariant) }
                            Icon(Icons.Filled.CardMembership, contentDescription = null, tint = Color(0xFF6A1B9A), modifier = Modifier.size(20.dp))
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun MemberDigitalCard(member: MemberUI, onBack: () -> Unit) {
    Column(modifier = Modifier.fillMaxSize().padding(16.dp), verticalArrangement = Arrangement.spacedBy(16.dp)) {
        // Kartu
        Card(modifier = Modifier.fillMaxWidth(), shape = RoundedCornerShape(20.dp), colors = CardDefaults.cardColors(containerColor = Color(0xFF1565C0))) {
            Column(modifier = Modifier.fillMaxWidth().padding(24.dp), verticalArrangement = Arrangement.spacedBy(16.dp)) {
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
                    Column { Text("KOPERASI KARYAWAN RSUI", fontSize = 11.sp, color = Color.White.copy(alpha = 0.8f)); Text("KARTU ANGGOTA", fontSize = 14.sp, fontWeight = FontWeight.Bold, color = Color.White) }
                    Icon(Icons.Filled.QrCode, contentDescription = null, tint = Color.White, modifier = Modifier.size(32.dp))
                }
                HorizontalDivider(color = Color.White.copy(alpha = 0.3f))
                Row(modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
                    Box(modifier = Modifier.size(56.dp).background(Color.White.copy(alpha = 0.2f), CircleShape), contentAlignment = Alignment.Center) { Text(member.nama.take(2).uppercase(), fontSize = 20.sp, fontWeight = FontWeight.Bold, color = Color.White) }
                    Spacer(modifier = Modifier.width(16.dp))
                    Column { Text(member.nama, fontSize = 16.sp, fontWeight = FontWeight.Bold, color = Color.White); Text(member.noAnggota, fontSize = 13.sp, color = Color.White.copy(alpha = 0.8f)); Text(member.jabatan, fontSize = 11.sp, color = Color.White.copy(alpha = 0.7f)) }
                }
                Box(modifier = Modifier.fillMaxWidth().height(60.dp).background(Color.White.copy(alpha = 0.1f), RoundedCornerShape(8.dp)), contentAlignment = Alignment.Center) { Text(member.noAnggota, fontSize = 20.sp, fontWeight = FontWeight.Bold, color = Color.White, letterSpacing = 4.sp) }
                Text("Berlaku sejak ${member.tanggalGabung}", fontSize = 10.sp, color = Color.White.copy(alpha = 0.6f))
            }
        }
        Card(modifier = Modifier.fillMaxWidth(), colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceContainerLow), shape = RoundedCornerShape(12.dp)) {
            Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) { Text("Informasi Kartu", fontSize = 14.sp, fontWeight = FontWeight.Bold); ProfileInfoRow("Status", member.status.value.replaceFirstChar { it.uppercase() }); ProfileInfoRow("Sejak", member.tanggalGabung); ProfileInfoRow("No. HP", member.noTelp) }
        }
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(12.dp)) {
            OutlinedButton(onClick = { }, modifier = Modifier.weight(1f), shape = RoundedCornerShape(12.dp)) { Text("Bagikan", fontSize = 13.sp) }
            OutlinedButton(onClick = { }, modifier = Modifier.weight(1f), shape = RoundedCornerShape(12.dp)) { Text("Unduh PNG", fontSize = 13.sp) }
        }
    }
}

// ══════════════════════════════════════════════════════════════
// SECTION 4: STATUS KEANGGOTAAN
// ══════════════════════════════════════════════════════════════

@Composable
private fun MembershipStatusSection() {
    var selectedFilter by remember { mutableIntStateOf(0) }
    val filters = listOf("Semua", "Aktif", "Nonaktif", "Keluar")
    val sampleMembers = remember {
        listOf(
            MemberUI(1, "KPR-001", "Budi Santoso", "Ketua", "081234567890", Member.MemberStatus.AKTIF, 2_500_000.0, 5_000_000.0, "15 Jan 2020"),
            MemberUI(2, "KPR-002", "Siti Rahayu", "Sekretaris", "081234567891", Member.MemberStatus.AKTIF, 1_800_000.0, 3_000_000.0, "20 Mar 2020"),
            MemberUI(3, "KPR-003", "Ahmad Fauzi", "Bendahara", "081234567892", Member.MemberStatus.AKTIF, 3_200_000.0, 10_000_000.0, "10 Jun 2020"),
            MemberUI(4, "KPR-004", "Dewi Lestari", "Anggota", "081234567893", Member.MemberStatus.AKTIF, 950_000.0, 0.0, "05 Jan 2021"),
            MemberUI(5, "KPR-005", "Rizky Pratama", "Anggota", "081234567894", Member.MemberStatus.NONAKTIF, 1_200_000.0, 2_000_000.0, "15 Jun 2021"),
            MemberUI(6, "KPR-006", "Putri Wulandari", "Anggota", "081234567895", Member.MemberStatus.KELUAR, 0.0, 0.0, "01 Sep 2022")
        )
    }
    val filtered = remember(selectedFilter) { when (selectedFilter) { 1 -> sampleMembers.filter { it.status == Member.MemberStatus.AKTIF }; 2 -> sampleMembers.filter { it.status == Member.MemberStatus.NONAKTIF }; 3 -> sampleMembers.filter { it.status == Member.MemberStatus.KELUAR }; else -> sampleMembers } }

    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) { filters.forEachIndexed { index, filter -> FilterChip(selected = selectedFilter == index, onClick = { selectedFilter = index }, label = { Text(filter, fontSize = 12.sp) }, colors = FilterChipDefaults.filterChipColors(selectedContainerColor = MaterialTheme.colorScheme.primaryContainer)) } }
        Spacer(modifier = Modifier.height(12.dp))
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            StatusSummary("Aktif", sampleMembers.count { it.status == Member.MemberStatus.AKTIF }, Color(0xFF2E7D32), Modifier.weight(1f))
            StatusSummary("Nonaktif", sampleMembers.count { it.status == Member.MemberStatus.NONAKTIF }, Color(0xFFC62828), Modifier.weight(1f))
            StatusSummary("Keluar", sampleMembers.count { it.status == Member.MemberStatus.KELUAR }, Color(0xFF9E9E9E), Modifier.weight(1f))
        }
        Spacer(modifier = Modifier.height(12.dp))
        LazyColumn(verticalArrangement = Arrangement.spacedBy(8.dp)) { items(filtered, key = { it.id }) { member -> StatusMemberCard(member) } }
    }
}

@Composable
private fun StatusMemberCard(member: MemberUI) {
    val statusColor = when (member.status) { Member.MemberStatus.AKTIF -> Color(0xFF2E7D32); Member.MemberStatus.NONAKTIF -> Color(0xFFC62828); Member.MemberStatus.KELUAR -> Color(0xFF9E9E9E) }
    Card(modifier = Modifier.fillMaxWidth(), colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceContainerLow), shape = RoundedCornerShape(12.dp)) {
        Row(modifier = Modifier.fillMaxWidth().padding(12.dp), verticalAlignment = Alignment.CenterVertically) {
            Box(modifier = Modifier.size(40.dp).background(statusColor.copy(alpha = 0.12f), CircleShape), contentAlignment = Alignment.Center) { Text(member.nama.take(2).uppercase(), fontSize = 12.sp, fontWeight = FontWeight.Bold, color = statusColor) }
            Spacer(modifier = Modifier.width(12.dp))
            Column(modifier = Modifier.weight(1f)) { Text(member.nama, fontSize = 14.sp, fontWeight = FontWeight.Bold); Text("${member.noAnggota} • ${member.jabatan}", fontSize = 11.sp, color = MaterialTheme.colorScheme.onSurfaceVariant) }
            Column(horizontalAlignment = Alignment.End) {
                Row(verticalAlignment = Alignment.CenterVertically) { Box(modifier = Modifier.size(8.dp).background(statusColor, CircleShape)); Spacer(modifier = Modifier.width(4.dp)); Text(member.status.value.replaceFirstChar { it.uppercase() }, fontSize = 12.sp, color = statusColor, fontWeight = FontWeight.Medium) }
                Text("Gabung: ${member.tanggalGabung}", fontSize = 10.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
            }
        }
    }
}

@Composable
private fun StatusSummary(label: String, count: Int, color: Color, modifier: Modifier = Modifier) {
    Card(modifier = modifier, colors = CardDefaults.cardColors(containerColor = color.copy(alpha = 0.08f)), shape = RoundedCornerShape(8.dp)) {
        Column(modifier = Modifier.fillMaxWidth().padding(8.dp), horizontalAlignment = Alignment.CenterHorizontally) {
            Text("$count", fontSize = 20.sp, fontWeight = FontWeight.Bold, color = color)
            Text(label, fontSize = 10.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
        }
    }
}

// ══════════════════════════════════════════════════════════════
// SECTION 5: DATA SUPLIER
// ══════════════════════════════════════════════════════════════

@Composable
private fun SupplierSection() {
    var searchQuery by remember { mutableStateOf("") }
    var showAddDialog by remember { mutableStateOf(false) }
    var selectedSupplier by remember { mutableStateOf<SupplierUI?>(null) }

    val sampleSuppliers = listOf(
        SupplierUI(1, "SUP-001", "Toko Sehat Bersama", "Ahmad Fauzi", "081234567892", "Obat & Vitamin", "Aktif"),
        SupplierUI(2, "SUP-002", "Apotek Prima Medika", "Siti Rahayu", "081234567891", "Obat Resep", "Aktif"),
        SupplierUI(3, "SUP-003", "Medika Supply Indonesia", "Budi Santoso", "081234567890", "Alat Kesehatan", "Aktif"),
        SupplierUI(4, "SUP-004", "Laboratorium Klinika", "Dewi Lestari", "081234567893", "Laboratorium", "Nonaktif")
    )
    val filtered = sampleSuppliers.filter { it.nama.contains(searchQuery, ignoreCase = true) || it.kode.contains(searchQuery, ignoreCase = true) }

    if (selectedSupplier != null) {
        SupplierDetail(supplier = selectedSupplier!!, onBack = { selectedSupplier = null })
    } else {
        Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
            OutlinedTextField(value = searchQuery, onValueChange = { searchQuery = it }, modifier = Modifier.fillMaxWidth(), placeholder = { Text("Cari suplier...") }, leadingIcon = { Icon(Icons.Filled.Search, contentDescription = null) }, singleLine = true, shape = RoundedCornerShape(12.dp), colors = OutlinedTextFieldDefaults.colors(unfocusedContainerColor = MaterialTheme.colorScheme.surfaceContainerLow))
            Spacer(modifier = Modifier.height(12.dp))
            LazyColumn(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                items(filtered, key = { it.id }) { supplier ->
                    SupplierCard(supplier = supplier, onClick = { selectedSupplier = supplier })
                }
            }
        }
    }

    if (showAddDialog) {
        AddSupplierDialog(onDismiss = { showAddDialog = false })
    }
}

@Composable
private fun SupplierCard(supplier: SupplierUI, onClick: () -> Unit) {
    Card(onClick = onClick, modifier = Modifier.fillMaxWidth(), colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceContainerLow), shape = RoundedCornerShape(12.dp)) {
        Row(modifier = Modifier.padding(12.dp), verticalAlignment = Alignment.CenterVertically) {
            Box(modifier = Modifier.size(44.dp).background(Color(0xFF00695C).copy(alpha = 0.12f), RoundedCornerShape(12.dp)), contentAlignment = Alignment.Center) {
                Icon(Icons.Filled.Store, contentDescription = null, tint = Color(0xFF00695C), modifier = Modifier.size(20.dp))
            }
            Spacer(modifier = Modifier.width(12.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(supplier.nama, fontSize = 14.sp, fontWeight = FontWeight.Bold)
                Text("${supplier.kode} • ${supplier.kategori}", fontSize = 11.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
            }
            Column(horizontalAlignment = Alignment.End) {
                val statusColor = if (supplier.status == "Aktif") Color(0xFF2E7D32) else Color(0xFFC62828)
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(modifier = Modifier.size(8.dp).background(statusColor, CircleShape))
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(supplier.status, fontSize = 12.sp, color = statusColor, fontWeight = FontWeight.Medium)
                }
            }
        }
    }
}

@Composable
private fun SupplierDetail(supplier: SupplierUI, onBack: () -> Unit) {
    LazyColumn(modifier = Modifier.fillMaxSize().padding(16.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
        item {
            Column(modifier = Modifier.fillMaxWidth(), horizontalAlignment = Alignment.CenterHorizontally) {
                Box(modifier = Modifier.size(80.dp).background(Color(0xFF00695C).copy(alpha = 0.15f), RoundedCornerShape(16.dp)), contentAlignment = Alignment.Center) {
                    Icon(Icons.Filled.Store, contentDescription = null, tint = Color(0xFF00695C), modifier = Modifier.size(36.dp))
                }
                Spacer(modifier = Modifier.height(8.dp))
                Text(supplier.nama, fontSize = 20.sp, fontWeight = FontWeight.Bold)
                Text(supplier.kode, fontSize = 14.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                Spacer(modifier = Modifier.height(4.dp))
                val statusColor = if (supplier.status == "Aktif") Color(0xFF2E7D32) else Color(0xFFC62828)
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(modifier = Modifier.size(10.dp).background(statusColor, CircleShape))
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(supplier.status, fontSize = 13.sp, color = statusColor, fontWeight = FontWeight.Medium)
                }
            }
        }
        item {
            SectionTitle("Informasi Suplier")
            ProfileInfoRow("Kode Suplier", supplier.kode)
            ProfileInfoRow("Nama Usaha", supplier.nama)
            ProfileInfoRow("Kontak", supplier.kontak)
            ProfileInfoRow("No. Telepon", supplier.telepon)
        }
        item {
            SectionTitle("Kategori")
            ProfileInfoRow("Jenis Barang", supplier.kategori)
        }
        item {
            SectionTitle("Aksi")
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                OutlinedButton(onClick = { }, modifier = Modifier.weight(1f), shape = RoundedCornerShape(12.dp)) {
                    Icon(Icons.Filled.Edit, contentDescription = null, modifier = Modifier.size(16.dp))
                    Spacer(modifier = Modifier.width(4.dp))
                    Text("Edit", fontSize = 12.sp)
                }
                OutlinedButton(onClick = { }, modifier = Modifier.weight(1f), shape = RoundedCornerShape(12.dp)) {
                    Icon(Icons.Filled.Phone, contentDescription = null, modifier = Modifier.size(16.dp))
                    Spacer(modifier = Modifier.width(4.dp))
                    Text("Hubungi", fontSize = 12.sp)
                }
                OutlinedButton(onClick = { }, modifier = Modifier.weight(1f), shape = RoundedCornerShape(12.dp)) {
                    Icon(Icons.Filled.Description, contentDescription = null, modifier = Modifier.size(16.dp))
                    Spacer(modifier = Modifier.width(4.dp))
                    Text("Riwayat", fontSize = 12.sp)
                }
            }
        }
    }
}

@Composable
private fun AddSupplierDialog(onDismiss: () -> Unit) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Tambah Suplier Baru") },
        text = { Text("Formulir tambah suplier akan ditampilkan di sini.") },
        confirmButton = { TextButton(onClick = onDismiss) { Text("Simpan") } },
        dismissButton = { TextButton(onClick = onDismiss) { Text("Batal") } }
    )
}

data class SupplierUI(
    val id: Int,
    val kode: String,
    val nama: String,
    val kontak: String,
    val telepon: String,
    val kategori: String,
    val status: String
)

// ══════════════════════════════════════════════════════════════
// SHARED COMPONENTS
// ══════════════════════════════════════════════════════════════

@Composable
fun SectionTitle(title: String) {
    Text(title, fontSize = 14.sp, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.primary)
    HorizontalDivider(modifier = Modifier.padding(vertical = 4.dp))
}

@Composable
fun ProfileInfoRow(label: String, value: String) {
    Row(modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp), horizontalArrangement = Arrangement.SpaceBetween) {
        Text(label, fontSize = 13.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
        Text(value, fontSize = 13.sp, fontWeight = FontWeight.Medium)
    }
}

@Composable
fun FinancialCard(label: String, amount: Double, color: Color, modifier: Modifier = Modifier) {
    Card(modifier = modifier, colors = CardDefaults.cardColors(containerColor = color.copy(alpha = 0.08f)), shape = RoundedCornerShape(8.dp)) {
        Column(modifier = Modifier.fillMaxWidth().padding(12.dp), horizontalAlignment = Alignment.CenterHorizontally) {
            Text(label, fontSize = 11.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
            Text("Rp ${amount.toLong().toString().reversed().chunked(3).joinToString(".").reversed()}", fontSize = 14.sp, fontWeight = FontWeight.Bold, color = color)
        }
    }
}