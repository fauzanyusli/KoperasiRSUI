package com.example.kopkarrsui.util

import java.text.NumberFormat
import java.util.Locale

private val idLocale = Locale("id", "ID")

fun formatRupiah(amount: Long): String = "Rp ${NumberFormat.getInstance(idLocale).format(amount)}"

fun formatRupiah(amount: Double): String = formatRupiah(amount.toLong())

fun formatRupiahShort(amount: Double): String = when {
    amount >= 1_000_000 -> "Rp ${(amount / 1_000_000).toString().take(4)}jt"
    amount >= 1_000 -> "Rp ${(amount / 1_000).toString().take(4)}rb"
    else -> "Rp ${amount.toLong()}"
}
