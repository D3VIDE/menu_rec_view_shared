package devide.apps.navigation

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
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
                    .resize(120, 120)
                    .centerCrop()
                    .into(binding.imgBahan)
            } else {
                binding.imgBahan.setImageResource(R.drawable.ic_placeholder)
            }

            // Setup keranjang icon
            val keranjangIcon = if (bahan.diKeranjang) {
                R.drawable.ic_cart_outline
            } else {
                R.drawable.ic_cart_outline
            }
            binding.btnKeranjang.setImageResource(keranjangIcon)

            setupCategorySpinner(bahan)

            // Click listeners
            binding.btnDelete.setOnClickListener { onDeleteClick(bahan) }
            binding.btnKeranjang.setOnClickListener { onKeranjangClick(bahan) }

            // Optional: Click pada item untuk detail
            binding.root.setOnClickListener {
                // Intent ke detail activity/fragment
            }
        }

        private fun setupCategorySpinner(bahan: Bahan) {
            val kategoriList = listOf("Sayuran", "Daging", "Bumbu", "Buah", "Lainnya")
            val adapter = ArrayAdapter(
                binding.root.context,
                android.R.layout.simple_spinner_item,
                kategoriList
            )
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            binding.spinnerEditKategori.adapter = adapter

            val currentIndex = kategoriList.indexOf(bahan.kategori)
            if (currentIndex != -1) {
                binding.spinnerEditKategori.setSelection(currentIndex)
            }

            binding.spinnerEditKategori.onItemSelectedListener =
                object : AdapterView.OnItemSelectedListener {
                    override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                        val newKategori = kategoriList[position]
                        if (newKategori != bahan.kategori) {
                            onCategoryChange(bahan, newKategori)
                        }
                    }

                    override fun onNothingSelected(parent: AdapterView<*>?) {}
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