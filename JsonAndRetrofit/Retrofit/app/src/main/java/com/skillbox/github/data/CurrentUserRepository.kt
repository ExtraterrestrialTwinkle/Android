package com.skillbox.github.data

import android.util.Log
import com.skillbox.github.GithubResponse
import com.skillbox.github.network.Network
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.lang.RuntimeException

class CurrentUserRepository {

    fun showUserInfo(
        onComplete: (GithubResponse.User?) -> Unit,
        onError: (GithubResponse.Error) -> Unit
    ) {
        Network.githubApi.showUserInfo()
            .enqueue(object : Callback<GithubResponse.User> {
                override fun onResponse(
                    call: Call<GithubResponse.User>,
                    response: Response<GithubResponse.User>
                ) {
                    if (response.isSuccessful) {
                        Log.d("Repo", "showUserInfo isSuccessful")
                        onComplete(response.body())
                    } else {
                        Log.e("Repo", "error: not Successful")
                        onError(GithubResponse.Error(RuntimeException("Incorrect status code")))
                    }
                }

                override fun onFailure(
                    call: Call<GithubResponse.User>,
                    t: Throwable
                ) {
                    Log.e("Repo", "${t.message}")
                    onError(GithubResponse.Error(t))
                }

            })
    }

}