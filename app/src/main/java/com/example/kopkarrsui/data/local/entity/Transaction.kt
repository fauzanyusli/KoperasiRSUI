package com.example.kopkarrsui.data.local.entity

data class Transaction(
     val id: Long = 0,
     val memberId: Long,
     val tipe: TransactionType,
     val jumlah: Long,
     val keterangan: String?,
     val refId: String?,
     val refUnitUsaha: String?,
     val tgl: Long,
     val status: TransactionStatus = TransactionStatus.SUKSES,
     val poinDihasilkan: Int = 0,
     val createdAt: Long = System.currentTimeMillis()
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
