package com.smolianinovasiuzanna.hw25.data

data class Contact(
    val id: Long,
    val name: String? =  "",
    val phoneNumbers: List<String>? = emptyList(),
    val emails: List<String>? = emptyList()
)
