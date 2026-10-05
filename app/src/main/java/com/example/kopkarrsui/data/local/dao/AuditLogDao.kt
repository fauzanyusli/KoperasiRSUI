package com.example.kopkarrsui.data.local.dao

import com.example.kopkarrsui.data.local.FirestoreSupport.fetch
import com.example.kopkarrsui.data.local.FirestoreSupport.queryFlow
import com.example.kopkarrsui.data.local.FirestoreSupport.save
import com.example.kopkarrsui.data.local.Serializers.auditLogFrom
import com.example.kopkarrsui.data.local.Serializers.toMap
import com.example.kopkarrsui.data.local.entity.AuditLog
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

/** Pengganti Room AuditLogDao (lihat [com.example.kopkarrsui.data.local.FirestoreSupport]). */
class AuditLogDao @Inject constructor(private val db: FirebaseFirestore) {

    private fun col() = db.collection("audit_logs")

    suspend fun insert(log: AuditLog): Long = db.save("audit_logs", log.id, log.toMap())

    fun getRecent(limit: Int = 100): Flow<List<AuditLog>> =
        queryFlow(col(), ::auditLogFrom) {
            it.sortedByDescending { l -> l.createdAt }.take(limit)
        }

    fun getByMember(memberId: Long): Flow<List<AuditLog>> =
        queryFlow(col().whereEqualTo("memberId", memberId), ::auditLogFrom) {
            it.sortedByDescending { l -> l.createdAt }
        }

    fun getByTable(tableName: String): Flow<List<AuditLog>> =
        queryFlow(col().whereEqualTo("tableName", tableName), ::auditLogFrom) {
            it.sortedByDescending { l -> l.createdAt }
        }

    fun getByDateRange(startDate: Long, endDate: Long): Flow<List<AuditLog>> =
        queryFlow(col(), ::auditLogFrom) { list ->
            list.filter { it.createdAt in startDate..endDate }.sortedByDescending { l -> l.createdAt }
        }

    fun getTotalCount(): Flow<Int> =
        queryFlow(col(), ::auditLogFrom).map { it.size }

    suspend fun deleteOlderThan(beforeDate: Long) {
        val targets = fetch(col().whereLessThan("createdAt", beforeDate), ::auditLogFrom)
        if (targets.isEmpty()) return
        val batch = db.batch()
        targets.forEach { log -> batch.delete(col().document(log.id.toString())) }
        batch.commit().await()
    }
}
