package com.smolianinovasiuzanna.hw6_5

fun main() {
    val setOfPersons = mutableSetOf <Person>()

    val person1 = Person (170, 65, "Vasily")
    val person2 = Person (170, 65, "Vasily")

    setOfPersons.add(person1)
    setOfPersons.add(person2)

    println(setOfPersons.size)

    val person3 = Person (150, 45, "Olga")
    setOfPersons.add(person3)

     setOfPersons.forEach{
        print ("Insert the quantity of pets for ${it.name}: ")
        val q = readLine()?.toIntOrNull()?:return
        var i = 0
        while (i < q) {
        it.buyPet()
            i++
        }

}
    setOfPersons.forEach{println(it)}

}
