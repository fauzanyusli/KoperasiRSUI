package com.example.kopkarrsui.data.local.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "members",
    indices = [Index(value = ["no_anggota"], unique = true)]
)
data class Member(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    @ColumnInfo(name = "no_anggota") val noAnggota: String,
    val nama: String,
    @ColumnInfo(name = "nik") val nik: String?,
    @ColumnInfo(name = "no_hp") val noHp: String?,
    val email: String?,
    val alamat: String?,
    @ColumnInfo(name = "tgl_gabung") val tglGabung: Long,
    @ColumnInfo(name = "status") val status: MemberStatus = MemberStatus.AKTIF,
    @ColumnInfo(name = "pin_hash") val pinHash: String?,
    @ColumnInfo(name = "biometric_enabled") val biometricEnabled: Boolean = false,
    @ColumnInfo(name = "created_at") val createdAt: Long = System.currentTimeMillis(),
    @ColumnInfo(name = "updated_at") val updatedAt: Long = System.currentTimeMillis()
) {
    enum class MemberStatus(val value: String) {
        AKTIF("aktif"),
        NONAKTIF("nonaktif"),
        KELUAR("keluar")
    }
}
