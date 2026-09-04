package com.example.model

import androidx.room.Entity
import androidx.room.PrimaryKey

enum class VideoType {
    VIDEO,
    SHORTS
}

@Entity(tableName = "skip_logs")
data class SkipLog(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val videoTitle: String,
    val matchedKeyword: String,
    val videoType: VideoType = VideoType.VIDEO,
    val channelName: String = "",
    val timestamp: Long = System.currentTimeMillis()
)
