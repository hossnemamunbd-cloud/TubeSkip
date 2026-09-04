package com.example.ui.viewmodel

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.data.repository.AppSettings
import com.example.data.repository.AppThemeMode
import com.example.data.repository.KeywordRepository
import com.example.data.repository.SettingsRepository
import com.example.model.BlockedKeyword
import com.example.model.CuratedPack
import com.example.model.MatchType
import com.example.model.SkipLog
import com.example.model.TargetArea
import com.example.service.YouTubeBlockerAccessibilityService
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

data class SimulatorResult(
    val isMatched: Boolean,
    val matchedKeyword: BlockedKeyword? = null,
    val testedText: String = ""
)

class MainViewModel(
    private val keywordRepository: KeywordRepository,
    private val settingsRepository: SettingsRepository
) : ViewModel() {

    val allKeywords: StateFlow<List<BlockedKeyword>> = keywordRepository.allKeywords
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val activeKeywords: StateFlow<List<BlockedKeyword>> = keywordRepository.activeKeywords
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val keywordCount: StateFlow<Int> = keywordRepository.keywordCount
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), 0)

    val activeKeywordCount: StateFlow<Int> = keywordRepository.activeKeywordCount
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), 0)

    val allLogs: StateFlow<List<SkipLog>> = keywordRepository.allLogs
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val recentLogs: StateFlow<List<SkipLog>> = keywordRepository.recentLogs
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val totalSkipCount: StateFlow<Int> = keywordRepository.totalSkipCount
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), 0)

    val shortsSkipCount: StateFlow<Int> = keywordRepository.shortsSkipCount
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), 0)

    val videoSkipCount: StateFlow<Int> = keywordRepository.videoSkipCount
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), 0)

    val estimatedMinutesSaved: StateFlow<Int> = keywordRepository.estimatedMinutesSaved
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), 0)

    val settings: StateFlow<AppSettings> = settingsRepository.settings

    private val _isServiceEnabled = MutableStateFlow(false)
    val isServiceEnabled: StateFlow<Boolean> = _isServiceEnabled.asStateFlow()

    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery.asStateFlow()

    private val _selectedCategory = MutableStateFlow("ALL")
    val selectedCategory: StateFlow<String> = _selectedCategory.asStateFlow()

    val filteredKeywords: StateFlow<List<BlockedKeyword>> = combine(
        allKeywords,
        _searchQuery,
        _selectedCategory
    ) { keywords, query, category ->
        keywords.filter { kw ->
            val matchesQuery = query.isBlank() || kw.keyword.contains(query, ignoreCase = true) || kw.category.contains(query, ignoreCase = true)
            val matchesCat = category == "ALL" || kw.category.equals(category, ignoreCase = true)
            matchesQuery && matchesCat
        }
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    private val _simInputText = MutableStateFlow("")
    val simInputText: StateFlow<String> = _simInputText.asStateFlow()

    private val _simResult = MutableStateFlow<SimulatorResult?>(null)
    val simResult: StateFlow<SimulatorResult?> = _simResult.asStateFlow()

    fun checkServiceStatus(context: Context) {
        val running = YouTubeBlockerAccessibilityService.isAccessibilityServiceEnabled(context)
        _isServiceEnabled.value = running
    }

    fun setSearchQuery(query: String) {
        _searchQuery.value = query
    }

    fun setSelectedCategory(cat: String) {
        _selectedCategory.value = cat
    }

    fun setSimInputText(text: String) {
        _simInputText.value = text
        if (text.isBlank()) {
            _simResult.value = null
        }
    }

    fun runSimulatorTest() {
        val text = _simInputText.value.trim()
        if (text.isBlank()) {
            _simResult.value = null
            return
        }

        val active = activeKeywords.value
        for (kw in active) {
            val pattern = kw.keyword.trim()
            if (pattern.isEmpty()) continue

            val src = if (kw.isCaseSensitive) text else text.lowercase()
            val tgt = if (kw.isCaseSensitive) pattern else pattern.lowercase()

            val isMatch = when (kw.matchType) {
                MatchType.CONTAINS -> src.contains(tgt)
                MatchType.EXACT -> src.equals(tgt, ignoreCase = !kw.isCaseSensitive)
                MatchType.STARTS_WITH -> src.startsWith(tgt)
                MatchType.REGEX -> try {
                    val regex = if (kw.isCaseSensitive) Regex(pattern) else Regex(pattern, RegexOption.IGNORE_CASE)
                    regex.containsMatchIn(text)
                } catch (e: Exception) {
                    false
                }
            }

            if (isMatch) {
                _simResult.value = SimulatorResult(isMatched = true, matchedKeyword = kw, testedText = text)
                return
            }
        }

        _simResult.value = SimulatorResult(isMatched = false, matchedKeyword = null, testedText = text)
    }

    fun addKeyword(
        keyword: String,
        category: String = "General",
        matchType: MatchType = MatchType.CONTAINS,
        targetArea: TargetArea = TargetArea.ALL,
        isCaseSensitive: Boolean = false
    ) {
        if (keyword.isBlank()) return
        viewModelScope.launch {
            keywordRepository.addKeyword(
                BlockedKeyword(
                    keyword = keyword.trim(),
                    category = category.trim().ifBlank { "General" },
                    matchType = matchType,
                    targetArea = targetArea,
                    isCaseSensitive = isCaseSensitive,
                    isEnabled = true
                )
            )
        }
    }

    fun updateKeyword(keyword: BlockedKeyword) {
        viewModelScope.launch {
            keywordRepository.updateKeyword(keyword)
        }
    }

    fun deleteKeyword(keyword: BlockedKeyword) {
        viewModelScope.launch {
            keywordRepository.deleteKeyword(keyword)
        }
    }

    fun toggleKeyword(id: Long, isEnabled: Boolean) {
        viewModelScope.launch {
            keywordRepository.setKeywordEnabled(id, isEnabled)
        }
    }

    fun toggleAllKeywords(isEnabled: Boolean) {
        viewModelScope.launch {
            keywordRepository.setAllKeywordsEnabled(isEnabled)
        }
    }

    fun installCuratedPack(pack: CuratedPack, onComplete: (Int) -> Unit = {}) {
        viewModelScope.launch {
            val count = keywordRepository.addCuratedPack(pack)
            onComplete(count)
        }
    }

    fun removeCuratedPack(pack: CuratedPack) {
        viewModelScope.launch {
            keywordRepository.removeCuratedPack(pack)
        }
    }

    fun clearAllLogs() {
        viewModelScope.launch {
            keywordRepository.clearAllLogs()
        }
    }

    fun setMasterActive(active: Boolean) {
        settingsRepository.setMasterActive(active)
    }

    fun setSkipFullVideos(enabled: Boolean) {
        settingsRepository.setSkipFullVideos(enabled)
    }

    fun setSkipShorts(enabled: Boolean) {
        settingsRepository.setSkipShorts(enabled)
    }

    fun setShowSkipToast(enabled: Boolean) {
        settingsRepository.setShowSkipToast(enabled)
    }

    fun setLanguage(lang: String) {
        settingsRepository.setLanguage(lang)
    }

    fun setThemeMode(theme: AppThemeMode) {
        settingsRepository.setThemeMode(theme)
    }

    suspend fun exportJson(): String {
        return keywordRepository.exportKeywordsJson(allKeywords.value)
    }

    suspend fun importJson(json: String): Int {
        return keywordRepository.importKeywordsJson(json)
    }
}

class MainViewModelFactory(
    private val keywordRepository: KeywordRepository,
    private val settingsRepository: SettingsRepository
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(MainViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return MainViewModel(keywordRepository, settingsRepository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
