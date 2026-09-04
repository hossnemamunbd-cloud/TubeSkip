package com.example.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import androidx.sqlite.db.SupportSQLiteDatabase
import com.example.model.BlockedKeyword
import com.example.model.MatchType
import com.example.model.SkipLog
import com.example.model.TargetArea
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@Database(
    entities = [BlockedKeyword::class, SkipLog::class],
    version = 1,
    exportSchema = false
)
@TypeConverters(Converters::class)
abstract class AppDatabase : RoomDatabase() {

    abstract fun blockedKeywordDao(): BlockedKeywordDao
    abstract fun skipLogDao(): SkipLogDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "tubeskip_database.db"
                )
                    .addCallback(DatabaseCallback())
                    .fallbackToDestructiveMigration()
                    .build()
                INSTANCE = instance
                instance
            }
        }

        private class DatabaseCallback : Callback() {
            override fun onCreate(db: SupportSQLiteDatabase) {
                super.onCreate(db)
                INSTANCE?.let { database ->
                    CoroutineScope(Dispatchers.IO).launch {
                        populateInitialKeywords(database.blockedKeywordDao())
                    }
                }
            }

            private suspend fun populateInitialKeywords(dao: BlockedKeywordDao) {
                val initialKeywords = listOf(
                    BlockedKeyword(
                        keyword = "দেখুন কি হলো",
                        category = "Clickbait",
                        matchType = MatchType.CONTAINS,
                        targetArea = TargetArea.ALL
                    ),
                    BlockedKeyword(
                        keyword = "বিশ্বাস করতে পারবেন না",
                        category = "Clickbait",
                        matchType = MatchType.CONTAINS,
                        targetArea = TargetArea.ALL
                    ),
                    BlockedKeyword(
                        keyword = "You Won't Believe",
                        category = "Clickbait",
                        matchType = MatchType.CONTAINS,
                        targetArea = TargetArea.ALL
                    ),
                    BlockedKeyword(
                        keyword = "Ending Explained",
                        category = "Spoilers",
                        matchType = MatchType.CONTAINS,
                        targetArea = TargetArea.TITLE_ONLY
                    ),
                    BlockedKeyword(
                        keyword = "Skibidi",
                        category = "Distraction",
                        matchType = MatchType.CONTAINS,
                        targetArea = TargetArea.ALL
                    )
                )
                dao.insertAll(initialKeywords)
            }
        }
    }
}
