package com.skillbox.github.ui.repository_detail

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
import com.skillbox.github.R
import com.skillbox.github.databinding.FragmentDetailInfoBinding
import kotlin.properties.Delegates

class DetailInfoFragment: Fragment(R.layout.fragment_detail_info) {

    private var _binding: FragmentDetailInfoBinding? = null
    private val binding: FragmentDetailInfoBinding get() = _binding!!
    private val args: DetailInfoFragmentArgs by navArgs()
    var owner by Delegates.notNull<String>()
    var reposName by Delegates.notNull<String>()
    var reposDescription by Delegates.notNull<String>()
    val viewModel: DetailInfoViewModel by viewModels(
        factoryProducer = {ViewModelFactory(owner, reposName, reposDescription)}
    )

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentDetailInfoBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        owner = args.owner
        reposName = args.repositoryName
        reposDescription = args.repositoryDescription
        bindViewModel()
        setInfo()
    }

    private fun bindViewModel(){

        viewModel.showIsStarred()
        viewModel.isStarredRepos.observe(viewLifecycleOwner){ isStarred ->
            showButton(isStarred)
            binding.starButton.setOnClickListener{
                when(isStarred){
                    false -> {
                        viewModel.giveStar()
                        binding.starButton.setImageResource(R.drawable.white_star)
                    }
                    true -> {
                        viewModel.deleteStar()
                        binding.starButton.setImageResource(R.drawable.black_star)
                    }
                }
            }

        }

    }

    private fun setInfo(){
        with(binding){
            nameTextView.text = owner
            infoTextView.text = reposDescription
        }
    }

    private fun showButton(isStarred: Boolean) {
        when (isStarred) {
            false -> binding.starButton.setImageResource(R.drawable.white_star)
            true -> binding.starButton.setImageResource(R.drawable.black_star)
        }
    }

}