package devide.apps.navigation

data class Bahan(
    val id: Int,
    var nama: String,
    var kategori: String
) {
    companion object {
        private var idCounter = 0
        fun getNextId(): Int {
            return idCounter++
        }
    }
}