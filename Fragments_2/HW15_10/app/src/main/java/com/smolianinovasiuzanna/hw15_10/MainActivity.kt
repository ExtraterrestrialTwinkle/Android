package com.smolianinovasiuzanna.hw15_10


import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import com.smolianinovasiuzanna.hw15_10.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity(), InterfaceForDialog {
    private lateinit var binding: ActivityMainBinding
    private var screenChoice = screens.toMutableList()
    private val adapter = OnboardingAdapter(this)
    private var valid: FormState = FormState(false, 0, checkedItems)
    var articles: State = State (screenChoice)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val wormDotsIndicator = binding.wormDotsIndicator
        val viewPager = binding.viewPager

        val tabLayout = binding.tabLayout

        viewPager.adapter = adapter

        var item = viewPager.currentItem

        viewPager.registerOnPageChangeCallback(object: ViewPager2.OnPageChangeCallback(){
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                item = position
                println ("$item")
            }
        })
        Log.d("ViewPager", "$item")
        valid = FormState(true, item, checkedItems)

        if(savedInstanceState == null) {
            adapter.updateScreens(screenChoice)
            viewPager.setCurrentItem(item, true)
            Log.d("setCurrentItem", "true $item")
        }
        if (savedInstanceState != null) {
            valid = savedInstanceState.getParcelable(KEY)?: error("Error")
            articles = savedInstanceState.getParcelable(ARTICLES_KEY)?: error("Error")
            screenChoice = articles.articles as MutableList<OnboardingScreen>
            println("getParcelable ${screenChoice.size}")
            adapter.updateScreens(screenChoice)
            viewPager.setCurrentItem(item, true)
            Log.d("getParcelable", "OK")

        }
        wormDotsIndicator.setViewPager2(viewPager)

        setPageTransformer(viewPager)

        setTabLayout(tabLayout, viewPager)

        binding.filterDialogButton.setOnClickListener {
            FilterDialogFragment()
                .show(supportFragmentManager, "filterDialogTag")
        }

    }

    private fun setPageTransformer(viewPager: ViewPager2){
        viewPager.setPageTransformer(object : ViewPager2.PageTransformer {
            override fun transformPage(page: View, position: Float) {
                when {
                    position < -1 || position > 1 -> {
                        page.alpha = 0f
                    }

                    position <= 0 -> {
                        page.alpha = 1 + position
                    }

                    position <= 1 -> {
                        page.alpha = 1 - position
                    }
                }
            }
        })
    }
    private fun setTabLayout (tabLayout: TabLayout, viewPager: ViewPager2) {
        TabLayoutMediator(tabLayout, viewPager) { tab, position ->
           tab.setIcon(screenChoice.elementAt(position).iconRes)

        }.attach()

    }

    override fun okClicked(filteredTags: HashSet<String>) {
        screenChoice = screens.filter{ article -> article.tags.map{it.tag}
            .intersect(filteredTags.toSet()).isNotEmpty()} as MutableList<OnboardingScreen>

        adapter.updateScreens(screenChoice)
        articles = State(screenChoice)
        println(screenChoice.size)
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        println("onSaveInstanceState ${screenChoice.size}")
        outState.putParcelable(ARTICLES_KEY, articles)
        outState.putParcelable(KEY, valid)
        Log.d("onSaveInstanceState", "OK")
    }

companion object {
    private const val KEY = "key"
    private const val ARTICLES_KEY = "articles key"

    var checkedItems: BooleanArray = booleanArrayOf(true, true, true)

    private val screens: List<OnboardingScreen> = listOf(
        OnboardingScreen(
            id = 0,
            imageRes = R.drawable.volumetric_flask,
            nameRes = R.string.volumetric_flask,
            infoRes = R.string.volumetric_flask_info,
            iconRes = R.drawable.volumetric_flask_icon,
            tags = listOf(ArticleType.VOLUMETRIC_GLASSWARE)
        ),

        OnboardingScreen(
            id = 1,
            imageRes = R.drawable.cylinder,
            nameRes = R.string.cylinder,
            infoRes = R.string.cylinder_info,
            iconRes = R.drawable.cylinder_icon,
            tags = listOf(ArticleType.VOLUMETRIC_GLASSWARE)
        ),

        OnboardingScreen(
            id = 2,
            imageRes = R.drawable.pipette,
            nameRes = R.string.pipette,
            infoRes = R.string.pipette_info,
            iconRes = R.drawable.pipette_icon,
            tags = listOf(ArticleType.VOLUMETRIC_GLASSWARE)
        ),

        OnboardingScreen(
            id = 3,
            imageRes = R.drawable.burette,
            nameRes = R.string.burette,
            infoRes = R.string.burette_info,
            iconRes = R.drawable.burette_icon,
            tags = listOf(ArticleType.VOLUMETRIC_GLASSWARE)
        ),

        OnboardingScreen(
            id = 4,
            imageRes = R.drawable.tubes,
            nameRes = R.string.tube,
            infoRes = R.string.tube_info,
            iconRes = R.drawable.tubes_icon,
            tags = listOf(ArticleType.LABORATORY_GLASSWARE)
        ),

        OnboardingScreen(
            id = 5,
            imageRes = R.drawable.glass,
            nameRes = R.string.glass,
            infoRes = R.string.glass_info,
            iconRes = R.drawable.glass_icon,
            tags = listOf(ArticleType.LABORATORY_GLASSWARE)
        ),

        OnboardingScreen(
            id = 6,
            imageRes = R.drawable.funnel,
            nameRes = R.string.funnel,
            infoRes = R.string.funnel_info,
            iconRes = R.drawable.funnel_icon,
            tags = listOf(ArticleType.LABORATORY_GLASSWARE)
        ),

        OnboardingScreen(
            id = 7,
            imageRes = R.drawable.flasks,
            nameRes = R.string.flask,
            infoRes = R.string.flask_info,
            iconRes = R.drawable.flasks_icon,
            tags = listOf(ArticleType.LABORATORY_GLASSWARE)
        ),

        OnboardingScreen(
            id = 8,
            imageRes = R.drawable.separating_funnel,
            nameRes = R.string.separating_funnel,
            infoRes = R.string.separating_funnel_info,
            iconRes = R.drawable.separating_funnel_icon,
            tags = listOf(ArticleType.SPECIAL_GLASSWARE)
        ),

        OnboardingScreen(
            id = 9,
            imageRes = R.drawable.petrie,
            nameRes = R.string.petrie,
            infoRes = R.string.petrie_info,
            iconRes = R.drawable.petrie_icon,
            tags = listOf(ArticleType.SPECIAL_GLASSWARE)
        ),

        OnboardingScreen(
            id = 10,
            imageRes = R.drawable.dropper,
            nameRes = R.string.dropper,
            infoRes = R.string.dropper_info,
            iconRes = R.drawable.dropper_icon,
            tags = listOf(ArticleType.SPECIAL_GLASSWARE)
        ),
        OnboardingScreen(
            id = 11,
            imageRes = R.drawable.bounzen,
            nameRes = R.string.bounzen,
            infoRes = R.string.bounzen_info,
            iconRes = R.drawable.bounzen_icon,
            tags = listOf(ArticleType.SPECIAL_GLASSWARE)
        ),

        OnboardingScreen(
            id = 12,
            imageRes = R.drawable.melting_pot,
            nameRes = R.string.melting_pot,
            infoRes = R.string.melting_pot_info,
            iconRes = R.drawable.melting_pot_icon,
            tags = listOf(ArticleType.SPECIAL_GLASSWARE)
        )
    )

    }

}

