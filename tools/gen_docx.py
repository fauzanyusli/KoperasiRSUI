#!/usr/bin/env python3
# Generator dokumentasi .docx (stdlib only, tanpa python-docx)
import zipfile, os

OUT = r"D:/OJAN/File BSI Fauzan/Semester 4/Tugas Kuliah/KopkarRSUI/Dokumentasi_Kompresi_Kode.docx"

def esc(t):
    return t.replace("&", "&amp;").replace("<", "&lt;").replace(">", "&gt;")

def para(text, bold=False, size=22, bullet=False):
    b = "<w:b/>" if bold else ""
    ind = '<w:ind w:left="360"/>' if bullet else ""
    ppr = f"<w:pPr>{ind}</w:pPr>" if ind else ""
    return (f"<w:p>{ppr}<w:r><w:rPr>{b}<w:sz w:val='{size}'/></w:rPr>"
            f"<w:t xml:space='preserve'>{esc(text)}</w:t></w:r></w:p>")

# (style, text): h1=judul bab, h2=sub, p=paragraf, b=bullet, c=kode
CONTENT = [
    ("h1", "Dokumentasi Kompresi Kode — Aplikasi KopkarRSUI"),
    ("p", "Proyek: Aplikasi Koperasi Karyawan RSUI (Android, Kotlin, Jetpack Compose, Hilt, Room)"),
    ("p", "Lokasi: D:\\OJAN\\File BSI Fauzan\\Semester 4\\Tugas Kuliah\\KopkarRSUI"),
    ("p", "Tanggal: 30 September 2026 (sesi review & optimasi kode)"),

    ("h2", "1. Ringkasan"),
    ("p", "Dilakukan review menyeluruh terhadap seluruh kode aplikasi (70 file Kotlin, ±6.900 baris di app/src/main). Kode dinilai sehat secara arsitektur (Clean Architecture berlapis), tetapi terdapat duplikasi, dead code, dan literal berulang yang bisa dikonsolidasi."),
    ("p", "Kompresi dilakukan bertahap dan tidak mengubah perilaku aplikasi:"),
    ("b", "Tahap 1 — Unifikasi fungsi formatRupiah (3 implementasi duplikat → 1 util)."),
    ("b", "Tahap 2 — Penghapusan dead code (±400 baris terbuang)."),
    ("b", "Tahap 3 — Konsolidasi warna hardcode hex → palet konstanta Kopkar (176 kemunculan)."),
    ("b", "Tahap 4 — Konsolidasi literal ukuran teks sp → objek TextSize (320 kemunculan)."),
    ("p", "Hasil akhir: 70 file, 6.596 baris (turun dari ±6.900 baris), build debug sukses, aplikasi terinstall dan berjalan normal di perangkat uji (Samsung SM-A325F)."),

    ("h2", "2. Tahap 1 — Unifikasi formatRupiah"),
    ("p", "Masalah: format rupiah diimplementasikan 3 kali dengan cara berbeda:"),
    ("c", "presentation/screen/savings/SavingsModels.kt — chunked manual (Double)"),
    ("c", "presentation/viewmodel/{Dashboard,Savings,SHU,Transaction}ViewModel.kt — NumberFormat (Long), identik 4x"),
    ("c", "presentation/screen/dashboard/DashboardScreen.kt — formatRupiahShort + chunked inline di FinancialCard (MemberManagementScreen.kt)"),
    ("p", "Solusi: satu sumber di file baru util/FormatUtils.kt:"),
    ("c", "fun formatRupiah(amount: Long): String  // NumberFormat locale id-ID"),
    ("c", "fun formatRupiah(amount: Double): String // delegasi ke versi Long"),
    ("c", "fun formatRupiahShort(amount: Double): String // ringkas: 1,5jt / 500rb"),
    ("p", "Keempat ViewModel kini mendelegasikan ke util (satu baris), sehingga 40+ titik pemanggilan di layar tidak perlu diubah. Impor java.text.NumberFormat/java.util.Locale yang tidak terpakai dibersihkan dari SavingsViewModel dan SHUViewModel."),

    ("h2", "3. Tahap 2 — Penghapusan Dead Code"),
    ("b", "data/local/AuditLogger.kt dihapus seluruhnya — class dengan 0 referensi (tidak pernah di-inject atau dipanggil)."),
    ("b", "DashboardScreen.kt: blok 'Aksi Cepat' (3 tombol dengan onClick kosong) dan composable QuickAction dihapus."),
    ("b", "DashboardScreen.kt: tombol Telepon dan Edit di kartu anggota (onClick kosong) dihapus."),
    ("b", "DashboardScreen.kt: state showMemberList dideclare tapi tidak pernah dipakai — dihapus."),
    ("b", "DashboardScreen.kt: formatRupiahShort lokal digantikan versi dari FormatUtils."),
    ("b", "DashboardScreen.kt: 7 impor ikon mati dihapus (AddCircle, Call, Delete, Edit, Phone, RemoveCircle, IconButton)."),
    ("p", "Penghematan bersih tahap ini: ±400 baris."),

    ("h2", "4. Tahap 3 — Konsolidasi Warna (Hex Hardcode)"),
    ("p", "Masalah: 11 nilai warna hex di-hardcode inline sebanyak 176 kemunculan di seluruh layar (contoh: Color(0xFF2E7D32) muncul 56 kali). Sulit konsisten dan sulit ganti tema."),
    ("p", "Solusi: palet bernama ditambahkan ke ui/theme/Color.kt:"),
    ("b", "KopkarGreen   = 0xFF2E7D32 (56x)  — aksi positif, saldo, status aktif"),
    ("b", "KopkarRed     = 0xFFC62828 (33x)  — aksi negatif, status nonaktif"),
    ("b", "KopkarBlue    = 0xFF1565C0 (26x)  — header, kartu utama"),
    ("b", "KopkarOrange  = 0xFFFF8F00 (16x)  — poin, aksen"),
    ("b", "KopkarPurple  = 0xFF6A1B9A (14x)  — statistik, kartu anggota"),
    ("b", "KopkarTeal    = 0xFF00897B (14x)  — aksen sekunder"),
    ("b", "KopkarTealDark= 0xFF00695C  (5x)  — suplier"),
    ("b", "KopkarGray    = 0xFF9E9E9E  (6x)  — status keluar"),
    ("b", "KopkarGrayDark= 0xFF757575  (1x)  — teks sekunder"),
    ("b", "KopkarDeepOrange = 0xFFE65100 (4x) — status keanggotaan"),
    ("b", "KopkarYellow  = 0xFFFFEB3B  (1x)  — aksen kartu"),
    ("p", "17 file di presentation/ dan util/ diubah: seluruh Color(0x...) diganti konstanta, ditambah satu impor wildcard import com.example.kopkarrsui.ui.theme.* per file. Hex tersisa hanya di ui/theme (definisi palet)."),

    ("h2", "5. Tahap 4 — Konsolidasi Ukuran Teks (Literal sp)"),
    ("p", "Masalah: 13 nilai ukuran teks literal berulang 320 kali (10.sp, 11.sp, 12.sp, ... 28.sp)."),
    ("p", "Solusi: objek TextSize di file baru ui/theme/TextSize.kt:"),
    ("c", "object TextSize { val s10 = 10.sp; val s11 = 11.sp; ... val s28 = 28.sp }"),
    ("p", "Seluruh pemanggilan diganti otomatis, contoh: fontSize = 14.sp menjadi fontSize = TextSize.s14. Skala teks kini terpusat — menyesuaikan tipografi aplikasi cukup dari satu file."),

    ("h2", "6. Hal yang Sengaja TIDAK Dikompresi"),
    ("b", "Lapisan Clean Architecture (domain/interface repository + data/repository impl): murni pass-through ke DAO, tetapi ini standar tugas kuliah dan bernilai akademik — tidak dihapus."),
    ("b", "Layar data dummy (sampleMembers 5x, RATScreen, SupplierSection): berpotensi menghapus ±600-700 baris, tetapi menyangkut kebutuhan demo/tampilan tugas — menunggu konfirmasi dosen/pengampu."),
    ("b", "Konstanta warna hex di dalam objek TextSize/palet memang tetap hex (1 tempat saja)."),
    ("b", "Placeholder tombol di layar login (Biometrik, Hubungi Pengurus, Daftar) dibiarkan — bagian ekspektasi UI autentikasi."),

    ("h2", "7. Contoh Sebelum vs Sesudah"),
    ("p", "Sebelum (terulang di banyak file):"),
    ("c", "StatCard(..., iconColor = Color(0xFF2E7D32), ...)"),
    ("c", "Text(\"...\", fontSize = 12.sp, color = Color(0xFFC62828))"),
    ("p", "Sesudah:"),
    ("c", "StatCard(..., iconColor = KopkarGreen, ...)"),
    ("c", "Text(\"...\", fontSize = TextSize.s12, color = KopkarRed)"),

    ("h2", "8. Daftar File yang Diubah"),
    ("b", "BARU: app/src/main/java/com/example/kopkarrsui/util/FormatUtils.kt"),
    ("b", "BARU: app/src/main/java/com/example/kopkarrsui/ui/theme/TextSize.kt"),
    ("b", "DIUBAH: ui/theme/Color.kt (palet Kopkar ditambahkan)"),
    ("b", "DIUBAH: 4 file viewmodel (delegasi formatRupiah, bersih impor)"),
    ("b", "DIUBAH: 17 file di presentation/ + util/ (warna & ukuran teks)"),
    ("b", "DIHAPUS: data/local/AuditLogger.kt"),
    ("b", "DIHAPUS: blok/matian di presentation/screen/dashboard/DashboardScreen.kt"),
    ("b", "DIUBAH: presentation/screen/savings/SavingsModels.kt, SavingsComponents.kt, SavingsTabContent.kt"),
    ("b", "DIUBAH: presentation/screen/dashboard/MemberManagementScreen.kt"),

    ("h2", "9. Cara Pakai Konstanta (untuk Pengembangan Berikutnya)"),
    ("c", "import com.example.kopkarrsui.ui.theme.*   // KopkarGreen, TextSize, dst."),
    ("c", "Button(colors = buttonColors(containerColor = KopkarGreen))"),
    ("c", "Text(\"Judul\", fontSize = TextSize.s16, fontWeight = FontWeight.Bold)"),
    ("p", "Aturan: jangan tulis Color(0x...) atau ...sp langsung di layar baru. Warna di luar palet = tambah val baru di Color.kt dulu. Ukuran teks di luar skala = tambah val baru di TextSize.kt dulu."),
]

def render():
    body = []
    for style, text in CONTENT:
        if style == "h1":
            body.append(para(text, bold=True, size=32))
        elif style == "h2":
            body.append(para(text, bold=True, size=26))
        elif style == "b":
            body.append(para("• " + text, size=22, bullet=True))
        elif style == "c":
            body.append(para(text, size=20))
        else:
            body.append(para(text, size=22))
    return ('<?xml version="1.0" encoding="UTF-8" standalone="yes"?>'
            '<w:document xmlns:w="http://schemas.openxmlformats.org/wordprocessingml/2006/main">'
            f"<w:body>{''.join(body)}</w:body></w:document>")

content_types = ('<?xml version="1.0" encoding="UTF-8" standalone="yes"?>'
    '<Types xmlns="http://schemas.openxmlformats.org/package/2006/content-types">'
    '<Default Extension="rels" ContentType="application/vnd.openxmlformats-package.relationships+xml"/>'
    '<Default Extension="xml" ContentType="application/xml"/>'
    '<Override PartName="/word/document.xml" ContentType="application/vnd.openxmlformats-officedocument.wordprocessingml.document.main+xml"/>'
    '</Types>')

rels = ('<?xml version="1.0" encoding="UTF-8" standalone="yes"?>'
    '<Relationships xmlns="http://schemas.openxmlformats.org/package/2006/relationships">'
    '<Relationship Id="rId1" Type="http://schemas.openxmlformats.org/officeDocument/2006/relationships/officeDocument" Target="word/document.xml"/>'
    '</Relationships>')

with zipfile.ZipFile(OUT, "w", zipfile.ZIP_DEFLATED) as z:
    z.writestr("[Content_Types].xml", content_types)
    z.writestr("_rels/.rels", rels)
    z.writestr("word/document.xml", render())

print("OK:", OUT, os.path.getsize(OUT), "bytes")
