---
status: new
setup_date: 2025-09-25
context_updated: 2025-09-25
has_context: true
has_architecture: false
---

# PROJECT.md — Quick Reference

## Commands
- `./gradlew assembleDebug` — build debug APK
- `./gradlew test` — run unit tests
- `./gradlew connectedAndroidTest` — run instrumented tests

## File Map (1-line/file)
- `app/src/main/java/com/example/kopkarrsui/MainActivity.kt` — entry point Compose
- `app/build.gradle.kts` — deps: Compose BOM, Material3, Activity Compose
- `settings.gradle.kts` — project name: KopkarRSUI

## Istilah Inti (1-line/istilah)
- **Anggota** — nasabah koperasi yang punya rekening tabungan & poin
- **Tabungan** — dana anggota (wajib/sukarela/hari tua), bisa setor/tarik
- **SHU** — Sisa Hasil Usaha, bagi hasil tahunan per anggota
- **Poin** — reward dari akumulasi belanja, konversi Rp→poin
- **Transaksi** — belanja, setoran, penarikan, pembayaran angsuran
- **Pengurus** — ketua, bendahara, sekretaris, PJ keanggotaan (role admin)

## Konvensi
- Package: `com.example.kopkarrsui`
- Min SDK 29, Target SDK 34, Kotlin 1.8, Compose BOM 2024.x
- MVVM + Repository + Room + Hilt (planned)
- Navigation Compose (planned)

## Database Schema (Proposed)
- **Member** — PK: Long (auto), unique noAnggota (String)
- **SavingsAccount** — PK: Long, FK memberId, jenis ENUM (wajib/sukarela/hariTua/khusus)
- **Transaction** — PK: Long, FK memberId, tipe ENUM, refUnitUsaha (String nullable)
- **PointLedger** — PK: Long, FK memberId, tipe ENUM (earn/redeem/expire), refTransaksiId
- **SHUAllocation** — PK: Long, FK memberId, tahun (Int), jumlah (Long), status ENUM
- **FinancialStatement** — PK: Long, tahun (Int), neracaJson (TEXT), labaRugiJson (TEXT)
- **Admin** — PK: Long, FK memberId (1:1), role ENUM, izin JSON