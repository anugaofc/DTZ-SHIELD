package com.example.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "quarantine_items")
data class QuarantineEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val threatName: String,
    val fileName: String,
    val detectionDate: Long = System.currentTimeMillis(),
    val severity: String, // Critical, High Risk, Warning, Info
    val originalLocation: String,
    val actionTaken: String, // Quarantined, Restored, Deleted
    val fileHash: String = "",
    val riskDescription: String = ""
)
