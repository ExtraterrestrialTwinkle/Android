package com.skillbox.multithreading


import android.util.Log
import androidx.fragment.app.Fragment
import kotlin.random.Random

class LivelockFragment : Fragment() {

    override fun onResume() {
        super.onResume()
        val husband = Diner("Bob")
        val wife = Diner("Alice")
        val spoon = Spoon(husband)

        Thread {
            husband.eatWith(spoon, wife)
        }.start()

        Thread {
            wife.eatWith(spoon, husband)
        }.start()

    }

    class Spoon(var owner: Diner) {

        @Synchronized
        fun use() {
            Thread.sleep(1000)
            Log.d("${owner.name} ", "обедает ${Thread.currentThread().name}")
        }
    }

    data class Diner(val name: String) {
        var isHungry = true


        fun eatWith(spoon: Spoon, spouse: Diner) {
            while (isHungry) {
                // Нет ложки -> ждет, пока отобедает супруг
                if (spoon.owner !== this) {
                    Thread.sleep(500)
                    continue
                }

                // Супруг голоден -> предлагает отдать ложку
                if (spouse.isHungry) {
                    //Решение: есть какой-то промежуток времени, затем отдать ложку: Решение закомментировано
//                    spoon.use()
//                    isHungry = Random.nextBoolean()
//                    Log.d("Голоден?", "${spoon.owner.name} $isHungry")

                    Log.d("$name ","Ешь, ${spouse.name}!"                    )
                    spoon.owner = spouse
//                    if (isHungry)
                    continue
//                    else break

                }

                    // Супруг наелся -> ест
                    spoon.use()
                    isHungry = false
                    Log.d(
                        "$name: ",
                        "Я наелся, ${spouse.name}!"
                    )
                    spoon.owner = spouse

            }
        }
    }
}