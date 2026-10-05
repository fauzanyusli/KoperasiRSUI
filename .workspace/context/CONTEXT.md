# CONTEXT.md — Domain & Technical Knowledge

## Glossary (Detail)

### Anggota
- **Definisi**: Nasabah terdaftar koperasi, memiliki nomor anggota unik
- **Atribut**: noAnggota (PK), nama, NIK, noHP, email, alamat, tglGabung, status (aktif/nonaktif/keluar), PIN/biometrik hash
- **Relasi**: 1 Anggota → * Tabungan, * Transaksi, * PoinLedger, * SHUAllocation

### Tabungan
- **Jenis**: 
  - **Wajib** — setoran bulanan wajib, tidak bisa ditarik sebelum keluar
  - **Sukarela** — setoran bebas, bisa ditarik kapan saja (syarat saldo min)
  - **Hari Tua** — pensiun, tarik usia tertentu
  - **Khusus** — pendidikan, haji, dll
- **Atribut**: id (PK), anggotaId (FK), jenisEnum, saldo, bungaTahun, tglBuka, status
- **Bunga**: dihitung bulanan/tahunan per jenis, masuk ke saldo

### SHU (Sisa Hasil Usaha)
- **Sumber**: keuntungan koperasi tahunan (pendapatan - biaya operasional - cadangan)
- **Alokasi**: 
  - Cadangan wajib (min 10%)
  - Pembangunan koperasi
  - **Bagi hasil anggota** — proporsional: tabungan + poin + nilai usaha
- **Per anggota**: SHUAllocation(tahun, jumlah, status: dihitung/dibagikan/dicairkan)

### Poin (Point Member)
- **Sumber**: belanja di koperasi (unit usaha: mart, simpan pinjol, dll)
- **Konversi**: misal Rp 1.000 = 1 poin (konfigurasi per unit usaha)
- **Kadaluarsa**: opsional, misal 2 tahun
- **Redeem**: tukar hadiah/diskon (belum MVP)
- **Ledger**: PoinLedger(id, anggotaId, tipe: EARN/REDEEM/EXPIRE, jumlah, refTransaksiId, tgl)

### Transaksi
- **Tipe**: BELANJA, SETORAN_TABUNGAN, PENARIKAN_TABUNGAN, PEMBAYARAN_ANGSURAN, LAINNYA
- **Atribut**: id (PK), anggotaId (FK), tipeEnum, jumlah, keterangan, refId (invoice/pinjaman), tgl, status (sukses/pending/gagal)
- **Poin**: hanya BELANJA generate poin (via trigger/service)

### Pengurus (Admin)
- **Role**: KETUA, BENDAHARA, SEKRETARIS, PJ_KEANGGOTAAN
- **Atribut**: anggotaId (FK, one-to-one), roleEnum, izin (CRUD per fitur), aktifSince
- **Auth**: login terpisah dari anggota (admin panel) atau role-based di app yang sama

## Domain Model (Relasi)
```
Anggota (1) ──────< Tabungan (jenis: wajib|sukarela|hariTua|khusus)
Anggota (1) ──────< Transaksi (tipe: belanja|setoran|penarikan|angsuran)
Anggota (1) ──────< PoinLedger (tipe: earn|redeem|expire)
Anggota (1) ──────< SHUAllocation (tahun, jumlah, status)
Koperasi (1) ──────< LaporanKeuangan (neraca, laba_rugi, SHU_total per tahun)
Pengurus (1) ────── Anggota (1:1, role-based)
```

## Business Rules (Draft)
1. **Poin hanya dari belanja** — setoran tabungan tidak generate poin
2. **SHU hitung tahunan** — setelah RAT/rapat anggota setuju
3. **Tabungan wajib tidak bisa ditarik** — kecuali keluar anggota
4. **Saldo tabungan = total setoran - total penarikan + bunga**
5. **Anggota nonaktif** — tidak bisa transaksi, poin freeze

## Technical Decisions (Open)
- [x] Room only vs Room + Backend API → **Firebase (Auth + Firestore)**; Room dihapus total (04-10-2026)
- [x] Auth: PIN lokal saja / JWT + biometrik / Firebase Auth → **bridging Firebase Auth** (email = noAnggota@kopkar.local, PIN = password; hash lokal BCrypt tetap validasi utama; offline → fallback anonymous)
- [x] Sync strategy → **realtime Firestore** (Flow listener per koleksi, lewat FirestoreSupport)
- [ ] SHU calculation: client-side (read-only) / server-side push

## Gotchas
- Nomor anggota bisa non-sekuensial (format: KOP-YYYY-XXXX)
- Transaksi belanja bisa dari unit usaha berbeda (mart, simpan pinjol) — perlu refUnitUsaha
- Bunga tabungan: metode flat vs efektif — konfirmasi ke bendahara
- SHU per anggota butuh data: total tabungan + total poin + nilai usaha anggota