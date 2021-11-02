package com.smolianinovasiuzanna.hw25.data

data class Contact(
    val id: Long,
    val name: String?
    ){
    var phoneNumbers = ArrayList<String>()
    var emails = ArrayList<String>()
}

