package com.skillbox.github.data

import android.util.Log
import com.skillbox.github.GithubResponse
import com.skillbox.github.network.Network
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.lang.Exception

class DetailInfoRepository (
    val owner: String,
    val repositoryName: String,
    val onError: (GithubResponse.Error) -> Unit,
    val isStarred: (Boolean) -> Unit
        ){

    fun showInfo(){
        Network.githubApi.checkIfStarred(owner, repositoryName)
            .enqueue(object: Callback<GithubResponse.Starred> {
                override fun onResponse(
                    call: Call<GithubResponse.Starred>,
                    response: Response<GithubResponse.Starred>
                ) {
                    when(response.code()){
                       204 -> {
                           isStarred(true)
                       }
                       304 -> onError(GithubResponse.Error(Exception("Not Modified")))
                       401 -> onError(GithubResponse.Error(Exception("Unauthorized")))
                       403 -> onError(GithubResponse.Error(Exception("Forbidden")))
                       404 -> {
                           isStarred(false)
                       }
                       else -> onError(GithubResponse.Error(Exception("Unexpectable response code")))
                    }

                }
                override fun onFailure(call: Call<GithubResponse.Starred>, t: Throwable) {
                    Log.e("Repo", "${t.message}")
                    onError(GithubResponse.Error(t))
                }
            })
        Log.d("Repos", "$isStarred")
    }

    fun giveStar(){
        Network.githubApi.giveStar(owner, repositoryName)
            .enqueue(object: Callback<GithubResponse.Starred>{
                override fun onResponse(
                    call: Call<GithubResponse.Starred>,
                    response: Response<GithubResponse.Starred>
                ) {
                    when(response.code()){
                        204 -> {
                            isStarred(true)
                        }
                        304 -> onError(GithubResponse.Error(Exception("Not Modified")))
                        401 -> onError(GithubResponse.Error(Exception("Unauthorized")))
                        403 -> onError(GithubResponse.Error(Exception("Forbidden")))
                        404 -> onError(GithubResponse.Error(Exception("Resource not found")))
                        else -> onError(GithubResponse.Error(Exception("Unexpectable response code")))
                    }
                }

                override fun onFailure(call: Call<GithubResponse.Starred>, t: Throwable) {
                    Log.e("Repo", "${t.message}")
                    onError(GithubResponse.Error(t))
                }
            })
    }

    fun deleteStar(){
        Network.githubApi.deleteStar(owner, repositoryName)
            .enqueue(object: Callback<GithubResponse.Starred>{
                override fun onResponse(
                    call: Call<GithubResponse.Starred>,
                    response: Response<GithubResponse.Starred>
                ) {
                    when(response.code()){
                        204 -> {
                            isStarred(false)
                        }
                        304 -> onError(GithubResponse.Error(Exception("Not Modified")))
                        401 -> onError(GithubResponse.Error(Exception("Unauthorized")))
                        403 -> onError(GithubResponse.Error(Exception("Forbidden")))
                        404 -> onError(GithubResponse.Error(Exception("Resource not found")))
                        else -> onError(GithubResponse.Error(Exception("Unexpectable response code")))
                    }
                }

                override fun onFailure(call: Call<GithubResponse.Starred>, t: Throwable) {
                    Log.e("Repo", "${t.message}")
                    onError(GithubResponse.Error(t))
                }
            })

    }
}
