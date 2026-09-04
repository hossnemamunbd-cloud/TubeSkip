package com.example.model

import androidx.room.Entity
import androidx.room.PrimaryKey

enum class MatchType {
    CONTAINS,
    EXACT,
    STARTS_WITH,
    REGEX
}

enum class TargetArea {
    ALL,
    TITLE_ONLY,
    DESCRIPTION_ONLY,
    CHANNEL_ONLY
}

@Entity(tableName = "blocked_keywords")
data class BlockedKeyword(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val keyword: String,
    val category: String = "General",
    val matchType: MatchType = MatchType.CONTAINS,
    val targetArea: TargetArea = TargetArea.ALL,
    val isEnabled: Boolean = true,
    val isCaseSensitive: Boolean = false,
    val skipCount: Int = 0,
    val createdAt: Long = System.currentTimeMillis()
)
