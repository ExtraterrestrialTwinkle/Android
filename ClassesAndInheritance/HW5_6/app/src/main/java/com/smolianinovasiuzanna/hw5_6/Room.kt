package com.smolianinovasiuzanna.hw5_6
//Homework is Done!!


open class Room (
    val area: Double = 24.0,
    var windows: Int = 2,
    var doors: Int = 1
   ) {

    protected open val title: String = "Обычная комната"
    protected open val carpet: Boolean = true
        fun getDescription() {
            print("Название комнаты - $title, площадь комнаты = $area кв.м., количество " +
                    "окон и дверей - $windows и $doors. ")
            if (carpet) println ("Есть ковер.") else println ("Ковра нет.")
        }
}