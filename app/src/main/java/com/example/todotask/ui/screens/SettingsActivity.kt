package com.example.todotask.ui.screens

import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import com.example.todotask.R
import com.example.todotask.data.Repository.LocalStorage
import com.example.todotask.model.*
import kotlinx.android.synthetic.main.activity_settings.*

class SettingsActivity : AppCompatActivity() {

    private val lastLang = LocalStorage.instance.lang

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)
        setSupportActionBar(toolbarSettings)
        supportActionBar?.title = Languages().settingsButton
        val localStorage = LocalStorage.instance
        loadLang()
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        if (localStorage.showNotes == SHOW_AS_GRID) showItemGroup.check(R.id.showAsGrid) else showItemGroup.check(R.id.showAsList)
        if (localStorage.lang == UZBEK_LANGUAGE) languageGroup.check(R.id.langUzb) else languageGroup.check(R.id.langEng)
        if (localStorage.createNotes == CREATE_AS_DIALOG) createItemGroup.check(R.id.createAsDialog) else createItemGroup.check(R.id.createAsActivity)

        cancelButtonSettings.setOnClickListener {
            setResult(0)
            finish()
        }
        var lastCheckedLanguage = localStorage.lang
        languageGroup.setOnCheckedChangeListener { _, checkedId ->
            when (checkedId) {
                R.id.langEng -> if (lastCheckedLanguage == UZBEK_LANGUAGE) {
                    localStorage.lang = ENGLISH_LANGUAGE
                    lastCheckedLanguage = ENGLISH_LANGUAGE
                    loadLang()
                }
                R.id.langUzb -> if (LocalStorage.instance.lang == ENGLISH_LANGUAGE) {
                    localStorage.lang = UZBEK_LANGUAGE
                    lastCheckedLanguage = UZBEK_LANGUAGE
                    loadLang()
                }
            }
        }

        saveButtonSettings.setOnClickListener {
            val lastShow = localStorage.showNotes

            localStorage.createNotes = when (createItemGroup.checkedId) {
                R.id.createAsActivity -> CREATE_AS_ACTIVITY
                else -> CREATE_AS_DIALOG
            }

            localStorage.showNotes = when (showItemGroup.checkedId) {
                R.id.showAsGrid -> SHOW_AS_GRID
                else -> SHOW_AS_LIST
            }

            if (lastLang != localStorage.lang) localStorage.languageChanged = true

            if (lastShow != localStorage.showNotes) localStorage.adapterChanged = true

            setResult(SETTINGS_ACTIVITY)
            finish()
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> finish()
        }
        return true
    }

    private fun loadLang() {
        tempText0.text = languages.showNotesAs()
        showAsGrid.text = languages.grid()
        showAsList.text = languages.list()
        tempText01.text = languages.createNoteAs()
        createAsDialog.text = languages.dialog()
        createAsActivity.text = languages.activity()
        tempText02.text = languages.language()
        langUzb.text = languages.uzbek()
        langEng.text = languages.english()
        cancelButtonSettings.text = languages.cancel()
        saveButtonSettings.text = languages.create()
    }

    private val languages = Languages()
}