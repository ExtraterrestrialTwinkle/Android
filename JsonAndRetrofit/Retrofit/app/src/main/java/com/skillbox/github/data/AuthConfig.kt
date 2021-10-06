package com.skillbox.github.data

import net.openid.appauth.ResponseTypeValues

object AuthConfig {

    const val AUTH_URI = "https://github.com/login/oauth/authorize"
    const val TOKEN_URI = "https://github.com/login/oauth/access_token"
    const val RESPONSE_TYPE = ResponseTypeValues.CODE
    const val SCOPE = "user,repo"

    const val CLIENT_ID = "6614a14fd151097eeda5"
    const val CLIENT_SECRET = "515d4950c1b23ee1a6bc5430e9fed2aab63e2d37"
    const val CALLBACK_URL = "skillbox://skillbox.ru/callback"
}