package com.example.peternguyen_comp304lab3_ex1.data

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import com.example.peternguyen_comp304lab3_ex1.data.activity.ActivityLog

@Dao
interface ActivityLogDao {
    @Insert
    suspend fun insert(log: ActivityLog)

    @Query("SELECT * FROM activity_log ORDER BY timestamp DESC")
    suspend fun getAllLogs(): List<ActivityLog>

    @Delete
    suspend fun delete(log: ActivityLog)
}