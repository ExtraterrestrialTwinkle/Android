package com.smolianinovasiuzanna.hw2_8
//homework done
import kotlin.math.sqrt


fun main () {

    val solutionSum = solveEquation(a = 1, b = 7, c = -3)
    println (solutionSum)
}
fun solveEquation(a: Int, b: Int, c: Int) : Double? {
    //Вычисление дискриминанта
        val d = (b * b - 4 * a * c).toDouble()

            if (d >= 0) {
                val dx = sqrt(d)
                //Вычисление корней уравнения
                val x1 = -b + dx / (2 * a)
                val x2 = -b - dx / (2 * a)

                println("x1 = $x1")
                println("x2 = $x2")

                val solutionSum: Double = x1 + x2

                return solutionSum

                }else {
                return null
            }

    }
