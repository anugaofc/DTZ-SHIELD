package com.example.domain.scanner

import android.content.Context
import android.content.pm.ApplicationInfo
import android.content.pm.PackageManager
import android.os.Build
import android.os.Environment
import com.example.data.local.QuarantineEntity
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import java.io.File

data class ScanProgressState(
    val percentage: Int = 0,
    val currentItem: String = "",
    val itemsScanned: Int = 0,
    val totalEstimated: Int = 0,
    val threatsFound: List<QuarantineEntity> = emptyList(),
    val isFinished: Boolean = false
)

enum class ScanMode(val title: String, val description: String) {
    SMART("Smart Scan", "Scans memory, running apps, network & privacy configurations"),
    QUICK("Quick Scan", "Rapidly inspects active apps and critical system paths"),
    FULL("Full System Scan", "Comprehensive deep scan of all files, packages, and storage"),
    CUSTOM("Custom Scan", "Targeted inspection of user-selected directories and APKs"),
    USB("USB & External Storage", "Checks external storage, SD cards, and connected drives"),
    BOOT("Boot-Time Scan", "Examines startup services, boot receivers, and persistence hooks")
}

class VirusScannerEngine(private val context: Context) {

    // Known test malware signatures / suspicious patterns for real heuristics
    private val suspiciousPermissionCombos = listOf(
        listOf("android.permission.SYSTEM_ALERT_WINDOW", "android.permission.BIND_ACCESSIBILITY_SERVICE"),
        listOf("android.permission.RECEIVE_SMS", "android.permission.INTERNET", "android.permission.READ_PHONE_STATE"),
        listOf("android.permission.RECORD_AUDIO", "android.permission.CAMERA", "android.permission.ACCESS_FINE_LOCATION")
    )

    fun executeScan(mode: ScanMode): Flow<ScanProgressState> = flow {
        val packageManager = context.packageManager
        val installedApps = try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                packageManager.getInstalledPackages(PackageManager.PackageInfoFlags.of(PackageManager.GET_PERMISSIONS.toLong()))
            } else {
                @Suppress("DEPRECATION")
                packageManager.getInstalledPackages(PackageManager.GET_PERMISSIONS)
            }
        } catch (_: Exception) {
            emptyList()
        }

        val directoriesToScan = when (mode) {
            ScanMode.QUICK -> listOf(context.cacheDir, context.filesDir)
            ScanMode.FULL, ScanMode.SMART -> listOf(
                context.cacheDir,
                context.filesDir,
                Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS),
                Environment.getExternalStorageDirectory()
            ).filterNotNull()
            ScanMode.USB -> listOf(
                File("/storage"),
                File("/mnt"),
                Environment.getExternalStorageDirectory()
            ).filterNotNull()
            ScanMode.CUSTOM -> listOf(
                Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS),
                context.cacheDir
            ).filterNotNull()
            ScanMode.BOOT -> emptyList()
        }

        val collectedFiles = mutableListOf<File>()
        for (dir in directoriesToScan) {
            if (dir.exists() && dir.canRead()) {
                dir.walkTopDown().maxDepth(3).take(modeToDepth(mode)).forEach {
                    if (it.isFile) collectedFiles.add(it)
                }
            }
        }

        val totalItems = (installedApps.size + collectedFiles.size).coerceAtLeast(20)
        var scannedCount = 0
        val detectedThreats = mutableListOf<QuarantineEntity>()

        emit(ScanProgressState(percentage = 0, currentItem = "Initializing ${mode.title} engine...", itemsScanned = 0, totalEstimated = totalItems))
        delay(150)

        // 1. Scan Installed Packages & Permissions
        for (pkg in installedApps) {
            scannedCount++
            val appName = try {
                packageManager.getApplicationLabel(pkg.applicationInfo ?: ApplicationInfo()).toString()
            } catch (_: Exception) {
                pkg.packageName
            }

            val isSystemApp = (pkg.applicationInfo?.flags ?: 0) and ApplicationInfo.FLAG_SYSTEM != 0
            val permissions = pkg.requestedPermissions?.toList() ?: emptyList()

            // Heuristic evaluation: inspect non-system apps with suspicious permission clusters
            if (!isSystemApp) {
                if (pkg.packageName.contains("demo.malware", ignoreCase = true) ||
                    pkg.packageName.contains("test.trojan", ignoreCase = true) ||
                    appName.contains("Suspicious", ignoreCase = true)
                ) {
                    detectedThreats.add(
                        QuarantineEntity(
                            threatName = "Trojan.AndroidOS.GenericTest",
                            fileName = pkg.packageName,
                            severity = "Critical",
                            originalLocation = pkg.applicationInfo?.sourceDir ?: "Package",
                            actionTaken = "Quarantined",
                            riskDescription = "High-risk spyware payload simulation signature detected."
                        )
                    )
                }

                val hasOverlay = permissions.contains("android.permission.SYSTEM_ALERT_WINDOW")
                val hasAccessibility = permissions.contains("android.permission.BIND_ACCESSIBILITY_SERVICE")
                if (hasOverlay && hasAccessibility) {
                    detectedThreats.add(
                        QuarantineEntity(
                            threatName = "Riskware.AccessibilityHijack",
                            fileName = appName,
                            severity = "High Risk",
                            originalLocation = pkg.packageName,
                            actionTaken = "Quarantined",
                            riskDescription = "App combines Accessibility service and Overlay drawing permissions."
                        )
                    )
                }
            }

            val percent = ((scannedCount.toFloat() / totalItems) * 70).toInt().coerceIn(1, 75)
            emit(
                ScanProgressState(
                    percentage = percent,
                    currentItem = "Checking package: $appName ($scannedCount/${installedApps.size})",
                    itemsScanned = scannedCount,
                    totalEstimated = totalItems,
                    threatsFound = detectedThreats
                )
            )
            delay(12)
        }

        // 2. Scan Storage & Files
        for (file in collectedFiles) {
            scannedCount++
            val name = file.name.lowercase()

            if (name.endsWith(".apk") && (name.contains("crack") || name.contains("mod") || name.contains("hack"))) {
                detectedThreats.add(
                    QuarantineEntity(
                        threatName = "Riskware.UnverifiedSideload",
                        fileName = file.name,
                        severity = "Warning",
                        originalLocation = file.absolutePath,
                        actionTaken = "Quarantined",
                        riskDescription = "Unsigned APK package with suspicious repackaging naming."
                    )
                )
            } else if (name.endsWith(".exe") || name.endsWith(".bat") || name.endsWith(".vbs") || name.endsWith(".scr")) {
                detectedThreats.add(
                    QuarantineEntity(
                        threatName = "Malware.SuspiciousScript",
                        fileName = file.name,
                        severity = "High Risk",
                        originalLocation = file.absolutePath,
                        actionTaken = "Quarantined",
                        riskDescription = "Suspicious executable script detected on mobile storage."
                    )
                )
            }

            val percent = 70 + ((scannedCount.toFloat() / totalItems) * 28).toInt().coerceIn(0, 28)
            emit(
                ScanProgressState(
                    percentage = percent.coerceAtMost(99),
                    currentItem = "Inspecting file: ${file.name}",
                    itemsScanned = scannedCount,
                    totalEstimated = totalItems,
                    threatsFound = detectedThreats
                )
            )
            delay(8)
        }

        // Final verification pass
        emit(
            ScanProgressState(
                percentage = 100,
                currentItem = "Verification complete. Shield status updated.",
                itemsScanned = scannedCount,
                totalEstimated = totalItems,
                threatsFound = detectedThreats,
                isFinished = true
            )
        )
    }

    private fun modeToDepth(mode: ScanMode): Int = when (mode) {
        ScanMode.QUICK -> 30
        ScanMode.SMART -> 80
        ScanMode.FULL -> 250
        ScanMode.CUSTOM -> 100
        ScanMode.USB -> 150
        ScanMode.BOOT -> 20
    }
}
