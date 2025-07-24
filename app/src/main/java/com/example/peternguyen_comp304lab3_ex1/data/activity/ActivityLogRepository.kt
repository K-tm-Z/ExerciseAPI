package com.example.peternguyen_comp304lab3_ex1.data.activity

import com.example.peternguyen_comp304lab3_ex1.data.ActivityLogDao

class ActivityLogRepository(private val dao: ActivityLogDao) {
    suspend fun getAllLogs(): List<ActivityLog> = dao.getAllLogs()
    suspend fun insert(log: ActivityLog) = dao.insert(log)
    suspend fun delete(log: ActivityLog) = dao.delete(log)
}