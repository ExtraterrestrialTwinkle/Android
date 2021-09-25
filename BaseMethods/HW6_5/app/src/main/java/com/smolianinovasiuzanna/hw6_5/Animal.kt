package com.smolianinovasiuzanna.hw6_5

public data class Animal(
    val energy: Int,
    val weight: Int,
    val name: String
) {
    override fun toString(): String {
        return this.name
    }
}

