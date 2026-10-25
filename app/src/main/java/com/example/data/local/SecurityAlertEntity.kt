package com.example.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "security_alerts")
data class SecurityAlertEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val title: String,
    val description: String,
    val severity: String, // Safe, Info, Warning, High Risk, Critical
    val source: String, // Antivirus, Web Shield, Network, Privacy, System
    val timestamp: Long = System.currentTimeMillis(),
    val isResolved: Boolean = false,
    val recommendedAction: String = ""
)
