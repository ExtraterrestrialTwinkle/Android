package com.smolianinovasiuzanna.hw8_4

import kotlin.random.Random

fun main() {
  println( result())

}

fun result(): Result<Int, String> {
    return if (Random.nextBoolean()){
        Result.Success(5)
    } else Result.Error ("Error")
}

sealed class Result <out T, R> () {


    data class Success<T, R>(val result: T) : Result<T, R>() {

    }

    data class Error<T, R>(val error: R) : Result<T, R>() {

    }
}


