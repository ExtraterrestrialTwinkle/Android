package com.smolianinovasiuzanna.hw17.posters

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.smolianinovasiuzanna.hw17.*
import com.smolianinovasiuzanna.hw17.databinding.FragmentPostersBinding


class PostersFragment: Fragment(){
    private var _binding: FragmentPostersBinding? = null
    private val binding: FragmentPostersBinding get() = _binding!!
    var state = PosterState(listOf())

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentPostersBinding.inflate(layoutInflater, container, false)

        Log.d("PostersFragment", "onCreateView")
        return binding.root

    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        initPosters(context)
        Log.d("ListHorizFragment", "onAttach")
    }

    private lateinit var posters: List<Posters>

    private fun initPosters(context: Context) {
        Log.d("ListHorizFragment", "fun initPosters")
        posters = listOf(
            Posters(
                id = 1,
                title = context.getString(R.string.the_shawshenk_redemption),
                posterLink = "https://movieposters2.com/images/1374353-b.jpg"
            ),
            Posters(
                id = 2,
                title = context.getString(R.string.the_godfather),
                posterLink = "https://movieposters2.com/images/694423-b.jpg"
            ),
            Posters(
                id = 3,
                title = context.getString(R.string.the_dark_knight),
                posterLink = "https://movieposters2.com/images/653694-b.jpg"
            ),
            Posters(
                id = 4,
                title = context.getString(R.string.schindlers_list),
                posterLink = "https://movieposters2.com/images/657002-b.jpg"
            ),
            Posters(
                id = 5,
                title = context.getString(R.string.pulp_fiction),
                posterLink = "https://movieposters2.com/images/739665-b.jpg"
            ),
            Posters(
                id = 6,
                title = context.getString(R.string.the_walking_dead),
                posterLink = "https://movieposters2.com/images/1148244-b.jpg"
            ),
            Posters(
                id = 7,
                title = context.getString(R.string.friends),
                posterLink = "https://movieposters2.com/images/645471-b.jpg"
            ),
            Posters(
                id = 8,
                title = context.getString(R.string.greys_anatomy),
                posterLink = "https://movieposters2.com/images/1199467-b.jpg"
            ),
            Posters(
                id = 9,
                title = context.getString(R.string.house_m_d),
                posterLink = "https://movieposters2.com/images/646575-b.jpg"
            ),
            Posters(
                id = 10,
                title = context.getString(R.string.bones),
                posterLink = "https://movieposters2.com/images/648896-b.jpg"
            )
        )
    }

    private var postersAdapter: PostersAdapter? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        with(binding) {
            Log.d("ListHorizFragment", "onViewCreated")

            if (savedInstanceState == null) {
                Log.d("savedInstanceState", "null")
                initList()
                val listOfPosters = posters
                postersAdapter?.setImages(listOfPosters)
                state = PosterState(listOfPosters)
            }
            if (savedInstanceState != null) {
                Log.d("savedInstanceState", "not null")
                initList()
                state = savedInstanceState.getParcelable(KEY) ?: error("error state")
                postersAdapter?.setImages(state.posterList)
                Log.d("isDialogOpen", "$state")
            }
            postersAdapter?.notifyDataSetChanged()
        }
    }


    private fun initList() {
        Log.d("ListHorizFragment", "fun initList")
        postersAdapter = PostersAdapter { position -> showInfo(position) }
        with(binding.postersList) {
            adapter = postersAdapter
            layoutManager = LinearLayoutManager(requireContext())
                .apply{orientation = LinearLayoutManager.HORIZONTAL}
            setHasFixedSize(true)
        }
    }

    private fun showInfo(position: Int) {
        Log.d("ListHoriz", "fun ShowInfo")
        val index = posters[position].id
        if(parentFragment is ItemSelectListener){
            (parentFragment as ItemSelectListener).onItemInListClicked(index)
        } else{
            Log.d("ListFragment", "parent is null")
        }

//        Проверка работы нажатия на элемент
//                Toast.makeText(
//                    requireContext(),
//                    "poster " + posters[position].title,
//                    Toast.LENGTH_SHORT
//                ).show()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        Log.d("ListHorizFragment", "onDestroyView")
        postersAdapter = null
        _binding = null
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        Log.d("onSaveInstanceState", "${state.posterList}")
        outState.putParcelable(KEY, state)

    }

    companion object {
        private const val KEY = "key"
    }
}

