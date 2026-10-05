package com.example.kopkarrsui.data.local.entity

data class AuditLog(
     val id: Long = 0,
     val memberId: Long? = null,
     val tableName: String,
     val recordId: Long? = null,
     val action: AuditAction,
     val oldValue: String? = null,
     val newValue: String? = null,
     val description: String? = null,
     val ipAddress: String? = null,
     val createdAt: Long = System.currentTimeMillis()
) {
    enum class AuditAction(val value: String, val label: String) {
        CREATE("create", "Tambah"),
        UPDATE("update", "Ubah"),
        DELETE("delete", "Hapus"),
        LOGIN("login", "Masuk"),
        LOGOUT("logout", "Keluar"),
        EXPORT("export", "Ekspor"),
        RESTORE("restore", "Restore")
    }
}
