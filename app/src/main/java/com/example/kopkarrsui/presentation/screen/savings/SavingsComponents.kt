package com.example.kopkarrsui.presentation.screen.savings

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable fun InfoCard(title: String, subtitle: String, icon: ImageVector, color: Color) {
    Card(modifier = Modifier.fillMaxWidth(), colors = CardDefaults.cardColors(containerColor = color.copy(alpha = 0.08f)), shape = RoundedCornerShape(12.dp)) {
        Row(modifier = Modifier.padding(16.dp), verticalAlignment = Alignment.CenterVertically) {
            Icon(icon, contentDescription = null, tint = color, modifier = Modifier.size(32.dp))
            Spacer(modifier = Modifier.width(12.dp))
            Column { Text(title, fontSize = 16.sp, fontWeight = FontWeight.Bold); Text(subtitle, fontSize = 12.sp, color = MaterialTheme.colorScheme.onSurfaceVariant) }
        }
    }
}

@Composable fun DetailCard(rows: List<Pair<String, String>>, statusColor: Color) {
    Card(modifier = Modifier.fillMaxWidth(), colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceContainerLow), shape = RoundedCornerShape(12.dp)) {
        Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
            rows.forEachIndexed { index, (label, value) ->
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                    Text(label, fontSize = 13.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                    Text(value, fontSize = 13.sp, fontWeight = FontWeight.Bold, color = if (label == "Status") statusColor else MaterialTheme.colorScheme.onSurface)
                }
                if (index < rows.lastIndex) HorizontalDivider()
            }
        }
    }
}

@Composable fun InfoDetailCard(rows: List<Pair<String, String>>) {
    Card(modifier = Modifier.fillMaxWidth(), colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceContainerLow), shape = RoundedCornerShape(12.dp)) {
        Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
            rows.forEach { (label, value) ->
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                    Text(label, fontSize = 13.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                    Text(value, fontSize = 13.sp, fontWeight = FontWeight.Medium)
                }
            }
        }
    }
}

@Composable fun StatusCard(title: String, subtitle: String, color: Color) {
    Card(modifier = Modifier.fillMaxWidth(), colors = CardDefaults.cardColors(containerColor = color.copy(alpha = 0.08f)), shape = RoundedCornerShape(12.dp)) {
        Row(modifier = Modifier.padding(16.dp), verticalAlignment = Alignment.CenterVertically) {
            Icon(Icons.Filled.CheckCircle, contentDescription = null, tint = color)
            Spacer(modifier = Modifier.width(12.dp))
            Column { Text(title, fontSize = 14.sp, fontWeight = FontWeight.Bold, color = color); Text(subtitle, fontSize = 12.sp, color = MaterialTheme.colorScheme.onSurfaceVariant) }
        }
    }
}

@Composable fun PrimaryButton(text: String, icon: ImageVector, color: Color, onClick: () -> Unit) {
    OutlinedButton(onClick = onClick, modifier = Modifier.fillMaxWidth(), colors = ButtonDefaults.outlinedButtonColors(containerColor = color, contentColor = Color.White), shape = RoundedCornerShape(12.dp)) {
        Icon(icon, contentDescription = null); Spacer(modifier = Modifier.width(8.dp)); Text(text, fontWeight = FontWeight.Bold)
    }
}

@Composable fun DualButton(leftLabel: String, leftColor: Color, rightLabel: String, rightColor: Color) {
    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(12.dp)) {
        OutlinedButton(onClick = {}, modifier = Modifier.weight(1f), colors = ButtonDefaults.outlinedButtonColors(containerColor = leftColor.copy(alpha = 0.05f), contentColor = leftColor), shape = RoundedCornerShape(12.dp)) { Text(leftLabel, fontWeight = FontWeight.Medium) }
        OutlinedButton(onClick = {}, modifier = Modifier.weight(1f), colors = ButtonDefaults.outlinedButtonColors(containerColor = rightColor.copy(alpha = 0.05f), contentColor = rightColor), shape = RoundedCornerShape(12.dp)) { Text(rightLabel, fontWeight = FontWeight.Medium) }
    }
}

@Composable fun SuccessDialog(message: String, onDismiss: () -> Unit) {
    AlertDialog(onDismissRequest = onDismiss, icon = { Icon(Icons.Filled.CheckCircle, contentDescription = null, tint = Color(0xFF2E7D32), modifier = Modifier.size(48.dp)) }, title = { Text("Berhasil!") }, text = { Text(message) }, confirmButton = { TextButton(onClick = onDismiss) { Text("OK") } })
}

@Composable fun LoanCard(loan: Loan) {
    val statusColor = when (loan.status) { Loan.LoanStatus.AKTIF -> Color(0xFF1565C0); Loan.LoanStatus.LUNAS -> Color(0xFF2E7D32); Loan.LoanStatus.MACET -> Color(0xFFC62828) }
    Card(modifier = Modifier.fillMaxWidth(), colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceContainerLow), shape = RoundedCornerShape(12.dp)) {
        Column(modifier = Modifier.padding(12.dp)) {
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
                Column { Text(loan.namaPeminjam, fontSize = 14.sp, fontWeight = FontWeight.Bold); Text("${loan.jenis.label} \u2022 ${loan.tenorBulan} bulan", fontSize = 11.sp, color = MaterialTheme.colorScheme.onSurfaceVariant) }
                Text(loan.status.label, fontSize = 12.sp, color = statusColor, fontWeight = FontWeight.Medium)
            }
            Spacer(modifier = Modifier.height(8.dp))
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                Column { Text("Pinjaman", fontSize = 10.sp, color = MaterialTheme.colorScheme.onSurfaceVariant); Text(formatRupiah(loan.jumlah), fontSize = 12.sp, fontWeight = FontWeight.Bold) }
                Column(horizontalAlignment = Alignment.End) { Text("Sisa Bayar", fontSize = 10.sp, color = MaterialTheme.colorScheme.onSurfaceVariant); Text(formatRupiah(loan.sisaBayar), fontSize = 12.sp, fontWeight = FontWeight.Bold, color = statusColor) }
            }
            Spacer(modifier = Modifier.height(4.dp))
            Text("Cicilan: ${formatRupiah(loan.cicilanBulanan)}/bulan", fontSize = 11.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
        }
    }
}
