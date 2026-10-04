package com.example.kopkarrsui.presentation.screen.savings
import com.example.kopkarrsui.ui.theme.*

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.FileDownload
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.TabRowDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.kopkarrsui.presentation.viewmodel.SavingsViewModel
import com.example.kopkarrsui.util.ExportHelper

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SavingsScreen(viewModel: SavingsViewModel = viewModel()) {
    var selectedTab by remember { mutableIntStateOf(0) }
    val tabs = listOf("Simpanan", "Pinjaman")
    val accounts by viewModel.accounts.collectAsStateWithLifecycle()
    val totalSaldo by viewModel.totalSaldo.collectAsStateWithLifecycle()
    val context = LocalContext.current

    Column(modifier = Modifier.fillMaxSize()) {
        // Header with export
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(MaterialTheme.colorScheme.primaryContainer)
                .padding(horizontal = 16.dp, vertical = 12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text("Tabungan & Pinjaman", fontSize = TextSize.s18, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.onPrimaryContainer)
                Text("Total Saldo: Rp ${viewModel.formatRupiah(totalSaldo)}", fontSize = TextSize.s13, color = MaterialTheme.colorScheme.onPrimaryContainer.copy(alpha = 0.8f))
            }
            IconButton(onClick = {
                if (accounts.isEmpty()) {
                    Toast.makeText(context, "Tidak ada data untuk diexport", Toast.LENGTH_SHORT).show()
                } else {
                    val exportData = accounts.map { acc ->
                        ExportHelper.SavingsExportRow(
                            jenis = acc.jenis.label,
                            saldo = acc.saldo,
                            tglBuka = acc.tglBuka,
                            status = acc.status.value
                        )
                    }
                    val uri = ExportHelper.exportSavingsToCsv(context, exportData)
                    if (uri != null) {
                        ExportHelper.shareFile(context, uri)
                        Toast.makeText(context, "File CSV siap dibagikan", Toast.LENGTH_SHORT).show()
                    } else {
                        Toast.makeText(context, "Gagal export data", Toast.LENGTH_SHORT).show()
                    }
                }
            }) {
                Icon(Icons.Filled.FileDownload, contentDescription = "Export CSV", tint = MaterialTheme.colorScheme.onPrimaryContainer)
            }
        }

        TabRow(
            selectedTabIndex = selectedTab,
            containerColor = MaterialTheme.colorScheme.surface,
            contentColor = MaterialTheme.colorScheme.primary,
            indicator = { tp ->
                TabRowDefaults.SecondaryIndicator(
                    modifier = Modifier.tabIndicatorOffset(tp[selectedTab]),
                    color = MaterialTheme.colorScheme.primary
                )
            }
        ) {
            tabs.forEachIndexed { i, t ->
                Tab(
                    selected = selectedTab == i,
                    onClick = { selectedTab = i },
                    text = {
                        Text(
                            t,
                            fontWeight = if (selectedTab == i) FontWeight.Bold else FontWeight.Normal,
                            fontSize = TextSize.s13
                        )
                    }
                )
            }
        }

        when (selectedTab) {
            0 -> SavingsTab(viewModel)
            1 -> LoanTab()
        }
    }
}

@Composable
private fun SavingsTab(viewModel: SavingsViewModel) {
    var sub by remember { mutableIntStateOf(0) }
    val subs = listOf("Pokok", "Wajib", "Sukarela", "Riwayat", "Laporan")

    Column(modifier = Modifier.fillMaxSize()) {
        TabRow(
            selectedTabIndex = sub,
            containerColor = MaterialTheme.colorScheme.surface,
            contentColor = MaterialTheme.colorScheme.primary,
            indicator = { tp ->
                TabRowDefaults.SecondaryIndicator(
                    modifier = Modifier.tabIndicatorOffset(tp[sub]),
                    color = MaterialTheme.colorScheme.primary
                )
            }
        ) {
            subs.forEachIndexed { i, t ->
                Tab(selected = sub == i, onClick = { sub = i }, text = { Text(t, fontSize = TextSize.s11) })
            }
        }

        when (sub) {
            0 -> SimpananPokok()
            1 -> SimpananWajib(viewModel)
            2 -> SimpananSukarela(viewModel)
            3 -> RiwayatSimpanan()
            4 -> LaporanSimpanan()
        }
    }
}

@Composable
private fun LoanTab() {
    var sub by remember { mutableIntStateOf(0) }
    val subs = listOf("Konsumtif", "Produktif", "Darurat", "Simulasi", "Cicilan")

    Column(modifier = Modifier.fillMaxSize()) {
        TabRow(
            selectedTabIndex = sub,
            containerColor = MaterialTheme.colorScheme.surface,
            contentColor = MaterialTheme.colorScheme.primary,
            indicator = { tp ->
                TabRowDefaults.SecondaryIndicator(
                    modifier = Modifier.tabIndicatorOffset(tp[sub]),
                    color = MaterialTheme.colorScheme.primary
                )
            }
        ) {
            subs.forEachIndexed { i, t ->
                Tab(selected = sub == i, onClick = { sub = i }, text = { Text(t, fontSize = TextSize.s11) })
            }
        }

        when (sub) {
            0 -> LoanTypeSection(LoanType.KONSUMTIF)
            1 -> LoanTypeSection(LoanType.PRODUKTIF)
            2 -> LoanTypeSection(LoanType.DARURAT)
            3 -> SimulasiSection()
            4 -> CicilanOtomatis()
        }
    }
}
