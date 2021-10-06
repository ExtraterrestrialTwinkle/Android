package com.skillbox.github.data

import android.util.Log
import com.skillbox.github.GithubResponse
import com.skillbox.github.network.Network
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.lang.RuntimeException

class StarredRepositoriesRepository {
    fun showStarredRepositories(
        onComplete: (List<GithubResponse.UserRepository>) -> Unit,
        onError: (GithubResponse.Error) -> Unit){
        Network.githubApi.getStarredRepos()
            .enqueue(object: Callback<List<GithubResponse.UserRepository>> {
                override fun onResponse(
                    call: Call<List<GithubResponse.UserRepository>>,
                    response: Response<List<GithubResponse.UserRepository>>
                ) {
                    if (response.isSuccessful) {
                        Log.d("Repo", "showUserInfo isSuccessful")
                        onComplete(response.body()?.toList().orEmpty())
                    } else {
                        Log.e("Repo", "error: not Successful")
                        onError(GithubResponse.Error(RuntimeException("Incorrect status code")))
                    }
                }

                override fun onFailure(
                    call: Call<List<GithubResponse.UserRepository>>,
                    t: Throwable
                ) {
                    Log.e("Repo", "${t.message}")
                    onError(GithubResponse.Error(t))
                }
            })
    }
}