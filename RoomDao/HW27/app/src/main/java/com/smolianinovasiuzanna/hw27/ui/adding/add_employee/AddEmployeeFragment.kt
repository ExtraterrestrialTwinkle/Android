package com.smolianinovasiuzanna.hw27.ui.adding.add_employee

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
import com.smolianinovasiuzanna.hw27.data.entities.department.Department
import com.smolianinovasiuzanna.hw27.data.entities.employee.Employee
import com.smolianinovasiuzanna.hw27.databinding.FragmentAddEmployeeBinding
import java.lang.Integer.parseInt
import kotlin.properties.Delegates

class AddEmployeeFragment : Fragment() {

   private var _binding: FragmentAddEmployeeBinding? = null
   private val binding: FragmentAddEmployeeBinding get() = _binding!!
   private val viewModel:AddEmployeeViewModel by viewModels()
   private val args: AddEmployeeFragmentArgs by navArgs()
   private lateinit var departmentList: List<Department>

   override fun onCreateView(
      inflater: LayoutInflater,
      container: ViewGroup?,
      savedInstanceState: Bundle?
   ): View? {
      _binding = FragmentAddEmployeeBinding.inflate(layoutInflater, container, false)
      return binding.root
   }

   override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
      super.onViewCreated(view, savedInstanceState)
      initToolbar()
      bindViewModel()
      binding.addButton.setOnClickListener {
         saveEmployee(args.id)
      }
      binding.goToWorkspaceButton.setOnClickListener {
         saveEmployee(args.id)
         findNavController()
            .navigate(AddEmployeeFragmentDirections.actionAddEmployeeFragmentToWorkspaceFragment(args.id))
      }
   }

   private fun initToolbar(){
      val toolbar = view?.findViewById<androidx.appcompat.widget.Toolbar>(R.id.toolbar)
      toolbar?.setTitle(R.string.add_employee)
      toolbar?.setNavigationIcon(R.drawable.ic_back)
      toolbar?.setNavigationOnClickListener {
         findNavController().popBackStack()
      }
   }

   private fun bindViewModel(){
      viewModel.init(args.id)
      viewModel.getDepartmentsList()
      viewModel.existingEmployee.observe(viewLifecycleOwner){
         setExistingEmployeeInfo(it)
      }
      viewModel.saveSuccess.observe(viewLifecycleOwner){
         findNavController().popBackStack()
      }
      viewModel.error.observe(viewLifecycleOwner){
         Toast.makeText(requireContext(), it.message, Toast.LENGTH_SHORT).show()
      }
      viewModel.departmentList.observe(viewLifecycleOwner){
         departmentList = it
         initDepartmentList(it)
      }
      viewModel.employeeDepartmentTitle.observe(viewLifecycleOwner){
         binding.autoComplete.setText(it)
      }
   }

   private fun saveEmployee(id: Int){
      with(binding){
         val department = departmentList.find { department ->
            department.departmentName== binding.departmentExposedDropdownMenu.editText?.text.toString()
         }!!
         val firstName = firstNameTextField.editText?.text.toString()
         val lastName = lastNameTextField.editText?.text.toString()
         val age = ageTextField.editText?.text.toString()
         val phoneNumber = phoneNumberTextField.editText?.text.toString()
         val hourSalary = hourSalaryTextField.editText?.text.toString().toFloat()
         var ageInt: Int by Delegates.notNull()
         try{
            ageInt = parseInt(age.trim())
         }catch(e: Exception){
            Toast.makeText(
               requireContext(),
               getString(R.string.incorrect_age_format),
               Toast.LENGTH_SHORT
            ).show()
            ageTextField.editText?.text?.clear()
         }
         if (departmentExposedDropdownMenu.editText?.text.isNullOrBlank() ||
            firstName.isBlank() ||
            lastName.isBlank() ||
            age.isBlank() ||
            phoneNumber.isBlank() ||
            hourSalary.toString().isBlank()
         ) {
            Toast.makeText(
               requireContext(),
               getString(R.string.fiil_in_all_fields),
               Toast.LENGTH_SHORT
            ).show()
         } else if (
            ageInt< 18 || ageInt > 65
         ){
            Toast.makeText(
               requireContext(),
               R.string.incorrect_age,
               Toast.LENGTH_SHORT
            ).show()
         }else {
            viewModel.save(
               id = id,
               firstName,
               lastName,
               ageInt,
               phoneNumber,
               hourSalary,
               departmentId = department.id
            )
         }
      }
   }

   private fun setExistingEmployeeInfo(employee: Employee){

      with(binding){
         firstNameTextField.editText?.setText(employee.firstName)
         lastNameTextField.editText?.setText(employee.lastName)
         ageTextField.editText?.setText(employee.age.toString())
         phoneNumberTextField.editText?.setText(employee.phoneNumber)
         hourSalaryTextField.editText?.setText(employee.hourSalary.toString())
      }
   }

   private fun initDepartmentList(departments: List<Department>){
      val items = departments.map{it.departmentName}
      val adapter = ArrayAdapter(requireContext(), R.layout.list_statuses_item, items)
      (binding.departmentExposedDropdownMenu.editText as? AutoCompleteTextView)?.setAdapter(adapter)
   }

   override fun onDestroy() {
      super.onDestroy()
      _binding = null
   }
}