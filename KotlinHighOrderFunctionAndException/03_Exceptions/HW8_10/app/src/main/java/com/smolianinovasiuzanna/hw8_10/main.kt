package com.smolianinovasiuzanna.hw8_10

fun main(){

    val wheel = Wheel()
    wheel.setPressure(2.2)
    wheel.check()
}

 class Wheel {

     var pressure: Double = 0.0
         private set




     inner class TooHighPressure {
         fun tooHigh() {
             throw Exception("Too high pressure")
         }
     }

     inner class TooLowPressure {
         fun tooLow() {
             throw Exception("Too low pressure")
         }
         }

     inner class IncorrectPressure {
         fun incorrect() {
             throw Exception("Too low pressure")
         }
     }

 }


fun setPressure(value: Double): Wheel {
    pressure = value

    when {
        pressure < 0.0 ->
            try {
                IncorrectPressure().incorrect()
            } catch (r: Exception) {
                println("The pressure is incorrect")
            }
        pressure > 10.0 ->
            try {
                IncorrectPressure().incorrect()
            } catch (r: Exception) {
                println("The pressure is incorrect")
            }
    }
}

fun check(){
    val minPressure = 1.6
    val maxPressure = 2.5
    when {pressure < minPressure ->
        try {
            TooLowPressure().tooLow()
        } catch (r: Exception) {
            println("The pressure is too low")
        }
        pressure > maxPressure ->
            try {
                TooHighPressure().tooHigh()
            } catch (r: Exception) {
                println("The pressure is too high")
            }
        else -> println ("You can use your wheel")
    }

}
