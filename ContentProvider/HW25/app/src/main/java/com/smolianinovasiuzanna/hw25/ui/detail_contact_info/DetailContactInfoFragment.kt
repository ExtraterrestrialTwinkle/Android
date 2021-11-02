package com.smolianinovasiuzanna.hw25.ui.detail_contact_info

import android.app.AlertDialog
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.isGone
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.smolianinovasiuzanna.hw25.R
import com.smolianinovasiuzanna.hw25.data.Contact
import com.smolianinovasiuzanna.hw25.data.PermissionType
import com.smolianinovasiuzanna.hw25.databinding.FragmentDetailContactInfoBinding
import com.smolianinovasiuzanna.hw25.printString
import com.smolianinovasiuzanna.hw25.ui.MainActivity
import com.smolianinovasiuzanna.hw25.ui.model.ContactsViewModel
import permissions.dispatcher.PermissionRequest
import permissions.dispatcher.ktx.constructPermissionsRequest
import kotlin.properties.Delegates

class DetailContactInfoFragment: Fragment(R.layout.fragment_detail_contact_info) {

    private var _binding: FragmentDetailContactInfoBinding? = null
    private val binding: FragmentDetailContactInfoBinding get() = _binding!!
    private val args: DetailContactInfoFragmentArgs by navArgs()
    var contactId by Delegates.notNull<Long>()
    private val viewModel: ContactsViewModel
        get() = (activity as MainActivity).contactsViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentDetailContactInfoBinding.inflate(layoutInflater, container, false)
        contactId = args.contactId
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.getContactInfo(contactId)
        bindViewModel()
        binding.deleteButton.setOnClickListener {
            deleteContactWithPermissionCheck()
        }
    }

    private fun deleteContactWithPermissionCheck() {
        Log.d("ContactsFragment", "getContactWithPermissionCheck")
        Handler(Looper.getMainLooper()).post {
            constructPermissionsRequest(
                android.Manifest.permission.WRITE_CONTACTS,
                onShowRationale = ::onContactsPermissionShowRationale,
                onPermissionDenied = { onContactPermissionDenied() },
                onNeverAskAgain = {onContactPermissionNeverAskAgain()},
                requiresPermission = {  deleteContact(contactId)}
            ).launch()
        }
    }

    private fun onContactsPermissionShowRationale(request: PermissionRequest){
        Log.d("ContactsFragment", "onContactsPermissionShowRationale")
        request.proceed()
    }

    private fun onContactPermissionDenied(){
        Log.d("ContactsFragment", "onContactPermissionDenied")
        Toast.makeText(
            requireContext(),
            R.string.contact_permission_denied,
            Toast.LENGTH_SHORT
        ).show()
    }

    private fun onContactPermissionNeverAskAgain(){
        Log.d("ContactsFragment", "onContactPermissionNeverAskAgain")
        Toast.makeText(
            requireContext(),
            R.string.permission_denied_never_ask_again,
            Toast.LENGTH_SHORT
        ).show()
    }

    private fun bindViewModel() {
        Log.d("DetailFragment", "bindViewModel")
        viewModel.error.observe(viewLifecycleOwner) { exceptionMessage ->
            Log.d("DetailFragment", "viewModel.error.observe")
            showError(exceptionMessage)
        }
        viewModel.contactInfo.observe(viewLifecycleOwner){ contact ->
            Log.d("DetailFragment", "viewModel.contactInfo.observe")
            showContactDetailInfo(contact)
        }
    }

    private fun showContactDetailInfo(contact: Contact){
        Log.d("DetailFragment", "showContactDetailInfo")
        with(binding){
            nameTextView.text = contact.name
            numberTextView.text = contact.phoneNumbers?.printString()
            if(contact.emails != null){emailText.isVisible = true} else {emailText.isGone = true}
            emailTextView.text = contact.emails?.printString()
        }
    }

    private fun showError(message: String){
        Log.d("DetailFragment", "fun showError message = $message")
        AlertDialog.Builder(requireContext())
            .setTitle(R.string.error)
            .setIcon(R.drawable.ic_error)
            .setMessage(message)
            .setPositiveButton("OK"){ dialog, _ ->
                dialog.cancel()
            }
            .show()
    }

    private fun deleteContact(contactId: Long){
        viewModel.deleteContact(contactId)
        findNavController().popBackStack()
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}