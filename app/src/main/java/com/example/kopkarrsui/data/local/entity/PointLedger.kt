package com.example.kopkarrsui.data.local.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "point_ledgers",
    foreignKeys = [
        ForeignKey(
            entity = Member::class,
            parentColumns = ["id"],
            childColumns = ["member_id"],
            onDelete = ForeignKey.CASCADE
        ),
        ForeignKey(
            entity = Transaction::class,
            parentColumns = ["id"],
            childColumns = ["ref_transaksi_id"],
            onDelete = ForeignKey.SET_NULL
        )
    ],
    indices = [
        Index(value = ["member_id"]),
        Index(value = ["tgl"]),
        Index(value = ["tipe"])
    ]
)
data class PointLedger(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    @ColumnInfo(name = "member_id") val memberId: Long,
    @ColumnInfo(name = "tipe") val tipe: PointType,
    @ColumnInfo(name = "jumlah") val jumlah: Int,
    @ColumnInfo(name = "ref_transaksi_id") val refTransaksiId: Long?,
    @ColumnInfo(name = "keterangan") val keterangan: String?,
    @ColumnInfo(name = "tgl") val tgl: Long = System.currentTimeMillis(),
    @ColumnInfo(name = "expired_at") val expiredAt: Long? = null,
    @ColumnInfo(name = "created_at") val createdAt: Long = System.currentTimeMillis()
) {
    enum class PointType(val value: String, val label: String) {
        EARN("earn", "Penghasilan Poin"),
        REDEEM("redeem", "Penukaran Poin"),
        EXPIRE("expire", "Kadaluarsa Poin"),
        ADJUSTMENT("adjustment", "Koreksi Manual")
    }
}
