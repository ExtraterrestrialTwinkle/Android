package com.smolianinovasiuzanna.hw16_8

import android.app.Activity
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
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.view.isGone
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.smolianinovasiuzanna.hw16_8.databinding.FragmentDialogBinding
import com.smolianinovasiuzanna.hw16_8.databinding.FragmentListBinding

class ListFragment: Fragment(){
    private var _binding: FragmentListBinding? = null
    private val binding: FragmentListBinding get() = _binding!!
    private var _dialogBinding: FragmentDialogBinding? = null
    private val dialogBinding: FragmentDialogBinding get() = _dialogBinding!!
   var isDialogOpen: Boolean = false
    var state = State(listOf(), isDialogOpen)

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentListBinding.inflate(layoutInflater, container, false)

        Log.d("ListFragment", "onCreateView")
        return binding.root

    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        initShows(context)
        Log.d("ListFragment", "onAttach")
    }
    private lateinit var shows: List<Shows>

    private fun initShows(context: Context) {
        Log.d("ListFragment", "fun initShows")
        shows = listOf(
        Shows.Film(
            title = context.getString(R.string.the_shawshenk_redemption),
            year = "1994",
            posterLink = "https://movieposters2.com/images/1374353-b.jpg",
            director = context.getString(R.string.frank_darabont)

        ),
        Shows.Film(
            title = context.getString(R.string.the_godfather),
            year = "1972",
            posterLink = "https://movieposters2.com/images/694423-b.jpg",
            director = context.getString(R.string.francis_ford_coppola)

        ),
        Shows.Film(
            title = context.getString(R.string.the_dark_knight),
            year = "2008",
            posterLink = "https://movieposters2.com/images/653694-b.jpg",
            director = context.getString(R.string.christopher_nolan)

        ),
        Shows.Film(
            title = context.getString(R.string.schindlers_list),
            year = "1993",
            posterLink = "https://movieposters2.com/images/657002-b.jpg",
            director = context.getString(R.string.steven_spielberg)

        ),
        Shows.Film(
            title = context.getString(R.string.pulp_fiction),
            year = "1994",
            posterLink = "https://movieposters2.com/images/739665-b.jpg",
            director = context.getString(R.string.quentin_tarantino)

        ),
        Shows.Series(
            title = context.getString(R.string.the_walking_dead),
            posterLink = "https://movieposters2.com/images/1148244-b.jpg",
            creators = context.getString(R.string.the_walking_dead_creators),
            seasons = "11"

        ),
        Shows.Series(
            title = context.getString(R.string.friends),
            posterLink = "https://movieposters2.com/images/645471-b.jpg",
            creators = context.getString(R.string.friends_creators),
            seasons = "10"

        ),
        Shows.Series(
            title = context.getString(R.string.greys_anatomy),
            posterLink = "https://movieposters2.com/images/1199467-b.jpg",
            creators = context.getString(R.string.greys_anatomy_creators),
            seasons = "17"

        ),
        Shows.Series(
            title = context.getString(R.string.house_m_d),
            posterLink = "https://movieposters2.com/images/646575-b.jpg",
            creators = context.getString(R.string.house_m_d_creators),
            seasons = "8"

        ),
        Shows.Series(
            title = context.getString(R.string.bones),
            posterLink = "https://movieposters2.com/images/648896-b.jpg",
            creators = context.getString(R.string.bones_creators),
            seasons = "12"

        )
    )
    }

    private var showsAdapter: ShowsAdapter? = null

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        Log.d("ListFragment", "onActivityCreated")

        if(savedInstanceState == null) {
            Log.d("savedInstanceState", "null")
            initList()
            binding.addFab.setOnClickListener { addShowDialog() }
            val listOfShows = shows.shuffled()
            showsAdapter?.updateShows(listOfShows)
            state = State(listOfShows, isDialogOpen)
        }
        if(savedInstanceState != null) {
            Log.d("savedInstanceState","not null")
            initList()
            binding.addFab.setOnClickListener { addShowDialog() }
            state = savedInstanceState.getParcelable(KEY)?: error("error state")
            showsAdapter?.updateShows(state.showList)
            Log.d("isDialogOpen", "$state")
            if (state.isDialogOpen){addShowDialog()}
            }
        showsAdapter?.notifyDataSetChanged()
        binding.addFab.setOnClickListener { addShowDialog() }
    }

 private fun addShowDialog(){

            val dialog = AlertDialog.Builder(requireContext())
          _dialogBinding = FragmentDialogBinding.inflate(LayoutInflater.from(requireContext()))
          isDialogOpen = true
            dialog.setView(dialogBinding.root)
                .setPositiveButton("Add", DialogInterface.OnClickListener { dialog, id ->
                    Log.d("ListFragment", "setPositiveButton")
                    when(type) {
                        SHOW_FILM -> addShowToList(SHOW_FILM)
                        SHOW_SERIES -> addShowToList(SHOW_SERIES)
                        else -> error("Incorrect show type")
                    }
                    showsAdapter?.updateShows(newShows)
                    Log.d("Adapter", "newShows = $newShows")
                    showsAdapter?.notifyItemChanged(0)

                    }
                )
                .setNegativeButton("Cancel") { _, _ ->
                    Toast.makeText(context, "You cancelled adding a show", Toast.LENGTH_SHORT)
                        .show()
                }
                .setOnCancelListener{
                        title=""
                        yearOrSeries=""
                        posterUri=""
                        directorOrCreators=""
                   isDialogOpen = false
                }

            dialogBinding.chooseShowRadioGroup.setOnCheckedChangeListener { _, checkedId ->

                when (checkedId) {
                    R.id.radioFilm -> {
                        dialogBinding.inputShowInfoLayout.isVisible = true
                        dialogBinding.inputDirectorTextView.text = getString(R.string.input_director_s_name)
                        dialogBinding.inputYearTextView.text = getString(R.string.input_the_year_of_release)
                        dialogBinding.directorEditText.hint = getString(R.string.input_director_s_name)
                        dialogBinding.yearEditText.hint = getString(R.string.input_the_year_of_release)
                        dialogListeners()
                        type = SHOW_FILM
                    }

                    R.id.radioSeries -> {
                        dialogBinding.inputShowInfoLayout.isVisible = true
                        dialogBinding.inputDirectorTextView.text = getString(R.string.input_creators_name)
                        dialogBinding.inputYearTextView.text = getString(R.string.input_the_number_of_seasons)
                        dialogBinding.directorEditText.hint = getString(R.string.input_creators_name)
                        dialogBinding.yearEditText.hint = getString(R.string.input_the_number_of_seasons)
                        dialogListeners()
                        type = SHOW_SERIES
                    }
                }
            }
            dialog.create()
                .show()
     isDialogOpen = false
    }


    private fun dialogListeners(){
        Log.d("AddShowDialogFragment", "listeners")
        dialogBinding.titleEditText.addTextChangedListener(object: TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {

                title = dialogBinding.titleEditText.text.toString()

            }

            override fun afterTextChanged(s: Editable?) {

            }

        })

        dialogBinding.directorEditText.addTextChangedListener(object: TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                directorOrCreators = dialogBinding.directorEditText.text.toString()

            }

            override fun afterTextChanged(s: Editable?) {

            }

        })
        dialogBinding.yearEditText.addTextChangedListener(object: TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                yearOrSeries = dialogBinding.yearEditText.text.toString()

            }

            override fun afterTextChanged(s: Editable?) {

            }

        })
        dialogBinding.downloadPosterButton.setOnClickListener(object : View.OnClickListener {
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

        posterUri = dialogBinding.uriTextView.text.toString()

    }

private fun addShowToList(type: Int): List<Shows> {
    Log.d("List fragment", "addShowToList")
    lateinit var newShow: Shows
    when(type) {
        SHOW_FILM -> {
            newShow = Shows.Film(
                title,
                yearOrSeries,
                posterUri,
                directorOrCreators
            )
            Log.d("NewShow", "Film")}
        SHOW_SERIES -> {
            newShow = Shows.Series(
                title,
                yearOrSeries,
                posterUri,
                directorOrCreators
            )
            Log.d("NewShow", "Series")}
        else -> Log.d("newShow", "incorrect type")
    }

    newShows = listOf(newShow) + shows
    state = State(newShows, isDialogOpen)
    Log.d("newShows", "newShow = $newShow, state = $state")
    return newShows
}


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        when (requestCode) {
            PICK_IMAGE -> if (resultCode == Activity.RESULT_OK) {
                val selectedImage: Uri? = data?.data
                dialogBinding.uriTextView.text = selectedImage.toString()
                dialogBinding.uriTextView.isVisible = true
                dialogBinding.downloadPosterButton.isGone = true
            }
        }
    }

    override fun onDetach() {
        super.onDetach()
        _binding = null
        _dialogBinding = null
    }

    private fun initList() {
        Log.d("ListFragment", "fun initList")
        showsAdapter = ShowsAdapter { position -> deleteShow(position) }
        with(binding.showsList) {
            adapter = showsAdapter
            layoutManager = LinearLayoutManager(requireContext())
            setHasFixedSize(true)
        }
    }

    private fun deleteShow(position: Int) {
        Log.d("ListFragment", "fun deleteShow")
        shows = shows.filterIndexed { index, show -> index != position }
        state = State(shows, isDialogOpen)
        Log.d("deleteShow", "${state.showList}")
        showsAdapter?.updateShows(shows)
        showsAdapter?.notifyItemRemoved(position)
        if (shows.isEmpty()){
            binding.emptyListTextView.isVisible = true
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        Log.d("ListFragment", "onDestroyView")
        showsAdapter = null
        _binding = null
        _dialogBinding = null
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        Log.d("onSaveInstanceState", "${state.showList}")
        outState.putParcelable(KEY, state)
        Log.d("onSaveInstanceState", "$isDialogOpen")
    }

    companion object{
        private const val KEY = "key"
        private const val PICK_IMAGE = 150
        private const val SHOW_FILM = 1
        private const val SHOW_SERIES = 0
        private var newShows = listOf<Shows>()
        private var type = -1
        private var title:String = ""
        private var directorOrCreators: String = ""
        private var yearOrSeries: String = ""
        private var posterUri: String = ""

    }

}