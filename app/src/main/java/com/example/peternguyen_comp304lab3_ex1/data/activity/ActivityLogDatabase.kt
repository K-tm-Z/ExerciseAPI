package com.example.peternguyen_comp304lab3_ex1.data.activity

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.peternguyen_comp304lab3_ex1.data.ActivityLogDao

@Database(entities = [ActivityLog::class], version = 2)
abstract class ActivityLogDatabase : RoomDatabase() {
    abstract fun activityLogDao(): ActivityLogDao

    companion object {
        @Volatile
        private var INSTANCE: ActivityLogDatabase? = null

        fun getDatabase(context: Context): ActivityLogDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    ActivityLogDatabase::class.java,
                    "activity_log_db"
                )
                    .fallbackToDestructiveMigration()
                    .build()
                INSTANCE = instance
                instance
            }
        }
    }
}