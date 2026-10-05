package com.example.kopkarrsui.data.local.entity

data class FinancialStatement(
     val id: Long = 0,
     val tahun: Int,
     val neracaJson: String,
     val labaRugiJson: String,
     val shuTotal: Long = 0,
     val cadanganWajib: Long = 0,
     val pembangunan: Long = 0,
     val bagiHasilTotal: Long = 0,
     val status: FinancialStatus = FinancialStatus.DRAFT,
     val tglRAT: Long? = null,
     val createdAt: Long = System.currentTimeMillis(),
     val updatedAt: Long = System.currentTimeMillis()
) {
    enum class FinancialStatus(val value: String, val label: String) {
        DRAFT("draft", "Draft"),
        FINAL("final", "Final (Disetujui RAT)"),
        DIARSIPKAN("diarsipkan", "Diarsipkan")
    }
}
