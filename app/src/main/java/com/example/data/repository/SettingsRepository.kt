package com.example.data.repository

import android.content.Context
import android.content.SharedPreferences
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

enum class AppThemeMode {
    SYSTEM,
    DARK,
    LIGHT
}

data class AppSettings(
    val isMasterActive: Boolean = true,
    val skipFullVideos: Boolean = true,
    val skipShorts: Boolean = true,
    val showSkipToast: Boolean = true,
    val language: String = "bn", // "bn" or "en"
    val themeMode: AppThemeMode = AppThemeMode.SYSTEM,
    val skipCooldownSeconds: Int = 2
)

class SettingsRepository(context: Context) {
    private val prefs: SharedPreferences =
        context.getSharedPreferences("tubeskip_prefs", Context.MODE_PRIVATE)

    private val _settings = MutableStateFlow(loadSettings())
    val settings: StateFlow<AppSettings> = _settings.asStateFlow()

    private fun loadSettings(): AppSettings {
        return AppSettings(
            isMasterActive = prefs.getBoolean(KEY_MASTER_ACTIVE, true),
            skipFullVideos = prefs.getBoolean(KEY_SKIP_FULL_VIDEOS, true),
            skipShorts = prefs.getBoolean(KEY_SKIP_SHORTS, true),
            showSkipToast = prefs.getBoolean(KEY_SHOW_SKIP_TOAST, true),
            language = prefs.getString(KEY_LANGUAGE, "bn") ?: "bn",
            themeMode = try {
                AppThemeMode.valueOf(prefs.getString(KEY_THEME_MODE, AppThemeMode.SYSTEM.name) ?: AppThemeMode.SYSTEM.name)
            } catch (e: Exception) {
                AppThemeMode.SYSTEM
            },
            skipCooldownSeconds = prefs.getInt(KEY_COOLDOWN, 2)
        )
    }

    fun setMasterActive(active: Boolean) {
        prefs.edit().putBoolean(KEY_MASTER_ACTIVE, active).apply()
        _settings.value = _settings.value.copy(isMasterActive = active)
    }

    fun setSkipFullVideos(enabled: Boolean) {
        prefs.edit().putBoolean(KEY_SKIP_FULL_VIDEOS, enabled).apply()
        _settings.value = _settings.value.copy(skipFullVideos = enabled)
    }

    fun setSkipShorts(enabled: Boolean) {
        prefs.edit().putBoolean(KEY_SKIP_SHORTS, enabled).apply()
        _settings.value = _settings.value.copy(skipShorts = enabled)
    }

    fun setShowSkipToast(enabled: Boolean) {
        prefs.edit().putBoolean(KEY_SHOW_SKIP_TOAST, enabled).apply()
        _settings.value = _settings.value.copy(showSkipToast = enabled)
    }

    fun setLanguage(lang: String) {
        prefs.edit().putString(KEY_LANGUAGE, lang).apply()
        _settings.value = _settings.value.copy(language = lang)
    }

    fun setThemeMode(theme: AppThemeMode) {
        prefs.edit().putString(KEY_THEME_MODE, theme.name).apply()
        _settings.value = _settings.value.copy(themeMode = theme)
    }

    fun setCooldownSeconds(seconds: Int) {
        prefs.edit().putInt(KEY_COOLDOWN, seconds).apply()
        _settings.value = _settings.value.copy(skipCooldownSeconds = seconds)
    }

    companion object {
        private const val KEY_MASTER_ACTIVE = "key_master_active"
        private const val KEY_SKIP_FULL_VIDEOS = "key_skip_full_videos"
        private const val KEY_SKIP_SHORTS = "key_skip_shorts"
        private const val KEY_SHOW_SKIP_TOAST = "key_show_skip_toast"
        private const val KEY_LANGUAGE = "key_language"
        private const val KEY_THEME_MODE = "key_theme_mode"
        private const val KEY_COOLDOWN = "key_cooldown"
    }
}
