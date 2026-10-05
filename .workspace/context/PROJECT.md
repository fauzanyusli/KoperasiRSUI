---
status: in-progress
setup_date: 2025-09-25
context_updated: 2026-10-04
has_context: true
has_architecture: false
---

# PROJECT.md — Quick Reference

## Commands
- `./gradlew assembleDebug` — build debug APK (output: `app/build/outputs/apk/debug/app-debug.apk`, ~69 MB)
- `./gradlew test` — unit test
- `./gradlew connectedAndroidTest` — instrumented test

## Status (04-10-2026)
- MVP jalan: Login (BCrypt), Dashboard, Tabungan, Poin, Transaksi, SHU, RAT, Profile, Admin (member mgmt, audit log, backup/restore)
- Fitur masuk: notifikasi lokal, export CSV, konfirmasi aksi destructive, routing role admin, siap build release
- **Migrasi data layer Room → Firebase (Auth + Firestore)** selesai di level kode, build & unit test lolos
- **BLOCKER runtime**: `google-services.json` belum ada → app buka layar "Firebase belum dikonfigurasi" (guard `FirebaseStatus` di `MainActivity`). Aktifkan dengan menaruh file dari Firebase console ke `app/` lalu build ulang.

## File Map (1-line/file)
- `MainActivity.kt` — entry Compose + guard Firebase (MissingFirebaseScreen) + bottom bar
- `util/FirebaseStatus.kt` — deteksi FirebaseApp ter-init tanpa crash
- `util/MemberAuth.kt` — mapping noAnggota → email internal (`<noAnggota>@kopkar.local`)
- `data/local/FirestoreSupport.kt` — helper `docFlow`/`queryFlow`/`save`/`fetch` Firestore
- `data/local/Serializers.kt` — entity ↔ Map untuk Firestore
- `data/local/Seeder.kt` — seed data awal sekali jalan (pengganti SeedCallback Room)
- `data/local/dao/*Dao.kt` — signature sama persis DAO Room, backend Firestore
- `di/DatabaseModule.kt` — provide `FirebaseAuth` + `FirebaseFirestore` (pengganti Room module)
- `presentation/viewmodel/LoginViewModel.kt` — login bridging Firebase Auth: self-provision saat login pertama, fallback offline
- `app/build.gradle.kts` — deps: Compose BOM, Material3, Hilt, Firebase; plugin google-services kondisional (hanya kalau file ada)

## Istilah Inti (1-line/istilah)
- **Anggota** — nasabah koperasi yang punya rekening tabungan & poin
- **Tabungan** — dana anggota (wajib/sukarela/hari tua), bisa setor/tarik
- **SHU** — Sisa Hasil Usaha, bagi hasil tahunan per anggota
- **Poin** — reward dari akumulasi belanja, konversi Rp→poin
- **Transaksi** — belanja, setoran, penarikan, pembayaran angsuran
- **Pengurus** — ketua, bendahara, sekretaris, PJ keanggotaan (role admin)

## Konvensi
- Package: `com.example.kopkarrsui`
- Min SDK 29, Target SDK 34, Kotlin 2.0.20, Compose BOM 2024.04.01
- MVVM + Repository + Hilt; backend Firebase Auth + Firestore (Room sudah dihapus)

## Schema (entity, disimpan di koleksi Firestore — nama koleksi per DAO)
- **Member** — PK: Long (auto), unique noAnggota (String)
- **SavingsAccount** — PK: Long, FK memberId, jenis ENUM (wajib/sukarela/hariTua/khusus)
- **Transaction** — PK: Long, FK memberId, tipe ENUM, refUnitUsaha (String nullable)
- **PointLedger** — PK: Long, FK memberId, tipe ENUM (earn/redeem/expire), refTransaksiId
- **SHUAllocation** — PK: Long, FK memberId, tahun (Int), jumlah (Long), status ENUM
- **FinancialStatement** — PK: Long, tahun (Int), neracaJson (TEXT), labaRugiJson (TEXT)
- **Admin** — PK: Long, FK memberId (1:1), role ENUM, izin JSON
- **AuditLog** — log aksi admin (lihat `AuditLogDao`)

## Next Steps
1. [ ] Taruh `google-services.json` → build ulang → smoke test login (no. anggota + PIN) di device
2. [ ] Aturan Firestore: buat rules (read/write per role) di console
3. [ ] Verifikasi end-to-end: seed → login → mutasi tabungan → poin → SHU
4. [ ] Buat release build (signing config) untuk distribusi
5. [ ] Studi banding checklist di `PROJECT_SPEC.md` (masih banyak belum diisi)
