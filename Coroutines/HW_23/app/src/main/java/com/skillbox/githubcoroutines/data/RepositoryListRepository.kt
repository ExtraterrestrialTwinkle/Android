package com.skillbox.githubcoroutines.data

import android.util.Log
import com.skillbox.githubcoroutines.GithubResponse
import com.skillbox.githubcoroutines.network.Network
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlin.RuntimeException
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.suspendCoroutine

class RepositoryListRepository {
    suspend fun showUserRepositories(): List<GithubResponse.UserRepository>{
        return withContext(Dispatchers.Default){
            suspendCoroutine {continuation ->
                val response = Network.githubApi.showRepositories().execute()
                if(response.isSuccessful) {
                    continuation.resume(response.body()?.toList().orEmpty())
                    Log.d("response", "${response.body()?.toList()}")
                }else{
                    continuation.resumeWithException(RuntimeException("response does not success"))
                }
            }
        }
    }
}