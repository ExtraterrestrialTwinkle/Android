package com.smolianinovasiuzanna.hw7_9


fun main() {

    val virtualWallet = Wallets.VirtualWallet() //виртуальный кошелек 1
    virtualWallet.addMoney(Currency.EUR)
    virtualWallet.convertMoneyInUSD(Currency.EUR)


    val virtualWallet1 = Wallets.VirtualWallet() //виртуальный кошелек 2
    virtualWallet1.addMoney(Currency.RUR)
    virtualWallet1.convertMoneyInUSD(Currency.RUR)

    println(virtualWallet1 == virtualWallet) //сравнение двух виртуальных кошельков


    val realWallet = Wallets.RealWallet() //реальный кошелек 1
    realWallet.addMoney(Currency.EUR)
    realWallet.convertMoneyInUSD(Currency.EUR)

    val realWallet1 = Wallets.RealWallet() // реальный кошелек 2
    realWallet1.addMoney(Currency.USD)
    realWallet1.convertMoneyInUSD(Currency.USD)

    println(virtualWallet1 == virtualWallet) // сравнение двух реальных кошельков


}