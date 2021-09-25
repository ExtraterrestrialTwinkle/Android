package com.smolianinovasiuzanna.application

import android.content.Context
import android.content.res.Resources
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import by.kirich1409.viewbindingdelegate.viewBinding
import com.smolianinovasiuzanna.application.adapter.ActorsAdapter
import com.smolianinovasiuzanna.application.adapter.ShowsAdapter
import com.smolianinovasiuzanna.application.databinding.FragmentActorsBinding
import com.smolianinovasiuzanna.application.repository.ActorsRepository
import com.smolianinovasiuzanna.application.viewmodel.ActorsViewModel
import com.smolianinovasiuzanna.application.viewmodel.ViewModelFactory
import jp.wasabeef.recyclerview.animators.ScaleInAnimator
import org.jetbrains.annotations.NotNull
import kotlin.properties.Delegates

class ActorsFragment: Fragment(R.layout.fragment_actors) {
    private val binding by viewBinding(FragmentActorsBinding::bind)
    private val args: ActorsFragmentArgs by navArgs()
    var index by Delegates.notNull<Long>()
    private val actorsViewModel: ActorsViewModel by viewModels(
        factoryProducer = {ViewModelFactory(index)}
    )
    private var actorsAdapter: ActorsAdapter? = null

    override fun onAttach(context: Context) {
        super.onAttach(context)

        if(ActorsResourcesData.resources == null){
            ActorsResourcesData.resources = resources;
        }
    }
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        index = args.id
        return super.onCreateView(inflater, container, savedInstanceState)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Log.d("ListFragment", "onActivityCreated")
        initList()
        observeViewModelState()
    }

    private fun initList() {
        Log.d("actorsFragment", "fun initList")
        actorsAdapter = ActorsAdapter()
        with(binding.actorsList) {
            adapter = actorsAdapter
            val span = 2
            layoutManager = StaggeredGridLayoutManager(span, StaggeredGridLayoutManager.VERTICAL)
            setHasFixedSize(true)
            addItemDecoration(DividerItemDecoration(requireContext(), DividerItemDecoration.VERTICAL))
            addItemDecoration(DividerItemDecoration(requireContext(), DividerItemDecoration.HORIZONTAL))
        }
    }

    private fun observeViewModelState() {
        actorsViewModel.actors
            .observe(viewLifecycleOwner) { newActors -> actorsAdapter?.setImages(newActors)}
    }

    override fun onDestroyView() {
        super.onDestroyView()
        Log.d("ListFragment", "onDestroyView")
        actorsAdapter = null
    }

}

object ActorsResourcesData {
    var resources: Resources? = null
}