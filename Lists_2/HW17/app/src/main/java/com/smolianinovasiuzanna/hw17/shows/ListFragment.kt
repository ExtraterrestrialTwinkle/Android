package com.smolianinovasiuzanna.hw17.shows


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
    import com.smolianinovasiuzanna.hw17.R
    import com.smolianinovasiuzanna.hw17.Shows
    import com.smolianinovasiuzanna.hw17.State
    import com.smolianinovasiuzanna.hw17.databinding.FragmentDialogBinding
    import com.smolianinovasiuzanna.hw17.databinding.FragmentListBinding
    import jp.wasabeef.recyclerview.animators.ScaleInAnimator
    import kotlin.random.Random

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

            Log.d("MainFragment", "onCreateView")
            return binding.root

        }

        override fun onAttach(context: Context) {
            super.onAttach(context)
            initShows(context)
            Log.d("ListFragment", "onAttach")
        }
        private lateinit var shows: List<Shows>

        fun initShows(context: Context) {
            Log.d("ListFragment", "fun initShows")
            shows = listOf(
                Shows.Film(
                    id = 1,
                    title = context.getString(R.string.the_shawshenk_redemption),
                    year = "1994",
                    posterLink = "https://movieposters2.com/images/1374353-b.jpg",
                    director = context.getString(R.string.frank_darabont)

                ),
                Shows.Film(
                    id = 2,
                    title = context.getString(R.string.the_godfather),
                    year = "1972",
                    posterLink = "https://movieposters2.com/images/694423-b.jpg",
                    director = context.getString(R.string.francis_ford_coppola)

                ),
                Shows.Film(
                    id = 3,
                    title = context.getString(R.string.the_dark_knight),
                    year = "2008",
                    posterLink = "https://movieposters2.com/images/653694-b.jpg",
                    director = context.getString(R.string.christopher_nolan)

                ),
                Shows.Film(
                    id = 4,
                    title = context.getString(R.string.schindlers_list),
                    year = "1993",
                    posterLink = "https://movieposters2.com/images/657002-b.jpg",
                    director = context.getString(R.string.steven_spielberg)

                ),
                Shows.Film(
                    id = 5,
                    title = context.getString(R.string.pulp_fiction),
                    year = "1994",
                    posterLink = "https://movieposters2.com/images/739665-b.jpg",
                    director = context.getString(R.string.quentin_tarantino)

                ),
                Shows.Series(
                    id = 6,
                    title = context.getString(R.string.the_walking_dead),
                    posterLink = "https://movieposters2.com/images/1148244-b.jpg",
                    creators = context.getString(R.string.the_walking_dead_creators),
                    seasons = "11"

                ),
                Shows.Series(
                    id = 7,
                    title = context.getString(R.string.friends),
                    posterLink = "https://movieposters2.com/images/645471-b.jpg",
                    creators = context.getString(R.string.friends_creators),
                    seasons = "10"

                ),
                Shows.Series(
                    id = 8,
                    title = context.getString(R.string.greys_anatomy),
                    posterLink = "https://movieposters2.com/images/1199467-b.jpg",
                    creators = context.getString(R.string.greys_anatomy_creators),
                    seasons = "17"

                ),
                Shows.Series(
                    id = 9,
                    title = context.getString(R.string.house_m_d),
                    posterLink = "https://movieposters2.com/images/646575-b.jpg",
                    creators = context.getString(R.string.house_m_d_creators),
                    seasons = "8"

                ),
                Shows.Series(
                    id = 10,
                    title = context.getString(R.string.bones),
                    posterLink = "https://movieposters2.com/images/648896-b.jpg",
                    creators = context.getString(R.string.bones_creators),
                    seasons = "12"

                )
            )
        }

        private var showsAdapter: ShowsAdapter? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        with(binding) {
            Log.d("ListFragment", "onActivityCreated")

            if (savedInstanceState == null) {
                Log.d("savedInstanceState", "null")
                initList()
                addFab.setOnClickListener { addShowDialog() }
                val listOfShows = shows.shuffled()
                showsAdapter?.items = listOfShows
                state = State(listOfShows, isDialogOpen)
            }
            if (savedInstanceState != null) {
                Log.d("savedInstanceState", "not null")
                initList()
                addFab.setOnClickListener { addShowDialog() }
                state = savedInstanceState.getParcelable(KEY) ?: error("error state")
                showsAdapter?.items = state.showList
                Log.d("isDialogOpen", "$state")
                if (state.isDialogOpen) {
                    addShowDialog()
                }
            }
            showsAdapter?.notifyDataSetChanged()
            addFab.setOnClickListener { addShowDialog() }
        }
    }

        private fun addShowDialog(){

            val dialog = AlertDialog.Builder(requireContext())
            _dialogBinding = FragmentDialogBinding.inflate(LayoutInflater.from(requireContext()))
            isDialogOpen = true
            with(dialogBinding) {
                dialog.setView(root)
                    .setPositiveButton("Add", DialogInterface.OnClickListener { dialog, id ->
                        Log.d("ListFragment", "setPositiveButton")
                        when (type) {
                            SHOW_FILM -> addShowToList(
                                SHOW_FILM
                            )
                            SHOW_SERIES -> addShowToList(
                                SHOW_SERIES
                            )
                            else -> error("Incorrect show type")
                        }
                        showsAdapter?.items = newShows
                        Log.d("Adapter", "newShows = $newShows")
                        showsAdapter?.notifyItemChanged(0)

                    }
                    )
                    .setNegativeButton("Cancel") { _, _ ->
                        Toast.makeText(context, "You cancelled adding a show", Toast.LENGTH_SHORT)
                            .show()
                    }
                    .setOnCancelListener {
                        title = ""
                        yearOrSeries = ""
                        posterUri = ""
                        directorOrCreators = ""
                        isDialogOpen = false
                    }

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
                            type = SHOW_FILM
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
                           type = SHOW_SERIES
                        }
                    }
                }
                dialog.create()
                    .show()
                isDialogOpen = false
            }
        }


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
                downloadPosterButton.setOnClickListener(object :
                    View.OnClickListener {
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

                posterUri = uriTextView.text.toString()
            }
        }

        private fun addShowToList(type: Int): List<Shows> {
            Log.d("List fragment", "addShowToList")
            lateinit var newShow: Shows
            when(type) {
                SHOW_FILM -> {
                    newShow = Shows.Film(
                        id = Random.nextLong(),
                        title,
                        yearOrSeries,
                        posterUri,
                        directorOrCreators
                    )
                    Log.d("NewShow", "Film")}
                SHOW_SERIES -> {
                    newShow = Shows.Series(
                        id = Random.nextLong(),
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

        with(dialogBinding) {
            when (requestCode) {
                PICK_IMAGE -> if (resultCode == Activity.RESULT_OK) {
                    val selectedImage: Uri? = data?.data
                    uriTextView.text = selectedImage.toString()
                    uriTextView.isVisible = true
                    downloadPosterButton.isGone = true
                }
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
                itemAnimator = ScaleInAnimator()
            }
        }

        private fun deleteShow(position: Int) {
            Log.d("ListFragment", "fun deleteShow")
            shows = shows.filterIndexed { index, show -> index != position }
            state = State(shows, isDialogOpen)
            Log.d("deleteShow", "${state.showList}")
            showsAdapter?.items = shows
            //showsAdapter?.notifyItemRemoved(position)
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
            private var id: Long = -1
            private var title:String = ""
            private var directorOrCreators: String = ""
            private var yearOrSeries: String = ""
            private var posterUri: String = ""

        }

    }
