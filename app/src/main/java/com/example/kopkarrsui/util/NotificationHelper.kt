package com.example.kopkarrsui.util

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.example.kopkarrsui.MainActivity
import com.example.kopkarrsui.R

object NotificationHelper {

    private const val CHANNEL_REMINDER = "channel_reminder"
    private const val CHANNEL_INFO = "channel_info"

    private const val NOTIF_SETORAN = 1001
    private const val NOTIF_SHU = 1002
    private const val NOTIF_PINJAMAN = 1003
    private const val NOTIF_WELCOME = 1004

    fun createChannels(context: Context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val manager = context.getSystemService(NotificationManager::class.java)

            val reminderChannel = NotificationChannel(
                CHANNEL_REMINDER,
                "Pengingat",
                NotificationManager.IMPORTANCE_DEFAULT
            ).apply {
                description = "Pengingat setoran dan pembayaran"
            }

            val infoChannel = NotificationChannel(
                CHANNEL_INFO,
                "Informasi",
                NotificationManager.IMPORTANCE_LOW
            ).apply {
                description = "Informasi umum koperasi"
            }

            manager.createNotificationChannels(listOf(reminderChannel, infoChannel))
        }
    }

    private fun getMainActivityIntent(context: Context): PendingIntent {
        val intent = Intent(context, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }
        return PendingIntent.getActivity(
            context, 0, intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )
    }

    fun showWelcomeNotification(context: Context, namaAnggota: String) {
        val notif = NotificationCompat.Builder(context, CHANNEL_INFO)
            .setSmallIcon(R.drawable.ic_launcher_foreground)
            .setContentTitle("Selamat Datang, $namaAnggota!")
            .setContentText("Selamat datang di aplikasi KopkarRSUI")
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setContentIntent(getMainActivityIntent(context))
            .setAutoCancel(true)
            .build()

        try {
            NotificationManagerCompat.from(context).notify(NOTIF_WELCOME, notif)
        } catch (_: SecurityException) { }
    }

    fun showSetoranReminder(context: Context) {
        val notif = NotificationCompat.Builder(context, CHANNEL_REMINDER)
            .setSmallIcon(R.drawable.ic_launcher_foreground)
            .setContentTitle("Pengingat Setoran")
            .setContentText("Setoran tabungan wajib bulan ini belum dilakukan. Segera setor!")
            .setStyle(NotificationCompat.BigTextStyle().bigText(
                "Setoran tabungan wajib bulan ini belum dilakukan. " +
                "Silakan melakukan setoran melalui aplikasi atau ke pengurus koperasi."
            ))
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setContentIntent(getMainActivityIntent(context))
            .setAutoCancel(true)
            .build()

        try {
            NotificationManagerCompat.from(context).notify(NOTIF_SETORAN, notif)
        } catch (_: SecurityException) { }
    }

    fun showShuNotification(context: Context, jumlah: String) {
        val notif = NotificationCompat.Builder(context, CHANNEL_INFO)
            .setSmallIcon(R.drawable.ic_launcher_foreground)
            .setContentTitle("SHU Tersedia")
            .setContentText("Bagian Hasil Usaha (SHU) tahun ini telah dihitung: $jumlah")
            .setStyle(NotificationCompat.BigTextStyle().bigText(
                "Bagian Hasil Usaha (SHU) tahun ini telah dihitung. " +
                "Anda berhak menerima SHU sebesar $jumlah. " +
                "Silakan cek detail di menu SHU."
            ))
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setContentIntent(getMainActivityIntent(context))
            .setAutoCancel(true)
            .build()

        try {
            NotificationManagerCompat.from(context).notify(NOTIF_SHU, notif)
        } catch (_: SecurityException) { }
    }

    fun showPinjamanNotification(context: Context, status: String) {
        val notif = NotificationCompat.Builder(context, CHANNEL_REMINDER)
            .setSmallIcon(R.drawable.ic_launcher_foreground)
            .setContentTitle("Status Pinjaman")
            .setContentText("Pengajuan pinjaman anda: $status")
            .setStyle(NotificationCompat.BigTextStyle().bigText(
                "Pengajuan pinjaman anda telah $status. " +
                "Silakan cek detail di menu Tabungan > Pinjaman."
            ))
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setContentIntent(getMainActivityIntent(context))
            .setAutoCancel(true)
            .build()

        try {
            NotificationManagerCompat.from(context).notify(NOTIF_PINJAMAN, notif)
        } catch (_: SecurityException) { }
    }

    fun hasNotificationPermission(context: Context): Boolean {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            NotificationManagerCompat.from(context).areNotificationsEnabled()
        } else {
            true
        }
    }
}
