package com.smolianinovasiuzanna.application

import android.app.Activity
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.content.res.Resources
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.view.isGone
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import by.kirich1409.viewbindingdelegate.viewBinding
import com.smolianinovasiuzanna.application.adapter.ShowsAdapter
import com.smolianinovasiuzanna.application.databinding.FragmentDialogBinding
import com.smolianinovasiuzanna.application.databinding.FragmentShowsBinding
import com.smolianinovasiuzanna.application.viewmodel.ShowsViewModel
import jp.wasabeef.recyclerview.animators.ScaleInAnimator

class ShowsFragment: Fragment(R.layout.fragment_shows) {
    private val binding by viewBinding (FragmentShowsBinding::bind)
    private var _dialogBinding: FragmentDialogBinding? = null
    private val dialogBinding: FragmentDialogBinding get() = _dialogBinding!!
    private var showsAdapter: ShowsAdapter? = null
    private val listViewModel: ShowsViewModel by viewModels()

    override fun onAttach(context: Context) {
        super.onAttach(context)

        if(ShowsResourcesData.resources == null){
            ShowsResourcesData.resources = resources;
        }
}

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initList()
        binding.addFab.setOnClickListener { addShowDialog() }
        observeViewModelState()

    }

    private fun initList(){
        Log.d("ListFragment", "fun initList")
            //быстрый клик - открывается ActorsFragment, долгий клик - удаление элемента
        showsAdapter = ShowsAdapter ({ id ->
            val action = ShowsFragmentDirections.actionListFragmentToActorsFragment(id)
            findNavController().navigate(action)},
            {position -> deleteShow(position)})
        with(binding.showsList) {
            adapter = showsAdapter
            layoutManager = LinearLayoutManager(requireContext())
            setHasFixedSize(true)
            itemAnimator = ScaleInAnimator()
        }
    }

    private fun addShowToList(type: Type){
        Log.d("List fragment", "addShowToList")
        stringsArray = arrayListOf<String>(title, directorOrCreators, yearOrSeries, posterUri)
        listViewModel.addShow(type, stringsArray)
        binding.showsList.scrollToPosition(0)
    }

    private fun addShowDialog(){
        _dialogBinding = FragmentDialogBinding.inflate(LayoutInflater.from(requireContext()))
        val dialog = AlertDialog.Builder(requireContext())
        lateinit var type: Type
        with(dialogBinding) {
            dialog.setView(root)
                .setPositiveButton("Add", DialogInterface.OnClickListener { dialog, id ->
                    Log.d("ListFragment", "setPositiveButton")
                    when (type) {

    // В зависимости от переданного при создании типа Shows, при нажатии на кнопку ОК вызывается метод
    // addShowToList() с необходимым типом в параметрах

                        Type.TYPE_FILM -> addShowToList(Type.TYPE_FILM)
                        Type.TYPE_SERIES -> addShowToList(Type.TYPE_SERIES)
                       // else -> error("Incorrect show type")
                    }
                }
                )
                .setNegativeButton("Cancel") { _, _ ->
                    Toast.makeText(context, "You cancelled adding a show", Toast.LENGTH_SHORT)
                        .show()
                }
                .setOnCancelListener {
                    it.cancel()
                }

// Радиогруппа: Выбираем тип добавляемого Shows: Shows.Film или Shows.Series. В зависимости от
// выбранного типа в выпадающей части диалога устанавливаются разные значения полей. Также тип затем
// передается при добавлении фильма/сериала
            chooseShowRadioGroup.setOnCheckedChangeListener { _, checkedId ->
                when (checkedId) {
                    R.id.radioFilm -> {
                        inputShowInfoLayout.isVisible = true
                        inputDirectorTextView.text =
                            getString(R.string.input_director_s_name)
                        inputYearTextView.text =
                            getString(R.string.input_the_year_of_release)
                        directorEditText.hint =
                            getString(R.string.input_director_s_name)
                        yearEditText.hint =
                            getString(R.string.input_the_year_of_release)
                        dialogListeners()
                        type = Type.TYPE_FILM
                    }

                    R.id.radioSeries -> {
                        inputShowInfoLayout.isVisible = true
                        inputDirectorTextView.text =
                            getString(R.string.input_creators_name)
                        inputYearTextView.text =
                            getString(R.string.input_the_number_of_seasons)
                        directorEditText.hint =
                            getString(R.string.input_creators_name)
                        yearEditText.hint =
                            getString(R.string.input_the_number_of_seasons)
                        dialogListeners()
                        type = Type.TYPE_SERIES
                    }
                }
            }
            dialog.create()
                .show()
        }

    }
    // Слушатели EditText и Button в диалоге
    private fun dialogListeners(){
        with(dialogBinding) {
            Log.d("AddShowDialogFragment", "listeners")
            titleEditText.addTextChangedListener(object : TextWatcher {
                override fun beforeTextChanged(
                    s: CharSequence?,
                    start: Int,
                    count: Int,
                    after: Int
                ) {
                }
                override fun onTextChanged(
                    s: CharSequence?,
                    start: Int,
                    before: Int,
                    count: Int
                ) {
                    title = titleEditText.text.toString()
                }
                override fun afterTextChanged(s: Editable?) {
                }
            })

            directorEditText.addTextChangedListener(object : TextWatcher {
                override fun beforeTextChanged(
                    s: CharSequence?,
                    start: Int,
                    count: Int,
                    after: Int
                ) {
                }
                override fun onTextChanged(
                    s: CharSequence?,
                    start: Int,
                    before: Int,
                    count: Int
                ) {
                    directorOrCreators = directorEditText.text.toString()
                }
                override fun afterTextChanged(s: Editable?) {
                }
            })
            yearEditText.addTextChangedListener(object : TextWatcher {
                override fun beforeTextChanged(
                    s: CharSequence?,
                    start: Int,
                    count: Int,
                    after: Int
                ) {
                }
                override fun onTextChanged(
                    s: CharSequence?,
                    start: Int,
                    before: Int,
                    count: Int
                ) {
                    yearOrSeries = yearEditText.text.toString()
                }
                override fun afterTextChanged(s: Editable?) {
                }
            })

            downloadPosterButton.setOnClickListener {
                val getIntent = Intent(Intent.ACTION_GET_CONTENT)
                getIntent.type = "image/*"

                val pickIntent =
                    Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
                pickIntent.type = "image/*"

                val chooserIntent = Intent.createChooser(getIntent, "Select Image")
                chooserIntent.putExtra(Intent.EXTRA_INITIAL_INTENTS, arrayOf(pickIntent))

                startActivityForResult(chooserIntent, PICK_IMAGE)
            }

        }
    }
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        with(dialogBinding) {
            when (requestCode) {
                PICK_IMAGE -> if (resultCode == Activity.RESULT_OK) {
                    val selectedImage: Uri? = data?.data
                    uriTextView.text = selectedImage.toString()
                    uriTextView.isVisible = true
                    downloadPosterButton.isGone = true
                    posterUri = uriTextView.text.toString()
                }
            }
        }
    }

    private fun deleteShow(position: Int): Boolean {
        listViewModel.deleteShow(position)
        if (showsAdapter?.itemCount == 0){
            binding.emptyListTextView.isVisible = true
        }
    return true
    }

    private fun observeViewModelState() {
        listViewModel.shows
            .observe(viewLifecycleOwner) { newShows -> showsAdapter?.items = newShows }

        listViewModel.showToast
            .observe(viewLifecycleOwner) {
                Toast.makeText(requireContext(), "Элемент удален", Toast.LENGTH_SHORT).show()
            }
    }

    companion object {

        var stringsArray = arrayListOf<String>()
        private const val PICK_IMAGE = 150
        private var title:String = ""
        private var directorOrCreators: String = ""
        private var yearOrSeries: String = ""
        private var posterUri: String = ""

    }
}

object ShowsResourcesData {
    var resources: Resources? = null
}
