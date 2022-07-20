package com.example.financemanager

import android.app.Application
import androidx.appcompat.app.AppCompatDelegate
import androidx.preference.PreferenceManager
import com.example.financemanager.utils.Utils
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class FinanceManagerApp : Application() {

    override fun onCreate() {
        super.onCreate()

        val theme = PreferenceManager.getDefaultSharedPreferences(applicationContext)
            .getString(Utils.THEME_PREFERENCE_KEY, Utils.THEME_DEFAULT)

        AppCompatDelegate.setDefaultNightMode(
            when (theme) {
                Utils.THEME_LIGHT -> AppCompatDelegate.MODE_NIGHT_NO
                Utils.THEME_DARK -> AppCompatDelegate.MODE_NIGHT_YES
                else -> AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM
            }
        )
    }

}