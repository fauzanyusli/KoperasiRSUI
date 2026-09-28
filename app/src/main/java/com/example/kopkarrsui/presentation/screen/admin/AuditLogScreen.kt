package com.example.kopkarrsui.presentation.screen.admin

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
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Login
import androidx.compose.material.icons.filled.Logout
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.kopkarrsui.data.local.entity.AuditLog

@Composable
fun AuditLogScreen() {
    val sampleLogs = listOf(
        AuditLog(1, 1, "member", 1, AuditLog.AuditAction.LOGIN, description = "Login berhasil", createdAt = System.currentTimeMillis() - 3600000),
        AuditLog(2, 1, "savings_account", 1, AuditLog.AuditAction.UPDATE, oldValue = "saldo: 1000000", newValue = "saldo: 1500000", description = "Setor simpanan wajib", createdAt = System.currentTimeMillis() - 7200000),
        AuditLog(3, 2, "member", 4, AuditLog.AuditAction.CREATE, newValue = "nama: Dewi Lestari", description = "Pendaftaran anggota baru", createdAt = System.currentTimeMillis() - 86400000),
        AuditLog(4, null, "system", null, AuditLog.AuditAction.EXPORT, description = "Backup database otomatis", createdAt = System.currentTimeMillis() - 604800000),
        AuditLog(5, 1, "transaction", 5, AuditLog.AuditAction.CREATE, newValue = "jumlah: 500000", description = "Setor tabungan wajib", createdAt = System.currentTimeMillis() - 172800000)
    )

    var selectedFilter by remember { mutableStateOf(0) }
    val filters = listOf("Semua", "Hari Ini", "Minggu Ini")

    LazyColumn(
        modifier = Modifier.fillMaxSize().padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        // Header
        item {
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
                Column {
                    Text("Audit Trail", fontSize = 20.sp, fontWeight = FontWeight.Bold)
                    Text("${sampleLogs.size} log tercatat", fontSize = 12.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                }
                Icon(Icons.Filled.Refresh, contentDescription = null, tint = MaterialTheme.colorScheme.primary)
            }
        }

        // Filter
        item {
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                filters.forEachIndexed { i, label ->
                    androidx.compose.material3.FilterChip(
                        selected = selectedFilter == i,
                        onClick = { selectedFilter = i },
                        label = { Text(label, fontSize = 11.sp) }
                    )
                }
            }
        }

        // Logs
        items(sampleLogs) { log ->
            AuditLogItem(log)
        }
    }
}

@Composable
private fun AuditLogItem(log: AuditLog) {
    val actionColor = when (log.action) {
        AuditLog.AuditAction.CREATE -> Color(0xFF2E7D32)
        AuditLog.AuditAction.UPDATE -> Color(0xFFFF8F00)
        AuditLog.AuditAction.DELETE -> Color(0xFFC62828)
        AuditLog.AuditAction.LOGIN -> Color(0xFF1565C0)
        AuditLog.AuditAction.LOGOUT -> Color(0xFF757575)
        AuditLog.AuditAction.EXPORT -> Color(0xFF6A1B9A)
        AuditLog.AuditAction.RESTORE -> Color(0xFF00897B)
    }
    val actionIcon = when (log.action) {
        AuditLog.AuditAction.CREATE -> Icons.Filled.Add
        AuditLog.AuditAction.UPDATE -> Icons.Filled.Edit
        AuditLog.AuditAction.DELETE -> Icons.Filled.Delete
        AuditLog.AuditAction.LOGIN -> Icons.Filled.Login
        AuditLog.AuditAction.LOGOUT -> Icons.Filled.Logout
        AuditLog.AuditAction.EXPORT -> Icons.Filled.Refresh
        AuditLog.AuditAction.RESTORE -> Icons.Filled.Refresh
    }

    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceContainerLow),
        shape = RoundedCornerShape(12.dp)
    ) {
        Row(modifier = Modifier.padding(12.dp), verticalAlignment = Alignment.CenterVertically) {
            Box(modifier = Modifier.size(36.dp).background(actionColor.copy(alpha = 0.12f), RoundedCornerShape(8.dp)), contentAlignment = Alignment.Center) {
                Icon(actionIcon, contentDescription = null, tint = actionColor, modifier = Modifier.size(18.dp))
            }
            Spacer(modifier = Modifier.width(12.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(log.description ?: log.action.label, fontSize = 13.sp, fontWeight = FontWeight.Medium)
                Text("${log.tableName} \u2022 ${log.action.label}", fontSize = 11.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                log.oldValue?.let { Text("Sebelum: $it", fontSize = 10.sp, color = Color.Gray) }
                log.newValue?.let { Text("Sesudah: $it", fontSize = 10.sp, color = Color.Gray) }
            }
            Text(formatTimestamp(log.createdAt), fontSize = 10.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
        }
    }
}

private fun formatTimestamp(ts: Long): String {
    val diff = System.currentTimeMillis() - ts
    return when {
        diff < 3600000 -> "${diff / 60000}m lalu"
        diff < 86400000 -> "${diff / 3600000}j lalu"
        diff < 604800000 -> "${diff / 86400000}h lalu"
        else -> "${diff / 604800000}w lalu"
    }
}
