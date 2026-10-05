package com.example.kopkarrsui.data.local.entity

data class Member(
     val id: Long = 0,
     val noAnggota: String,
    val nama: String,
     val nik: String?,
     val noHp: String?,
    val email: String?,
    val alamat: String?,
     val tglGabung: Long,
     val status: MemberStatus = MemberStatus.AKTIF,
     val pinHash: String?,
     val biometricEnabled: Boolean = false,
     val createdAt: Long = System.currentTimeMillis(),
     val updatedAt: Long = System.currentTimeMillis()
) {
    enum class MemberStatus(val value: String) {
        AKTIF("aktif"),
        NONAKTIF("nonaktif"),
        KELUAR("keluar")
    }
}
