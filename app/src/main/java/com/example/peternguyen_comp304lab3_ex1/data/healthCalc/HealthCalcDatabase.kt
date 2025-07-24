package com.example.peternguyen_comp304lab3_ex1.data.healthCalc

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities= [HealthCalc::class], version = 1)
abstract class HealthCalcDatabase : RoomDatabase() {
    abstract fun healthCalcDao(): HealthCalcDao
    companion object {
        @Volatile
        private var INSTANCE: HealthCalcDatabase? = null

        fun getDatabase(context: Context): HealthCalcDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    HealthCalcDatabase::class.java,
                    "health_calc_database"
                ).build()
                INSTANCE = instance
                instance
            }
        }
    }
}