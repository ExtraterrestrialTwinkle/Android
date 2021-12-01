package com.smolianinovasiuzanna.hw27.ui.entities.contractors

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.smolianinovasiuzanna.hw27.R
import com.smolianinovasiuzanna.hw27.data.entities.contractor.Contractor
import com.smolianinovasiuzanna.hw27.databinding.FragmentListBinding
import com.smolianinovasiuzanna.hw27.databinding.ItemTableBinding
import com.smolianinovasiuzanna.hw27.ui.MyAdapter

class ContractorsFragment : Fragment() {

    private var _binding: FragmentListBinding? = null
    private val binding: FragmentListBinding get() = _binding!!
    private var myAdapter: MyAdapter<Contractor>? = null
    private val viewModel: ContractorsViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentListBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        Log.d("ContractorFragment", "onViewCreated")
        super.onViewCreated(view, savedInstanceState)
        initToolbar()
        initList()
        bindViewModel()
        viewModel.loadList()
    }

    private fun initToolbar() {
        val toolbar = view?.findViewById<androidx.appcompat.widget.Toolbar>(R.id.toolbar)
        toolbar?.setTitle(R.string.contractors)
        toolbar?.setNavigationIcon(R.drawable.ic_back)
        toolbar?.setNavigationOnClickListener {
            findNavController().popBackStack()
        }
    }

    private fun initList(){
        myAdapter = MyAdapter<Contractor>(
            { layoutInflater, parent, attach ->
                ItemTableBinding.inflate(layoutInflater, parent, attach) },
            { item, vb ->
                (vb as ItemTableBinding).itemTitleTextView.text = item.name },
            { contractor ->
                findNavController()
                    .navigate(ContractorsFragmentDirections.actionContarctorsFragmentToContractsFragment(contractor.id))},
            { old, new -> old.id == new.id },
            { old, new -> old == new }
        )
        with(binding.itemRecyclerView) {
            adapter = myAdapter
            layoutManager = LinearLayoutManager(requireContext())
            setHasFixedSize(true)
            addItemDecoration(
                DividerItemDecoration(
                    requireContext(),
                    DividerItemDecoration.VERTICAL
                )
            )
        }
            val itemTouchHelperCallback =
                object : ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT) {
                    override fun onMove(
                        recyclerView: RecyclerView,
                        viewHolder: RecyclerView.ViewHolder,
                        target: RecyclerView.ViewHolder
                    ) = false

                    override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                        Log.d("ContractorsFragment", "onSwiped")
                        val contractor = myAdapter?.currentList?.get(viewHolder.absoluteAdapterPosition)
                        if (contractor != null) {
                            viewModel.deleteContractor(contractor)
                        }
                    }
                }
            ItemTouchHelper(itemTouchHelperCallback).attachToRecyclerView(binding.itemRecyclerView)
        }


    private fun bindViewModel(){
        binding.addFAB.setOnClickListener{
            findNavController().navigate(ContractorsFragmentDirections.actionContarctorsFragmentToAddFragment())
        }
        viewModel.contractors.observe(viewLifecycleOwner){ myAdapter?.setItems(it) }
        viewModel.error.observe(viewLifecycleOwner){error ->
            Toast.makeText(requireContext(), error.message, Toast.LENGTH_SHORT).show()
            Log.e("Error", "${error.message}")
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
        myAdapter = null
    }

}