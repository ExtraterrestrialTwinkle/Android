package com.smolianinovasiuzanna.hw3_7

import kotlin.math.abs

fun main () {
    print("Введите число: ")
    val n = readLine()?.toIntOrNull() ?: return

    var currentNumber: Int = 0
    var sumOfPositive: Int = 0
    var sumOfNumbers: Int = 0
    println("Введите $n чисел: ")

    while (currentNumber < n) {
        val number1 = readLine()?.toIntOrNull() ?: continue
        sumOfNumbers += number1
        if (number1 > 0) {
            sumOfPositive++
        }
        currentNumber++
    }
    println("Вы ввели $sumOfPositive положительных чисел")
    println("Сумма введенных чисел: $sumOfNumbers")

    println("Введите еще одно число: ")


    readLine()?.toIntOrNull()

        ?.let { number ->
            tailrec fun gcd(sumOfNumbers: Int, b: Int): Int {
            if (b == 0)
                return abs(sumOfNumbers)
            return gcd(b, sumOfNumbers % b)
        }
            println ("НОД: ${gcd(sumOfNumbers, number)}")
        }

    ?: println("Вы ввели не число!")

    }
