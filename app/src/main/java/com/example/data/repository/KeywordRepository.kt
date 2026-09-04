package com.example.data.repository

import com.example.data.local.BlockedKeywordDao
import com.example.data.local.SkipLogDao
import com.example.model.BlockedKeyword
import com.example.model.CuratedPack
import com.example.model.MatchType
import com.example.model.SkipLog
import com.example.model.TargetArea
import com.example.model.VideoType
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import org.json.JSONArray
import org.json.JSONObject

class KeywordRepository(
    private val keywordDao: BlockedKeywordDao,
    private val skipLogDao: SkipLogDao
) {
    val allKeywords: Flow<List<BlockedKeyword>> = keywordDao.getAllKeywords()
    val activeKeywords: Flow<List<BlockedKeyword>> = keywordDao.getActiveKeywords()
    val keywordCount: Flow<Int> = keywordDao.getKeywordCount()
    val activeKeywordCount: Flow<Int> = keywordDao.getActiveKeywordCount()

    val allLogs: Flow<List<SkipLog>> = skipLogDao.getAllLogs()
    val recentLogs: Flow<List<SkipLog>> = skipLogDao.getRecentLogs()
    val totalSkipCount: Flow<Int> = skipLogDao.getTotalSkipCount()
    val shortsSkipCount: Flow<Int> = skipLogDao.getShortsSkipCount()
    val videoSkipCount: Flow<Int> = skipLogDao.getVideoSkipCount()

    // Calculated time saved: assume average 45s saved per skipped item
    val estimatedMinutesSaved: Flow<Int> = totalSkipCount.map { count ->
        (count * 45) / 60
    }

    suspend fun addKeyword(keyword: BlockedKeyword): Long {
        return keywordDao.insert(keyword)
    }

    suspend fun updateKeyword(keyword: BlockedKeyword) {
        keywordDao.update(keyword)
    }

    suspend fun deleteKeyword(keyword: BlockedKeyword) {
        keywordDao.delete(keyword)
    }

    suspend fun deleteKeywordById(id: Long) {
        keywordDao.deleteById(id)
    }

    suspend fun setKeywordEnabled(id: Long, isEnabled: Boolean) {
        keywordDao.setEnabled(id, isEnabled)
    }

    suspend fun setAllKeywordsEnabled(isEnabled: Boolean) {
        keywordDao.setAllEnabled(isEnabled)
    }

    suspend fun addCuratedPack(pack: CuratedPack): Int {
        val keywordsToAdd = pack.keywords.map { word ->
            BlockedKeyword(
                keyword = word.trim(),
                category = pack.categoryName,
                matchType = MatchType.CONTAINS,
                targetArea = TargetArea.ALL,
                isEnabled = true
            )
        }
        val inserted = keywordDao.insertAll(keywordsToAdd)
        return inserted.count { it != -1L }
    }

    suspend fun removeCuratedPack(pack: CuratedPack) {
        keywordDao.deleteByCategory(pack.categoryName)
    }

    suspend fun recordSkip(
        title: String,
        matchedKeyword: String,
        videoType: VideoType,
        channelName: String = "",
        keywordId: Long? = null
    ) {
        skipLogDao.insert(
            SkipLog(
                videoTitle = title,
                matchedKeyword = matchedKeyword,
                videoType = videoType,
                channelName = channelName
            )
        )
        if (keywordId != null && keywordId > 0) {
            keywordDao.incrementSkipCount(keywordId)
        }
    }

    suspend fun clearAllLogs() {
        skipLogDao.clearAllLogs()
    }

    suspend fun exportKeywordsJson(keywords: List<BlockedKeyword>): String {
        val jsonArray = JSONArray()
        for (kw in keywords) {
            val obj = JSONObject().apply {
                put("keyword", kw.keyword)
                put("category", kw.category)
                put("matchType", kw.matchType.name)
                put("targetArea", kw.targetArea.name)
                put("isEnabled", kw.isEnabled)
                put("isCaseSensitive", kw.isCaseSensitive)
            }
            jsonArray.put(obj)
        }
        return jsonArray.toString(2)
    }

    suspend fun importKeywordsJson(jsonString: String): Int {
        return try {
            val jsonArray = JSONArray(jsonString)
            val list = mutableListOf<BlockedKeyword>()
            for (i in 0 until jsonArray.length()) {
                val obj = jsonArray.getJSONObject(i)
                val kw = BlockedKeyword(
                    keyword = obj.getString("keyword"),
                    category = obj.optString("category", "Imported"),
                    matchType = try {
                        MatchType.valueOf(obj.optString("matchType", MatchType.CONTAINS.name))
                    } catch (e: Exception) {
                        MatchType.CONTAINS
                    },
                    targetArea = try {
                        TargetArea.valueOf(obj.optString("targetArea", TargetArea.ALL.name))
                    } catch (e: Exception) {
                        TargetArea.ALL
                    },
                    isEnabled = obj.optBoolean("isEnabled", true),
                    isCaseSensitive = obj.optBoolean("isCaseSensitive", false)
                )
                list.add(kw)
            }
            val inserted = keywordDao.insertAll(list)
            inserted.count { it != -1L }
        } catch (e: Exception) {
            0
        }
    }
}
