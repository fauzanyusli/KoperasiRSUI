package com.example.kopkarrsui.data.local.entity

data class PointLedger(
     val id: Long = 0,
     val memberId: Long,
     val tipe: PointType,
     val jumlah: Int,
     val refTransaksiId: Long?,
     val keterangan: String?,
     val tgl: Long = System.currentTimeMillis(),
     val expiredAt: Long? = null,
     val createdAt: Long = System.currentTimeMillis()
) {
    enum class PointType(val value: String, val label: String) {
        EARN("earn", "Penghasilan Poin"),
        REDEEM("redeem", "Penukaran Poin"),
        EXPIRE("expire", "Kadaluarsa Poin"),
        ADJUSTMENT("adjustment", "Koreksi Manual")
    }
}
