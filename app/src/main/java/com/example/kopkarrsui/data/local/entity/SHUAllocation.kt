package com.example.kopkarrsui.data.local.entity

data class SHUAllocation(
     val id: Long = 0,
     val memberId: Long,
     val tahun: Int,
     val jumlah: Long,
     val status: SHUStatus = SHUStatus.DIHITUNG,
     val detailJson: String?,
     val tglHitung: Long = System.currentTimeMillis(),
     val tglBagi: Long? = null,
     val tglCair: Long? = null,
     val createdAt: Long = System.currentTimeMillis(),
     val updatedAt: Long = System.currentTimeMillis()
) {
    enum class SHUStatus(val value: String, val label: String) {
        DIHITUNG("dihitung", "Sedang Dihitung"),
        DIBAGIKAN("dibagikan", "Sudah Dibagikan (Belum Cair)"),
        DICAIRKAN("dicairkan", "Sudah Dicairkan ke Tabungan"),
        DITOLAK("ditolak", "Ditolak/Dibatalkan")
    }
}
