package com.example.kopkarrsui.data.local.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "transactions",
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
        Index(value = ["tgl"]),
        Index(value = ["tipe"]),
        Index(value = ["status"])
    ]
)
data class Transaction(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    @ColumnInfo(name = "member_id") val memberId: Long,
    @ColumnInfo(name = "tipe") val tipe: TransactionType,
    @ColumnInfo(name = "jumlah") val jumlah: Long,
    @ColumnInfo(name = "keterangan") val keterangan: String?,
    @ColumnInfo(name = "ref_id") val refId: String?,
    @ColumnInfo(name = "ref_unit_usaha") val refUnitUsaha: String?,
    @ColumnInfo(name = "tgl") val tgl: Long,
    @ColumnInfo(name = "status") val status: TransactionStatus = TransactionStatus.SUKSES,
    @ColumnInfo(name = "poin_dihasilkan") val poinDihasilkan: Int = 0,
    @ColumnInfo(name = "created_at") val createdAt: Long = System.currentTimeMillis()
) {
    enum class TransactionType(val value: String, val label: String) {
        BELANJA("belanja", "Belanja"),
        SETORAN_TABUNGAN("setoran_tabungan", "Setoran Tabungan"),
        PENARIKAN_TABUNGAN("penarikan_tabungan", "Penarikan Tabungan"),
        PEMBAYARAN_ANGSURAN("pembayaran_angsuran", "Pembayaran Angsuran"),
        LAINNYA("lainnya", "Lainnya")
    }

    enum class TransactionStatus(val value: String) {
        SUKSES("sukses"),
        PENDING("pending"),
        GAGAL("gagal"),
        DIBATALKAN("dibatalkan")
    }
}
