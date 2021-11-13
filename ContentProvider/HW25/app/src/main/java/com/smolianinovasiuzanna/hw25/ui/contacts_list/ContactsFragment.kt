package com.smolianinovasiuzanna.hw25.ui.contacts_list

import android.app.AlertDialog
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.LayoutInflater
import androidx.fragment.app.Fragment
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.smolianinovasiuzanna.hw25.R
import com.smolianinovasiuzanna.hw25.data.PermissionType
import com.smolianinovasiuzanna.hw25.databinding.FragmentContactsBinding
import com.smolianinovasiuzanna.hw25.ui.MainActivity
import com.smolianinovasiuzanna.hw25.ui.model.ContactsViewModel
import permissions.dispatcher.PermissionRequest
import permissions.dispatcher.ktx.constructPermissionsRequest


class ContactsFragment : Fragment(R.layout.fragment_contacts) {

    private var _binding: FragmentContactsBinding? = null
    private val binding: FragmentContactsBinding get() = _binding!!
    private var contactsAdapter: ContactsListAdapter? = null
    private val viewModel: ContactsViewModel
        get() = (activity as MainActivity).contactsViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentContactsBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initList()
        bindViewModel()
        getContactWithPermissionCheck()

        binding.addFAB.setOnClickListener{
            addContactWithPermissionCheck()
        }
    }

    private fun initList() {
        Log.d("ContactsFragment", "initList")
        contactsAdapter = ContactsListAdapter{ contactId ->
            openContact(contactId) }
        with(binding.contactsList){
            adapter = contactsAdapter
            layoutManager = LinearLayoutManager(requireContext())
            setHasFixedSize(true)
            addItemDecoration(DividerItemDecoration(requireContext(), DividerItemDecoration.VERTICAL))
        }
    }

    private fun openContact(contactId: Long){
        findNavController().navigate(
            ContactsFragmentDirections.actionContactsFragmentToDetailContactInfoFragment(contactId))
    }

    private fun bindViewModel(){
        Log.d("ContactsFragment", "bindViewModel")
        viewModel.error.observe(viewLifecycleOwner) { exceptionMessage ->
            Log.d("Fragment", "viewModel.error.observe")
            showError(exceptionMessage)
        }
        viewModel.contacts.observe(viewLifecycleOwner){contactsList ->
            contactsAdapter?.setList(contactsList)
        }
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

    // Под капотом создается фрагмент, поэтому нужно поставить задачу в очередь
    private fun getContactWithPermissionCheck() {
        Log.d("ContactsFragment", "getContactWithPermissionCheck")
        Handler(Looper.getMainLooper()).post {
            constructPermissionsRequest(
                android.Manifest.permission.READ_CONTACTS,
                onShowRationale = ::onContactsPermissionShowRationale,
                onPermissionDenied = { onContactPermissionDenied(PermissionType.TYPE_READ_CONTACTS) },
                onNeverAskAgain = {onContactPermissionNeverAskAgain(PermissionType.TYPE_READ_CONTACTS)},
                requiresPermission = { viewModel.loadContactsList() }
            ).launch()
        }
    }
    private fun addContactWithPermissionCheck() {
        Log.d("ContactsFragment", "getContactWithPermissionCheck")
        Handler(Looper.getMainLooper()).post {
            constructPermissionsRequest(
                android.Manifest.permission.WRITE_CONTACTS,
                onShowRationale = ::onContactsPermissionShowRationale,
                onPermissionDenied = { onContactPermissionDenied(PermissionType.TYPE_WRITE_CONTACTS) },
                onNeverAskAgain = {onContactPermissionNeverAskAgain(PermissionType.TYPE_WRITE_CONTACTS)},
                requiresPermission = { goToAddContactFragment()}
            ).launch()
        }
    }

    private fun onContactsPermissionShowRationale(request: PermissionRequest){
        Log.d("ContactsFragment", "onContactsPermissionShowRationale")
        request.proceed()
    }

    private fun onContactPermissionDenied(permissionType: PermissionType){
        Log.d("ContactsFragment", "onContactPermissionDenied")
        val string  = when(permissionType){
            PermissionType.TYPE_WRITE_CONTACTS ->  R.string.contact_permission_denied_read
            PermissionType.TYPE_READ_CONTACTS ->  R.string.contact_permission_denied_write
        }
        Toast.makeText(
            requireContext(),
            string,
            Toast.LENGTH_SHORT
        ).show()
    }

    private fun onContactPermissionNeverAskAgain(permissionType: PermissionType){
        Log.d("ContactsFragment", "onContactPermissionNeverAskAgain")
        val string  = when(permissionType){
            PermissionType.TYPE_WRITE_CONTACTS ->  R.string.permission_denied_read_never_ask_again
            PermissionType.TYPE_READ_CONTACTS ->  R.string.permission_denied_write_never_ask_again
        }
        Toast.makeText(
            requireContext(),
            string,
            Toast.LENGTH_SHORT
        ).show()
    }

    private fun goToAddContactFragment(){
        findNavController().navigate(
            ContactsFragmentDirections.actionContactsFragmentToAddContactFragment()
        )
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
        contactsAdapter = null
    }
}