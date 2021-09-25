package com.smolianinovasiuzanna.hw8_4

fun main (){
    val queue = Queue<String>()
    queue.enqueue("kjkjk")
    queue.enqueue("fgrtey")
    println(queue)
    queue.dequeue()
    println(queue)
    queue.dequeue()
    queue.dequeue()
    println(queue)

}


class Queue <T>  {

    var items = mutableListOf <T> ()

    fun enqueue (item: T) = items.add(item)
    fun dequeue (): T? {
        return if (items.isEmpty()) null
        else items.removeAt(0)
    }

    override fun toString(): String {
        return items.toString()
    }
}


