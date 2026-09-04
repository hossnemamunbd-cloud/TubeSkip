package com.example.data.local

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.model.SkipLog
import kotlinx.coroutines.flow.Flow

@Dao
interface SkipLogDao {
    @Query("SELECT * FROM skip_logs ORDER BY timestamp DESC LIMIT 200")
    fun getAllLogs(): Flow<List<SkipLog>>

    @Query("SELECT * FROM skip_logs ORDER BY timestamp DESC LIMIT 5")
    fun getRecentLogs(): Flow<List<SkipLog>>

    @Query("SELECT COUNT(*) FROM skip_logs")
    fun getTotalSkipCount(): Flow<Int>

    @Query("SELECT COUNT(*) FROM skip_logs WHERE videoType = 'SHORTS'")
    fun getShortsSkipCount(): Flow<Int>

    @Query("SELECT COUNT(*) FROM skip_logs WHERE videoType = 'VIDEO'")
    fun getVideoSkipCount(): Flow<Int>

    @Query("SELECT COUNT(*) FROM skip_logs WHERE timestamp >= :sinceTimestamp")
    fun getSkipsSince(sinceTimestamp: Long): Flow<Int>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(log: SkipLog): Long

    @Delete
    suspend fun delete(log: SkipLog)

    @Query("DELETE FROM skip_logs WHERE id = :id")
    suspend fun deleteById(id: Long)

    @Query("DELETE FROM skip_logs")
    suspend fun clearAllLogs()
}
