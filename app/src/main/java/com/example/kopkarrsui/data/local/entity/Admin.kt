package com.example.kopkarrsui.data.local.entity

data class Admin(
     val id: Long = 0,
     val memberId: Long,
     val role: AdminRole,
     val izinJson: String?,
     val aktifSejak: Long = System.currentTimeMillis(),
     val status: AdminStatus = AdminStatus.AKTIF,
     val createdAt: Long = System.currentTimeMillis(),
     val updatedAt: Long = System.currentTimeMillis()
) {
    enum class AdminRole(val value: String, val label: String) {
        KETUA("ketua", "Ketua"),
        BENDAHARA("bendahara", "Bendahara"),
        SEKRETARIS("sekretaris", "Sekretaris"),
        PJ_KEANGGOTAAN("pj_keanggotaan", "Penanggung Jawab Keanggotaan")
    }

    enum class AdminStatus(val value: String) {
        AKTIF("aktif"),
        NONAKTIF("nonaktif"),
        DICABUT("dicabut")
    }
}

object AdminPermission {
    const val MEMBER_CREATE = "MEMBER_CREATE"
    const val MEMBER_READ = "MEMBER_READ"
    const val MEMBER_UPDATE = "MEMBER_UPDATE"
    const val MEMBER_DELETE = "MEMBER_DELETE"
    const val TRANSACTION_CREATE = "TRANSACTION_CREATE"
    const val TRANSACTION_READ = "TRANSACTION_READ"
    const val TRANSACTION_UPDATE = "TRANSACTION_UPDATE"
    const val TRANSACTION_DELETE = "TRANSACTION_DELETE"
    const val SAVINGS_READ = "SAVINGS_READ"
    const val SAVINGS_UPDATE = "SAVINGS_UPDATE"
    const val SHU_CALCULATE = "SHU_CALCULATE"
    const val SHU_APPROVE = "SHU_APPROVE"
    const val FINANCIAL_READ = "FINANCIAL_READ"
    const val FINANCIAL_CREATE = "FINANCIAL_CREATE"
    const val ADMIN_MANAGE = "ADMIN_MANAGE"
    const val REPORT_EXPORT = "REPORT_EXPORT"
}
