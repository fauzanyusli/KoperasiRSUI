package com.example.kopkarrsui.data.local.entity

data class SavingsAccount(
     val id: Long = 0,
     val memberId: Long,
     val jenis: SavingsType,
     val saldo: Long = 0,
     val bungaTahunPersen: Double = 0.0,
     val tglBuka: Long = System.currentTimeMillis(),
     val status: SavingsStatus = SavingsStatus.AKTIF,
     val createdAt: Long = System.currentTimeMillis(),
     val updatedAt: Long = System.currentTimeMillis()
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
