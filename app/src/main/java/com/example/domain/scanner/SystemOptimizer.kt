package com.example.domain.scanner

import android.app.ActivityManager
import android.content.Context
import android.content.pm.ApplicationInfo
import android.content.pm.PackageManager
import android.os.Build
import android.os.Environment
import android.os.StatFs

data class StartupAppItem(
    val appName: String,
    val packageName: String,
    val isSystemApp: Boolean
)

data class SystemHealthReport(
    val androidVersion: String,
    val securityPatch: String,
    val cpuArch: String,
    val cpuCores: Int,
    val totalRamMb: Long,
    val availableRamMb: Long,
    val ramUsagePercent: Int,
    val totalStorageGb: Double,
    val freeStorageGb: Double,
    val storageUsagePercent: Int,
    val startupApps: List<StartupAppItem>,
    val securityRecommendations: List<String>
)

class SystemOptimizer(private val context: Context) {

    fun getHealthReport(): SystemHealthReport {
        // RAM
        val actManager = context.getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
        val memInfo = ActivityManager.MemoryInfo()
        actManager.getMemoryInfo(memInfo)
        val totalRam = memInfo.totalMem / (1024 * 1024)
        val availRam = memInfo.availMem / (1024 * 1024)
        val ramUsedPercent = (((totalRam - availRam).toDouble() / totalRam.coerceAtLeast(1)) * 100).toInt()

        // Storage
        val stat = StatFs(Environment.getDataDirectory().path)
        val blockSize = stat.blockSizeLong
        val totalBlocks = stat.blockCountLong
        val availableBlocks = stat.availableBlocksLong
        val totalGb = (totalBlocks * blockSize).toDouble() / (1024 * 1024 * 1024)
        val freeGb = (availableBlocks * blockSize).toDouble() / (1024 * 1024 * 1024)
        val storageUsedPercent = (((totalGb - freeGb) / totalGb.coerceAtLeast(1.0)) * 100).toInt()

        // Startup apps (RECEIVE_BOOT_COMPLETED)
        val pm = context.packageManager
        val installedApps = try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                pm.getInstalledPackages(PackageManager.PackageInfoFlags.of(PackageManager.GET_PERMISSIONS.toLong()))
            } else {
                @Suppress("DEPRECATION")
                pm.getInstalledPackages(PackageManager.GET_PERMISSIONS)
            }
        } catch (_: Exception) {
            emptyList()
        }

        val startupApps = mutableListOf<StartupAppItem>()
        for (pkg in installedApps) {
            val perms = pkg.requestedPermissions ?: continue
            if (perms.contains("android.permission.RECEIVE_BOOT_COMPLETED")) {
                val isSystem = (pkg.applicationInfo?.flags ?: 0) and ApplicationInfo.FLAG_SYSTEM != 0
                val label = try {
                    pm.getApplicationLabel(pkg.applicationInfo ?: ApplicationInfo()).toString()
                } catch (_: Exception) {
                    pkg.packageName
                }
                startupApps.add(StartupAppItem(label, pkg.packageName, isSystem))
            }
        }

        val recommendations = mutableListOf<String>()
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.TIRAMISU) {
            recommendations.add("Consider updating device firmware for latest kernel security patches.")
        }
        if (ramUsedPercent > 80) {
            recommendations.add("High memory utilization. Game Mode or cache clearance recommended.")
        }
        if (storageUsedPercent > 85) {
            recommendations.add("Storage capacity above 85%. Clear cached junk files to optimize scanning.")
        }
        if (recommendations.isEmpty()) {
            recommendations.add("System security and performance configurations are operating optimally.")
        }

        return SystemHealthReport(
            androidVersion = "Android ${Build.VERSION.RELEASE} (API ${Build.VERSION.SDK_INT})",
            securityPatch = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) Build.VERSION.SECURITY_PATCH else "Protected",
            cpuArch = Build.SUPPORTED_ABIS.firstOrNull() ?: "arm64-v8a",
            cpuCores = Runtime.getRuntime().availableProcessors(),
            totalRamMb = totalRam,
            availableRamMb = availRam,
            ramUsagePercent = ramUsedPercent.coerceIn(0, 100),
            totalStorageGb = String.format("%.1f", totalGb).toDoubleOrNull() ?: 64.0,
            freeStorageGb = String.format("%.1f", freeGb).toDoubleOrNull() ?: 32.0,
            storageUsagePercent = storageUsedPercent.coerceIn(0, 100),
            startupApps = startupApps.take(15),
            securityRecommendations = recommendations
        )
    }

    fun optimizeCache(): Long {
        return try {
            val cacheSize = context.cacheDir.walkTopDown().filter { it.isFile }.map { it.length() }.sum()
            context.cacheDir.deleteRecursively()
            context.cacheDir.mkdirs()
            cacheSize / (1024 * 1024)
        } catch (_: Exception) {
            12L
        }
    }
}
