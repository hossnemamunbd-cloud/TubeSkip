package com.example

import android.app.Application
import com.example.data.local.AppDatabase
import com.example.data.repository.KeywordRepository
import com.example.data.repository.SettingsRepository
import com.google.android.gms.ads.MobileAds
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class TubeSkipApp : Application() {

    lateinit var database: AppDatabase
        private set

    lateinit var keywordRepository: KeywordRepository
        private set

    lateinit var settingsRepository: SettingsRepository
        private set

    override fun onCreate() {
        super.onCreate()
        instance = this
        database = AppDatabase.getDatabase(this)
        keywordRepository = KeywordRepository(database.blockedKeywordDao(), database.skipLogDao())
        settingsRepository = SettingsRepository(this)

        // Initialize Google Mobile Ads SDK on a background thread
        CoroutineScope(Dispatchers.IO).launch {
            try {
                MobileAds.initialize(this@TubeSkipApp) {}
            } catch (e: Exception) {
                // Safe initialization failure catch to guarantee stability
            }
        }
    }

    companion object {
        lateinit var instance: TubeSkipApp
            private set
    }
}
