package com.example.kopkarrsui.data.local.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "shu_allocations",
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
        Index(value = ["tahun"]),
        Index(value = ["member_id", "tahun"], unique = true)
    ]
)
data class SHUAllocation(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    @ColumnInfo(name = "member_id") val memberId: Long,
    @ColumnInfo(name = "tahun") val tahun: Int,
    @ColumnInfo(name = "jumlah") val jumlah: Long,
    @ColumnInfo(name = "status") val status: SHUStatus = SHUStatus.DIHITUNG,
    @ColumnInfo(name = "detail_json") val detailJson: String?,
    @ColumnInfo(name = "tgl_hitung") val tglHitung: Long = System.currentTimeMillis(),
    @ColumnInfo(name = "tgl_bagi") val tglBagi: Long? = null,
    @ColumnInfo(name = "tgl_cair") val tglCair: Long? = null,
    @ColumnInfo(name = "created_at") val createdAt: Long = System.currentTimeMillis(),
    @ColumnInfo(name = "updated_at") val updatedAt: Long = System.currentTimeMillis()
) {
    enum class SHUStatus(val value: String, val label: String) {
        DIHITUNG("dihitung", "Sedang Dihitung"),
        DIBAGIKAN("dibagikan", "Sudah Dibagikan (Belum Cair)"),
        DICAIRKAN("dicairkan", "Sudah Dicairkan ke Tabungan"),
        DITOLAK("ditolak", "Ditolak/Dibatalkan")
    }
}
