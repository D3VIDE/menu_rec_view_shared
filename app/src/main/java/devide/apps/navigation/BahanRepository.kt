package devide.apps.navigation

import android.content.Context

object BahanRepository {
    private val bahanList = mutableListOf<Bahan>()
    private var isInitialized = false

    fun initializeData(context: Context) {
        if (isInitialized) return

        val namaArray = context.resources.getStringArray(R.array.data_bahan_nama)
        val kategoriArray = context.resources.getStringArray(R.array.data_bahan_kategori)
        val gambarArray = context.resources.getStringArray(R.array.data_bahan_gambar)

        bahanList.clear()
        Bahan.getResetId()

        val minSize = minOf(namaArray.size, kategoriArray.size, gambarArray.size)

        for (i in 0 until minSize) {
            val nama = namaArray[i]
            val kategori = kategoriArray[i]
            val gambarUrl = gambarArray[i]

            val newBahan = Bahan(Bahan.getNextId(), nama, kategori, gambarUrl)
            bahanList.add(newBahan)
        }

        isInitialized = true
    }

    fun getAllBahan(): List<Bahan> = bahanList.toList()

    fun addBahan(nama: String, kategori: String, gambarUrl: String = "") {
        val newBahan = Bahan(Bahan.getNextId(), nama, kategori, gambarUrl)
        bahanList.add(newBahan)
    }

    fun updateBahan(id: Int, newKategori: String) {
        val bahan = bahanList.find { it.id == id }
        bahan?.kategori = newKategori
    }

    fun toggleKeranjang(id: Int) {
        val bahan = bahanList.find { it.id == id }
        bahan?.diKeranjang = !(bahan?.diKeranjang ?: false)
    }

    fun deleteBahan(id: Int) {
        bahanList.removeAll { it.id == id }
    }

    fun getKategoriList(): List<String> {
        return listOf("Sayuran", "Daging", "Bumbu", "Buah", "Lainnya")
    }
}