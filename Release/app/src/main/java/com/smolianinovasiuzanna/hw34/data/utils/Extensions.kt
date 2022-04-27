package com.smolianinovasiuzanna.hw34.data.utils

import android.text.Editable
import android.text.TextWatcher
import android.widget.EditText
import android.widget.RadioGroup
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.channels.sendBlocking
import kotlinx.coroutines.channels.trySendBlocking
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow

fun EditText.textChangedFlow(): Flow<String> {
    return callbackFlow<String> {
        val textChangedListener = object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                trySendBlocking(s?.toString().orEmpty())
            }

            override fun afterTextChanged(s: Editable?) {}
        }
        this@textChangedFlow.addTextChangedListener(textChangedListener)
        awaitClose {
            this@textChangedFlow.removeTextChangedListener(textChangedListener)
        }
    }
}

fun RadioGroup.checkedChangeFlow(): Flow<Int>{
    return callbackFlow<Int> {
        val checkedChangeListener =
            RadioGroup.OnCheckedChangeListener { _, checkedId ->
                trySendBlocking(checkedId)
            }
        setOnCheckedChangeListener(checkedChangeListener)
        awaitClose{
            setOnCheckedChangeListener(null)
        }
    }
}
