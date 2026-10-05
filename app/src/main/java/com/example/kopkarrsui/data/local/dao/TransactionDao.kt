package com.example.kopkarrsui.data.local.dao

import com.example.kopkarrsui.data.local.FirestoreSupport.docFlow
import com.example.kopkarrsui.data.local.FirestoreSupport.fetch
import com.example.kopkarrsui.data.local.FirestoreSupport.queryFlow
import com.example.kopkarrsui.data.local.FirestoreSupport.save
import com.example.kopkarrsui.data.local.Serializers.toMap
import com.example.kopkarrsui.data.local.Serializers.transactionFrom
import com.example.kopkarrsui.data.local.entity.Transaction
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.map
import javax.inject.Inject

/** Pengganti Room TransactionDao (lihat [com.example.kopkarrsui.data.local.FirestoreSupport]). */
class TransactionDao @Inject constructor(private val db: FirebaseFirestore) {

    private fun col() = db.collection("transactions")

    suspend fun insert(transaction: Transaction): Long =
        db.save("transactions", transaction.id, transaction.toMap())

    suspend fun insertAll(transactions: List<Transaction>): List<Long> = transactions.map { insert(it) }

    suspend fun update(transaction: Transaction): Int {
        if (transaction.id == 0L) return 0
        db.save("transactions", transaction.id, transaction.toMap())
        return 1
    }

    fun getById(id: Long): Flow<Transaction?> =
        docFlow(col().document(id.toString()), ::transactionFrom)

    fun getByMemberPaged(memberId: Long, limit: Int, offset: Int): Flow<List<Transaction>> =
        queryFlow(col().whereEqualTo("memberId", memberId), ::transactionFrom) {
            it.sortedByDescending { t -> t.tgl }.drop(offset).take(limit)
        }

    fun getByMemberAndType(memberId: Long, tipe: Transaction.TransactionType): Flow<List<Transaction>> =
        queryFlow(col().whereEqualTo("memberId", memberId).whereEqualTo("tipe", tipe.value), ::transactionFrom) {
            it.sortedByDescending { t -> t.tgl }
        }

    fun getByMemberAndDateRange(memberId: Long, startDate: Long, endDate: Long): Flow<List<Transaction>> =
        queryFlow(col().whereEqualTo("memberId", memberId), ::transactionFrom) { list ->
            list.filter { it.tgl in startDate..endDate }.sortedByDescending { t -> t.tgl }
        }

    fun getByMemberAndStatus(memberId: Long, status: Transaction.TransactionStatus): Flow<List<Transaction>> =
        queryFlow(col().whereEqualTo("memberId", memberId).whereEqualTo("status", status.value), ::transactionFrom) {
            it.sortedByDescending { t -> t.tgl }
        }

    fun getTotalBelanjaByMember(memberId: Long): Flow<Long?> =
        queryFlow(col().whereEqualTo("memberId", memberId).whereEqualTo("tipe", "belanja").whereEqualTo("status", "sukses"), ::transactionFrom)
            .map { list -> list.sumOf { it.jumlah }.takeIf { list.isNotEmpty() } }

    fun getTotalPoinByMember(memberId: Long): Flow<Int?> =
        queryFlow(col().whereEqualTo("memberId", memberId).whereEqualTo("status", "sukses"), ::transactionFrom)
            .map { list -> list.sumOf { it.poinDihasilkan }.takeIf { list.isNotEmpty() } }

    fun getByUnitUsahaAndDateRange(unitUsaha: String, startDate: Long, endDate: Long): Flow<List<Transaction>> =
        queryFlow(col().whereEqualTo("refUnitUsaha", unitUsaha), ::transactionFrom) { list ->
            list.filter { it.tgl in startDate..endDate }.sortedByDescending { t -> t.tgl }
        }

    suspend fun countSuccessByMember(memberId: Long): Int =
        fetch(col().whereEqualTo("memberId", memberId).whereEqualTo("status", "sukses"), ::transactionFrom).size
}
