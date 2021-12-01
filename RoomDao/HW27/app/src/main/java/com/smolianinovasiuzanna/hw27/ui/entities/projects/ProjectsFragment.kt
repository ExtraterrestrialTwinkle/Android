package com.smolianinovasiuzanna.hw27.ui.entities.projects

import androidx.lifecycle.ViewModelProvider
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
import com.smolianinovasiuzanna.hw27.data.entities.employee.Employee
import com.smolianinovasiuzanna.hw27.data.entities.project.Project
import com.smolianinovasiuzanna.hw27.databinding.FragmentListBinding
import com.smolianinovasiuzanna.hw27.databinding.ItemTableBinding
import com.smolianinovasiuzanna.hw27.ui.MyAdapter
import com.smolianinovasiuzanna.hw27.ui.entities.employees.EmployeesFragmentDirections
import com.smolianinovasiuzanna.hw27.ui.entities.employees.EmployeesViewModel

class ProjectsFragment : Fragment() {

    private var _binding: FragmentListBinding? = null
    private val binding: FragmentListBinding get() = _binding!!
    private lateinit var myAdapter: MyAdapter<Project>
    private val viewModel: ProjectsViewModel by viewModels()

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
        viewModel.loadList() //args.id
        initToolbar()
        initList()
        bindViewModel()
    }

    private fun initToolbar(){
        val toolbar = view?.findViewById<androidx.appcompat.widget.Toolbar>(R.id.toolbar)
        toolbar?.setTitle(R.string.projects)
        toolbar?.setNavigationIcon(R.drawable.ic_back)
        toolbar?.setNavigationOnClickListener {
            findNavController().popBackStack()
        }
    }

    private fun initList(){
        myAdapter = MyAdapter<Project>(
            { layoutInflater, parent, attach ->
                ItemTableBinding.inflate(layoutInflater, parent, attach) },
            { item, vb ->
                (vb as ItemTableBinding).itemTitleTextView.text = item.title
            },
            {findNavController()
                .navigate(ProjectsFragmentDirections.actionProjectsFragmentToProjectInfoFragment(it.id))},
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
                    Log.d("ProjectsFragment", "onSwiped")
                    val project = myAdapter.currentList[viewHolder.absoluteAdapterPosition]
                    viewModel.deleteProject(project)
                }
            }
        ItemTouchHelper(itemTouchHelperCallback).attachToRecyclerView(binding.itemRecyclerView)
    }

    private fun bindViewModel(){
        binding.addFAB.setOnClickListener {
            findNavController()
                .navigate(ProjectsFragmentDirections.actionProjectsFragmentToAddProjectFragment())
        }
        viewModel.projects.observe(viewLifecycleOwner){
            myAdapter.setItems(it)
        }
        viewModel.error.observe(viewLifecycleOwner){
            Toast.makeText(requireContext(), R.string.add_error_incorrect_format, Toast.LENGTH_SHORT).show()
            Log.e("Error",  it.message.toString())
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }


}