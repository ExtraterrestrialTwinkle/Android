

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
import com.smolianinovasiuzanna.hw14_10.databinding.FragmentDetailBinding
import com.smolianinovasiuzanna.hw14_10.withArguments


class DetailFragment:Fragment(R.layout.fragment_detail) {
    private var _binding: FragmentDetailBinding? = null
    private val binding: FragmentDetailBinding get() = _binding!!

    val parFragment: Fragment? = parentFragment
    val parentActivity: Activity? = activity

    val parent: ItemSelectListener? = parFragment?.let{it as? ItemSelectListener}
        ?:parentActivity?.let{it as? ItemSelectListener}





    private var valid: FormState = FormState(false, "")


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

//        if (savedInstanceState != null){
//            buttonIndex = savedInstanceState.getInt(DKEY)
//        }


        _binding = FragmentDetailBinding.inflate(layoutInflater, container, false)
        Log.d("DetailFragment", "onCreateView")

        binding.detailImage2
        binding.detailImage1
        binding.detailText2
        binding.detailText1

        val buttonIndex = arguments?.getInt(
            INDEX,
            INDEX_DEF
        ) ?: INDEX_DEF

        if (buttonIndex != INDEX_DEF) {
            setDescription(buttonIndex)

        }else Log.d("setDescription", "no description")

        return binding.root

    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        Log.d("DetailFragment", "onAttach")
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {

        super.onActivityCreated(savedInstanceState)
        Log.d("DetailFragment", "onActivityCreated")
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
       // outState.putInt(DKEY, valid)

        Log.d("DetailFragment", "onSaveInstanceState")
    }

    fun setDescription(buttonIndex: Int) {


        Log.d("DetailFragment", "fun setDescription")

        when (buttonIndex) {
            1 -> {
                binding.detailImage1.setImageResource(R.drawable.totoro1)
                binding.detailImage2.setImageResource(R.drawable.totoro_hieroglyph1)
                binding.detailText1.text = getString(R.string.totoro)
                binding.detailText2.text = getString(R.string.totoro_info)
            }
            2 -> {
                binding.detailImage1.setImageResource(R.drawable.catbus1)
                binding.detailImage2.setImageResource(R.drawable.catbus_hieroglyph1)
                binding.detailText1.text = getString(R.string.catbus)
                binding.detailText2.text = getString(R.string.catbus_info)
            }
            3 -> {
                binding.detailImage1.setImageResource(R.drawable.satsuki1)
                binding.detailImage2.setImageResource(R.drawable.satsuki_hieroglyph1)
                binding.detailText1.text = getString(R.string.satsuki)
                binding.detailText2.text = getString(R.string.satsuki_info)
            }
            4 -> {
                binding.detailImage1.setImageResource(R.drawable.mei1)
                binding.detailImage2.setImageResource(R.drawable.mei_hieroglyph1)
                binding.detailText1.text = getString(R.string.mei)
                binding.detailText2.text = getString(R.string.mei_info)
            }
            5 -> {
                binding.detailImage1.setImageResource(R.drawable.tatsuo1)
                binding.detailImage2.setImageResource(R.drawable.tatsuo_hieroglyph1)
                binding.detailText1.text = getString(R.string.tatsuo)
                binding.detailText2.text = getString(R.string.tatsuo_info)
            }
            6 -> {
                binding.detailImage1.setImageResource(R.drawable.yasuko1)
                binding.detailImage2.setImageResource(R.drawable.yasuko_hieroglyph1)
                binding.detailText1.text = getString(R.string.yasuko)
                binding.detailText2.text = getString(R.string.yasuko_info)
            }
            7 -> {
                binding.detailImage1.setImageResource(R.drawable.susuwatari1)
                binding.detailImage2.setImageResource(R.drawable.susuwatari_hieroglyph1)
                binding.detailText1.text = getString(R.string.susuwatari)
                binding.detailText2.text = getString(R.string.susuwatari_info)
            }
            else -> return
        }

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
        Log.d("DetailFragment", "onDestroyView, ${hashCode()}")
    }

    override fun toString(): String {
        return ""
    }
    companion object {
        private const val DKEY = "dkey"
        private const val BKEY = "bkey"

        const val INDEX = "btn_index"
        private const val INDEX_DEF = -1

        fun newInstance(buttonIndex: Int): DetailFragment {

            return DetailFragment().withArguments {
               putInt (INDEX, buttonIndex)
            }
        }
    }
}