package com.skillbox.githubcoroutines.ui.current_user

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.skillbox.githubcoroutines.GithubResponse
import com.skillbox.githubcoroutines.R
import com.skillbox.githubcoroutines.databinding.FragmentCurrentUserBinding

class CurrentUserFragment: Fragment(R.layout.fragment_current_user) {

    private var _binding: FragmentCurrentUserBinding? = null
    private val binding: FragmentCurrentUserBinding get() = _binding!!
    private val viewModel: CurrentUserViewModel by viewModels()
    private var followingsAdapter: CurrentUserAdapter? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentCurrentUserBinding.inflate(layoutInflater, container, false)
       return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Log.d("onViewCreated", "CurrentUserFragment")
        initList()
        bindViewModel()
    }

    private fun initList(){
        followingsAdapter = CurrentUserAdapter()
        with(binding.followingsRecyclerView) {
            adapter = followingsAdapter
            layoutManager = LinearLayoutManager(requireContext())
            setHasFixedSize(true)
            addItemDecoration(DividerItemDecoration(requireContext(), DividerItemDecoration.VERTICAL))
        }
    }

    private fun bindViewModel() {
        Log.d("CurrentUserFragment", "bindViewModel")
        viewModel.showUserInfo()
        viewModel.user.observe(viewLifecycleOwner) { user -> bindInfo(user)}
        viewModel.followings.observe(viewLifecycleOwner){followingsList ->
            followingsAdapter?.setList(followingsList)
        }
    }

    private fun bindInfo(user: GithubResponse.User) {
        with(binding) {
            with(user) {
                Glide.with(this@CurrentUserFragment)
                    .load(avatarUrl)
                    .error(R.drawable.ic_error)
                    .placeholder(R.drawable.ic_image)
                    .into(userAvatarImageView)

                userNameTextView.text = (login + " " + name)
                userInfoTextView.text = """
                    company: ${company}
                    e-mail: ${email}
                    location: ${location}
                    public repositories: ${numberOfPublicRepositories}
                    followers: ${numberOfFollowers}
                    following: ${numberOfFollowing}
                    
                    ${bio}
                """.trimIndent()
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
        followingsAdapter = null
    }

}