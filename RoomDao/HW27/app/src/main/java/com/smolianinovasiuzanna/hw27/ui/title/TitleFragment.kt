package com.smolianinovasiuzanna.hw27.ui.title


import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import android.widget.Toolbar
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.smolianinovasiuzanna.hw27.R
import com.smolianinovasiuzanna.hw27.data.repository.FactoryRepository
import com.smolianinovasiuzanna.hw27.databinding.FragmentTitleBinding
import com.smolianinovasiuzanna.hw27.databinding.ToolbarBinding
import kotlinx.coroutines.launch

class TitleFragment : Fragment(R.layout.fragment_title) {

    private var _binding: FragmentTitleBinding? = null
    private val binding: FragmentTitleBinding get() = _binding!!
    private val viewModel: TitleViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentTitleBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.error.observe(viewLifecycleOwner){
            Toast.makeText(requireContext(), it.message, Toast.LENGTH_LONG).show()
        }
        viewModel.checkFirstTimeLaunch(requireContext())
        initToolbar()
        with(binding){
            contractorsButton.setOnClickListener {
                findNavController().navigate(TitleFragmentDirections.actionTitleFragmentToContarctorsFragment())
            }

            departmentsButton.setOnClickListener {
                findNavController().navigate(TitleFragmentDirections.actionTitleFragmentToDepartmentsFragment())
            }

            employeesButton.setOnClickListener {
                findNavController().navigate(TitleFragmentDirections.actionTitleFragmentToEmployeesFragment())
            }

            projectsButton.setOnClickListener {
                findNavController().navigate(TitleFragmentDirections.actionTitleFragmentToProjectsFragment())
            }
        }

    }

    private fun initToolbar() {
        val toolbar = view?.findViewById<androidx.appcompat.widget.Toolbar>(R.id.toolbar)
        toolbar?.setTitle(R.string.database_for_something_plant)
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}