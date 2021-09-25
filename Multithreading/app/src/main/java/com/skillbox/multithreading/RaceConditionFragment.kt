package com.skillbox.multithreading

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.navArgs
import by.kirich1409.viewbindingdelegate.viewBinding
import com.skillbox.multithreading.databinding.FragmentRaceBinding
import kotlin.properties.Delegates

class RaceConditionFragment : Fragment(R.layout.fragment_race) {
    private val binding by viewBinding(FragmentRaceBinding::bind)
    private var threadCount = -1
    private var incrementCount = -1
    private var variable = 0

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        with(binding){
            numberOfThreadsEditText.addTextChangedListener(object: TextWatcher {
                override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

                override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                   threadCount = Integer.parseInt(p0?.takeIf{it.isNotBlank()}?.toString()?:"-1")
                }

                override fun afterTextChanged(p0: Editable?) {}

            })

            typeMEditText.addTextChangedListener(object : TextWatcher{
                override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

                override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                    incrementCount = Integer.parseInt(p0?.takeIf{it.isNotBlank()}?.toString()?:"-1")
                }

                override fun afterTextChanged(p0: Editable?) {}

            })

            noSyncButton.setOnClickListener{
                variable = 0
                makeNoSyncMultithreadingIncrement()
            }

            withSyncButton.setOnClickListener{
                variable = 0
                makeWithSyncMultithreadingIncrement()
            }
        }

    }

    private fun makeNoSyncMultithreadingIncrement(){
        val expectedValue = variable + threadCount * incrementCount
        var startTime = 0L

        (0 until threadCount).map{
            Thread {
                startTime = System.currentTimeMillis()
                for(i in 0 until incrementCount){
                variable++
                }
            }.apply {
                start()
            }
        }.map {it.join()}
        val time = System.currentTimeMillis() - startTime

        binding.resultTextView.text = """
            Ожидаемое значение = $expectedValue
            Реальное значение = $variable
            Время инкремента = $time 
        """.trimIndent()
    }

    private fun makeWithSyncMultithreadingIncrement(){
        val expectedValue = variable + threadCount * incrementCount
        var startTime = 0L

        (0 until threadCount).map{
            Thread {
                synchronized(this) {
                    startTime = System.currentTimeMillis()
                    for (i in 0 until incrementCount) {
                        variable++
                    }
                }
            }.apply {
                start()
            }
        }.map {it.join()}
        val time = System.currentTimeMillis() - startTime

        binding.resultTextView.text = """
            Ожидаемое значение = $expectedValue
            Реальное значение = $variable
            Время инкремента = $time 
        """.trimIndent()
    }


}