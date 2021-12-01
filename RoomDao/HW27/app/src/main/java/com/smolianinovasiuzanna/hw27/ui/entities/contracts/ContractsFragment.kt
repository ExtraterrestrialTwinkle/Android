package com.smolianinovasiuzanna.hw27.ui.entities.contracts

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.smolianinovasiuzanna.hw27.R
import com.smolianinovasiuzanna.hw27.data.entities.contract.Contract
import com.smolianinovasiuzanna.hw27.databinding.FragmentListBinding
import com.smolianinovasiuzanna.hw27.databinding.ItemTableBinding
import com.smolianinovasiuzanna.hw27.ui.MyAdapter

class ContractsFragment : Fragment() {

    private var _binding: FragmentListBinding? = null
    private val binding: FragmentListBinding get() = _binding!!
    private val viewModel: ContractsViewModel by viewModels()
    private lateinit var myAdapter: MyAdapter<Contract>
    private val args: ContractsFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentListBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initToolbar()
        initList()
        bindViewModel()
        viewModel.loadList(args.id)
        binding.addFAB.setOnClickListener {
            findNavController().navigate(
                ContractsFragmentDirections.actionContractsFragmentToAddContractFragment(0, args.id)
            )
        }
    }

    private fun initToolbar(){
        val toolbar = view?.findViewById<androidx.appcompat.widget.Toolbar>(R.id.toolbar)
        toolbar?.setTitle(R.string.contracts)
        toolbar?.setNavigationIcon(R.drawable.ic_back)
        toolbar?.setNavigationOnClickListener {
            findNavController().popBackStack()
        }
    }
    private fun initList(){
        myAdapter = MyAdapter<Contract>(
            { layoutInflater, parent, attach ->
                ItemTableBinding.inflate(layoutInflater, parent, attach) },
            { item, vb ->
                (vb as ItemTableBinding).itemTitleTextView.text = item.title },
            { contract ->
                findNavController()
                .navigate(ContractsFragmentDirections.actionContractsFragmentToAddContractFragment(contract.id, contract.contractorId))},
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
                    Log.d("ContractsFragment", "onSwiped")
                    val contract = myAdapter.currentList[viewHolder.absoluteAdapterPosition]
                    if (contract != null) {
                        viewModel.deleteContract(contract)
                    }
                }
            }
        ItemTouchHelper(itemTouchHelperCallback).attachToRecyclerView(binding.itemRecyclerView)
    }

    private fun bindViewModel(){
        viewModel.contractsList.observe(viewLifecycleOwner){
            myAdapter.setItems(it)
        }
        viewModel.error.observe(viewLifecycleOwner){
            Toast.makeText(requireContext(), it.message, Toast.LENGTH_SHORT).show()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}