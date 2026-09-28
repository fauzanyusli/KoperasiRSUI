# KopkarRSUI - Aplikasi Anggota Koperasi

## Tujuan
Aplikasi mobile untuk anggota koperasi guna mengakses informasi keuangan dan keanggotaan secara real-time.

## Fitur Utama (MVP)
1. **Dashboard Poin Member** - Total poin dari akumulasi belanja
2. **Tabungan Anggota** - Saldo tabungan, mutasi, setoran/penarikan
3. **Potensi SHU** - Estimasi Sisa Hasil Usaha tahunan per anggota
4. **Neraca Koperasi** - Laporan keuntungan/rugi koperasi (transparansi)
5. **Autentikasi** - Login anggota (nomor anggota + PIN/biometrik)

## Fitur Lanjutan (Backlog)
- Notifikasi pembagian SHU, promo, meeting
- Riwayat transaksi belanja/detail poin
- Pengajuan pinjaman/simpanan
- Voting e-musyawarah
- Chat/admin support
- Dark mode, multi-bahasa

## Target User
- Seluruh anggota koperasi (usia beragam, tech literacy bervariasi)
- Admin/pengurus koperasi (dashboard terpisah atau role-based)

## Tech Stack
- **Language**: Kotlin
- **UI**: Jetpack Compose + Material 3
- **Arch**: MVVM + Clean Architecture (Repository pattern)
- **DI**: Hilt / Koin
- **Local DB**: Room (offline-first, sync saat online)
- **Network**: Retrofit + OkHttp
- **Auth**: Token-based (JWT) + BiometricPrompt
- **Navigation**: Navigation Compose
- **Testing**: JUnit, Espresso, Compose Testing

## Studi Banding Checklist
| Aspek | Pertanyaan | Catatan |
|---|---|---|
| **Auth** | Login pakai apa? (No anggota, NIK, OTP, biometrik) | |
| **Dashboard** | Layout apa? Card/grid/list? Info apa yang tampil pertama? | |
| **Poin** | Cara hitung poin? Konversi rupiah→poin? Riwayat detail ada? | |
| **Tabungan** | Jenis tabungan apa saja? (wajib, sukarela, hari tua, dll) | |
| **SHU** | Rumus hitung SHU? Tampil per anggota atau total? Proyeksi real-time? | |
| **Neraca** | Detail level? (hanya ringkasan atau full laporan keuangan) | |
| **Notif** | Push notification? In-app? Channel apa? (FCM, OneSignal) | |
| **Offline** | Bisa lihat data offline? Sync strategy? | |
| **Performa** | Loading time? Caching strategy? | |
| **UX** | Onboarding? Tutorial? Empty state? Error handling? | |
| **Aksesibilitas** | TalkBack, font scaling, kontras? | |
| **Admin** | Ada app/admin panel terpisah? CMS untuk konten? | |

## Arsitektur Data (Draft)
```
Member (1) ──< Transaction (belanja, setoran, penarikan)
Member (1) ──< SavingsAccount (wajib, sukarela, dst)
Member (1) ──< PointLedger (earn, redeem, expire)
Cooperative (1) ──< FinancialStatement (neraca, SHU per tahun)
Member (1) ──< SHUAllocation (tahun, jumlah, status)
```

## Next Steps
1. [ ] Selesai studi banding (isi checklist di atas)
2. [ ] Finalisasi requirement detail per fitur
3. [ ] Desain skema DB (Room entities)
4. [ ] Setup project: Hilt, Room, Retrofit, Navigation
5. [ ] Implementasi Auth + Token storage
6. [ ] Dashboard skeleton + navigation
7. [ ] Fitur Poin + Tabungan (CRUD + sync)
8. [ ] Fitur SHU + Neraca (read-only, computed)
9. [ ] Polish UI, testing, build release