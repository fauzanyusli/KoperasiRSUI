package com.example.kopkarrsui.util

import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.core.content.FileProvider
import com.example.kopkarrsui.data.local.entity.Transaction
import java.io.File
import java.io.FileWriter
import java.text.NumberFormat
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

object ExportHelper {

    fun exportTransactionsToCsv(context: Context, transactions: List<Transaction>): Uri? {
        return try {
            val timestamp = SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(Date())
            val fileName = "transaksi_$timestamp.csv"
            val file = File(context.cacheDir, "exports").apply { mkdirs() }
            val csvFile = File(file, fileName)

            FileWriter(csvFile).use { writer ->
                // Header
                writer.appendLine("ID,Tanggal,Tipe,Keterangan,Jumlah,Poin,Status")

                // Data
                val nf = NumberFormat.getInstance(Locale("id", "ID"))
                val sdf = SimpleDateFormat("dd/MM/yyyy HH:mm", Locale("id", "ID"))

                transactions.forEach { tx ->
                    writer.appendLine(
                        "${tx.id}," +
                        "${sdf.format(Date(tx.tgl))}," +
                        "${tx.tipe.label}," +
                        "\"${tx.keterangan ?: "-"}\"," +
                        "${tx.jumlah}," +
                        "${tx.poinDihasilkan}," +
                        "${tx.status.value}"
                    )
                }
            }

            FileProvider.getUriForFile(
                context,
                "${context.packageName}.fileprovider",
                csvFile
            )
        } catch (e: Exception) {
            null
        }
    }

    fun exportSavingsToCsv(context: Context, data: List<SavingsExportRow>): Uri? {
        return try {
            val timestamp = SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(Date())
            val fileName = "tabungan_$timestamp.csv"
            val file = File(context.cacheDir, "exports").apply { mkdirs() }
            val csvFile = File(file, fileName)

            FileWriter(csvFile).use { writer ->
                writer.appendLine("No,Jenis,Saldo,Tanggal Buka,Status")

                val nf = NumberFormat.getInstance(Locale("id", "ID"))
                val sdf = SimpleDateFormat("dd/MM/yyyy", Locale("id", "ID"))

                data.forEachIndexed { index, row ->
                    writer.appendLine(
                        "${index + 1}," +
                        "${row.jenis}," +
                        "${row.saldo}," +
                        "${sdf.format(Date(row.tglBuka))}," +
                        "${row.status}"
                    )
                }
            }

            FileProvider.getUriForFile(
                context,
                "${context.packageName}.fileprovider",
                csvFile
            )
        } catch (e: Exception) {
            null
        }
    }

    fun shareFile(context: Context, uri: Uri, mimeType: String = "text/csv") {
        val shareIntent = Intent(Intent.ACTION_SEND).apply {
            type = mimeType
            putExtra(Intent.EXTRA_STREAM, uri)
            addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
        }
        context.startActivity(Intent.createChooser(shareIntent, "Bagikan file"))
    }

    data class SavingsExportRow(
        val jenis: String,
        val saldo: Long,
        val tglBuka: Long,
        val status: String
    )
}
