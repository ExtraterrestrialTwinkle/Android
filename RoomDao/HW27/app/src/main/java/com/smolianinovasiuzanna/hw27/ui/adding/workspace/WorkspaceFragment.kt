package com.smolianinovasiuzanna.hw27.ui.adding.workspace

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.smolianinovasiuzanna.hw27.R
import com.smolianinovasiuzanna.hw27.databinding.FragmentWorkspaceBinding
import java.math.BigInteger
import java.security.MessageDigest

class WorkspaceFragment: Fragment() {

    private var _binding: FragmentWorkspaceBinding? = null
    private val binding: FragmentWorkspaceBinding get() = _binding!!
    private val viewModel: WorkspaceViewModel by viewModels()
    private val args: WorkspaceFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentWorkspaceBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        bindViewModel()
        initToolbar()
        initEquipment()
        binding.okButton.setOnClickListener {
           saveWorkspace()
        }
    }

    private fun bindViewModel(){
        viewModel.error.observe(viewLifecycleOwner) {
            Toast.makeText(requireContext(), it.message, Toast.LENGTH_SHORT).show()
        }
        viewModel.saveSuccess.observe(viewLifecycleOwner){findNavController().popBackStack()}
    }

    private fun initToolbar(){
        val toolbar = view?.findViewById<androidx.appcompat.widget.Toolbar>(R.id.toolbar)
        toolbar?.setTitle(R.string.workspace)
        toolbar?.setNavigationIcon(R.drawable.ic_back)
        toolbar?.setNavigationOnClickListener {
            findNavController().popBackStack()
        }
    }

    private fun initEquipment(){
        val items = arrayOf("Да", "Нет")
        val adapter = ArrayAdapter(requireContext(), R.layout.list_statuses_item, items)
        (binding.equipmentExposedDropdownMenu.editText as? AutoCompleteTextView)?.setAdapter(adapter)
    }

    private fun md5Hash(password: String): String {
        val md = MessageDigest.getInstance("MD5")
        val bigInt = BigInteger(1, md.digest(password.toByteArray(Charsets.UTF_8)))
        return String.format("%032x", bigInt)
    }

    private fun saveWorkspace(){

        if(binding.equipmentExposedDropdownMenu.editText?.text.isNullOrBlank()){
            Toast.makeText(
                requireContext(),
                getString(R.string.fiil_in_all_fields),
                Toast.LENGTH_SHORT
            ).show()
        } else {
            val equipment = when(binding.equipmentExposedDropdownMenu.editText?.text.toString()){
                "Да" -> true
                else -> false
            }
            viewModel.saveWorkspace(
                id = args.employeeId,
                equipment = equipment,
                email = binding.emailTextField.editText?.text.toString(),
                passwordHash = md5Hash(binding.passwordTextField.editText?.text.toString())
            )
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}