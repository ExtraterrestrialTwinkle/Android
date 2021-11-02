package com.smolianinovasiuzanna.hw25.ui

import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.smolianinovasiuzanna.hw25.R
import com.smolianinovasiuzanna.hw25.ui.model.ContactsViewModel

class MainActivity : AppCompatActivity(R.layout.activity_main){
    val contactsViewModel: ContactsViewModel by viewModels()
}