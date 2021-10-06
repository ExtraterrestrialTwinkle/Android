package com.skillbox.github.network

import com.skillbox.github.GithubResponse
import retrofit2.Call
import retrofit2.http.*

interface GithubApiInterface {

    @GET("user")
    fun showUserInfo(): Call<GithubResponse.User>

    @GET("repositories")
    fun showRepositories(): Call<List<GithubResponse.UserRepository>>

    @GET("user/starred/{owner}/{repo}")
    fun checkIfStarred(
        @Path("owner")ownerName: String,
        @Path("repo")repository: String
    ): Call<GithubResponse.Starred>

    @PUT("user/starred/{owner}/{repo}")
    fun giveStar(
        @Path("owner")ownerName: String,
        @Path("repo")repository: String
    ): Call<GithubResponse.Starred>

    @DELETE("user/starred/{owner}/{repo}")
    fun deleteStar(
        @Path("owner")ownerName: String,
        @Path("repo")repository: String
    ): Call<GithubResponse.Starred>

    @GET("user/starred")
    fun getStarredRepos(): Call<List<GithubResponse.UserRepository>>

}