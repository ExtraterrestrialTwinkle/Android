package com.smolianinovasiuzanna.hw6_5

import kotlin.random.Random
import com.smolianinovasiuzanna.hw6_5.DelegatePets as DelegatePets

open class Person(
    var height: Int,
    var weight: Int,
    var name: String,

){
    val pets: HashSet <Animal> by DelegatePets(hashSetOf())

   public fun buyPet () {
       val energy = Random.nextInt(5)
       val weight = Random.nextInt(10)
       var name = "pet"
       when ((1..5).random()) {
           1 -> name = "dog"
           2 -> name = "cat"
           3 -> name = "snake"
           4 -> name = "parrot"
           5 -> name = "fish"
       }
       val pet = Animal (energy, weight, name)
       this.pets.add(pet)
       println ("Pet $name was bought: weight = $weight; energy = $energy")
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Person

        if (height != other.height) return false
        if (weight != other.weight) return false
        if (name != other.name) return false
        if (pets != other.pets) return false

        return true
    }

    override fun hashCode(): Int {
        var result = height.hashCode()
        result = 31 * result + weight.hashCode()
        result = 31 * result + name.hashCode()
        result = 31 * result + pets.hashCode()
        return result
    }

    override fun toString(): String {
        return "Person: $name; height=$height cm, weight=$weight kg, pets: $pets)"
    }


}