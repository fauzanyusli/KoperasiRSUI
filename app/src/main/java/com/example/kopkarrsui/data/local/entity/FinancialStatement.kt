package com.example.kopkarrsui.data.local.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "financial_statements",
    indices = [
        Index(value = ["tahun"], unique = true)
    ]
)
data class FinancialStatement(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    @ColumnInfo(name = "tahun") val tahun: Int,
    @ColumnInfo(name = "neraca_json") val neracaJson: String,
    @ColumnInfo(name = "laba_rugi_json") val labaRugiJson: String,
    @ColumnInfo(name = "shu_total") val shuTotal: Long = 0,
    @ColumnInfo(name = "cadangan_wajib") val cadanganWajib: Long = 0,
    @ColumnInfo(name = "pembangunan") val pembangunan: Long = 0,
    @ColumnInfo(name = "bagi_hasil_total") val bagiHasilTotal: Long = 0,
    @ColumnInfo(name = "status") val status: FinancialStatus = FinancialStatus.DRAFT,
    @ColumnInfo(name = "tgl_rat") val tglRAT: Long? = null,
    @ColumnInfo(name = "created_at") val createdAt: Long = System.currentTimeMillis(),
    @ColumnInfo(name = "updated_at") val updatedAt: Long = System.currentTimeMillis()
) {
    enum class FinancialStatus(val value: String, val label: String) {
        DRAFT("draft", "Draft"),
        FINAL("final", "Final (Disetujui RAT)"),
        DIARSIPKAN("diarsipkan", "Diarsipkan")
    }
}
