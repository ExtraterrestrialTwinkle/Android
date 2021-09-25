package com.smolianinovasiuzanna.hw5_6
//Homework is Done!!


fun main () {
    val room = Room (27.0)
    room.getDescription()

    val bedroom = Bedroom ()
    bedroom.getDescription()

    val livingroom = Livingroom ()
    livingroom.getDescription()

    val kitchen = Kitchen ()
    kitchen.getDescription()

    val bathroom = Bathroom ()
    bathroom.getDescription()

    val childroom = Childroom ()
    childroom.title = "комната кошки"
    childroom.getDescription()

    val freeroom = Freeroom ( "Кабинет")
    freeroom.title = "комната собаки"
    freeroom.getDescription()
}