package com.smolianinovasiuzanna.hw4_5

//homework 4-5 done!!!

fun main () {
    print("Введите количество номеров: ")
    val n = readLine()?.toIntOrNull() ?: return
    val mutableList = numbersList(n)

    println ("Список номеров, начинающихся на +7: ${mutableList.filter{it.startsWith("+7")}}")
    //Фильтрация по номерам телефонов, начинающихся с +7

    val phoneNumbersSet = mutableList.toSet() //Преобразуем список в множество
    println("Количество уникальных номеров: ${phoneNumbersSet.size} ")
    //Выводим в консоль количество уникальных номеров, как размер множества
    println ("Сумма всех длин номеров: ${mutableList.sumBy {it.length}}")//Выводиим сумму длин номеров

    val phoneNumbersWithUsers = mutableMapOf<String, String>()
    for (phone in phoneNumbersSet) {
        print("Введите имя человека с номером телефона $phone: ")
        phoneNumbersWithUsers[phone] = readLine(). toString()
    } //Заполняем мапу именами

    for (key in phoneNumbersWithUsers.keys)
        println("Человек: ${phoneNumbersWithUsers[key]}. Номер телефона: $key.")
    //Выводим мапу на печать
}

fun numbersList (n: Int): MutableList<String> {
    val phoneNumbers = mutableListOf<String>()
    var a: Int = 0
    while (a < n) { //Вводим номера телефонов, количеством n
        print("Введите номер телефона: ")
        val phone = readLine() ?: continue
        phoneNumbers.add(phone)
        a++
    }
return phoneNumbers
}


