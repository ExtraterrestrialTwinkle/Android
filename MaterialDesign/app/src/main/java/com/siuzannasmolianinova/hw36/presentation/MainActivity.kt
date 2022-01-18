package com.siuzannasmolianinova.hw36.presentation

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.fragment.app.Fragment
import com.siuzannasmolianinova.hw36.R
import com.siuzannasmolianinova.hw36.core.itemSelectedFlow
import com.siuzannasmolianinova.hw36.data.db.Category
import com.siuzannasmolianinova.hw36.databinding.ActivityMainBinding
import kotlinx.coroutines.flow.Flow

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    lateinit var category: Flow<Category>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        category = binding.bottomNavigation.itemSelectedFlow()
        navigateToFragment(GlobalNewsFragment())
        binding.bottomNavigation.setOnItemSelectedListener { item ->
            when(item.itemId){
                R.id.page_1 -> {
                    navigateToFragment(GlobalNewsFragment())
                    true
                }
                R.id.page_2 -> {
                    navigateToFragment(HealthNewsFragment())
                    true
                }
                R.id.page_3 -> {
                    navigateToFragment(ScienceNewsFragment())
                    true
                }
                R.id.page_4 -> {
                    navigateToFragment(TechNewsFragment())
                    true
                }
                else -> {
                    Toast.makeText(this, "Unexpected page ID", Toast.LENGTH_SHORT).show()
                    false
                }
            }
        }
    }

    private fun navigateToFragment(fragment: Fragment){
        supportFragmentManager.beginTransaction()
            .replace(R.id.container, fragment)
            .commit()
    }
}