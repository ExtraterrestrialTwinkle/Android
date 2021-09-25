package com.smolianinovasiuzanna.homework2_5
//homework done

fun main() {
    val firstName = "Smolianinova"
    val lastName = "Siuzanna"
    var height = 170
    val weight = 70F

    var isChild: Boolean = true
    isChild = height < 150 || weight < 40

    var info: String  = """My name is $firstName and my last name is $lastName. 
My height is $height cm and my weight is $weight kg. 
I'm a ${if (isChild) "child" else "grown-up."}"""

    println (info)

    height = 120

    isChild = height < 150 || weight < 40

    info = """My name is $firstName and my last name is $lastName. 
My height is $height cm and my weight is $weight kg. 
I'm a ${if (isChild) "child" else "grown-up."}"""

    println (info)

}
