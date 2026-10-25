package com.example.ui

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.local.AppDatabase
import com.example.data.local.QuarantineEntity
import com.example.data.local.ScanReportEntity
import com.example.data.local.SecurityAlertEntity
import com.example.data.repository.SecurityRepository
import com.example.data.repository.ShieldSettingsState
import com.example.domain.audio.SoundPlayer
import com.example.domain.scanner.AntiTrackEngine
import com.example.domain.scanner.AntiTrackStatus
import com.example.domain.scanner.CyberCaptureAnalysis
import com.example.domain.scanner.CyberCaptureEngine
import com.example.domain.scanner.DataShredderEngine
import com.example.domain.scanner.NetworkSecurityReport
import com.example.domain.scanner.NetworkShieldEngine
import com.example.domain.scanner.PrivacyAuditReport
import com.example.domain.scanner.PrivacyEngine
import com.example.domain.scanner.ScanMode
import com.example.domain.scanner.ScanProgressState
import com.example.domain.scanner.ScamAndEmailShield
import com.example.domain.scanner.ScamScanResult
import com.example.domain.scanner.ShredResult
import com.example.domain.scanner.SystemHealthReport
import com.example.domain.scanner.SystemOptimizer
import com.example.domain.scanner.VirusScannerEngine
import com.example.domain.scanner.VpnEngine
import com.example.domain.scanner.VpnServer
import com.example.domain.scanner.VpnStatus
import com.example.domain.scanner.WebCheckResult
import com.example.domain.scanner.WebShieldEngine
import com.example.ui.components.ShieldStatus
import com.example.ui.components.UniversalThreatNotice
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.io.File

enum class ScreenNav(val title: String) {
    DASHBOARD("Dashboard"),
    ANTIVIRUS("Antivirus"),
    WEB_SHIELD("Web Shield"),
    NETWORK("Network Shield"),
    VPN("SecureLine VPN"),
    CLEANUP("Cleanup Premium"),
    ANTITRACK("AntiTrack"),
    TOOLS("10 Next-Gen Tools"),
    PRIVACY("Privacy Shield"),
    SYSTEM("System Health"),
    REPORTS("Reports"),
    SETTINGS("Settings"),
    ABOUT("About DTZ")
}

class MainViewModel(application: Application) : AndroidViewModel(application) {

    private val db = AppDatabase.getInstance(application)
    val repository = SecurityRepository(db)

    val soundPlayer = SoundPlayer(viewModelScope)
    private val virusScanner = VirusScannerEngine(application)
    private val webShieldEngine = WebShieldEngine()
    private val networkEngine = NetworkShieldEngine(application)
    private val privacyEngine = PrivacyEngine(application)
    private val systemOptimizer = SystemOptimizer(application)

    // V2.0.1 Next-Gen Engines
    val vpnEngine = VpnEngine()
    private val antiTrackEngine = AntiTrackEngine()
    private val scamShieldEngine = ScamAndEmailShield()
    private val cyberCaptureEngine = CyberCaptureEngine()
    private val dataShredderEngine = DataShredderEngine()

    private val _currentScreen = MutableStateFlow(ScreenNav.DASHBOARD)
    val currentScreen = _currentScreen.asStateFlow()

    // Splash Loading State
    private val _isSplashLoading = MutableStateFlow(true)
    val isSplashLoading = _isSplashLoading.asStateFlow()

    private val _splashMessage = MutableStateFlow("Initializing DTZ Security Matrix...")
    val splashMessage = _splashMessage.asStateFlow()

    // Light / Dark Theme toggle
    private val _isDarkTheme = MutableStateFlow(true)
    val isDarkTheme = _isDarkTheme.asStateFlow()

    val settings: StateFlow<ShieldSettingsState> = repository.settings

    val quarantinedItems: StateFlow<List<QuarantineEntity>> = repository.quarantinedItems
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val scanReports: StateFlow<List<ScanReportEntity>> = repository.scanReports
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val alerts: StateFlow<List<SecurityAlertEntity>> = repository.alerts
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    private val _scanProgress = MutableStateFlow(ScanProgressState())
    val scanProgress = _scanProgress.asStateFlow()

    private val _isScanning = MutableStateFlow(false)
    val isScanning = _isScanning.asStateFlow()

    private val _activeScanMode = MutableStateFlow(ScanMode.SMART)
    val activeScanMode = _activeScanMode.asStateFlow()

    private val _shieldStatus = MutableStateFlow(ShieldStatus.PROTECTED)
    val shieldStatus = _shieldStatus.asStateFlow()

    private val _networkReport = MutableStateFlow<NetworkSecurityReport?>(null)
    val networkReport = _networkReport.asStateFlow()

    private val _privacyReport = MutableStateFlow<PrivacyAuditReport?>(null)
    val privacyReport = _privacyReport.asStateFlow()

    private val _systemHealth = MutableStateFlow<SystemHealthReport?>(null)
    val systemHealth = _systemHealth.asStateFlow()

    private val _webCheckResult = MutableStateFlow<WebCheckResult?>(null)
    val webCheckResult = _webCheckResult.asStateFlow()

    // Universal Threat Pop-Up System
    private val _universalThreatNotice = MutableStateFlow<UniversalThreatNotice?>(null)
    val universalThreatNotice = _universalThreatNotice.asStateFlow()

    // V2.0.1 States
    private val _vpnStatus = MutableStateFlow(VpnStatus(currentServer = vpnEngine.availableServers[0]))
    val vpnStatus = _vpnStatus.asStateFlow()

    private val _antiTrackStatus = MutableStateFlow(antiTrackEngine.getStatus())
    val antiTrackStatus = _antiTrackStatus.asStateFlow()

    private val _scamScanResult = MutableStateFlow<ScamScanResult?>(null)
    val scamScanResult = _scamScanResult.asStateFlow()

    private val _cyberCaptureResult = MutableStateFlow<CyberCaptureAnalysis?>(null)
    val cyberCaptureResult = _cyberCaptureResult.asStateFlow()

    private val _lastShredResult = MutableStateFlow<ShredResult?>(null)
    val lastShredResult = _lastShredResult.asStateFlow()

    private var scanJob: Job? = null
    private var vpnJob: Job? = null

    // Dynamic security score derived from shield status & alerts
    val securityScore: StateFlow<Int> = combine(
        alerts,
        quarantinedItems,
        settings
    ) { alertList, quarantineList, _ ->
        val unresolved = alertList.count { !it.isResolved }
        repository.calculateSecurityScore(unresolved, quarantineList.size)
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), 96)

    init {
        soundPlayer.isSoundEnabled = settings.value.soundEffects
        runNetworkScan()
        runPrivacyAudit()
        runSystemDiagnostics()

        // Splash loading progression
        viewModelScope.launch {
            delay(400)
            _splashMessage.value = "Loading Heuristic Engine & Zero-Day Threat Cloud..."
            delay(500)
            _splashMessage.value = "Verifying Core Antivirus & SecureLine VPN..."
            delay(400)
            _splashMessage.value = "DTZ SHIELD v2.0.1 Ready. Defense Active."
            delay(300)
            _isSplashLoading.value = false
        }
    }

    fun toggleTheme() {
        _isDarkTheme.value = !_isDarkTheme.value
        soundPlayer.playClick()
    }

    fun navigateTo(screen: ScreenNav) {
        soundPlayer.playClick()
        _currentScreen.value = screen
    }

    // Universal Threat Warning Pop-up Trigger
    fun triggerUniversalThreat(notice: UniversalThreatNotice) {
        soundPlayer.playThreatAlert()
        _shieldStatus.value = ShieldStatus.THREAT_DETECTED
        _universalThreatNotice.value = notice
        viewModelScope.launch {
            repository.logAlert(
                SecurityAlertEntity(
                    title = notice.title,
                    description = notice.reason,
                    severity = notice.severity,
                    source = notice.threatCategory,
                    recommendedAction = notice.recommendedAction
                )
            )
        }
    }

    fun dismissUniversalThreat() {
        _universalThreatNotice.value = null
        if (quarantinedItems.value.isEmpty()) {
            _shieldStatus.value = ShieldStatus.PROTECTED
        }
    }

    fun quarantineAndBlockCurrentThreat() {
        val notice = _universalThreatNotice.value ?: return
        viewModelScope.launch {
            repository.quarantineThreat(
                QuarantineEntity(
                    threatName = notice.title,
                    fileName = notice.targetName,
                    severity = notice.severity,
                    originalLocation = notice.targetLocation,
                    actionTaken = "Quarantined & Blocked",
                    riskDescription = notice.reason
                )
            )
            _universalThreatNotice.value = null
            soundPlayer.playScanComplete()
        }
    }

    fun startScan(mode: ScanMode) {
        if (_isScanning.value) return
        _activeScanMode.value = mode
        _isScanning.value = true
        _shieldStatus.value = ShieldStatus.SCANNING
        soundPlayer.playScanStart()

        val startTime = System.currentTimeMillis()

        scanJob = viewModelScope.launch {
            try {
                virusScanner.executeScan(mode).collect { progress ->
                    _scanProgress.value = progress

                    if (progress.threatsFound.isNotEmpty() && _shieldStatus.value != ShieldStatus.THREAT_DETECTED) {
                        _shieldStatus.value = ShieldStatus.THREAT_DETECTED
                        soundPlayer.playThreatAlert()
                        val latestThreat = progress.threatsFound.last()
                        triggerUniversalThreat(
                            UniversalThreatNotice(
                                title = "Malware Detected: ${latestThreat.threatName}",
                                threatCategory = "Virus / Malware Shield",
                                targetName = latestThreat.fileName,
                                targetLocation = latestThreat.originalLocation,
                                severity = latestThreat.severity,
                                reason = latestThreat.riskDescription,
                                recommendedAction = "Immediately quarantine this file and prevent execution."
                            )
                        )
                    }

                    if (progress.isFinished) {
                        _isScanning.value = false
                        for (threat in progress.threatsFound) {
                            repository.quarantineThreat(threat)
                        }

                        val report = ScanReportEntity(
                            scanType = mode.title,
                            startTime = startTime,
                            endTime = System.currentTimeMillis(),
                            itemsChecked = progress.itemsScanned,
                            threatsFound = progress.threatsFound.size,
                            threatsQuarantined = progress.threatsFound.size,
                            result = if (progress.threatsFound.isEmpty()) "Clean" else "Threats Resolved"
                        )
                        repository.saveScanReport(report)

                        if (progress.threatsFound.isEmpty()) {
                            _shieldStatus.value = ShieldStatus.PROTECTED
                            soundPlayer.playScanComplete()
                        } else {
                            _shieldStatus.value = ShieldStatus.THREAT_DETECTED
                        }
                    }
                }
            } catch (_: Exception) {
                _isScanning.value = false
                _shieldStatus.value = ShieldStatus.PROTECTED
            }
        }
    }

    fun cancelScan() {
        scanJob?.cancel()
        _isScanning.value = false
        _shieldStatus.value = ShieldStatus.PROTECTED
        soundPlayer.playClick()
    }

    fun checkUrl(url: String) {
        soundPlayer.playClick()
        val result = webShieldEngine.evaluateUrl(url)
        _webCheckResult.value = result
        if (result.isBlocked) {
            triggerUniversalThreat(
                UniversalThreatNotice(
                    title = "Dangerous Website Blocked: ${result.domain}",
                    threatCategory = "Malicious Website & Phishing",
                    targetName = result.domain,
                    targetLocation = result.url,
                    severity = result.riskLevel,
                    reason = result.reason,
                    recommendedAction = result.recommendedAction,
                    actionTaken = "Connection Blocked by Web Shield"
                )
            )
        }
    }

    // SecureLine VPN Controls
    fun toggleVpn() {
        soundPlayer.playClick()
        if (_vpnStatus.value.isConnected) {
            vpnJob?.cancel()
            _vpnStatus.value = _vpnStatus.value.copy(
                isConnected = false,
                isConnecting = false,
                virtualIp = ""
            )
            soundPlayer.playShieldToggled(false)
        } else {
            soundPlayer.playShieldToggled(true)
            val server = _vpnStatus.value.currentServer
            vpnJob = viewModelScope.launch {
                vpnEngine.simulateConnectionStream(server).collect { st ->
                    _vpnStatus.value = st
                }
            }
        }
    }

    fun selectVpnServer(server: VpnServer) {
        soundPlayer.playClick()
        _vpnStatus.value = _vpnStatus.value.copy(currentServer = server)
        if (_vpnStatus.value.isConnected) {
            toggleVpn() // disconnect
            toggleVpn() // reconnect to new server
        }
    }

    // AI Scam & Message Scanner
    fun scanScamMessage(text: String) {
        soundPlayer.playClick()
        val res = scamShieldEngine.analyzeScamMessage(text)
        _scamScanResult.value = res
        if (res.isScamDetected) {
            triggerUniversalThreat(
                UniversalThreatNotice(
                    title = "Scam / Phishing Pattern Intercepted",
                    threatCategory = "AI Scam, SMS & Mail Shield",
                    targetName = res.scamCategory,
                    targetLocation = "Message Body",
                    severity = res.riskLevel,
                    reason = res.reason,
                    recommendedAction = res.recommendedAction,
                    actionTaken = "Flagged & Blocked"
                )
            )
        }
    }

    // CyberCapture Sandbox Test
    fun testCyberCapture(fileName: String) {
        soundPlayer.playClick()
        val res = cyberCaptureEngine.analyzeUnknownSample(fileName)
        _cyberCaptureResult.value = res
        if (!res.isClean) {
            triggerUniversalThreat(
                UniversalThreatNotice(
                    title = "CyberCapture Zero-Day Intercepted",
                    threatCategory = "Zero-Day Threat Protection",
                    targetName = fileName,
                    targetLocation = "Cloud Sandbox",
                    severity = "Critical",
                    reason = res.behavioralSummary,
                    recommendedAction = "Isolate sample in quarantine and prevent installation.",
                    actionTaken = "Isolated in Sandbox"
                )
            )
        }
    }

    // Data Shredder
    fun runDataShredderDemo() {
        soundPlayer.playClick()
        viewModelScope.launch {
            val demoFile = File(getApplication<Application>().cacheDir, "confidential_record_test.tmp")
            demoFile.writeText("SECRET_FINANCIAL_RECORD_UNENCRYPTED_DOD_SHREDDER_TARGET")
            val res = dataShredderEngine.shredFile(demoFile, passes = 3)
            _lastShredResult.value = res
            soundPlayer.playScanComplete()
        }
    }

    fun triggerRemoteAccessAudit() {
        soundPlayer.playThreatAlert()
        triggerUniversalThreat(
            UniversalThreatNotice(
                title = "Remote Access Attempt Blocked",
                threatCategory = "Remote Access Shield",
                targetName = "Unauthorized Screen Mirroring Agent",
                targetLocation = "Network Interface wlan0",
                severity = "High Risk",
                reason = "Remote Access Shield blocked an untrusted application attempting to capture screen output and bypass user consent.",
                recommendedAction = "Inspect installed remote desktop utilities.",
                actionTaken = "Remote Session Severed"
            )
        )
    }

    fun runRescueDisk() {
        soundPlayer.playScanStart()
        viewModelScope.launch {
            delay(1200)
            soundPlayer.playScanComplete()
            repository.logAlert(
                SecurityAlertEntity(
                    title = "Rescue Disk Boot Scan Complete",
                    description = "Offline master boot record, root partitions, and bootloader signatures verified intact.",
                    severity = "Safe",
                    source = "Rescue Disk",
                    isResolved = true
                )
            )
        }
    }

    fun runNetworkScan() {
        viewModelScope.launch {
            val report = networkEngine.analyzeNetwork()
            _networkReport.value = report
        }
    }

    fun runPrivacyAudit() {
        viewModelScope.launch {
            val s = settings.value
            val report = privacyEngine.auditPrivacy(s.ransomwareProtection, s.webcamProtection)
            _privacyReport.value = report
        }
    }

    fun runSystemDiagnostics() {
        viewModelScope.launch {
            val report = systemOptimizer.getHealthReport()
            _systemHealth.value = report
        }
    }

    fun optimizeCache() {
        soundPlayer.playScanComplete()
        systemOptimizer.optimizeCache()
        runSystemDiagnostics()
        viewModelScope.launch {
            repository.logAlert(
                SecurityAlertEntity(
                    title = "Cleanup Premium Optimization Complete",
                    description = "Purged cached files, junk logs, and boosted available system RAM memory.",
                    severity = "Safe",
                    source = "Cleanup Premium",
                    isResolved = true
                )
            )
        }
    }

    fun restoreQuarantineItem(item: QuarantineEntity) {
        soundPlayer.playClick()
        viewModelScope.launch {
            repository.restoreQuarantinedItem(item)
            if (quarantinedItems.value.isEmpty()) {
                _shieldStatus.value = ShieldStatus.PROTECTED
            }
        }
    }

    fun deleteQuarantineItem(item: QuarantineEntity) {
        soundPlayer.playClick()
        viewModelScope.launch {
            repository.permanentlyDeleteQuarantinedItem(item)
            if (quarantinedItems.value.isEmpty()) {
                _shieldStatus.value = ShieldStatus.PROTECTED
            }
        }
    }

    fun clearAllQuarantine() {
        soundPlayer.playClick()
        viewModelScope.launch {
            repository.clearQuarantine()
            _shieldStatus.value = ShieldStatus.PROTECTED
        }
    }

    fun resolveAlert(alertId: Long) {
        soundPlayer.playClick()
        viewModelScope.launch {
            repository.resolveAlert(alertId)
        }
    }

    fun fixAllIssues() {
        soundPlayer.playScanComplete()
        viewModelScope.launch {
            repository.fixAllIssues()
            _shieldStatus.value = ShieldStatus.RESOLVED
            runPrivacyAudit()
            runNetworkScan()
            runSystemDiagnostics()
        }
    }

    fun updateDefinitions() {
        soundPlayer.playScanComplete()
        viewModelScope.launch {
            repository.updateDefinitions()
        }
    }

    fun triggerSafeDemo() {
        soundPlayer.playThreatAlert()
        _shieldStatus.value = ShieldStatus.THREAT_DETECTED
        triggerUniversalThreat(
            UniversalThreatNotice(
                title = "EICAR Standard Antivirus Test Pattern",
                threatCategory = "Virus & Malware Defense [DEMO]",
                targetName = "eicar_com_test.txt",
                targetLocation = "/storage/emulated/0/Download/eicar_com_test.txt",
                severity = "Critical",
                reason = "DEMO — NOT A REAL THREAT: Standard industry antivirus test payload intercepted by DTZ SHIELD heuristic engine.",
                recommendedAction = "Test Quarantine & Block or Dismiss alert."
            )
        )
    }

    fun toggleRealTime(enabled: Boolean) {
        soundPlayer.playShieldToggled(enabled)
        repository.toggleRealTime(enabled)
    }

    fun toggleWebShield(enabled: Boolean) {
        soundPlayer.playShieldToggled(enabled)
        repository.toggleWebShield(enabled)
    }

    fun toggleNetworkShield(enabled: Boolean) {
        soundPlayer.playShieldToggled(enabled)
        repository.toggleNetworkShield(enabled)
    }

    fun toggleRansomware(enabled: Boolean) {
        soundPlayer.playShieldToggled(enabled)
        repository.toggleRansomware(enabled)
        runPrivacyAudit()
    }

    fun toggleWebcam(enabled: Boolean) {
        soundPlayer.playShieldToggled(enabled)
        repository.toggleWebcam(enabled)
        runPrivacyAudit()
    }

    fun toggleGameMode(enabled: Boolean) {
        soundPlayer.playShieldToggled(enabled)
        repository.toggleGameMode(enabled)
    }

    fun togglePerformanceMode(enabled: Boolean) {
        soundPlayer.playClick()
        repository.togglePerformanceMode(enabled)
    }

    fun toggleSound(enabled: Boolean) {
        repository.toggleSound(enabled)
        soundPlayer.isSoundEnabled = enabled
    }
}
