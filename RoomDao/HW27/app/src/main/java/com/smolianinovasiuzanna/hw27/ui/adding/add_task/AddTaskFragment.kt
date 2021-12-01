package com.smolianinovasiuzanna.hw27.ui.adding.add_task

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.smolianinovasiuzanna.hw27.R
import com.smolianinovasiuzanna.hw27.data.entities.task.TaskStatus
import com.smolianinovasiuzanna.hw27.databinding.FragmentAddTaskBinding
import kotlin.properties.Delegates

class AddTaskFragment : Fragment() {

    private var _binding: FragmentAddTaskBinding? = null
    private val binding: FragmentAddTaskBinding get() = _binding!!
    private val viewModel: AddTaskViewModel by viewModels()
    private val args: AddTaskFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentAddTaskBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        bindViewModel()
        initStatusList()
        binding.addTaskButton.setOnClickListener {
           saveTask()
        }
    }

    private fun bindViewModel(){
        viewModel.error.observe(viewLifecycleOwner) {
            Toast.makeText(requireContext(), it.message, Toast.LENGTH_SHORT).show()
        }
        viewModel.saveSuccess.observe(viewLifecycleOwner) {
            findNavController().popBackStack()
        }
    }

    private fun initStatusList(){
        val items = TaskStatus.values().map{it.toString()}
        val adapter = ArrayAdapter(requireContext(), R.layout.list_statuses_item, items)
        (binding.statusExposedDropdownMenu.editText as? AutoCompleteTextView)?.setAdapter(adapter)
    }

    private fun saveTask(){
        val title = binding.titleTextField.editText?.text.toString()
        val description = binding.descriptionTextField.editText?.text.toString()
        val duration = binding.durationTextField.editText?.text.toString()
        var durationInt: Int by Delegates.notNull()
        try{
            durationInt = Integer.parseInt(duration)
        }catch (e: Exception){
            Toast.makeText(
                requireContext(),
                getString(R.string.incorrect_duration_format),
                Toast.LENGTH_SHORT
            ).show()
            binding.durationTextField.editText?.text?.clear()
        }

        if(title.isBlank() || duration.isBlank() || description.isBlank() ||
            binding.statusExposedDropdownMenu.editText?.text.isNullOrBlank()
        ){
            Toast.makeText(
                requireContext(),
                getString(R.string.fiil_in_all_fields),
                Toast.LENGTH_SHORT
            ).show()
        } else {
            viewModel.saveTask(
                title,
                description,
                durationInt,
                TaskStatus.valueOf(binding.statusExposedDropdownMenu.editText?.text.toString()),
                projectId = args.projectId
            )
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}