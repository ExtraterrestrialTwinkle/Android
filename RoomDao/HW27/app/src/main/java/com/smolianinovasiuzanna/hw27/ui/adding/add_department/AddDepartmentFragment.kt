package com.smolianinovasiuzanna.hw27.ui.adding.add_department

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.smolianinovasiuzanna.hw27.R
import com.smolianinovasiuzanna.hw27.databinding.FragmentAddBinding

class AddDepartmentFragment : Fragment() {

    private var _binding: FragmentAddBinding? = null
    private val binding: FragmentAddBinding get() = _binding!!
    private val viewModel: AddDepartmentViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentAddBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initToolbar()
        bindViewModel()
    }

    private fun initToolbar() {
        val toolbar = view?.findViewById<androidx.appcompat.widget.Toolbar>(R.id.toolbar)
        toolbar?.setTitle(R.string.add_department)
        toolbar?.setNavigationIcon(R.drawable.ic_back)
        toolbar?.setNavigationOnClickListener {
            findNavController().popBackStack()
        }
    }

    private fun bindViewModel() {
        binding.addButton.setOnClickListener {
            if (binding.contractorsNameTextField.editText?.text.isNullOrBlank()) {
                Toast.makeText(
                    requireContext(),
                    getString(R.string.fiil_in_all_fields),
                    Toast.LENGTH_SHORT
                ).show()
            } else {
                viewModel.save(binding.contractorsNameTextField.editText?.text.toString())
                findNavController().popBackStack()
            }
        }
        viewModel.error.observe(viewLifecycleOwner) { error ->
            Toast.makeText(requireContext(), error.message, Toast.LENGTH_SHORT).show()
        }
    }

}