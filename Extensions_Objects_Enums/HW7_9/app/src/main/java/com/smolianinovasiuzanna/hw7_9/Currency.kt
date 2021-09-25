package com.smolianinovasiuzanna.hw7_9

enum class Currency {
    RUR,
    USD,
    EUR;


    //выбор национальной валюты руб
    companion object {
        val nationalCurrency: Currency = RUR
    }

}
// проверка валюты: национальная или нет

val Currency.isNational : Boolean
    get() = this == Currency.nationalCurrency


fun Currency.Companion.convertToUSD (type: Currency, money: Double) {

    //oпределяем национальную валюту
    if (type.isNational) {
        println("Your currency is RUR")
    }
    else {
        println("Your currency is not RUR")
    }

    var moneyInUSD = 0.0
    // конвертируем деньги в зависимости от типа валюты
    when (type) {
        Currency.RUR -> moneyInUSD += money * CurrencyConverter.courseRUR
        Currency.EUR -> moneyInUSD += money * CurrencyConverter.courseEUR
        Currency.USD -> moneyInUSD = money
    }
    println ("You have $moneyInUSD USD.")
}