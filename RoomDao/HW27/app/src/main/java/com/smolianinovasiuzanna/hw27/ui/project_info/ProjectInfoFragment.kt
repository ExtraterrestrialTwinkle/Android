package com.smolianinovasiuzanna.hw27.ui.project_info

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.smolianinovasiuzanna.hw27.data.entities.project.Project
import com.smolianinovasiuzanna.hw27.databinding.FragmentProjectInfoBinding

import android.view.Gravity
import android.widget.*
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

import com.smolianinovasiuzanna.hw27.R
import com.smolianinovasiuzanna.hw27.data.entities.contractor.Contractor
import com.smolianinovasiuzanna.hw27.data.entities.department.Department
import com.smolianinovasiuzanna.hw27.data.entities.project.ProjectStatus
import com.smolianinovasiuzanna.hw27.data.entities.task.Task
import com.smolianinovasiuzanna.hw27.databinding.ItemTableBinding
import com.smolianinovasiuzanna.hw27.ui.MyAdapter
import com.smolianinovasiuzanna.hw27.ui.entities.contractors.ContractorsFragmentDirections
import kotlin.properties.Delegates


class ProjectInfoFragment : Fragment() {

    private var _binding: FragmentProjectInfoBinding? = null
    private val binding: FragmentProjectInfoBinding get() = _binding!!
    private val viewModel: ProjectInfoViewModel by viewModels()
    private val args: ProjectInfoFragmentArgs by navArgs()
    private var departmentsList = listOf<Department>()
    private lateinit var departmentsAdapter: MyAdapter<Department>
    private lateinit var tasksAdapter: MyAdapter<Task>

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentProjectInfoBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initToolbar()
        loadInfoAboutProject(args.id)
        bindViewModel()
        buttonListener()
        initStatusList()
        initDeparmentsAdapter()
        initTasksAdapter()
    }

    private fun initToolbar(){
        val toolbar = view?.findViewById<androidx.appcompat.widget.Toolbar>(R.id.toolbar)
        toolbar?.setTitle(R.string.project_info)
        toolbar?.setNavigationIcon(R.drawable.ic_back)
        toolbar?.setNavigationOnClickListener {
            findNavController().popBackStack()
        }
    }

    private fun bindViewModel(){
        viewModel.getDepartmentsListToChoose()
        viewModel.error.observe(viewLifecycleOwner){
            Toast.makeText(requireContext(), it.message, Toast.LENGTH_SHORT).show()
        }
        viewModel.departmentsSearch.observe(viewLifecycleOwner){
            departmentsList = it
            initDepartmentsList(departmentsList)
        }
        viewModel.status.observe(viewLifecycleOwner){
            binding.statusExposedDropdownMenu.editText?.setText(it.toString())
        }
        viewModel.departments.observe(viewLifecycleOwner){ departmentsAdapter.setItems(it)}
        viewModel.tasks.observe(viewLifecycleOwner){ tasksAdapter.setItems(it)}
    }

    private fun loadInfoAboutProject(projectId: Int){
        viewModel.loadDepartments(projectId)
        viewModel.loadTasks(projectId)
        viewModel.loadStatus(projectId)
    }

    private fun initDepartmentsList(departments: List<Department>){
        val items = departments.map{it.toString()}
        val adapter = ArrayAdapter(requireContext(), R.layout.list_statuses_item, items)
        (binding.departmentsExposedDropdownMenu.editText as? AutoCompleteTextView)?.setAdapter(adapter)
    }

    private fun initStatusList(){
        val items = ProjectStatus.values().map{it.toString()}
        val adapter = ArrayAdapter(requireContext(), R.layout.list_statuses_item, items)
        (binding.statusExposedDropdownMenu.editText as? AutoCompleteTextView)?.setAdapter(adapter)
    }

    private fun initDeparmentsAdapter(){
        departmentsAdapter = MyAdapter<Department>(
            { layoutInflater, parent, attach ->
                ItemTableBinding.inflate(layoutInflater, parent, attach) },
            { item, vb ->
                (vb as ItemTableBinding).itemTitleTextView.text = item.departmentName},
            {},
            { old, new -> old.id == new.id },
            { old, new -> old == new }
        )
        with(binding.departmentTitles) {
            adapter = departmentsAdapter
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
                    val department = departmentsAdapter.currentList[viewHolder.absoluteAdapterPosition]
                    if (department != null) {
                        viewModel.removeRelation(departmentId = department.id, projectId = args.id )
                    }
                }
            }
        ItemTouchHelper(itemTouchHelperCallback).attachToRecyclerView(binding.departmentTitles)
    }

    private fun initTasksAdapter(){
        tasksAdapter = MyAdapter<Task>(
            { layoutInflater, parent, attach ->
                ItemTableBinding.inflate(layoutInflater, parent, attach) },
            { item, vb ->
                (vb as ItemTableBinding).itemTitleTextView.text = item.title},
            { task ->
                findNavController()
                    .navigate(ProjectInfoFragmentDirections.actionProjectInfoFragmentToTaskInfoFragment(task.id))
            },
            { old, new -> old.id == new.id },
            { old, new -> old == new }
        )
        with(binding.tasksTitles) {
            adapter = tasksAdapter
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
                    val task = tasksAdapter.currentList[viewHolder.absoluteAdapterPosition]
                    if (task != null) {
                        viewModel.deleteTask(task.id, args.id)
                    }
                }
            }
        ItemTouchHelper(itemTouchHelperCallback).attachToRecyclerView(binding.departmentTitles)
    }

    private fun getDepartmentIdToAdd(): Int {
        Log.d("ProjectInfoFragment", "getDepartmentIdToAdd")
        val departmentName = binding.departmentsExposedDropdownMenu.editText?.text.toString()
        val department = departmentsList.find {
            it.departmentName == departmentName
        }
        Log.d("ProjectInfoFragment", "department id = ${department?.id}")
        return department?.id?:0
    }

    private fun buttonListener(){
        binding.addNewTask.setOnClickListener {
            findNavController()
                .navigate(ProjectInfoFragmentDirections.actionProjectInfoFragmentToAddTaskFragment(args.id))
        }
        binding.addDepsButton.setOnClickListener {
            Log.d("ProjectInfoFragment", "OK clicked")
            val departmentId = getDepartmentIdToAdd()
            viewModel.addDepartmentRelationToProject(args.id, departmentId)
        }
        binding.deleteProjectButton.setOnClickListener {
            viewModel.deleteProject(args.id)
            findNavController().popBackStack()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}