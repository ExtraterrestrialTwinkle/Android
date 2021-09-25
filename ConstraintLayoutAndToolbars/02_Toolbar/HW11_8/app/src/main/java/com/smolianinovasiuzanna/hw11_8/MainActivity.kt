package com.smolianinovasiuzanna.hw11_8

import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.widget.SearchView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.smolianinovasiuzanna.hw11_8.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initToolbar()

    }

    private val searchList = listOf(
        "Cat Researcher",
        "Mermaid",
        "King Kascshey",
        "Uncle Chernomore",
        "Granny Yagha"
    )


    private fun toast(text: String) {
        Toast.makeText(this, text, Toast.LENGTH_SHORT).show()
    }

    private fun initToolbar() {
        binding.toolbar.setNavigationOnClickListener {
            toast("Navigation clicked")
        }

        binding.toolbar.setOnMenuItemClickListener { menuItem ->
            when (menuItem.itemId) {
                R.id.action -> {
                    toast("Go to Oak")
                    true
                }
                R.id.go_to_bed -> {
                    toast("Let the Cat Researcher sleep")
                    true
                }
                else -> false
            }
        }
        val searchItem = binding.toolbar.menu.findItem(R.id.search)

        searchItem.setOnActionExpandListener(object : MenuItem.OnActionExpandListener {

            override fun onMenuItemActionExpand(item: MenuItem?): Boolean {
                toast("Look for Fairytale heroes")
                return true
            }

            override fun onMenuItemActionCollapse(item: MenuItem?): Boolean {
                toast("Go back")
                binding.searchTextView.visibility = View.GONE
                return true
            }
        })

        (searchItem.actionView as androidx.appcompat.widget.SearchView)
                .setOnQueryTextListener(object :
                        SearchView.OnQueryTextListener,
                        androidx.appcompat.widget.SearchView.OnQueryTextListener {

                    override fun onQueryTextSubmit(query: String?): Boolean {
                        return true
                    }

                    override fun onQueryTextChange(newText: String?): Boolean {
                        searchList.filter { it.contains(newText ?: "", true) }
                                .joinToString()
                                .let {
                                    binding.searchTextView.text = it
                                }
                        return true
                    }
                })
    }
}
