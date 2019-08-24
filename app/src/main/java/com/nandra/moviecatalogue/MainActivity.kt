package com.nandra.moviecatalogue

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.tabs.TabLayout
import kotlinx.android.synthetic.main.e_activity_main.*

class MainActivity : AppCompatActivity() {

    private lateinit var tabLayout: TabLayout
    private lateinit var viewPagerPageAdapter: ViewPagerPageAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.e_activity_main)

        tabLayout = findViewById(R.id.main_tab_layout)
        viewPagerPageAdapter = ViewPagerPageAdapter(supportFragmentManager, tabLayout.tabCount)
        main_viewpager.adapter = viewPagerPageAdapter

        tabLayout.addOnTabSelectedListener(object: TabLayout.OnTabSelectedListener{
            override fun onTabReselected(p0: TabLayout.Tab?) {}

            override fun onTabUnselected(p0: TabLayout.Tab?) {}

            override fun onTabSelected(tab: TabLayout.Tab?) {
                main_viewpager.currentItem = tab?.position!!
            }

        })

        main_viewpager.addOnPageChangeListener(TabLayout.TabLayoutOnPageChangeListener(tabLayout))

    }

    companion object {
        @JvmStatic val EXTRA_MOVIE = "extra_movie"
    }
}
