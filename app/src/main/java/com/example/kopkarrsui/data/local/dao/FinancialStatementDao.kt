package com.example.kopkarrsui.data.local.dao

import com.example.kopkarrsui.data.local.FirestoreSupport.docFlow
import com.example.kopkarrsui.data.local.FirestoreSupport.fetch
import com.example.kopkarrsui.data.local.FirestoreSupport.queryFlow
import com.example.kopkarrsui.data.local.FirestoreSupport.save
import com.example.kopkarrsui.data.local.Serializers.financialFrom
import com.example.kopkarrsui.data.local.Serializers.toMap
import com.example.kopkarrsui.data.local.entity.FinancialStatement
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

/** Pengganti Room FinancialStatementDao (lihat [com.example.kopkarrsui.data.local.FirestoreSupport]). */
class FinancialStatementDao @Inject constructor(private val db: FirebaseFirestore) {

    private fun col() = db.collection("financial_statements")

    suspend fun insert(statement: FinancialStatement): Long =
        db.save("financial_statements", statement.id, statement.toMap())

    suspend fun insertAll(statements: List<FinancialStatement>): List<Long> = statements.map { insert(it) }

    suspend fun update(statement: FinancialStatement): Int {
        if (statement.id == 0L) return 0
        db.save("financial_statements", statement.id, statement.toMap())
        return 1
    }

    fun getById(id: Long): Flow<FinancialStatement?> =
        docFlow(col().document(id.toString()), ::financialFrom)

    fun getByYear(tahun: Int): Flow<FinancialStatement?> =
        queryFlow(col().whereEqualTo("tahun", tahun), ::financialFrom)
            .map { it.firstOrNull() }.distinctUntilChanged()

    fun getAll(): Flow<List<FinancialStatement>> =
        queryFlow(col(), ::financialFrom) { it.sortedByDescending { s -> s.tahun } }

    fun getByStatus(status: FinancialStatement.FinancialStatus): Flow<List<FinancialStatement>> =
        queryFlow(col().whereEqualTo("status", status.value), ::financialFrom) {
            it.sortedByDescending { s -> s.tahun }
        }

    fun getLatest(): Flow<FinancialStatement?> =
        queryFlow(col(), ::financialFrom)
            .map { list -> list.maxByOrNull { it.tahun } }.distinctUntilChanged()

    suspend fun updateStatus(tahun: Int, status: FinancialStatement.FinancialStatus, tglRAT: Long?, updatedAt: Long): Int {
        val targets = fetch(col().whereEqualTo("tahun", tahun), ::financialFrom)
        if (targets.isEmpty()) return 0
        val batch = db.batch()
        targets.forEach { statement ->
            batch.update(
                col().document(statement.id.toString()),
                mapOf("status" to status.value, "tglRAT" to tglRAT, "updatedAt" to updatedAt)
            )
        }
        batch.commit().await()
        return targets.size
    }
}
