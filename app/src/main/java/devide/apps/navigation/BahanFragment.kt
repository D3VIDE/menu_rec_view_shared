package devide.apps.navigation

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Spinner
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import devide.apps.navigation.databinding.FragmentBahanBinding

class BahanFragment : Fragment() {

    private var _binding: FragmentBahanBinding? = null
    private val binding get() = _binding!!
    private lateinit var adapter: BahanAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentBahanBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupUI()
        setupRecyclerView()
        loadInitialData()
    }

    private fun setupUI() {
        // Setup kategori spinner untuk input baru
        val kategoriAdapter = ArrayAdapter(
            requireContext(),
            android.R.layout.simple_spinner_item,
            listOf("Sayuran", "Daging", "Bumbu", "Buah", "Lainnya")
        )
        kategoriAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.spinnerKategori.adapter = kategoriAdapter

        // Setup add button
        binding.btnTambahBahan.setOnClickListener {
            addNewBahan()
        }
    }

    private fun setupRecyclerView() {
        adapter = BahanAdapter(
            bahanList = emptyList(),
            onDeleteClick = { bahan ->
                showDeleteConfirmation(bahan)
            },
            onCategoryChange = { bahan, newKategori ->
                BahanRepository.updateBahan(bahan.id, newKategori)
                refreshData()
            },
            onKeranjangClick = { bahan ->
                BahanRepository.toggleKeranjang(bahan.id)
                refreshData()
            }
        )

        binding.recyclerViewBahan.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = this@BahanFragment.adapter
            addItemDecoration(DividerItemDecoration(requireContext(), LinearLayoutManager.VERTICAL))
        }
    }

    private fun loadInitialData() {
        BahanRepository.initializeData(requireContext())
        refreshData()
    }

    private fun addNewBahan() {
        val nama = binding.etNamaBahan.text.toString().trim()
        val gambarUrl = binding.etGambarUrl.text.toString().trim()
        val kategori = binding.spinnerKategori.selectedItem.toString()

        if (nama.isEmpty()) {
            binding.etNamaBahan.error = "Nama bahan harus diisi"
            return
        }

        BahanRepository.addBahan(nama, kategori, gambarUrl)
        refreshData()
        clearInputFields()

        // Scroll ke item baru
        binding.recyclerViewBahan.smoothScrollToPosition(adapter.itemCount - 1)
    }

    private fun showDeleteConfirmation(bahan: Bahan) {
        AlertDialog.Builder(requireContext())
            .setTitle("Hapus Bahan")
            .setMessage("Hapus ${bahan.nama} dari daftar?")
            .setPositiveButton("Hapus") { _, _ ->
                BahanRepository.deleteBahan(bahan.id)
                refreshData()
            }
            .setNegativeButton("Batal", null)
            .show()
    }

    private fun refreshData() {
        val allBahan = BahanRepository.getAllBahan()
        adapter.updateData(allBahan)

        // Toggle empty state
        if (allBahan.isEmpty()) {
            binding.textEmpty.visibility = View.VISIBLE
            binding.recyclerViewBahan.visibility = View.GONE
        } else {
            binding.textEmpty.visibility = View.GONE
            binding.recyclerViewBahan.visibility = View.VISIBLE
        }
    }

    private fun clearInputFields() {
        binding.etNamaBahan.text?.clear()
        binding.etGambarUrl.text?.clear()
        binding.spinnerKategori.setSelection(0)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}