package com.smolianinovasiuzanna.hw27.ui.task_info

import android.os.Bundle
import android.util.Log
import android.view.Gravity
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.core.view.isGone
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.smolianinovasiuzanna.hw27.R
import com.smolianinovasiuzanna.hw27.data.entities.employee.Employee
import com.smolianinovasiuzanna.hw27.data.entities.task.Task
import com.smolianinovasiuzanna.hw27.databinding.FragmentTaskInfoBinding
import com.smolianinovasiuzanna.hw27.databinding.ItemTableBinding
import com.smolianinovasiuzanna.hw27.ui.MyAdapter
import com.smolianinovasiuzanna.hw27.ui.entities.employees.EmployeesFragmentDirections
import com.smolianinovasiuzanna.hw27.ui.project_info.ProjectInfoFragmentDirections

class TaskInfoFragment : Fragment() {

    private var _binding: FragmentTaskInfoBinding? = null
    private val binding: FragmentTaskInfoBinding get() = _binding!!
    private val viewModel: TaskInfoViewModel by viewModels()
    private lateinit var myAdapter: MyAdapter<Employee>
    private lateinit var listAdapter: MyAdapter<Employee>
    private val args: TaskInfoFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentTaskInfoBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initToolbar()
        initEmployeesList()
        bindViewModel()
        binding.okButton.setOnClickListener {
            findNavController().popBackStack()
        }
        binding.employeeSearchView.setOnSearchClickListener {
            Log.d("Search", "SetOmSearchClickListener")
            viewModel.getAllEmployees()
            searchEmployeesByLastName()
        }
        binding.employeeSearchView.setOnCloseListener {
            viewModel.getAddedEmployeeListByTaskId(args.taskId)
            true
        }
    }

    private fun initToolbar(){
        val toolbar = view?.findViewById<androidx.appcompat.widget.Toolbar>(R.id.toolbar)
        toolbar?.setTitle(R.string.employees_with_task)
        toolbar?.setNavigationIcon(R.drawable.ic_back)
        toolbar?.setNavigationOnClickListener {
            findNavController().popBackStack()
        }
    }

    private fun initEmployeesList(){
        listAdapter = MyAdapter<Employee>(
            { layoutInflater, parent, attach ->
                ItemTableBinding.inflate(layoutInflater, parent, attach) },
            { item, vb ->
                (vb as ItemTableBinding).itemTitleTextView.text = item.lastName + " " + item.firstName},
            { employee ->
                saveTaskWithEmployees(args.taskId, employee)
            },
            { old, new -> old.id == new.id },
            { old, new -> old == new }
        )
        with(binding.employeesContainer) {
            adapter = listAdapter
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
                    Log.d("TaskInfoFragment", "onSwiped")
                    val employee = listAdapter.currentList[viewHolder.absoluteAdapterPosition]
                    if (employee != null) {
                        viewModel.removeRelation(args.taskId, employee.id)
                    }
                }
            }
        ItemTouchHelper(itemTouchHelperCallback).attachToRecyclerView(binding.employeesContainer)
    }

    private fun bindViewModel(){
        viewModel.getAddedEmployeeListByTaskId(args.taskId)
        viewModel.addedEmployees.observe(viewLifecycleOwner) { listAdapter.setItems(it)}
         viewModel.error.observe(viewLifecycleOwner) {
            Toast.makeText(requireContext(), it.message, Toast.LENGTH_SHORT).show()
        }
    }
    private fun searchEmployeesByLastName(){
        Log.d("Search", "SearchEmployeeByLastName")
        binding.employeeSearchView.setOnQueryTextListener(object :
                SearchView.OnQueryTextListener,
                androidx.appcompat.widget.SearchView.OnQueryTextListener {
                override fun onQueryTextSubmit(query: String?): Boolean {
                    return true
                }
                override fun onQueryTextChange(newText: String?): Boolean {
                    Log.d("Search", "text = $newText")
                    viewModel.getEmployeesSearchListByLastName(newText)
                    return true
                }
                })
    }
    private fun saveTaskWithEmployees(taskId: Int, employee: Employee){
        viewModel.saveTaskWithEmployees(taskId,employee)
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}