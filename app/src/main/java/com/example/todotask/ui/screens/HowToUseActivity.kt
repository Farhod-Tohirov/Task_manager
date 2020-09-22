package com.example.todotask.ui.screens

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.todotask.R
import kotlinx.android.synthetic.main.activity_how_to_use.*


class HowToUseActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_how_to_use)

        backButtonHowToUse.setOnClickListener { finish() }

        webView.settings.allowFileAccess = true
        webView.settings.setSupportZoom(true)


        webView.loadUrl("file:///android_asset/index.html")

    }
}
