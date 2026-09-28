package com.example.kopkarrsui.data.local.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "audit_logs",
    indices = [
        Index(value = ["member_id"]),
        Index(value = ["table_name"]),
        Index(value = ["created_at"])
    ]
)
data class AuditLog(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    @ColumnInfo(name = "member_id") val memberId: Long? = null,
    @ColumnInfo(name = "table_name") val tableName: String,
    @ColumnInfo(name = "record_id") val recordId: Long? = null,
    @ColumnInfo(name = "action") val action: AuditAction,
    @ColumnInfo(name = "old_value") val oldValue: String? = null,
    @ColumnInfo(name = "new_value") val newValue: String? = null,
    @ColumnInfo(name = "description") val description: String? = null,
    @ColumnInfo(name = "ip_address") val ipAddress: String? = null,
    @ColumnInfo(name = "created_at") val createdAt: Long = System.currentTimeMillis()
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
