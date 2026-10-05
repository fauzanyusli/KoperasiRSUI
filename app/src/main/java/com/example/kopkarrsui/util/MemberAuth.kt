package com.example.kopkarrsui.util

/**
 * Mapping No. Anggota → email internal buat Firebase Auth.
 * Login tetap No. Anggota + PIN; PIN dipakai sebagai password Firebase Auth.
 */
object MemberAuth {
    private const val DOMAIN = "@kopkar.local"

    fun emailFor(noAnggota: String): String =
        noAnggota.trim().lowercase() + DOMAIN
}
