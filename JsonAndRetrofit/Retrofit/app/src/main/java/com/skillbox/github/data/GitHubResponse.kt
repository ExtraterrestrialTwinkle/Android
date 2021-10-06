package com.skillbox.github

import com.skillbox.github.data.OwnerInfo
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass


sealed class GithubResponse{

    @JsonClass(generateAdapter = true)
    data class User(
        @Json(name = "id") val id: String,
        @Json(name = "login") val login: String? = "",
        @Json(name = "name") val name: String? = "",
        @Json(name = "avatar_url") val avatarUrl: String? = "",
        @Json(name = "repos_url") val repositoriesUrl: String? = "",
        @Json(name = "company") val company: String? = "",
        @Json(name = "location") val location: String? = "",
        @Json(name = "email") val email: String? = "",
        @Json(name = "bio") val bio: String? = "",
        @Json(name = "public_repos") val numberOfPublicRepositories: Int? = -1,
        @Json(name = "followers") val numberOfFollowers: Int? = -1,
        @Json(name = "following") val numberOfFollowing: Int? = -1
    )

    @JsonClass(generateAdapter = true)
    data class UserRepository(
        @Json(name = "id") val id: Int,
        @Json(name = "name") val repositoryName: String? = "",
        @Json(name = "html_url") val repositoryUrl: String? = "",
        @Json(name = "description") val repositoryDescription: String? = "",
        @Json(name = "owner") val owner: OwnerInfo
    )

    @JsonClass(generateAdapter = true)
    data class Starred(
        val isStarred: Boolean
    )

    data class Error(val error: Throwable) {
        override fun toString(): String {
            return "${error.message}"
        }
    }
}
