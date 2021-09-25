package com.smolianinovasiuzanna.hw14_10.com.smolianinovasiuzanna.hw14_10

import android.content.Context
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.ProgressBar
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.smolianinovasiuzanna.hw14_10.FormState
import com.smolianinovasiuzanna.hw14_10.R
import com.smolianinovasiuzanna.hw14_10.databinding.FragmentLoginBinding


class LoginFragment:Fragment(R.layout.fragment_login) {
    private var _binding: FragmentLoginBinding? = null
    private val binding: FragmentLoginBinding get() = _binding!!

    private var valid: FormState = FormState(false, "")
    private val itemSelectListener: ItemSelectListener?
    get() = activity?.let{it as? ItemSelectListener}


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentLoginBinding.inflate(layoutInflater, container, false)

        return binding.root

    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        val loadingProgressBar = ProgressBar(context)
        loadingProgressBar.layoutParams = LinearLayout.LayoutParams(
            ViewGroup.LayoutParams.WRAP_CONTENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        )

        val errorText = TextView(context)
        errorText.layoutParams = LinearLayout.LayoutParams(
            ViewGroup.LayoutParams.WRAP_CONTENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        )

        if (savedInstanceState != null) {
            valid = savedInstanceState.getParcelable(KEY) ?: error("Error")
            binding.error.addView(errorText)
            errorText.visibility = View.VISIBLE
            errorText.text = valid.message
        }

        binding.editEmail.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                validateFormData(
                    email = p0.toString(),
                    password = binding.editPassword.text.toString(),
                    isTermsChecked = binding.checkbox.isChecked
                )
            }

            override fun afterTextChanged(p0: Editable?) {
            }
        })
        binding.editPassword.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                validateFormData(
                    email = p0.toString(),
                    password = binding.editPassword.text.toString(),
                    isTermsChecked = binding.checkbox.isChecked
                )
            }

            override fun afterTextChanged(p0: Editable?) {
            }
        })

        binding.checkbox.setOnCheckedChangeListener { compoundButton, isChecked ->
            validateFormData(
                email = binding.editEmail.text.toString(),
                password = binding.editPassword.text.toString(),
                isTermsChecked = isChecked
            )
        }
        binding.ANRButton.setOnClickListener {
            Thread.sleep(10000)
        }

        binding.loginButton.setOnClickListener {
            if (binding.editEmail.text.toString().contains('@')
            ) {
                binding.error.removeView(errorText)

                binding.frame.addView(loadingProgressBar)
                loadingProgressBar.visibility = View.VISIBLE

                openMainFragment()



            } else {
                valid = FormState(false, "E-mail is incorrect")
                binding.error.addView(errorText)
                errorText.visibility = View.VISIBLE
                errorText.text = valid.message
            }
            binding.editEmail.setText("")
            binding.editPassword.setText("")
        }

        val imageView = binding.imageView
        Glide
            .with(this)
            .load("https://mir-s3-cdn-cf.behance.net/project_modules/2800_opt_1/c068f451582427.58f316acc1842.jpg")
            .into(imageView)

        Log.d(tag, "OnCreate ${hashCode()}")
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
        Log.d(tag, "onDestroyView, ${hashCode()}")
    }

    private fun validateFormData(email: String, password: String, isTermsChecked: Boolean) {
        binding.loginButton.isEnabled = email.isNotEmpty() && password.isNotEmpty() && isTermsChecked
    }

    private fun openMainFragment(){
        itemSelectListener?.showMainFragment()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putParcelable(KEY, valid)
    }
    companion object {
        private const val KEY = "key"
    }
}