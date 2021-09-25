package com.smolianinovasiuzanna.hw8_10

fun main() {
    try {
        val wheel = Wheel()
        wheel.setPressure(10.1)
        wheel.check()
    } catch (ex: Exception) {
        println("Exception! $ex")
    }
}

class Wheel {

    var pressure: Double = 0.0
        private set

    inner class TooHighPressure : Exception() {
        override fun toString(): String {
            return "Your wheel pressure is too high!"
        }
    }
    inner class TooLowPressure : Exception() {
        override fun toString(): String {
           return "Your wheel pressure is too low!"
        }
    }

    inner class IncorrectPressure : Exception() {
        override fun toString(): String {
            return "Your wheel pressure is incorrect!"
        }
    }

    fun setPressure(value: Double) {
        println("Set pressure is $value")
        when {
            value < 0.0 -> throw IncorrectPressure()
            value > 10.0 -> throw IncorrectPressure()
            else -> println("You can use your wheel")
        }
        pressure += value
    }

    fun check() {

        val minPressure = 1.6
        val maxPressure = 2.5
        when {
            pressure < minPressure -> throw TooLowPressure()
            pressure > maxPressure -> throw TooHighPressure()
        }
    }
}
