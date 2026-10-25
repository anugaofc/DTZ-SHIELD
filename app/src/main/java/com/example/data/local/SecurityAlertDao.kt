package com.example.data.local

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface SecurityAlertDao {
    @Query("SELECT * FROM security_alerts ORDER BY timestamp DESC")
    fun getAllAlerts(): Flow<List<SecurityAlertEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(alert: SecurityAlertEntity): Long

    @Update
    suspend fun update(alert: SecurityAlertEntity)

    @Query("UPDATE security_alerts SET isResolved = 1 WHERE id = :id")
    suspend fun markResolved(id: Long)

    @Query("UPDATE security_alerts SET isResolved = 1")
    suspend fun markAllResolved()

    @Delete
    suspend fun delete(alert: SecurityAlertEntity)

    @Query("DELETE FROM security_alerts")
    suspend fun clearAll()
}
