package com.smolianinovasiuzanna.hw25.ui.add_contact

import android.app.AlertDialog
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.smolianinovasiuzanna.hw25.R
import com.smolianinovasiuzanna.hw25.databinding.FragmentAddContactBinding
import com.smolianinovasiuzanna.hw25.ui.MainActivity
import com.smolianinovasiuzanna.hw25.ui.model.ContactsViewModel
import kotlin.properties.Delegates


class AddContactFragment: Fragment(R.layout.fragment_add_contact) {

    private var _binding: FragmentAddContactBinding? = null
    private val binding: FragmentAddContactBinding get() = _binding!!
    private val viewModel: ContactsViewModel
        get() = (activity as MainActivity).contactsViewModel

    private var name: String by Delegates.notNull<String>()
    private var phoneNumbers = mutableListOf<String>()
    private var emails = mutableListOf<String>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentAddContactBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        bindViewModel()
        buttonListeners()
    }

    private fun bindViewModel() {
        viewModel.error.observe(viewLifecycleOwner){exceptionMessage ->
            showError(exceptionMessage)}
    }

    private fun buttonListeners() {
        with(binding) {
            addContactButton.setOnClickListener {
                addContact()
            }
            addPhoneButton.setOnClickListener {
                addAnotherPhoneNumber()
            }
            addEmailButton.setOnClickListener {
                addAnotherEmailAddress()
            }
        }
    }

    private fun addContact() {
        with(binding){
            name = nameEditText.text.toString()
            val number1 = phoneEditText.text.toString()
            phoneNumbers.add(number1)
            val number2 = anotherPhoneEditText.text?.toString()
            if (number2 !=null){
                phoneNumbers.add(number2)
            }
            val email1 = emailEditText.text?.toString()
            val email2 = emailEditText.text?.toString()
            if (email1 != null){
                emails.add(email1)
            }
            if (email2 != null){
                emails.add(email2)
            }
        }
        validateFormData(name, phoneNumbers)
        viewModel.addContact(name, phoneNumbers, emails)
        findNavController().popBackStack()
    }

    private fun addAnotherPhoneNumber() {
        with(binding) {
            anotherPhoneTextField.isVisible = true
        }
    }

    private fun addAnotherEmailAddress() {
        with(binding) {
            anotherEmailTextField.isVisible = true
        }
    }

    private fun validateFormData(name: String, phones: List<String>) {
        if(name.isNotEmpty() && phones.isNotEmpty()) return
        showError("Заполните все обязательные поля!")
    }

    private fun showError(message: String){
        Log.d("Fragment", "fun showError message = $message")
        AlertDialog.Builder(requireContext())
            .setTitle(R.string.error)
            .setIcon(R.drawable.ic_error)
            .setMessage(message)
            .setPositiveButton("OK"){ dialog, _ ->
                dialog.cancel()
            }
            .show()
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

}