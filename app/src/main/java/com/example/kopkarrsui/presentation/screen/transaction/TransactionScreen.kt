package com.example.kopkarrsui.presentation.screen.transaction
import com.example.kopkarrsui.ui.theme.*

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
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
import androidx.compose.material.icons.filled.ArrowDownward
import androidx.compose.material.icons.filled.ArrowUpward
import androidx.compose.material.icons.filled.FilterList
import androidx.compose.material.icons.filled.ReceiptLong
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.kopkarrsui.data.local.entity.Transaction
import com.example.kopkarrsui.presentation.component.EmptyState
import com.example.kopkarrsui.presentation.component.ErrorState
import com.example.kopkarrsui.presentation.component.LoadingOverlay
import com.example.kopkarrsui.presentation.viewmodel.TransactionViewModel
import com.example.kopkarrsui.util.ExportHelper
import android.widget.Toast
import androidx.compose.material.icons.filled.FileDownload
import androidx.compose.material3.IconButton
import androidx.compose.ui.platform.LocalContext
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TransactionScreen(viewModel: TransactionViewModel = viewModel()) {
    val transactions by viewModel.transactions.collectAsStateWithLifecycle()
    val totalPoin by viewModel.totalPoin.collectAsStateWithLifecycle()
    val isLoading by viewModel.isLoading.collectAsStateWithLifecycle()
    val error by viewModel.error.collectAsStateWithLifecycle()
    val context = LocalContext.current
    var filterType by remember { mutableStateOf<Transaction.TransactionType?>(null) }
    var selectedTransaction by remember { mutableStateOf<Transaction?>(null) }

    Box(modifier = Modifier.fillMaxWidth()) {
        Column {
            // Header
            Column(
                modifier = Modifier.fillMaxWidth().background(KopkarOrange.copy(alpha = 0.1f)).padding(16.dp)
            ) {
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
                    Column {
                        Text("Transaksi & Poin", fontSize = TextSize.s20, fontWeight = FontWeight.Bold, color = KopkarDeepOrange)
                        Text("Total Poin: ${viewModel.formatPoin(totalPoin)}", fontSize = TextSize.s14, color = KopkarDeepOrange.copy(alpha = 0.8f))
                    }
                    IconButton(onClick = {
                        if (transactions.isEmpty()) {
                            Toast.makeText(context, "Tidak ada data untuk diexport", Toast.LENGTH_SHORT).show()
                        } else {
                            val uri = ExportHelper.exportTransactionsToCsv(context, transactions)
                            if (uri != null) {
                                ExportHelper.shareFile(context, uri)
                                Toast.makeText(context, "File CSV siap dibagikan", Toast.LENGTH_SHORT).show()
                            } else {
                                Toast.makeText(context, "Gagal export data", Toast.LENGTH_SHORT).show()
                            }
                        }
                    }) {
                        Icon(Icons.Filled.FileDownload, contentDescription = "Export CSV", tint = KopkarDeepOrange)
                    }
                }
            }

            // Filter chips
            Row(modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 8.dp), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                FilterChip(
                    selected = filterType == null,
                    onClick = { filterType = null },
                    label = { Text("Semua") }
                )
                Transaction.TransactionType.entries.forEach { type ->
                    FilterChip(
                        selected = filterType == type,
                        onClick = { filterType = if (filterType == type) null else type },
                        label = { Text(type.label, fontSize = TextSize.s11) }
                    )
                }
            }

            error?.let { ErrorState(message = it, onRetry = { viewModel.loadTransactions(1, refresh = true) }) }

            if (!isLoading) {
                val filtered = if (filterType != null) transactions.filter { it.tipe == filterType } else transactions
                if (filtered.isEmpty()) {
                    EmptyState(icon = Icons.Filled.ReceiptLong, title = "Belum Ada Transaksi", message = "Transaksi akan muncul di sini")
                } else {
                    LazyColumn(modifier = Modifier.padding(horizontal = 16.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                        items(filtered, key = { it.id }) { tx -> TransactionItem(tx, viewModel, onClick = { selectedTransaction = tx }) }
                    }
                }
            }
        }
        if (isLoading && transactions.isEmpty()) LoadingOverlay("Memuat transaksi...")

    selectedTransaction?.let { tx ->
        TransactionDetailScreen(transaction = tx, onBack = { selectedTransaction = null })
    }
    }
}

@Composable
private fun TransactionItem(transaction: Transaction, viewModel: TransactionViewModel, onClick: () -> Unit = {}) {
    val isIncome = transaction.jumlah > 0
    val typeColor = when (transaction.tipe) {
        Transaction.TransactionType.BELANJA -> KopkarOrange
        Transaction.TransactionType.SETORAN_TABUNGAN -> KopkarGreen
        Transaction.TransactionType.PENARIKAN_TABUNGAN -> KopkarRed
        Transaction.TransactionType.PEMBAYARAN_ANGSURAN -> KopkarBlue
        Transaction.TransactionType.LAINNYA -> MaterialTheme.colorScheme.primary
    }

    Card(
        modifier = Modifier.fillMaxWidth().clickable { onClick() },
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceContainerLow),
        shape = RoundedCornerShape(12.dp)
    ) {
        Row(modifier = Modifier.padding(12.dp), verticalAlignment = Alignment.CenterVertically) {
            Box(
                modifier = Modifier.size(40.dp).background(typeColor.copy(alpha = 0.12f), CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = when (transaction.tipe) {
                        Transaction.TransactionType.SETORAN_TABUNGAN -> Icons.Filled.ArrowDownward
                        Transaction.TransactionType.PENARIKAN_TABUNGAN -> Icons.Filled.ArrowUpward
                        else -> Icons.Filled.ReceiptLong
                    },
                    contentDescription = "",
                    tint = typeColor,
                    modifier = Modifier.size(20.dp)
                )
            }
            Spacer(modifier = Modifier.width(12.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(transaction.keterangan ?: transaction.tipe.label, fontSize = TextSize.s14, fontWeight = FontWeight.Medium)
                val sdf = SimpleDateFormat("dd MMM yyyy, HH:mm", Locale("id", "ID"))
                Text(sdf.format(Date(transaction.tgl)), fontSize = TextSize.s11, color = MaterialTheme.colorScheme.onSurfaceVariant)
            }
            Column(horizontalAlignment = Alignment.End) {
                Text(
                    "${if (isIncome) "+" else "-"}${viewModel.formatRupiah(transaction.jumlah)}",
                    fontSize = TextSize.s14, fontWeight = FontWeight.Bold,
                    color = if (isIncome) KopkarGreen else KopkarRed
                )
                if (transaction.poinDihasilkan > 0) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Filled.Star, contentDescription = null, tint = KopkarOrange, modifier = Modifier.size(12.dp))
                        Text("+${viewModel.formatPoin(transaction.poinDihasilkan)}", fontSize = TextSize.s10, color = KopkarOrange)
                    }
                }
            }
        }
    }
}

