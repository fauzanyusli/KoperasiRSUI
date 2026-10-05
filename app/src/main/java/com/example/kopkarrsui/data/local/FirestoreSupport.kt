package com.example.kopkarrsui.data.local

import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.SetOptions
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.tasks.await

/**
 * Helper lapisan Firestore untuk menggantikan query Room.
 *
 * ponytail: semua query hanya memakai filter equality (index otomatis Firestore),
 * sort/filter/paging dilakukan di memori — aman tanpa composite index karena
 * data koperasi kecil (ratusan baris). Upgrade path: kalau data ratusan ribu,
 * buat composite index + pindahkan sort/paging ke server.
 */
object FirestoreSupport {

    /** Flow dokumen dari query (equality-only), hasil diurutkan via [sort]. */
    fun <T> queryFlow(query: Query, parse: (Map<String, Any?>) -> T?, sort: (List<T>) -> List<T> = { it }): Flow<List<T>> =
        callbackFlow {
            val registration = query.addSnapshotListener { snapshot, error ->
                if (error != null) {
                    close(error)
                    return@addSnapshotListener
                }
                val items = snapshot?.documents.orEmpty()
                    .mapNotNull { doc -> parse(doc.data.orEmpty()) }
                trySend(sort(items))
            }
            awaitClose { registration.remove() }
        }.distinctUntilChanged()

    /** Flow satu dokumen (null kalau tidak ada). */
    fun <T> docFlow(ref: DocumentReference, parse: (Map<String, Any?>) -> T?): Flow<T?> =
        callbackFlow {
            val registration = ref.addSnapshotListener { snapshot, error ->
                if (error != null) {
                    close(error)
                    return@addSnapshotListener
                }
                trySend(parse(snapshot?.data.orEmpty()).takeIf { snapshot?.exists() == true })
            }
            awaitClose { registration.remove() }
        }.distinctUntilChanged()

    /** Ambil semua dokumen sekali (untuk agregat/backup). */
    suspend fun <T> fetch(query: Query, parse: (Map<String, Any?>) -> T?): List<T> =
        query.get().await().documents.mapNotNull { doc -> parse(doc.data.orEmpty()) }

    suspend fun <T> fetchAll(collection: String, parse: (Map<String, Any?>) -> T?): List<T> =
        fetch(com.google.firebase.firestore.FirebaseFirestore.getInstance().collection(collection), parse)

    /**
     * Alokasi ID numerik via counter atomik (transaksi Firestore),
     * pengganti autoGenerate PK Room.
     */
    suspend fun FirebaseFirestore.nextId(collection: String): Long {
        val counterRef = collection("counters").document(collection)
        return runTransaction { transaction ->
            val current = transaction.get(counterRef).getLong("value") ?: 0L
            val next = current + 1
            transaction.set(counterRef, mapOf("value" to next))
            next
        }.await()
    }

    /** Set counter langsung (dipakai seeder/restore supaya ID lanjut dari data yang ada). */
    suspend fun FirebaseFirestore.setCounter(collection: String, value: Long) {
        collection("counters").document(collection)
            .set(mapOf("value" to value), SetOptions.merge())
            .await()
    }

    /** Tulis dokumen dengan ID numerik; ID 0 = alokasi baru (setara REPLACE Room). */
    suspend fun FirebaseFirestore.save(collection: String, id: Long, data: Map<String, Any?>): Long {
        val finalId = if (id != 0L) id else nextId(collection)
        collection(collection).document(finalId.toString())
            .set(data + ("id" to finalId))
            .await()
        return finalId
    }
}
