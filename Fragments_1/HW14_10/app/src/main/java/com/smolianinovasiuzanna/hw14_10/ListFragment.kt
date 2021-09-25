package com.smolianinovasiuzanna.hw14_10.com.smolianinovasiuzanna.hw14_10

import android.app.Activity
import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.smolianinovasiuzanna.hw14_10.FormState
import com.smolianinovasiuzanna.hw14_10.R
import com.smolianinovasiuzanna.hw14_10.databinding.FragmentListBinding


class ListFragment:Fragment(R.layout.fragment_list),  View.OnClickListener {
    private var _binding: FragmentListBinding? = null
    private val binding: FragmentListBinding get() = _binding!!

    private val parFragment: Fragment? = parentFragment
    private val parentActivity: Activity? = activity

    private var parent: ItemSelectListener? = parFragment?.let{it as? ItemSelectListener}
        ?:parentActivity?.let{it as? ItemSelectListener}

    private var valid: FormState = FormState(false, "")

     override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        Log.d("ListFragment", "onCreateView")

        _binding = FragmentListBinding.inflate(layoutInflater)

        binding.listText1.setOnClickListener(this)
        binding.listText2.setOnClickListener(this)
        binding.listText3.setOnClickListener(this)
        binding.listText4.setOnClickListener(this)
        binding.listText5.setOnClickListener(this)
        binding.listText6.setOnClickListener(this)
        binding.listText7.setOnClickListener(this)

         valid = FormState(true, "")

        return binding.root

    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        Log.d("ListFragment", "onAttach")
    }
    override fun onActivityCreated(savedInstanceState: Bundle?) {

        super.onActivityCreated(savedInstanceState)
        Log.d("ListFragment", "onActivityCreated")

    }

    override fun onStart() {
        super.onStart()
        Log.d("ListFragment", "onStart")
    }
    override fun onResume() {
        super.onResume()
        Log.d("ListFragment", "onResume")
    }
    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putParcelable(LKEY, valid)
        Log.d("ListFragment", "onSaveInstanceState")
    }

    private fun translateIdToIndex(id: Int): Int {
        Log.d("ListFragment", "fun translateIdToIndex")
        var index = -1
        when (id) {
            R.id.listText1 -> index = 1
            R.id.listText2 -> index = 2
            R.id.listText3 -> index = 3
            R.id.listText4 -> index = 4
            R.id.listText5 -> index = 5
            R.id.listText6 -> index = 6
            R.id.listText7 -> index = 7
        }
        return index
    }

    override fun onClick(v: View?) {
        Log.d("ListFragment", "fun onClick")
        val buttonIndex = translateIdToIndex(v!!.id)
        // Toast.makeText(context, "Кнопка $buttonIndex нажата", Toast.LENGTH_SHORT ).show()

        if(parentFragment is ItemSelectListener){
            (parentFragment as ItemSelectListener).onButtonInListClicked(buttonIndex)
        } else{
            Log.d("ListFragment", "parent is null")
        }
    }



    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
        Log.d("ListFragment", "onDestroyView, ${hashCode()}")
    }

    companion object {
        private const val LKEY = "lkey"
    }



}
