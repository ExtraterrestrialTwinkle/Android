package com.smolianinovasiuzanna.hw25.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.smolianinovasiuzanna.hw25.R
import com.smolianinovasiuzanna.hw25.databinding.FragmentNavigationBinding

class NavigationFragment : Fragment(R.layout.fragment_navigation) {

    private var _binding: FragmentNavigationBinding? = null
    private val binding: FragmentNavigationBinding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentNavigationBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.toContactsButton.setOnClickListener {
            findNavController()
                .navigate(NavigationFragmentDirections.actionNavigationFragmentToContactsFragment())
        }

        binding.toShareFilesButton.setOnClickListener {
            findNavController()
                .navigate(NavigationFragmentDirections.actionNavigationFragmentToShareFilesFragment())
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}