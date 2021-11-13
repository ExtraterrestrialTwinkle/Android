package com.smolianinovasiuzanna.hw25

fun List<String>.printString(): String{
    var result: String = ""
    for (i in this.indices) {
        result += this[i]+ "\n"
    }
    return result
}