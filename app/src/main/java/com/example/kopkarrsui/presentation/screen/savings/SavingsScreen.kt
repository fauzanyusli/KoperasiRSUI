package com.example.kopkarrsui.presentation.screen.savings

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.TabRowDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.material3.MaterialTheme
import androidx.compose.ui.unit.sp
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.kopkarrsui.presentation.viewmodel.SavingsViewModel

@Composable
fun SavingsScreen(viewModel: SavingsViewModel = viewModel()) {
    var selectedTab by remember { mutableIntStateOf(0) }
    val tabs = listOf("Simpanan", "Pinjaman")

    Column(modifier = Modifier.fillMaxSize()) {
        TabRow(
            selectedTabIndex = selectedTab,
            containerColor = MaterialTheme.colorScheme.primaryContainer,
            contentColor = MaterialTheme.colorScheme.onPrimaryContainer,
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
                            fontSize = 13.sp
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
                Tab(selected = sub == i, onClick = { sub = i }, text = { Text(t, fontSize = 11.sp) })
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
                Tab(selected = sub == i, onClick = { sub = i }, text = { Text(t, fontSize = 11.sp) })
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
