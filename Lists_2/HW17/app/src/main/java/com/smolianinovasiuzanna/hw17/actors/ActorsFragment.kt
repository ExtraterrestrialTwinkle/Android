package com.smolianinovasiuzanna.hw17.actors

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.smolianinovasiuzanna.hw17.Actors
import com.smolianinovasiuzanna.hw17.ActorsState
import com.smolianinovasiuzanna.hw17.ItemOffsetDecoration
import com.smolianinovasiuzanna.hw17.R
import com.smolianinovasiuzanna.hw17.databinding.FragmentActorsBinding
import withArguments


class ActorsFragment: Fragment(){
    private var _binding: FragmentActorsBinding? = null
    private val binding: FragmentActorsBinding get() = _binding!!
    var state = ActorsState(listOf())

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentActorsBinding.inflate(layoutInflater, container, false)

        Log.d("MainFragment", "onCreateView")
        return binding.root

    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        val index = arguments?.getInt(
            INDEX,
            INDEX_DEF
        ) ?: INDEX_DEF

        if (index != INDEX_DEF) {
            initActors(context, index)

        }else Log.d("initActors", "no list")

        Log.d("ActorsFragment", "onAttach")
    }
    private lateinit var actors: ArrayList<Actors>

    private fun initActors(context: Context, index: Int) {
        Log.d("ListFragment", "fun initShows")
        val shawshenk = context.resources.getStringArray(R.array.shawshenk_actors)
        val shawshenkPhotos = context.resources.getStringArray(R.array.shawshenk_photos)
        val godfather = context.resources.getStringArray(R.array.godfather_actors)
        val godfatherPhotos = context.resources.getStringArray(R.array.godfather_photos)
        val darkKnight = context.resources.getStringArray(R.array.dark_knight_actors)
        val darkKnightPhotos = context.resources.getStringArray(R.array.dark_knight_photos)
        val schindler = context.resources.getStringArray(R.array.schindler_actors)
        val schindlerPhotos = context.resources.getStringArray(R.array.schindler_actors)
        val pulpFiction = context.resources.getStringArray(R.array.pulp_fiction_actors)
        val pulpFictionPhotos = context.resources.getStringArray(R.array.pulp_fiction_photos)
        val walkingDead = context.resources.getStringArray(R.array.walking_dead_actors)
        val walkingDeadPhotos = context.resources.getStringArray(R.array.walking_dead_photos)
        val friends = context.resources.getStringArray(R.array.friends_actors)
        val friendsPhotos = context.resources.getStringArray(R.array.friends_photos)
        val greys = context.resources.getStringArray(R.array.greys_anatomy_actors)
        val greysPhotos = context.resources.getStringArray(R.array.greys_anatomy_photos)
        val house = context.resources.getStringArray(R.array.house_actors)
        val housePhotos = context.resources.getStringArray(R.array.house_photos)
        val bones = context.resources.getStringArray(R.array.bones_actors)
        val bonesPhotos = context.resources.getStringArray(R.array.bones_photos)
        actors = arrayListOf()
        when (index) {
            1 -> {
                for (i in shawshenk.indices) {
                    val actor = Actors(
                            id = i+1,
                            name = shawshenk[i],
                            photoLink = shawshenkPhotos[i]
                        )
                    actors.add(actor)
                }
            }

            2 -> {
                for (i in godfather.indices) {
                    val actor = Actors(
                            id = i+1,
                            name = godfather[i],
                            photoLink = godfatherPhotos[i]
                    )
                    actors.add(actor)
                }
            }
            3 -> {
                for (i in darkKnight.indices) {
                    val actor = Actors(
                        id = i + 1,
                        name = darkKnight[i],
                        photoLink = darkKnightPhotos[i]
                    )
                    actors.add(actor)
                }
            }
            4 -> {
                for (i in schindler.indices) {
                    val actor = Actors(
                            id = i+1,
                            name = schindler[i],
                            photoLink = schindlerPhotos[i]
                    )
                    actors.add(actor)
                }
            }
            5 -> {
                for (i in pulpFiction.indices) {
                    val actor = Actors(
                            id = i+1,
                            name = pulpFiction[i],
                            photoLink = pulpFictionPhotos[i]
                    )
                    actors.add(actor)
                }
            }
            6 -> {
                for (i in walkingDead.indices) {
                    val actor = Actors(
                            id = i+1,
                            name = walkingDead[i],
                            photoLink = walkingDeadPhotos[i]
                    )
                    actors.add(actor)
                }
            }
            7 -> {
                for (i in friends.indices) {
                    val actor = Actors(
                            id = i+1,
                            name = friends[i],
                            photoLink = friendsPhotos[i]
                    )
                    actors.add(actor)
                }
            }
            8 -> {
                for (i in greys.indices) {
                    val actor = Actors(
                            id = i+1,
                            name = greys[i],
                            photoLink = greysPhotos[i]
                    )
                    actors.add(actor)
                }
            }
            9 -> {
                for (i in house.indices) {
                    val actor = Actors(
                            id = i+1,
                            name = house[i],
                            photoLink = housePhotos[i]
                    )
                    actors.add(actor)
                }
            }
            10 -> {
                for (i in bones.indices) {
                    val actor = Actors(
                            id = i+1,
                            name = bones[i],
                            photoLink = bonesPhotos[i]
                    )
                    actors.add(actor)
                }
            }
        }
    }

    private var actorsAdapter: ActorsAdapter? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Log.d("ListFragment", "onActivityCreated")

        if(savedInstanceState == null) {
            Log.d("savedInstanceState", "null")
            initList()
            val listOfActors = actors
            actorsAdapter?.setImages(listOfActors)
            state = ActorsState(listOfActors)
        }
        if(savedInstanceState != null) {
            Log.d("savedInstanceState","not null")
            initList()
            state = savedInstanceState.getParcelable(KEY)?: error("error state")
            actorsAdapter?.setImages(state.actorsList)
            Log.d("isDialogOpen", "$state")
        }
        actorsAdapter?.notifyDataSetChanged()
    }

    override fun onDetach() {
        super.onDetach()
        _binding = null
    }

    private fun initList() {
        Log.d("actorsFragment", "fun initList")
        actorsAdapter = ActorsAdapter{position -> onClick(position)}
        with(binding.actorsList) {
            adapter = actorsAdapter
            val span = 2
            layoutManager = StaggeredGridLayoutManager(span, StaggeredGridLayoutManager.VERTICAL)
            setHasFixedSize(true)
            addItemDecoration(DividerItemDecoration(requireContext(), DividerItemDecoration.VERTICAL))
            addItemDecoration(DividerItemDecoration(requireContext(), DividerItemDecoration.HORIZONTAL))
            addItemDecoration(ItemOffsetDecoration(requireContext()))
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        Log.d("ListFragment", "onDestroyView")
        actorsAdapter = null
        _binding = null
    }

    private fun onClick(position: Int) {
        Log.d("ListActors", "fun ShowInfo")
        Toast.makeText(
                    requireContext(),
                    actors[position].name,
                    Toast.LENGTH_SHORT
                ).show()
    }


    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        Log.d("onSaveInstanceState", "${state.actorsList}")
        outState.putParcelable(KEY, state)
    }

    companion object{
        private const val KEY = "key"
        private const val INDEX = "index"
        private const val INDEX_DEF = -1

        fun newInstance(index: Int): ActorsFragment {
            return ActorsFragment().withArguments {
                putInt (INDEX, index)
            }
        }
    }

}
