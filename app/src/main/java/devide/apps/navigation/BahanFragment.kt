package devide.apps.navigation

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
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

        setupRecyclerView()
        setupSpinner()
        setupAddButton()
        refreshData()
    }

    private fun setupRecyclerView() {
        adapter = BahanAdapter(
            bahanList = emptyList(),
            onDeleteClick = { bahan ->
                BahanRepository.deleteBahan(bahan.id)
                refreshData()
            },
            onCategoryChange = { bahan, newKategori ->
                BahanRepository.updateBahan(bahan.id, newKategori)
                refreshData()
            }
        )

        binding.recyclerViewBahan.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = this@BahanFragment.adapter
        }
    }

    private fun setupSpinner() {
        val kategoriList = BahanRepository.getKategoriList()
        val spinnerAdapter = ArrayAdapter(
            requireContext(),
            android.R.layout.simple_spinner_item,
            kategoriList
        )
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.spinnerKategori.adapter = spinnerAdapter
    }

    private fun setupAddButton() {
        binding.btnTambahBahan.setOnClickListener {
            val namaBahan = binding.etNamaBahan.text.toString().trim()
            val kategori = binding.spinnerKategori.selectedItem.toString()

            if (namaBahan.isEmpty()) {
                binding.etNamaBahan.error = "Nama bahan tidak boleh kosong"
                return@setOnClickListener
            }

            BahanRepository.addBahan(namaBahan, kategori)
            refreshData()
            clearInput()
        }
    }

    private fun refreshData() {
        val allBahan = BahanRepository.getAllBahan()
        adapter.updateData(allBahan)

        // Tampilkan pesan jika tidak ada data
        if (allBahan.isEmpty()) {
            binding.textEmpty.visibility = View.VISIBLE
            binding.recyclerViewBahan.visibility = View.GONE
        } else {
            binding.textEmpty.visibility = View.GONE
            binding.recyclerViewBahan.visibility = View.VISIBLE
        }
    }

    private fun clearInput() {
        binding.etNamaBahan.text?.clear()
        binding.spinnerKategori.setSelection(0)
        binding.etNamaBahan.clearFocus()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}