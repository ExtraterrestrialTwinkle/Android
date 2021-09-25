package com.skillbox.multithreading

import android.util.Log
import androidx.fragment.app.Fragment

class DeadlockFragment: Fragment() {

    private var i = 0
    private val lock1 = Any()
    private val lock2 = Any()

    override fun onResume() {
        super.onResume()

//Зачем нам в этом дз Вася с Петей?))))
//        val friend1 = Person("Вася")
//        val friend2 = Person("Петя")
//
//        val thread1 = Thread{
//            friend1.throwBallTo(friend2)
//        }
//        val thread2 = Thread{
//            friend2.throwBallTo(friend1)
//        }
//
//        thread1.start()
//        thread2.start()

// Нет дедлока -> i = число
        noDeadlock()

// Дедлок -> i = 0
        deadlock()

        Log.d("i = ", "$i")
    }

    private fun noDeadlock(){
        val thread1 = Thread {
            Log.d("NoDeadlock", "Start1")

            (0..1000000).forEach {
                synchronized(i) {
                        i++
                }
            }
            Log.d("NoDeadlock", "End1")
        }

        val thread2 = Thread {
            Log.d("NoDeadlock", "Start2")
            (0..1000000).forEach {
                synchronized(i) {
                        i++
                }
            }

            Log.d("NoDeadlock", "End2")
        }

        thread1.start()
        thread2.start()
        thread1.join()
        thread2.join()

    }

    private fun deadlock(){
        val thread1 = Thread {
            Log.d("Deadlock", "Start1")

            (0..1000000).forEach {
                synchronized(lock1) {
                    synchronized(lock2) {
                        i++
                    }
                }
            }
            Log.d("Deadlock", "End1")
        }

        val thread2 = Thread {
            Log.d("Deadlock", "Start2")
            (0..1000000).forEach {
                synchronized(lock2) {
                    synchronized(lock1) {
                        i++
                    }
                }
            }

            Log.d("Deadlock", "End2")
        }

        thread1.start()
        thread2.start()
    }

//    data class Person(
//        val name: String
//    ) {
//
//        fun throwBallTo(friend: Person) {
//            synchronized(this) {
//                Log.d(
//                    "Person",
//                    "$name бросает мяч ${friend.name} на потоке ${Thread.currentThread().name}"
//                )
//                Thread.sleep(500)
//            }
//            friend.throwBallTo(this)
//        }
//    }
}
