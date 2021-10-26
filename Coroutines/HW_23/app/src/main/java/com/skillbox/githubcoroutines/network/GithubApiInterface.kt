package com.skillbox.githubcoroutines.network

import com.skillbox.githubcoroutines.GithubResponse
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.*

interface GithubApiInterface {

    @GET("user")
    suspend fun showUserInfo(): GithubResponse.User

    @GET("repositories")
    fun showRepositories(): Call<List<GithubResponse.UserRepository>>

    @GET("user/starred/{owner}/{repo}")
    fun checkIfStarred(
        @Path("owner")ownerName: String,
        @Path("repo")repository: String
    ): Call<GithubResponse.Starred>

    @PUT("user/starred/{owner}/{repo}")
    suspend fun giveStar(
        @Path("owner")ownerName: String,
        @Path("repo")repository: String
    ): Response<Unit>

    @DELETE("user/starred/{owner}/{repo}")
    suspend fun deleteStar(
        @Path("owner")ownerName: String,
        @Path("repo")repository: String
    ): Response<Unit>

    @GET("user/starred")
    fun getStarredRepos(): Call<List<GithubResponse.UserRepository>>

    @GET("user/following")
    suspend fun getFollowingList(): List<GithubResponse.Following>

}