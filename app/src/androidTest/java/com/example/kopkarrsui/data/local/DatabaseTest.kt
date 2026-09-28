package com.example.kopkarrsui.data.local

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.kopkarrsui.data.local.entity.Member
import com.example.kopkarrsui.data.local.entity.Member.MemberStatus
import com.example.kopkarrsui.data.local.entity.SavingsAccount
import com.example.kopkarrsui.data.local.entity.SavingsAccount.SavingsType
import com.example.kopkarrsui.data.local.entity.Transaction
import com.example.kopkarrsui.data.local.entity.Transaction.TransactionStatus
import com.example.kopkarrsui.data.local.entity.Transaction.TransactionType
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class DatabaseTest {

    private lateinit var db: KopkarDatabase
    private lateinit var context: Context

    @Before
    fun setup() {
        context = ApplicationProvider.getApplicationContext<Context>()
        db = Room.inMemoryDatabaseBuilder(context, KopkarDatabase::class.java)
            .allowMainThreadQueries()
            .build()
    }

    @After
    fun tearDown() {
        db.close()
    }

    @Test
    fun insertAndGetMember() = runBlocking {
        val member = Member(
            noAnggota = "KOP-2024-0001",
            nama = "Budi Santoso",
            nik = "1234567890123456",
            noHp = "081234567890",
            email = "budi@example.com",
            alamat = "Jl. Merdeka No. 1",
            tglGabung = System.currentTimeMillis(),
            status = MemberStatus.AKTIF,
            pinHash = "hashed_pin_123"
        )

        val id = db.memberDao().insert(member)
        assertTrue(id > 0)

        val retrieved = db.memberDao().getById(id).first()
        assertNotNull(retrieved)
        assertEquals("KOP-2024-0001", retrieved?.noAnggota)
        assertEquals("Budi Santoso", retrieved?.nama)
        assertEquals(MemberStatus.AKTIF, retrieved?.status)
    }

    @Test
    fun insertAndGetSavingsAccount() = runBlocking {
        val member = Member(
            noAnggota = "KOP-2024-0002",
            nama = "Siti Aminah",
            nik = "1234567890123457",
            noHp = "081234567891",
            email = "siti@example.com",
            alamat = "Jl. Sudirman No. 2",
            tglGabung = System.currentTimeMillis(),
            status = MemberStatus.AKTIF
        )

        val memberId = db.memberDao().insert(member)

        val savings = SavingsAccount(
            memberId = memberId,
            jenis = SavingsType.WAJIB,
            saldo = 500_000,
            bungaTahunPersen = 3.5
        )

        val savingsId = db.savingsAccountDao().insert(savings)
        assertTrue(savingsId > 0)

        val retrieved = db.savingsAccountDao().getByMemberAndType(memberId, SavingsType.WAJIB).first()
        assertNotNull(retrieved)
        assertEquals(500_000, retrieved?.saldo)
        assertEquals(SavingsType.WAJIB, retrieved?.jenis)
    }

    @Test
    fun insertTransactionAndPoinLedger() = runBlocking {
        val member = Member(
            noAnggota = "KOP-2024-0003",
            nama = "Ahmad Yani",
            nik = "1234567890123458",
            noHp = "081234567892",
            email = "ahmad@example.com",
            alamat = "Jl. Thamrin No. 3",
            tglGabung = System.currentTimeMillis(),
            status = MemberStatus.AKTIF
        )

        val memberId = db.memberDao().insert(member)

        // Belanja transaction
        val transaction = Transaction(
            memberId = memberId,
            tipe = TransactionType.BELANJA,
            jumlah = 150_000,
            keterangan = "Beli beras 10kg",
            refUnitUsaha = "mart",
            tgl = System.currentTimeMillis(),
            status = TransactionStatus.SUKSES,
            poinDihasilkan = 150 // 1000 = 1 poin
        )

        val transId = db.transactionDao().insert(transaction)
        assertTrue(transId > 0)

        // Point ledger
        val ledger = com.example.kopkarrsui.data.local.entity.PointLedger(
            memberId = memberId,
            tipe = com.example.kopkarrsui.data.local.entity.PointLedger.PointType.EARN,
            jumlah = 150,
            refTransaksiId = transId,
            keterangan = "Poin dari belanja beras"
        )

        val ledgerId = db.pointLedgerDao().insert(ledger)
        assertTrue(ledgerId > 0)

        // Verify total poin
        val totalPoin = db.pointLedgerDao().getTotalPoinByMember(memberId).first()
        assertEquals(150, totalPoin)
    }

    @Test
    fun uniqueConstraintNoAnggota() = runBlocking {
        val member1 = Member(
            noAnggota = "KOP-2024-0004",
            nama = "User 1",
            tglGabung = System.currentTimeMillis(),
            status = MemberStatus.AKTIF
        )

        val member2 = Member(
            noAnggota = "KOP-2024-0004", // same no_anggota
            nama = "User 2",
            tglGabung = System.currentTimeMillis(),
            status = MemberStatus.AKTIF
        )

        db.memberDao().insert(member1)
        val id2 = db.memberDao().insert(member2) // should replace due to OnConflictStrategy.REPLACE
        assertTrue(id2 > 0)

        val count = db.memberDao().getAll().first().size
        assertEquals(1, count) // only 1 because of unique constraint
    }
}