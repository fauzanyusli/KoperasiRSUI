package com.example.kopkarrsui.presentation.screen.shu
import com.example.kopkarrsui.ui.theme.*

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
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
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import com.example.kopkarrsui.presentation.viewmodel.SHUViewModel

@Composable
fun SHUScreen(viewModel: SHUViewModel) {
    var selectedTab by remember { mutableIntStateOf(0) }
    val tabs = listOf("Perhitungan", "Distribusi", "Riwayat", "Laporan", "Transparansi")

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
                            fontSize = TextSize.s11
                        )
                    }
                )
            }
        }

        when (selectedTab) {
            0 -> PerhitunganTab(viewModel)
            1 -> DistribusiTab(viewModel)
            2 -> RiwayatSHUTab(viewModel)
            3 -> LaporanDetailTab(viewModel)
            4 -> TransparansiTab(viewModel)
        }
    }
}
