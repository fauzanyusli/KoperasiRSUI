package com.example.kopkarrsui.data.local.dao

import com.example.kopkarrsui.data.local.FirestoreSupport.docFlow
import com.example.kopkarrsui.data.local.FirestoreSupport.queryFlow
import com.example.kopkarrsui.data.local.FirestoreSupport.save
import com.example.kopkarrsui.data.local.Serializers.savingsFrom
import com.example.kopkarrsui.data.local.Serializers.toMap
import com.example.kopkarrsui.data.local.entity.SavingsAccount
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

/** Pengganti Room SavingsAccountDao (lihat [com.example.kopkarrsui.data.local.FirestoreSupport]). */
class SavingsAccountDao @Inject constructor(private val db: FirebaseFirestore) {

    private fun col() = db.collection("savings_accounts")

    suspend fun insert(account: SavingsAccount): Long =
        db.save("savings_accounts", account.id, account.toMap())

    suspend fun insertAll(accounts: List<SavingsAccount>): List<Long> = accounts.map { insert(it) }

    suspend fun update(account: SavingsAccount): Int {
        if (account.id == 0L) return 0
        db.save("savings_accounts", account.id, account.toMap())
        return 1
    }

    fun getById(id: Long): Flow<SavingsAccount?> =
        docFlow(col().document(id.toString()), ::savingsFrom)

    fun getByMemberAndType(memberId: Long, jenis: SavingsAccount.SavingsType): Flow<SavingsAccount?> =
        queryFlow(col().whereEqualTo("memberId", memberId).whereEqualTo("jenis", jenis.value), ::savingsFrom)
            .map { it.firstOrNull() }.distinctUntilChanged()

    fun getByMember(memberId: Long): Flow<List<SavingsAccount>> =
        queryFlow(col().whereEqualTo("memberId", memberId), ::savingsFrom) {
            it.sortedBy { a -> a.jenis.ordinal }
        }

    fun getActiveByMember(memberId: Long): Flow<List<SavingsAccount>> =
        queryFlow(col().whereEqualTo("memberId", memberId).whereEqualTo("status", "aktif"), ::savingsFrom) {
            it.sortedBy { a -> a.jenis.ordinal }
        }

    fun getTotalSaldoByMember(memberId: Long): Flow<Long?> =
        queryFlow(col().whereEqualTo("memberId", memberId).whereEqualTo("status", "aktif"), ::savingsFrom)
            .map { list -> list.sumOf { it.saldo }.takeIf { list.isNotEmpty() } }

    fun getByType(jenis: SavingsAccount.SavingsType): Flow<List<SavingsAccount>> =
        queryFlow(col().whereEqualTo("jenis", jenis.value).whereEqualTo("status", "aktif"), ::savingsFrom)

    suspend fun adjustSaldo(id: Long, amount: Long, updatedAt: Long): Int {
        val ref = col().document(id.toString())
        if (!ref.get().await().exists()) return 0
        ref.update(
            mapOf(
                "saldo" to FieldValue.increment(amount),
                "updatedAt" to updatedAt
            )
        ).await()
        return 1
    }

    suspend fun updateStatus(id: Long, status: SavingsAccount.SavingsStatus, updatedAt: Long): Int {
        val ref = col().document(id.toString())
        if (!ref.get().await().exists()) return 0
        ref.update(mapOf("status" to status.value, "updatedAt" to updatedAt)).await()
        return 1
    }
}
