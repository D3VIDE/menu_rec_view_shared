package devide.apps.navigation

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.recyclerview.widget.RecyclerView
import devide.apps.navigation.databinding.ItemBahanBinding

class BahanAdapter(
    private var bahanList: List<Bahan>,
    private val onDeleteClick: (Bahan) -> Unit,
    private val onCategoryChange: (Bahan, String) -> Unit
) : RecyclerView.Adapter<BahanAdapter.BahanViewHolder>() {

    inner class BahanViewHolder(private val binding: ItemBahanBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(bahan: Bahan) {
            binding.tvNamaBahan.text = bahan.nama
            binding.tvKategori.text = bahan.kategori

            // Setup spinner untuk mengubah kategori
            setupCategorySpinner(bahan)

            // Setup delete button
            binding.btnDelete.setOnClickListener {
                onDeleteClick(bahan)
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