package com.smolianinovasiuzanna.hw8_7


fun main (){

    val queue = Queue<String>()
    queue.enqueue("kjkjk")
    queue.enqueue("23")
    queue.enqueue("k22kjhhfb")
    queue.enqueue("k")
    queue.enqueue("лололололол")
    queue.enqueue("12344567")
    queue.enqueue("fgrtey")
    println(queue.filterQueue {it.length >= 4})
    println(queue.filterQueue { it.contains('k') })
    println(queue.filterQueue(::test))
    println(queue)

    queue.dequeue()
    println(queue)
    queue.dequeue()
    queue.dequeue()
    println(queue)

}
fun test (s: String): Boolean = s.isNotBlank()

class Queue <T> {

    private val items = mutableListOf<T>()
    fun enqueue (item: T) = items.add(item)
    fun dequeue (): T? {
        return if (items.isEmpty()) null
        else items.removeAt(0)
    }

    fun filterQueue (filter: (T) -> Boolean): MutableList<T> {
        val newItems = mutableListOf<T>()
        items.forEach{if (filter(it)) newItems.add(it)}

        return newItems
    }

        override fun toString(): String {
        return items.toString()
    }

}

