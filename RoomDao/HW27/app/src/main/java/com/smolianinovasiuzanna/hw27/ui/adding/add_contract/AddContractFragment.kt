package com.smolianinovasiuzanna.hw27.ui.adding.add_contract

import android.os.Bundle
import android.util.Log
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
import com.smolianinovasiuzanna.hw27.data.entities.contract.Contract
import com.smolianinovasiuzanna.hw27.data.entities.contract.ContractStatus
import com.smolianinovasiuzanna.hw27.databinding.FragmentAddContractBinding
import java.lang.Integer.parseInt
import kotlin.properties.Delegates

class AddContractFragment : Fragment() {

    private var _binding: FragmentAddContractBinding? = null
    private val binding: FragmentAddContractBinding get() = _binding!!
    private val viewModel: AddContractViewModel by viewModels()
    private val args: AddContractFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentAddContractBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.init(args.id)
        bindViewModel()
        initDropdownMenu()
        binding.addButton.setOnClickListener {
            saveContract()
        }
    }

    private fun bindViewModel(){
        viewModel.existingContract.observe(viewLifecycleOwner){
            setExistingContractInfo(it)
        }
        viewModel.saveSuccess.observe(viewLifecycleOwner){
            findNavController().popBackStack()
        }
        viewModel.error.observe(viewLifecycleOwner){
            Toast.makeText(requireContext(), it.message, Toast.LENGTH_SHORT).show()
            Log.e("Error", it.message.toString())
        }
    }

    private fun initDropdownMenu(){
        val items = ContractStatus.values().map{it.toString()}
        val adapter = ArrayAdapter(requireContext(), R.layout.list_statuses_item, items)
        (binding.statusExposedDropdownMenu.editText as? AutoCompleteTextView)?.setAdapter(adapter)
    }

    private fun saveContract(){
        with(binding){
            val duration = durationTextField.editText?.text.toString()
            var durationInt by Delegates.notNull<Int>()
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
            if(
                titleTextField.editText?.text.isNullOrBlank() || durationTextField.editText?.text.isNullOrBlank() ||
                priceTextField.editText?.text.isNullOrBlank() || statusExposedDropdownMenu.editText?.text.isNullOrBlank()
            ) {
                Toast.makeText(requireContext(), getString(R.string.fiil_in_all_fields), Toast.LENGTH_SHORT).show()
            } else {
                viewModel.save(
                    id = 0,
                    title = titleTextField.editText?.text.toString(),
                    durationInt,
                    price = priceTextField.editText?.text.toString().toFloat(),
                    status = ContractStatus.valueOf(statusExposedDropdownMenu.editText?.text.toString()),
                    contractorId = args.contractorId
                )
            }
        }
    }
    private fun setExistingContractInfo(contract: Contract){
        val position = contract.status.ordinal
        with(binding){
            titleTextField.editText?.setText(contract.title)
            durationTextField.editText?.setText(contract.duration.toString())
            priceTextField.editText?.setText(contract.price.toString())
            binding.autoComplete.setText(ContractStatus.values()[position].toString())
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}