package com.example.kopkarrsui.data.local

import android.content.Context
import com.example.kopkarrsui.data.local.FirestoreSupport.setCounter
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.tasks.await
import org.json.JSONArray
import org.json.JSONObject
import java.io.File
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

/**
 * Backup/restore Firestore ↔ file JSON.
 * Pengganti backup file .db Room — database sekarang cloud, file lokal hanya ekspor.
 * ponytail: restore menimpa dokumen per ID; tidak ada version/conflict handling —
 * cukup untuk data koperasi skala kecil. Upgrade path: Cloud Firestore export
 * (gcloud firestore export) untuk backup skala besar.
 */
object DatabaseBackup {

    private const val BACKUP_DIR = "backups"

    data class BackupResult(val success: Boolean, val message: String, val filePath: String? = null)

    fun backup(context: Context): BackupResult {
        return try {
            val root = JSONObject()
            val db = FirebaseFirestore.getInstance()
            runBlocking {
                for (collection in Serializers.all) {
                    @Suppress("UNCHECKED_CAST")
                    val col = collection as Serializers.BackupCollection<Any>
                    val array = JSONArray()
                    db.collection(col.name).get().await().documents.forEach { doc ->
                        val entity = col.fromMap(doc.data.orEmpty())
                        array.put(toJson(col.toMap(entity)))
                    }
                    root.put(col.name, array)
                }
            }

            val timestamp = SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(Date())
            val backupDir = File(context.getExternalFilesDir(null), BACKUP_DIR)
            if (!backupDir.exists()) backupDir.mkdirs()

            val backupFile = File(backupDir, "kopkar_backup_$timestamp.json")
            backupFile.writeText(root.toString())

            val sizeKb = backupFile.length() / 1024
            BackupResult(true, "Backup berhasil: ${backupFile.name} (${sizeKb}KB)", backupFile.absolutePath)
        } catch (e: Exception) {
            BackupResult(false, "Gagal backup: ${e.message}")
        }
    }

    fun restore(context: Context, backupFile: File): BackupResult {
        return try {
            if (!backupFile.exists()) {
                return BackupResult(false, "File backup tidak ditemukan")
            }
            val root = JSONObject(backupFile.readText())
            val db = FirebaseFirestore.getInstance()

            runBlocking {
                for (collection in Serializers.all) {
                    @Suppress("UNCHECKED_CAST")
                    val col = collection as Serializers.BackupCollection<Any>
                    val array = root.optJSONArray(col.name) ?: continue

                    var batch = db.batch()
                    var ops = 0
                    var maxId = 0L
                    for (i in 0 until array.length()) {
                        val data = fromJson(array.getJSONObject(i))
                        val entity = col.fromMap(data)
                        val id = col.id(entity)
                        if (id > maxId) maxId = id
                        batch.set(
                            db.collection(col.name).document(id.toString()),
                            data + ("id" to id)
                        )
                        ops++
                        if (ops >= 400) {
                            batch.commit().await()
                            batch = db.batch()
                            ops = 0
                        }
                    }
                    if (ops > 0) batch.commit().await()
                    if (maxId > 0) db.setCounter(col.name, maxId)
                }
            }

            BackupResult(true, "Restore berhasil dari: ${backupFile.name}")
        } catch (e: Exception) {
            BackupResult(false, "Gagal restore: ${e.message}")
        }
    }

    fun getBackupList(context: Context): List<File> {
        val backupDir = File(context.getExternalFilesDir(null), BACKUP_DIR)
        if (!backupDir.exists()) return emptyList()
        return backupDir.listFiles { file -> file.extension == "json" }
            ?.sortedByDescending { it.lastModified() }
            ?: emptyList()
    }

    fun deleteBackup(file: File): Boolean {
        return file.delete()
    }

    private fun toJson(map: Map<String, Any?>): JSONObject {
        val json = JSONObject()
        map.forEach { (key, value) -> json.put(key, value ?: JSONObject.NULL) }
        return json
    }

    private fun fromJson(json: JSONObject): Map<String, Any?> {
        val map = mutableMapOf<String, Any?>()
        val keys = json.keys()
        while (keys.hasNext()) {
            val key = keys.next()
            val value = json.opt(key)
            map[key] = if (value == JSONObject.NULL) null else value
        }
        return map
    }
}
