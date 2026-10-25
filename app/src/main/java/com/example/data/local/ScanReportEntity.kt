package com.example.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "scan_reports")
data class ScanReportEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val scanType: String,
    val startTime: Long,
    val endTime: Long,
    val itemsChecked: Int,
    val threatsFound: Int,
    val threatsQuarantined: Int,
    val result: String // Clean, Resolved, Threats Detected
)
