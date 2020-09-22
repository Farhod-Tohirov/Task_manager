package com.example.todotask.data.Repository

import android.content.Context
import android.content.SharedPreferences
import com.example.task1.Tools.BooleanPreference
import com.example.task1.Tools.IntPreference
import com.example.todotask.model.CREATE_AS_ACTIVITY
import com.example.todotask.model.CREATE_AS_DIALOG
import com.example.todotask.model.SHOW_AS_GRID
import com.example.todotask.model.UZBEK_LANGUAGE

class LocalStorage private constructor(context: Context) {
    companion object {
        lateinit var instance: LocalStorage; private set

        fun init(context: Context) {
            instance = LocalStorage(context)
        }
    }

    private val pref: SharedPreferences =
        context.getSharedPreferences("LocalStorage", Context.MODE_PRIVATE)

    var lastSelected by IntPreference(pref, -1)
    var firstTimeLaunchApp by BooleanPreference(pref, true)

    var lang by IntPreference(pref, UZBEK_LANGUAGE)
    var showNotes by IntPreference(pref, SHOW_AS_GRID)
    var createNotes by IntPreference(pref, CREATE_AS_DIALOG)

    var languageChanged by BooleanPreference(pref, false)
    var adapterChanged by BooleanPreference(pref, false)
}