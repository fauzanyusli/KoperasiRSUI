package com.example.kopkarrsui.data.local

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

object DatabaseBackup {

    private const val DB_NAME = "kopkar_database"

    data class BackupResult(val success: Boolean, val message: String, val filePath: String? = null)

    fun backup(context: Context): BackupResult {
        return try {
            val dbFile = context.getDatabasePath(DB_NAME)
            if (!dbFile.exists()) {
                return BackupResult(false, "Database belum ada")
            }

            val timestamp = SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(Date())
            val backupDir = File(context.getExternalFilesDir(null), "backups")
            if (!backupDir.exists()) backupDir.mkdirs()

            val backupFile = File(backupDir, "kopkar_backup_$timestamp.db")

            // Close WAL to ensure consistent backup
            SQLiteDatabase.openDatabase(dbFile.path, null, SQLiteDatabase.OPEN_READONLY).use { db ->
                db.rawQuery("PRAGMA wal_checkpoint(FULL)", null).use { it.moveToFirst() }
            }

            // Copy main db file
            dbFile.copyTo(backupFile, overwrite = true)

            // Copy WAL file if exists
            val walFile = File(dbFile.path + "-wal")
            if (walFile.exists()) {
                walFile.copyTo(File(backupFile.path + "-wal"), overwrite = true)
            }

            // Copy SHM file if exists
            val shmFile = File(dbFile.path + "-shm")
            if (shmFile.exists()) {
                shmFile.copyTo(File(backupFile.path + "-shm"), overwrite = true)
            }

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

            val dbFile = context.getDatabasePath(DB_NAME)

            // Close existing database
            SQLiteDatabase.openDatabase(dbFile.path, null, SQLiteDatabase.OPEN_READONLY).use { it.close() }

            // Delete old database
            dbFile.delete()
            File(dbFile.path + "-wal").delete()
            File(dbFile.path + "-shm").delete()
            File(dbFile.path + "-journal").delete()

            // Copy backup to db location
            backupFile.copyTo(dbFile, overwrite = true)

            // Copy WAL if exists in backup
            val backupWal = File(backupFile.path + "-wal")
            if (backupWal.exists()) {
                backupWal.copyTo(File(dbFile.path + "-wal"), overwrite = true)
            }

            // Copy SHM if exists in backup
            val backupShm = File(backupFile.path + "-shm")
            if (backupShm.exists()) {
                backupShm.copyTo(File(dbFile.path + "-shm"), overwrite = true)
            }

            BackupResult(true, "Restore berhasil dari: ${backupFile.name}")
        } catch (e: Exception) {
            BackupResult(false, "Gagal restore: ${e.message}")
        }
    }

    fun getBackupList(context: Context): List<File> {
        val backupDir = File(context.getExternalFilesDir(null), "backups")
        if (!backupDir.exists()) return emptyList()
        return backupDir.listFiles { file -> file.extension == "db" }
            ?.sortedByDescending { it.lastModified() }
            ?: emptyList()
    }

    fun deleteBackup(file: File): Boolean {
        return file.delete()
    }
}
