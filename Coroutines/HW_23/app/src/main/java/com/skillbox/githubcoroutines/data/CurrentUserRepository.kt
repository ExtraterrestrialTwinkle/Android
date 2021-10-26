package com.skillbox.githubcoroutines.data

import com.skillbox.githubcoroutines.GithubResponse
import com.skillbox.githubcoroutines.network.Network

class CurrentUserRepository {

    suspend fun showUserInfo(): GithubResponse.User {
        return Network.githubApi.showUserInfo()
    }

    suspend fun showFollowings(): List<GithubResponse.Following>{
        return Network.githubApi.getFollowingList()
    }

}