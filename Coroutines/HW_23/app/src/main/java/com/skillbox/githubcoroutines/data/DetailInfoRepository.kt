package com.skillbox.githubcoroutines.data

import android.util.Log
import com.skillbox.githubcoroutines.GithubResponse
import com.skillbox.githubcoroutines.network.Network
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlinx.coroutines.withContext
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.lang.Exception
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException

class DetailInfoRepository (
    val owner: String,
    val repositoryName: String
        ){

    suspend fun showInfo(): Boolean{
        return withContext(Dispatchers.IO){
            suspendCancellableCoroutine { continuation ->
                    val call = Network.githubApi.checkIfStarred(owner, repositoryName)
                        call.enqueue(object: Callback<GithubResponse.Starred> {
                            override fun onResponse(
                                call: Call<GithubResponse.Starred>,
                                response: Response<GithubResponse.Starred>
                            ) {
                                Log.d("OnResponse", response.code().toString())
                                when(response.code()){
                                    204 -> continuation.resume(true)
                                    304 -> continuation.resumeWithException(Exception("Not Modified"))
                                    401 -> continuation.resumeWithException(Exception("Unauthorized"))
                                    403 -> continuation.resumeWithException(Exception("Forbidden"))
                                    404 -> continuation.resume(false)
                                    else -> continuation.resumeWithException(Exception("Unexpectable response code"))
                                }
                            }
                            override fun onFailure(call: Call<GithubResponse.Starred>, t: Throwable) {
                                Log.e("Repo", "${t.message}")
                                continuation.resumeWithException(t)
                            }
                        })
                    Log.d("Repos", continuation.toString())

                continuation.invokeOnCancellation {
                    call.cancel()
                }
            }
        }
    }

    suspend fun giveStar(): Boolean {
        Log.d("Repo give", "true")
        return Network.githubApi.giveStar(owner, repositoryName).isSuccessful
    }

    suspend fun deleteStar(): Boolean{
        Log.d("Repo delete", "false")
        return Network.githubApi.deleteStar(owner, repositoryName).isSuccessful.not()
    }

}
