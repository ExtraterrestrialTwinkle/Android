package com.smolianinovasiuzanna.hw14_10.com.smolianinovasiuzanna.hw14_10

import android.content.Context
import android.content.res.Configuration
import android.graphics.drawable.GradientDrawable
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import com.smolianinovasiuzanna.hw14_10.FormState
import com.smolianinovasiuzanna.hw14_10.R
import com.smolianinovasiuzanna.hw14_10.databinding.FragmentMainBinding

class MainFragment:Fragment(R.layout.fragment_main), ItemSelectListener, OnBackPressedListener{
private var valid: FormState = FormState(false, "")
    private var _binding: FragmentMainBinding? = null
    private val binding: FragmentMainBinding get() = _binding!!


    override fun onAttach(context: Context) {
        super.onAttach(context)
        Log.d("MainFragment", "onAttach")

    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

       // if (childFragmentManager.findFragmentById(R.id.listFragment) == null ) { // если null значит телефон
            showListFragment()
      //  } else {inflater.inflate(R.layout.fragment_main, container, false)}

        _binding = FragmentMainBinding.inflate(layoutInflater)
            return binding.root
    }
    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        Log.d("MainFragment", "onActivityCreated")

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
        Log.d("MainFragment", "onDestroyView")
    }

    override fun onDetach() {
        super.onDetach()
        Log.d("MainFragment", "onDetach")
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putParcelable(MKEY, valid)
        Log.d("MainFragment", "onSaveInstanceState")
    }

    override fun onButtonInListClicked(buttonIndex: Int){
        binding.detailContainer.visibility = View.VISIBLE
           childFragmentManager
               .beginTransaction()
                .setCustomAnimations(android.R.animator.fade_in, android.R.animator.fade_out)
                .replace(R.id.detailContainer, DetailFragment.newInstance(buttonIndex))
                .addToBackStack("Detail")
                .commit()

        valid = FormState(true, "")

        Log.d("ListFragment", "fun onBtnInListClicked")
    }


    private fun showListFragment (){
        childFragmentManager.beginTransaction()
            .setCustomAnimations(android.R.animator.fade_in, android.R.animator.fade_out)
            .replace(R.id.listContainer, ListFragment())
            .commit()
    }

    override fun onBackPressed() {
        childFragmentManager.takeIf {
                it.backStackEntryCount > 0
            }?.popBackStack() ?: activity?.finish()
    }
    companion object {
        private const val MKEY = "mkey"
    }
}
