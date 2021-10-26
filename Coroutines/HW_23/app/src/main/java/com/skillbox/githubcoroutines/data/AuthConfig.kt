package com.skillbox.githubcoroutines.data

import net.openid.appauth.ResponseTypeValues

object AuthConfig {

    const val AUTH_URI = "https://github.com/login/oauth/authorize"
    const val TOKEN_URI = "https://github.com/login/oauth/access_token"
    const val RESPONSE_TYPE = ResponseTypeValues.CODE
    const val SCOPE = "user,repo"

    const val CLIENT_ID = "YOUR_CLIENT_ID"
    const val CLIENT_SECRET = "YOUR_CLIENT_SECRET"
    const val CALLBACK_URL = "skillbox://skillbox.ru/callback"
}