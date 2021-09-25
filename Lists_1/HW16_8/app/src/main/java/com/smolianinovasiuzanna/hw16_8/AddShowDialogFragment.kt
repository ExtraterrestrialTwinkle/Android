package com.smolianinovasiuzanna.hw16_8

import android.app.Activity.RESULT_OK
import android.app.Dialog
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.widget.RadioGroup
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.view.isGone
import androidx.core.view.isVisible
import androidx.fragment.app.DialogFragment
import com.smolianinovasiuzanna.hw16_8.databinding.FragmentDialogBinding


class AddShowDialogFragment: DialogFragment()  {
    private var _binding: FragmentDialogBinding? = null
    private val binding: FragmentDialogBinding get() = _binding!!

    private var title:String = ""
    private var directorOrCreators: String = ""
    private var yearOrSeries: String = ""
    private var posterUri: String = ""

       override fun onAttach(context: Context) {
        super.onAttach(context)
    }

        @Throws(IllegalStateException::class)
        override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
            _binding = FragmentDialogBinding.inflate(LayoutInflater.from(requireContext()))
            return activity?.let {
            val dialog = AlertDialog.Builder(requireContext())
                val inflater = layoutInflater.inflate(R.layout.fragment_dialog, null)
            dialog.setView(inflater)
                .setPositiveButton("Add") { _, _ -> } // дописать код обработки нажатия кнопки Add
                .setNegativeButton("Cancel") { _, _ ->
                    Toast.makeText(context, "You cancelled adding a show", Toast.LENGTH_SHORT)
                        .show()
                }

            binding.chooseShowRadioGroup.setOnCheckedChangeListener{ _, checkedId ->
               when (checkedId) {
                   R.id.radioFilm -> {
                       Toast.makeText(context, "RadioFilm is clicked", Toast.LENGTH_SHORT).show()
                       binding.inputShowInfoLayout.isVisible = true
                       addFilm()
                   }

                   R.id.radioSeries -> {
                       Toast.makeText(context, "RadioSeries is clicked", Toast.LENGTH_SHORT).show()
                       binding.inputShowInfoLayout.isVisible = true
                       binding.inputDirectorTextView.text = getString(R.string.input_creators_name)
                       binding.inputYearTextView.text = getString(R.string.input_the_number_of_seasons)
                       addSeries()
                   }
               }

           }
            dialog.create()
            } ?: throw IllegalStateException("Activity cannot be null")
        }


    private fun addFilm(): Shows.Film{
        Log.d("addFilm", "OK")

        binding.titleEditText.addTextChangedListener(object: TextWatcher{
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                TODO("Not yet implemented")
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                title = binding.titleEditText.text.toString()

            }

            override fun afterTextChanged(s: Editable?) {
                TODO("Not yet implemented")
            }

        })

        binding.directorEditText.addTextChangedListener(object: TextWatcher{
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                TODO("Not yet implemented")
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                directorOrCreators = binding.directorEditText.text.toString()

            }

            override fun afterTextChanged(s: Editable?) {
                TODO("Not yet implemented")
            }

        })
        binding.yearEditText.addTextChangedListener(object: TextWatcher{
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                TODO("Not yet implemented")
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                yearOrSeries = binding.yearEditText.text.toString()

            }

            override fun afterTextChanged(s: Editable?) {
                TODO("Not yet implemented")
            }

        })
        binding.downloadPosterButton.setOnClickListener(object : View.OnClickListener {
            override fun onClick(v: View?) {
                val getIntent = Intent(Intent.ACTION_GET_CONTENT)
                getIntent.type = "image/*"

                val pickIntent =
                    Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
                pickIntent.type = "image/*"

                val chooserIntent = Intent.createChooser(getIntent, "Select Image")
                chooserIntent.putExtra(Intent.EXTRA_INITIAL_INTENTS, arrayOf(pickIntent))

                startActivityForResult(chooserIntent, PICK_IMAGE)

            }
        })

        posterUri = binding.uriTextView.text.toString()
       val newFilm = Shows.Film(
            title,
            yearOrSeries,
            posterUri,
            directorOrCreators
        )
        return newFilm
    }

    private fun addSeries(): Shows.Series{
        Log.d("addseries", "OK")

        binding.titleEditText.addTextChangedListener(object: TextWatcher{
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                TODO("Not yet implemented")
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                title = binding.titleEditText.text.toString()
                binding.titleEditText.clearFocus()
                binding.titleEditText.isCursorVisible = false
            }

            override fun afterTextChanged(s: Editable?) {
                TODO("Not yet implemented")
            }

        })

        binding.directorEditText.addTextChangedListener(object: TextWatcher{
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                TODO("Not yet implemented")
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                directorOrCreators = binding.directorEditText.text.toString()
                binding.directorEditText.clearFocus()
                binding.directorEditText.isCursorVisible = false
            }

            override fun afterTextChanged(s: Editable?) {
                TODO("Not yet implemented")
            }

        })
        binding.yearEditText.addTextChangedListener(object: TextWatcher{
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                TODO("Not yet implemented")
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                yearOrSeries = binding.yearEditText.text.toString()
                binding.yearEditText.clearFocus()
                binding.yearEditText.isCursorVisible = false
            }

            override fun afterTextChanged(s: Editable?) {
                TODO("Not yet implemented")
            }

        })
        binding.downloadPosterButton.setOnClickListener(object : View.OnClickListener {
            override fun onClick(v: View?) {
                val getIntent = Intent(Intent.ACTION_GET_CONTENT)
                getIntent.type = "image/*"

                val pickIntent =
                    Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
                pickIntent.type = "image/*"

                val chooserIntent = Intent.createChooser(getIntent, "Select Image")
                chooserIntent.putExtra(Intent.EXTRA_INITIAL_INTENTS, arrayOf(pickIntent))

                startActivityForResult(pickIntent, PICK_IMAGE)

            }
        })
        posterUri = binding.uriTextView.text.toString()
        val newSeries: Shows.Series = Shows.Series(
            title,
            directorOrCreators,
            posterUri,
            yearOrSeries
        )
        return newSeries
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        when (requestCode) {
            PICK_IMAGE -> if (resultCode == RESULT_OK) {
                val selectedImage: Uri? = data?.data
                binding.uriTextView.text = selectedImage.toString()
                binding.uriTextView.isVisible = true
                binding.downloadPosterButton.isGone = true

            }
        }
    }

    override fun onDetach() {
        super.onDetach()
        _binding = null
    }

    companion object{
        private const val PICK_IMAGE = 1
    }

}
