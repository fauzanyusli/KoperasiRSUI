package com.example.kopkarrsui.data.local

import com.example.kopkarrsui.data.local.dao.AuditLogDao
import com.example.kopkarrsui.data.local.entity.AuditLog
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AuditLogger @Inject constructor(
    private val auditLogDao: AuditLogDao
) {
    suspend fun log(
        tableName: String,
        action: AuditLog.AuditAction,
        recordId: Long? = null,
        memberId: Long? = null,
        oldValue: String? = null,
        newValue: String? = null,
        description: String? = null
    ) {
        val log = AuditLog(
            memberId = memberId,
            tableName = tableName,
            recordId = recordId,
            action = action,
            oldValue = oldValue,
            newValue = newValue,
            description = description
        )
        auditLogDao.insert(log)
    }

    suspend fun logCreate(tableName: String, recordId: Long, memberId: Long? = null, desc: String? = null) {
        log(tableName, AuditLog.AuditAction.CREATE, recordId, memberId, description = desc)
    }

    suspend fun logUpdate(tableName: String, recordId: Long, memberId: Long? = null, old: String, new: String) {
        log(tableName, AuditLog.AuditAction.UPDATE, recordId, memberId, old, new)
    }

    suspend fun logDelete(tableName: String, recordId: Long, memberId: Long? = null, desc: String? = null) {
        log(tableName, AuditLog.AuditAction.DELETE, recordId, memberId, description = desc)
    }

    suspend fun logLogin(memberId: Long) {
        log("member", AuditLog.AuditAction.LOGIN, memberId = memberId, description = "Login berhasil")
    }

    suspend fun logExport(format: String) {
        log("system", AuditLog.AuditAction.EXPORT, description = "Ekspor data format: $format")
    }

    suspend fun logRestore(backupName: String) {
        log("system", AuditLog.AuditAction.RESTORE, description = "Restore dari: $backupName")
    }
}
