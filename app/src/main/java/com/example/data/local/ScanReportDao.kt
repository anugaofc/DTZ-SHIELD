package com.example.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface ScanReportDao {
    @Query("SELECT * FROM scan_reports ORDER BY startTime DESC")
    fun getAllReports(): Flow<List<ScanReportEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(report: ScanReportEntity): Long

    @Query("SELECT COUNT(*) FROM scan_reports")
    suspend fun getScanCount(): Int

    @Query("DELETE FROM scan_reports")
    suspend fun clearAll()
}
