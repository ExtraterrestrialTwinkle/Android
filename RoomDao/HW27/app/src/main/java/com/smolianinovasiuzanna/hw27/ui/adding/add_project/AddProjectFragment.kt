package com.smolianinovasiuzanna.hw27.ui.adding.add_project

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.smolianinovasiuzanna.hw27.R
import com.smolianinovasiuzanna.hw27.data.entities.contract.Contract
import com.smolianinovasiuzanna.hw27.data.entities.contract.ContractStatus
import com.smolianinovasiuzanna.hw27.data.entities.department.Department
import com.smolianinovasiuzanna.hw27.data.entities.project.ProjectStatus
import com.smolianinovasiuzanna.hw27.databinding.FragmentAddBinding
import com.smolianinovasiuzanna.hw27.databinding.FragmentAddProjectBinding
import com.smolianinovasiuzanna.hw27.ui.adding.add_department.AddDepartmentViewModel
import java.lang.Integer.parseInt
import kotlin.properties.Delegates

class AddProjectFragment : Fragment() {

    private var _binding: FragmentAddProjectBinding? = null
    private val binding: FragmentAddProjectBinding get() = _binding!!
    private val viewModel: AddProjectViewModel by viewModels()
    private var contractList = listOf<Contract>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentAddProjectBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initToolbar()
        bindViewModel()
        initStatuses()
        binding.addButton.setOnClickListener {
            saveProject()
        }
    }

    private fun initToolbar() {
        val toolbar = view?.findViewById<androidx.appcompat.widget.Toolbar>(R.id.toolbar)
        toolbar?.setTitle(R.string.add_project)
        toolbar?.setNavigationIcon(R.drawable.ic_back)
        toolbar?.setNavigationOnClickListener {
            findNavController().popBackStack()
        }
    }

    private fun initContracts(contracts: List<Contract>){
        val adapter = ArrayAdapter(requireContext(), R.layout.list_statuses_item, contracts)
        (binding.contractDropdownMenu.editText as? AutoCompleteTextView)?.setAdapter(adapter)
    }

    private fun initStatuses(){
        val statuses = ProjectStatus.values().map{it.toString()}
        val adapter = ArrayAdapter(requireContext(), R.layout.list_statuses_item, statuses)
        (binding.statusTextField.editText as? AutoCompleteTextView)?.setAdapter(adapter)
    }

    private fun bindViewModel() {
        viewModel.getContracts()
        viewModel.error.observe(viewLifecycleOwner) { error ->
            Toast.makeText(requireContext(), error.message, Toast.LENGTH_SHORT).show()
        }
        viewModel.contracts.observe(viewLifecycleOwner){
            contractList = it
            initContracts(contractList)
        }
    }

    private fun getContractId(): Int {
        val contractTitle= binding.contractDropdownMenu.editText?.text.toString()
        val contract = contractList.find {
            it.title == contractTitle
        }
        return contract?.id ?: 0
    }

    private fun saveProject(){
        val title = binding.titleTextField.editText?.text.toString()
        val duration = binding.durationTextField.editText?.text.toString()
        var durationInt: Int by Delegates.notNull()

        try{
            durationInt = parseInt(duration)
        }catch (e: Exception){
            Toast.makeText(
                requireContext(),
                getString(R.string.incorrect_duration_format),
                Toast.LENGTH_SHORT
            ).show()
            binding.durationTextField.editText?.text?.clear()
        }

        if(title.isBlank() || duration.isBlank() ||
            binding.statusTextField.editText?.text.isNullOrBlank() ||
                binding.contractDropdownMenu.editText?.text.isNullOrBlank()
        ){
            Toast.makeText(
                requireContext(),
                getString(R.string.fiil_in_all_fields),
                Toast.LENGTH_SHORT
            ).show()
        } else {
            viewModel.save(
                title,
                durationInt,
                ProjectStatus.valueOf(binding.statusTextField.editText?.text.toString()),
                contractId = getContractId()
            )
            findNavController().popBackStack()
        }
    }
}