package com.example.kopkarrsui.presentation.screen.savings

enum class LoanType(val label: String, val maxAmount: Double, val tenorBulan: Int, val bungaPersen: Double, val description: String) {
    KONSUMTIF("Pinjaman Konsumtif", 10_000_000.0, 12, 5.0, "Kebutuhan konsumtif, maks Rp 10 jt"),
    PRODUKTIF("Pinjaman Produktif", 25_000_000.0, 24, 6.0, "Usaha/produktif, maks Rp 25 jt"),
    DARURAT("Pinjaman Darurat", 5_000_000.0, 6, 3.0, "Keadaan darurat, maks Rp 5 jt")
}

data class Loan(val id: Long = 0, val anggotaId: Long = 0, val namaPeminjam: String = "", val jenis: LoanType = LoanType.KONSUMTIF, val jumlah: Double = 0.0, val sisaBayar: Double = 0.0, val cicilanBulanan: Double = 0.0, val tanggalPinjam: String = "", val jatuhTempo: String = "", val status: LoanStatus = LoanStatus.AKTIF, val tenorBulan: Int = 0, val approvedBy: String? = null) {
    enum class LoanStatus(val label: String) { AKTIF("Aktif"), LUNAS("Lunas"), MACET("Macet") }
}

data class SavingsHistory(val id: Long, val deskripsi: String, val jenis: String, val jumlah: Double, val tanggal: String, val isIncome: Boolean)
data class MemberSavings(val noAnggota: String, val nama: String, val jabatan: String, val pokok: Double, val wajib: Double, val sukarela: Double)
data class CicilanItem(val id: Long, val nama: String, val jenis: String, val jumlahCicilan: Double, val bulanDibayar: Int, val totalBulan: Int, val status: String, val color: androidx.compose.ui.graphics.Color)
