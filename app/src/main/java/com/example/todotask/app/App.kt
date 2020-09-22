package com.example.todotask.app

import android.app.Application
import com.example.todotask.data.Repository.LocalStorage

class App : Application(){

    companion object {
        lateinit var instance: App
    }

    override fun onCreate() {
        super.onCreate()
        instance = this
        LocalStorage.init(this)

    }
}