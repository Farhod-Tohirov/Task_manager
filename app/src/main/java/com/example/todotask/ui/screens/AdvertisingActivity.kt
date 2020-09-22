package com.example.todotask.ui.screens

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.viewpager2.widget.ViewPager2
import com.example.todotask.R
import com.example.todotask.data.Repository.LocalStorage
import com.example.todotask.ui.adapters.recyclerViewAdapters.AdvertisingAdapter
import com.example.todotask.utils.changeNavigationBarColor
import com.example.todotask.utils.changeStatusBarColor
import com.example.todotask.utils.toDarkenColor
import com.google.android.material.tabs.TabLayoutMediator
import kotlinx.android.synthetic.main.activity_advertising.*

class AdvertisingActivity : AppCompatActivity() {

    private val data = arrayListOf(R.drawable.ad_1, R.drawable.ad_2, R.drawable.ad_3, R.drawable.ad_4)
    private val colors = arrayListOf(
        Color.parseColor("#FFFCF5"), Color.parseColor("#DED1C9"), Color.parseColor("#D7AC81"), Color.parseColor("#FFFCF5")
    )
    private val adapter = AdvertisingAdapter(data)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_advertising)

        viewPagerAdvertisement.adapter = adapter
        viewPagerAdvertisement.registerOnPageChangeCallback(pageChangeCallback)
        TabLayoutMediator(tabLayoutAdvertisement, viewPagerAdvertisement) { _, _ -> }.attach()
        nextButton.setOnClickListener {
            if (viewPagerAdvertisement.currentItem == data.size - 2) {
                nextButton.text = getString(R.string.launch)
            }
            if (viewPagerAdvertisement.currentItem == data.size - 1) {
                if (LocalStorage.instance.firstTimeLaunchApp) {
                    finish()
                    startActivity(Intent(this, MainActivity::class.java))
                } else {
                    finish()
                }
            } else {
                viewPagerAdvertisement.setCurrentItem(viewPagerAdvertisement.currentItem + 1, true)
            }
        }
        prefButton.setOnClickListener {
            if (viewPagerAdvertisement.currentItem != data.size - 2) {
                nextButton.text = getString(R.string.next)
            }
            viewPagerAdvertisement.setCurrentItem(viewPagerAdvertisement.currentItem - 1, true)
        }
    }

    private val pageChangeCallback = object : ViewPager2.OnPageChangeCallback() {
        override fun onPageSelected(position: Int) {
            changeStatusBarColor(colors[position].toDarkenColor())
            changeNavigationBarColor(colors[position].toDarkenColor())
            if (viewPagerAdvertisement.currentItem == data.size - 1) {
                nextButton.text = getString(R.string.launch)
            } else {
                nextButton.text = getString(R.string.next)
            }
        }
    }
}
