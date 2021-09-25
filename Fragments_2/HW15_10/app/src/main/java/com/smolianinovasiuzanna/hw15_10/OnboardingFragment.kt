package com.smolianinovasiuzanna.hw15_10

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.fragment.app.Fragment
import com.smolianinovasiuzanna.hw15_10.databinding.FragmentOnboardingBinding

class OnboardingFragment: Fragment(R.layout.fragment_onboarding) {

    private var _binding: FragmentOnboardingBinding? = null
    private val binding: FragmentOnboardingBinding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentOnboardingBinding.inflate(layoutInflater, container, false)

        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        binding.imageView.setImageResource(requireArguments().getInt(KEY_IMAGE))
        binding.nameTextView.setText(requireArguments().getInt(KEY_NAME))
        binding.infoTextView.setText(requireArguments().getInt(KEY_INFO))

    }

    companion object{
        private const val KEY_IMAGE = "image"
        private const val KEY_NAME = "name"
        private const val KEY_INFO = "info"
        private const val KEY_TAG = "tag"

        fun newInstance (
            @DrawableRes imageRes: Int,
            @StringRes nameRes: Int,
            @StringRes infoRes: Int,
            tag: String,


        ): OnboardingFragment {
            return OnboardingFragment().withArguments {
                putInt (KEY_IMAGE, imageRes)
                putInt(KEY_NAME, nameRes)
                putInt(KEY_INFO, infoRes)
                putString(KEY_TAG, tag)
            }
        }
    }
}