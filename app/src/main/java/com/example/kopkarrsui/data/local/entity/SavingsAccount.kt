package com.example.kopkarrsui.data.local.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "savings_accounts",
    foreignKeys = [
        ForeignKey(
            entity = Member::class,
            parentColumns = ["id"],
            childColumns = ["member_id"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [
        Index(value = ["member_id"]),
        Index(value = ["member_id", "jenis"], unique = true)
    ]
)
data class SavingsAccount(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    @ColumnInfo(name = "member_id") val memberId: Long,
    @ColumnInfo(name = "jenis") val jenis: SavingsType,
    @ColumnInfo(name = "saldo") val saldo: Long = 0,
    @ColumnInfo(name = "bunga_tahun_persen") val bungaTahunPersen: Double = 0.0,
    @ColumnInfo(name = "tgl_buka") val tglBuka: Long = System.currentTimeMillis(),
    @ColumnInfo(name = "status") val status: SavingsStatus = SavingsStatus.AKTIF,
    @ColumnInfo(name = "created_at") val createdAt: Long = System.currentTimeMillis(),
    @ColumnInfo(name = "updated_at") val updatedAt: Long = System.currentTimeMillis()
) {
    enum class SavingsType(val value: String, val label: String) {
        WAJIB("wajib", "Tabungan Wajib"),
        SUKARELA("sukarela", "Tabungan Sukarela"),
        HARI_TUA("hari_tua", "Tabungan Hari Tua"),
        KHUSUS("khusus", "Tabungan Khusus")
    }

    enum class SavingsStatus(val value: String) {
        AKTIF("aktif"),
        DITUTUP("ditutup"),
        DIBEKUKAN("dibekukan")
    }
}
