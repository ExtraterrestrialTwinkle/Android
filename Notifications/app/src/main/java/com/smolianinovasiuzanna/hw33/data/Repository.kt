package com.smolianinovasiuzanna.hw33.data

import com.google.firebase.messaging.FirebaseMessaging
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.suspendCoroutine


class Repository() {

    suspend fun receiveToken(): String? = suspendCoroutine { continuation ->
        FirebaseMessaging.getInstance().token
            .addOnSuccessListener { token ->
                continuation.resume(token)
            }
            .addOnFailureListener { exception ->
                continuation.resumeWithException(exception)
            }
            .addOnCanceledListener {
                continuation.resume(null)
            }
    }

}