package com.smolianinovasiuzanna.hw8_4

fun main() {
    val list1 = listOf(1.2, 4.3, 6, 3.0, 0, 18)
    println(genericFunction(list1))



}

fun <T: Number> genericFunction (list: List <T>): List<T> {

    return list.filter{ it.toInt() % 2 == 0 }

}