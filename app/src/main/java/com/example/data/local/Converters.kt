package com.example.data.local

import androidx.room.TypeConverter
import com.example.model.MatchType
import com.example.model.TargetArea
import com.example.model.VideoType

class Converters {
    @TypeConverter
    fun fromMatchType(value: MatchType): String = value.name

    @TypeConverter
    fun toMatchType(value: String): MatchType = try {
        MatchType.valueOf(value)
    } catch (e: Exception) {
        MatchType.CONTAINS
    }

    @TypeConverter
    fun fromTargetArea(value: TargetArea): String = value.name

    @TypeConverter
    fun toTargetArea(value: String): TargetArea = try {
        TargetArea.valueOf(value)
    } catch (e: Exception) {
        TargetArea.ALL
    }

    @TypeConverter
    fun fromVideoType(value: VideoType): String = value.name

    @TypeConverter
    fun toVideoType(value: String): VideoType = try {
        VideoType.valueOf(value)
    } catch (e: Exception) {
        VideoType.VIDEO
    }
}
