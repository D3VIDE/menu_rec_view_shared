package devide.apps.navigation

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
import devide.apps.navigation.databinding.ItemBahanBinding

class BahanAdapter(
    private var bahanList: List<Bahan>,
    private val onDeleteClick: (Bahan) -> Unit,
    private val onCategoryChange: (Bahan, String) -> Unit,
    private val onKeranjangClick: (Bahan) -> Unit
) : RecyclerView.Adapter<BahanAdapter.BahanViewHolder>() {

    inner class BahanViewHolder(private val binding: ItemBahanBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(bahan: Bahan) {
            binding.tvNamaBahan.text = bahan.nama
            binding.tvKategori.text = bahan.kategori

            // Load gambar dengan Picasso
            if (bahan.gambarUrl.isNotEmpty()) {
                Picasso.get()
                    .load(bahan.gambarUrl)
                    .placeholder(R.drawable.ic_placeholder)
                    .error(R.drawable.ic_placeholder)
                    .resize(100, 100) // Optional: resize gambar
                    .centerCrop()     // Optional: crop ke tengah
                    .into(binding.imgBahan)

                // Tampilkan URL singkat
                binding.tvGambarUrl.text = "Gambar: ${getShortUrl(bahan.gambarUrl)}"
                binding.tvGambarUrl.visibility = View.VISIBLE
            } else {
                binding.imgBahan.setImageResource(R.drawable.ic_placeholder)
                binding.tvGambarUrl.visibility = View.GONE
            }

            // Set icon keranjang berdasarkan status
            val keranjangIcon = if (bahan.diKeranjang) {
                R.drawable.ic_cart_outline
            } else {
                R.drawable.ic_cart_outline
            }
            binding.btnKeranjang.setImageResource(keranjangIcon)

            // Setup spinner untuk mengubah kategori
            setupCategorySpinner(bahan)

            // Setup delete button
            binding.btnDelete.setOnClickListener {
                onDeleteClick(bahan)
            }

            // Setup keranjang button
            binding.btnKeranjang.setOnClickListener {
                onKeranjangClick(bahan)
            }
        }

        private fun setupCategorySpinner(bahan: Bahan) {
            val kategoriList = BahanRepository.getKategoriList()
            val spinnerAdapter = ArrayAdapter(
                binding.root.context,
                android.R.layout.simple_spinner_item,
                kategoriList
            )
            spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            binding.spinnerEditKategori.adapter = spinnerAdapter

            // Set selected item berdasarkan kategori saat ini
            val currentIndex = kategoriList.indexOf(bahan.kategori)
            if (currentIndex != -1) {
                binding.spinnerEditKategori.setSelection(currentIndex)
            }

            // Handle perubahan kategori
            binding.spinnerEditKategori.onItemSelectedListener = object : android.widget.AdapterView.OnItemSelectedListener {
                override fun onItemSelected(parent: android.widget.AdapterView<*>?, view: android.view.View?, position: Int, id: Long) {
                    val newKategori = kategoriList[position]
                    if (newKategori != bahan.kategori) {
                        onCategoryChange(bahan, newKategori)
                    }
                }

                override fun onNothingSelected(parent: android.widget.AdapterView<*>?) {}
            }
        }

        private fun getShortUrl(url: String): String {
            return if (url.length > 30) {
                "${url.substring(0, 27)}..."
            } else {
                url
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BahanViewHolder {
        val binding = ItemBahanBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return BahanViewHolder(binding)
    }

    override fun onBindViewHolder(holder: BahanViewHolder, position: Int) {
        holder.bind(bahanList[position])
    }

    override fun getItemCount(): Int = bahanList.size

    fun updateData(newBahanList: List<Bahan>) {
        bahanList = newBahanList
        notifyDataSetChanged()
    }
}