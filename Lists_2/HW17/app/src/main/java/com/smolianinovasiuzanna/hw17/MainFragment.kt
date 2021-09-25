package com.smolianinovasiuzanna.hw17

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.FrameLayout
import androidx.core.view.isGone
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager.POP_BACK_STACK_INCLUSIVE
import com.smolianinovasiuzanna.hw17.actors.ActorsFragment
import com.smolianinovasiuzanna.hw17.databinding.FragmentMainBinding
import com.smolianinovasiuzanna.hw17.posters.PostersFragment
import com.smolianinovasiuzanna.hw17.shows.ListFragment

class MainFragment: Fragment(R.layout.fragment_main), OnBackPressedListener, ItemSelectListener {

    lateinit var state: FragmentMainState
    private var _binding: FragmentMainBinding? = null
    private val binding: FragmentMainBinding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentMainBinding.inflate(layoutInflater, container, false)

        Log.d("MainFragment", "onCreateView")
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        with(binding) {


            if (savedInstanceState == null) {

                click(goToListButton, ListFragment())
                click(goToPostersListButton, PostersFragment())
                click(goToPicturesListButton, PicturesFragment())

            } else {
                buttonsLayout.isVisible = true
                state = savedInstanceState.getParcelable(KEY) ?: error("Error")

            }
        }
    }

    fun click (button: Button, fragment: Fragment){
        with(binding){
        button.setOnClickListener {
            childFragmentManager.beginTransaction()
                .setCustomAnimations(
                    android.R.animator.fade_in,
                    android.R.animator.fade_out
                )
                .replace(R.id.container, fragment)
                .addToBackStack("Main")
                .commit()
            container.isVisible = true
            buttonsLayout.isGone = true
            fragmentMain.background.alpha = 100
            state = FragmentMainState(true, "true")
        }
        }

    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putParcelable(KEY, state)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        Log.d("MainFragment", "onDestroyView")
        _binding = null
    }


    override fun onBackPressed() {
        Log.d("Main onBackPressed", " ${childFragmentManager.backStackEntryCount} ")
        binding.container.isGone = true
        binding.buttonsLayout.isVisible = true
        binding.fragmentMain.background.alpha = 255
        childFragmentManager.takeIf {
            it.backStackEntryCount > 0
        }?.popBackStack()?: activity?.finish()
    }

    override fun onItemInListClicked(index: Int) {
        binding.buttonsLayout.isGone = true
        binding.fragmentMain.background.alpha = 100
       // binding.container.isGone = true
        childFragmentManager
            .beginTransaction()
            .setCustomAnimations(android.R.animator.fade_in, android.R.animator.fade_out)
            .replace(R.id.container, ActorsFragment.newInstance(index))
            .addToBackStack("Actors")
            .commit()


        Log.d("MainFragment", "fun onBtnInListClicked")
    }

    companion object{
        private const val KEY = "main_key"
    }
}
