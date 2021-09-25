package com.smolianinovasiuzanna.hw7_9

sealed class Wallets {

    abstract var money: Double

    abstract fun convertMoneyInUSD (type: Currency)


    class VirtualWallet : Wallets() { //класс виртуальных кошельков
        private var moneyRUR: Double = 0.0
        private var moneyUSD: Double = 0.0
        private var moneyEUR: Double = 0.0

        override var money: Double = 0.0 //деньги в кошельке

        fun addMoney (type: Currency) { // Вносим деньги в кошелек

            print ("Enter your money: ")
            money = readLine()?.toDoubleOrNull()?: return
            println ("You have $money $type.")

            when (type) { //кладем деньги в нужную "ячейку" по наименованию валюты
                Currency.RUR -> moneyRUR += money
                Currency.EUR -> moneyEUR += money
                Currency.USD -> moneyUSD += money
            }

        }

        override fun convertMoneyInUSD(type: Currency) { //конвертация в доллары
            when (type) {
                Currency.USD -> return
                else -> Currency.convertToUSD (type, money)
            }

        }

        override fun equals(other: Any?): Boolean {
            if (this === other) return true
            if (javaClass != other?.javaClass) return false

            other as VirtualWallet

            if (money != other.money) return false

            return true
        }

        override fun hashCode(): Int {
            return money.hashCode()
        }

    }
    class RealWallet : Wallets() { //класс реальных кошельков

        private var MoneyRUR = mutableMapOf<Int, Int>()
        private var MoneyUSD = mutableMapOf<Int, Int>()
        private var MoneyEUR = mutableMapOf<Int, Int>()

        override var money: Double = 0.0 // деньги в кошельке

        fun addMoney(type: Currency) { // вносим деньги в кошелек

            print("Enter your bill quantity: ") // вводим количество купюр
            val billQuantity = readLine()?.toIntOrNull() ?: return

            print("Enter your bill denomimation: ") // вводим номинал купюр
            val denomination = readLine()?.toIntOrNull() ?: return

            money = (denomination * billQuantity).toDouble()

            println("You have $money $type.")

            when (type) { // вносим деньги в нужную "ячейку" мапы по номиналу и валюте купюр
                Currency.RUR -> MoneyRUR [denomination] = billQuantity
                Currency.EUR -> MoneyEUR [denomination] = billQuantity
                Currency.USD -> MoneyUSD [denomination] = billQuantity
            }

        }

        override fun convertMoneyInUSD(type: Currency) { // конвертируем в доллары
            when (type) {
                Currency.USD -> return
                else -> Currency.convertToUSD(type, money)
            }
        }

        override fun equals(other: Any?): Boolean {
            if (this === other) return true
            if (javaClass != other?.javaClass) return false

            other as RealWallet

            if (money != other.money) return false

            return true
        }

        override fun hashCode(): Int {
            return money.hashCode()
        }


    }

}


