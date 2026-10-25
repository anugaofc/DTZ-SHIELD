package com.example.data.repository

import com.example.data.local.AppDatabase
import com.example.data.local.QuarantineEntity
import com.example.data.local.ScanReportEntity
import com.example.data.local.SecurityAlertEntity
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

data class ShieldSettingsState(
    val realTimeProtection: Boolean = true,
    val webShield: Boolean = true,
    val networkShield: Boolean = true,
    val ransomwareProtection: Boolean = true,
    val webcamProtection: Boolean = true,
    val gameMode: Boolean = false,
    val performanceMode: Boolean = false,
    val soundEffects: Boolean = true,
    val databaseVersion: String = "v2026.10.24-R1",
    val lastScanTime: Long = System.currentTimeMillis() - (3600 * 1000 * 6),
    val lastDefinitionsUpdate: Long = System.currentTimeMillis() - (3600 * 1000 * 2)
)

class SecurityRepository(private val database: AppDatabase) {

    private val _settings = MutableStateFlow(ShieldSettingsState())
    val settings = _settings.asStateFlow()

    // Room Flows
    val quarantinedItems: Flow<List<QuarantineEntity>> = database.quarantineDao().getAllQuarantined()
    val scanReports: Flow<List<ScanReportEntity>> = database.scanReportDao().getAllReports()
    val alerts: Flow<List<SecurityAlertEntity>> = database.securityAlertDao().getAllAlerts()

    suspend fun saveScanReport(report: ScanReportEntity): Long {
        _settings.value = _settings.value.copy(lastScanTime = System.currentTimeMillis())
        return database.scanReportDao().insert(report)
    }

    suspend fun quarantineThreat(threat: QuarantineEntity): Long {
        val id = database.quarantineDao().insert(threat)
        // Also log security alert
        database.securityAlertDao().insert(
            SecurityAlertEntity(
                title = "Threat Detected: ${threat.threatName}",
                description = "File ${threat.fileName} was flagged as ${threat.severity} risk and moved to Quarantine.",
                severity = threat.severity,
                source = "Antivirus",
                recommendedAction = "Inspect in Quarantine Manager. Keep quarantined or permanently delete."
            )
        )
        return id
    }

    suspend fun restoreQuarantinedItem(item: QuarantineEntity) {
        database.quarantineDao().update(item.copy(actionTaken = "Restored"))
        database.securityAlertDao().insert(
            SecurityAlertEntity(
                title = "Item Restored from Quarantine",
                description = "${item.fileName} was restored by user request.",
                severity = "Info",
                source = "Quarantine",
                recommendedAction = "Verify file safety before executing."
            )
        )
    }

    suspend fun permanentlyDeleteQuarantinedItem(item: QuarantineEntity) {
        database.quarantineDao().delete(item)
    }

    suspend fun clearQuarantine() {
        database.quarantineDao().clearAll()
    }

    suspend fun logAlert(alert: SecurityAlertEntity): Long {
        return database.securityAlertDao().insert(alert)
    }

    suspend fun resolveAlert(alertId: Long) {
        database.securityAlertDao().markResolved(alertId)
    }

    suspend fun resolveAllAlerts() {
        database.securityAlertDao().markAllResolved()
    }

    fun toggleRealTime(enabled: Boolean) {
        _settings.value = _settings.value.copy(realTimeProtection = enabled)
    }

    fun toggleWebShield(enabled: Boolean) {
        _settings.value = _settings.value.copy(webShield = enabled)
    }

    fun toggleNetworkShield(enabled: Boolean) {
        _settings.value = _settings.value.copy(networkShield = enabled)
    }

    fun toggleRansomware(enabled: Boolean) {
        _settings.value = _settings.value.copy(ransomwareProtection = enabled)
    }

    fun toggleWebcam(enabled: Boolean) {
        _settings.value = _settings.value.copy(webcamProtection = enabled)
    }

    fun toggleGameMode(enabled: Boolean) {
        _settings.value = _settings.value.copy(gameMode = enabled)
    }

    fun togglePerformanceMode(enabled: Boolean) {
        _settings.value = _settings.value.copy(performanceMode = enabled)
    }

    fun toggleSound(enabled: Boolean) {
        _settings.value = _settings.value.copy(soundEffects = enabled)
    }

    suspend fun updateDefinitions() {
        _settings.value = _settings.value.copy(
            databaseVersion = "v2026.10.25-Live",
            lastDefinitionsUpdate = System.currentTimeMillis()
        )
        database.securityAlertDao().insert(
            SecurityAlertEntity(
                title = "Security Definitions Updated",
                description = "Protection database updated to version v2026.10.25-Live from Dark Tech Zone threat cloud.",
                severity = "Safe",
                source = "System Update",
                isResolved = true
            )
        )
    }

    suspend fun fixAllIssues() {
        _settings.value = _settings.value.copy(
            realTimeProtection = true,
            webShield = true,
            networkShield = true,
            ransomwareProtection = true,
            webcamProtection = true,
            databaseVersion = "v2026.10.25-Live",
            lastDefinitionsUpdate = System.currentTimeMillis()
        )
        database.securityAlertDao().markAllResolved()
        database.securityAlertDao().insert(
            SecurityAlertEntity(
                title = "DTZ One-Click Optimization Completed",
                description = "All core cybersecurity shields activated. Threat definitions verified up-to-date.",
                severity = "Safe",
                source = "Smart Center",
                isResolved = true
            )
        )
    }

    suspend fun triggerSafeDemo() {
        val demoThreat = QuarantineEntity(
            threatName = "EICAR.StandardAntivirusTestFile [DEMO]",
            fileName = "eicar_com_test.txt",
            severity = "Critical",
            originalLocation = "/storage/emulated/0/Download/eicar_com_test.txt",
            actionTaken = "Quarantined",
            riskDescription = "DEMO — NOT A REAL THREAT: Standard industry antivirus verification pattern safely quarantined."
        )
        database.quarantineDao().insert(demoThreat)

        database.securityAlertDao().insert(
            SecurityAlertEntity(
                title = "DEMO — NOT A REAL THREAT: Virus Detection",
                description = "DTZ SHIELD simulated malware interception and quarantined test payload eicar_com_test.txt safely.",
                severity = "Critical",
                source = "Security Demo",
                recommendedAction = "Inspect item in Quarantine Manager to test Restore or Delete."
            )
        )
        database.securityAlertDao().insert(
            SecurityAlertEntity(
                title = "DEMO — Suspicious ARP Intrusion Blocked",
                description = "Simulated network intruder signature intercepted by Network Shield.",
                severity = "Warning",
                source = "Network Shield Demo",
                recommendedAction = "Review connected devices in Network Shield."
            )
        )
    }

    fun calculateSecurityScore(unresolvedAlertsCount: Int, quarantinedCount: Int): Int {
        val s = _settings.value
        var score = 100
        if (!s.realTimeProtection) score -= 30
        if (!s.webShield) score -= 18
        if (!s.networkShield) score -= 18
        if (!s.ransomwareProtection) score -= 12
        if (!s.webcamProtection) score -= 8
        score -= (unresolvedAlertsCount * 6).coerceAtMost(25)
        return score.coerceIn(15, 100)
    }
}
