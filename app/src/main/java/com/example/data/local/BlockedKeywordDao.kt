package com.example.data.local

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.model.BlockedKeyword
import kotlinx.coroutines.flow.Flow

@Dao
interface BlockedKeywordDao {
    @Query("SELECT * FROM blocked_keywords ORDER BY createdAt DESC")
    fun getAllKeywords(): Flow<List<BlockedKeyword>>

    @Query("SELECT * FROM blocked_keywords WHERE isEnabled = 1")
    fun getActiveKeywords(): Flow<List<BlockedKeyword>>

    @Query("SELECT * FROM blocked_keywords WHERE isEnabled = 1")
    suspend fun getActiveKeywordsDirect(): List<BlockedKeyword>

    @Query("SELECT * FROM blocked_keywords WHERE id = :id")
    suspend fun getKeywordById(id: Long): BlockedKeyword?

    @Query("SELECT COUNT(*) FROM blocked_keywords")
    fun getKeywordCount(): Flow<Int>

    @Query("SELECT COUNT(*) FROM blocked_keywords WHERE isEnabled = 1")
    fun getActiveKeywordCount(): Flow<Int>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(keyword: BlockedKeyword): Long

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertAll(keywords: List<BlockedKeyword>): List<Long>

    @Update
    suspend fun update(keyword: BlockedKeyword)

    @Delete
    suspend fun delete(keyword: BlockedKeyword)

    @Query("DELETE FROM blocked_keywords WHERE id = :id")
    suspend fun deleteById(id: Long)

    @Query("UPDATE blocked_keywords SET isEnabled = :isEnabled WHERE id = :id")
    suspend fun setEnabled(id: Long, isEnabled: Boolean)

    @Query("UPDATE blocked_keywords SET isEnabled = :isEnabled")
    suspend fun setAllEnabled(isEnabled: Boolean)

    @Query("UPDATE blocked_keywords SET skipCount = skipCount + 1 WHERE id = :id")
    suspend fun incrementSkipCount(id: Long)

    @Query("DELETE FROM blocked_keywords")
    suspend fun deleteAll()

    @Query("DELETE FROM blocked_keywords WHERE category = :category")
    suspend fun deleteByCategory(category: String)
}
