package devide.apps.navigation

object BahanRepository {
    private val bahanList = mutableListOf<Bahan>()

    fun getAllBahan(): List<Bahan> {
        return bahanList.toList()
    }

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