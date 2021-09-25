package com.smolianinovasiuzanna.hw15_10

import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter

class OnboardingAdapter(activity: AppCompatActivity): FragmentStateAdapter(activity) {

   var screens: List <OnboardingScreen> = emptyList()

    fun updateScreens(newScreens: List<OnboardingScreen>) {
        screens = newScreens
        notifyDataSetChanged()
    }

    override fun getItemCount(): Int {
        return screens.size
    }
    override fun getItemId(position: Int): Long {
        return screens[position].id.toLong()
    }
    override fun containsItem(itemId: Long): Boolean {
        return screens.any { it.id.toLong() == itemId }
    }

    override fun createFragment(position: Int): Fragment {
        val screen: OnboardingScreen = screens [position]
        return OnboardingFragment.newInstance(
            imageRes = screen.imageRes,
            nameRes = screen.nameRes,
            infoRes = screen.infoRes,
            tag = screen.tags.toString()
        )




    }
}