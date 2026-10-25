package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CleaningServices
import androidx.compose.material.icons.filled.Memory
import androidx.compose.material.icons.filled.PowerSettingsNew
import androidx.compose.material.icons.filled.Security
import androidx.compose.material.icons.filled.SportsEsports
import androidx.compose.material.icons.filled.Storage
import androidx.compose.material.icons.filled.SystemUpdate
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.ui.MainViewModel
import com.example.ui.components.GlassCard
import com.example.ui.components.StatusPill
import com.example.ui.theme.CyberCardBorder
import com.example.ui.theme.CyberSurfaceVariant
import com.example.ui.theme.NeonAmber
import com.example.ui.theme.NeonCyan
import com.example.ui.theme.NeonEmerald
import com.example.ui.theme.NeonRed
import com.example.ui.theme.TextPrimary
import com.example.ui.theme.TextSecondary

@Composable
fun SystemScreen(
    viewModel: MainViewModel,
    modifier: Modifier = Modifier
) {
    val settings by viewModel.settings.collectAsStateWithLifecycle()
    val health by viewModel.systemHealth.collectAsStateWithLifecycle()

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Section Header
        item {
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = "SYSTEM HEALTH & OPTIMIZATION",
                fontSize = 19.sp,
                fontWeight = FontWeight.Black,
                color = TextPrimary,
                letterSpacing = 0.8.sp
            )
            Text(
                text = "Performance diagnostics, startup monitors & Game Mode",
                fontSize = 12.sp,
                color = TextSecondary
            )
        }

        // 🎮 DTZ Game Mode Card
        item {
            GlassCard(
                modifier = Modifier.fillMaxWidth(),
                borderColor = if (settings.gameMode) NeonEmerald.copy(alpha = 0.6f) else CyberCardBorder
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .size(46.dp)
                            .clip(CircleShape)
                            .background(if (settings.gameMode) NeonEmerald.copy(alpha = 0.2f) else CyberSurfaceVariant),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.SportsEsports,
                            contentDescription = null,
                            tint = if (settings.gameMode) NeonEmerald else TextSecondary,
                            modifier = Modifier.size(26.dp)
                        )
                    }

                    Spacer(modifier = Modifier.width(14.dp))

                    Column(modifier = Modifier.weight(1f)) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(
                                text = "🎮 DTZ Game Mode",
                                fontSize = 15.sp,
                                fontWeight = FontWeight.Black,
                                color = TextPrimary
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            StatusPill(severity = if (settings.gameMode) "Active" else "Info")
                        }
                        Text(
                            text = if (settings.gameMode) "Active: Notifications suppressed, background scans paused, performance prioritized"
                            else "Suppresses non-critical alerts & prioritizes CPU/RAM for gaming while keeping real-time shield active",
                            fontSize = 11.sp,
                            color = TextSecondary,
                            lineHeight = 15.sp
                        )
                    }

                    Switch(
                        checked = settings.gameMode,
                        onCheckedChange = { viewModel.toggleGameMode(it) },
                        colors = SwitchDefaults.colors(
                            checkedThumbColor = Color(0xFF00220F),
                            checkedTrackColor = NeonEmerald,
                            uncheckedThumbColor = Color(0xFF64748B),
                            uncheckedTrackColor = Color(0xFF1E293B)
                        )
                    )
                }
            }
        }

        if (health != null) {
            val h = health!!

            // Hardware & Diagnostics Metrics
            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    // RAM Card
                    GlassCard(modifier = Modifier.weight(1f)) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(imageVector = Icons.Default.Memory, contentDescription = null, tint = NeonCyan, modifier = Modifier.size(18.dp))
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(text = "Memory (RAM)", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = TextPrimary)
                        }
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = "${h.ramUsagePercent}% Used",
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Black,
                            color = if (h.ramUsagePercent > 80) NeonAmber else NeonCyan
                        )
                        Text(
                            text = "${h.availableRamMb} MB free of ${h.totalRamMb} MB",
                            fontSize = 10.sp,
                            color = TextSecondary
                        )
                        Spacer(modifier = Modifier.height(6.dp))
                        LinearProgressIndicator(
                            progress = { h.ramUsagePercent / 100f },
                            modifier = Modifier.fillMaxWidth().height(6.dp).clip(RoundedCornerShape(3.dp)),
                            color = NeonCyan,
                            trackColor = Color(0xFF1E293B),
                            strokeCap = StrokeCap.Round
                        )
                    }

                    // Storage Card
                    GlassCard(modifier = Modifier.weight(1f)) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(imageVector = Icons.Default.Storage, contentDescription = null, tint = NeonEmerald, modifier = Modifier.size(18.dp))
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(text = "Storage", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = TextPrimary)
                        }
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = "${h.storageUsagePercent}% Used",
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Black,
                            color = if (h.storageUsagePercent > 85) NeonAmber else NeonEmerald
                        )
                        Text(
                            text = "${h.freeStorageGb} GB free of ${h.totalStorageGb} GB",
                            fontSize = 10.sp,
                            color = TextSecondary
                        )
                        Spacer(modifier = Modifier.height(6.dp))
                        LinearProgressIndicator(
                            progress = { h.storageUsagePercent / 100f },
                            modifier = Modifier.fillMaxWidth().height(6.dp).clip(RoundedCornerShape(3.dp)),
                            color = NeonEmerald,
                            trackColor = Color(0xFF1E293B),
                            strokeCap = StrokeCap.Round
                        )
                    }
                }
            }

            // Cleanup & Cache Button
            item {
                Button(
                    onClick = { viewModel.optimizeCache() },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = CyberSurfaceVariant,
                        contentColor = NeonCyan
                    ),
                    shape = RoundedCornerShape(10.dp),
                    modifier = Modifier.fillMaxWidth().testTag("optimize_cache_button")
                ) {
                    Icon(imageVector = Icons.Default.CleaningServices, contentDescription = null, modifier = Modifier.size(18.dp))
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(text = "Clean Junk Files & Free Cache Memory", fontSize = 12.sp, fontWeight = FontWeight.Bold)
                }
            }

            // Platform OS & Security Patch
            item {
                GlassCard(modifier = Modifier.fillMaxWidth()) {
                    Text(text = "SECURITY PLATFORM SPECS", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = NeonCyan)
                    Spacer(modifier = Modifier.height(8.dp))

                    DetailSpecRow("Android OS", h.androidVersion)
                    DetailSpecRow("Security Patch", h.securityPatch)
                    DetailSpecRow("CPU Architecture", h.cpuArch)
                    DetailSpecRow("Active CPU Cores", "${h.cpuCores} Cores Available")
                }
            }

            // Startup Monitor Section
            item {
                Text(
                    text = "STARTUP APPLICATION MONITOR (${h.startupApps.size})",
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold,
                    color = TextSecondary,
                    letterSpacing = 0.8.sp
                )
                Text(
                    text = "Monitors background services registered to launch upon device boot",
                    fontSize = 11.sp,
                    color = TextSecondary
                )
            }

            items(h.startupApps) { app ->
                GlassCard(modifier = Modifier.fillMaxWidth()) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Box(
                            modifier = Modifier
                                .size(34.dp)
                                .clip(CircleShape)
                                .background(CyberSurfaceVariant),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(imageVector = Icons.Default.PowerSettingsNew, contentDescription = null, tint = NeonCyan, modifier = Modifier.size(16.dp))
                        }
                        Spacer(modifier = Modifier.width(12.dp))
                        Column(modifier = Modifier.weight(1f)) {
                            Text(text = app.appName, fontSize = 13.sp, fontWeight = FontWeight.Bold, color = TextPrimary)
                            Text(text = app.packageName, fontSize = 10.sp, color = TextSecondary, fontFamily = FontFamily.Monospace)
                        }
                        StatusPill(severity = if (app.isSystemApp) "Safe" else "Info")
                    }
                }
            }
        }

        item {
            Spacer(modifier = Modifier.height(24.dp))
        }
    }
}

@Composable
private fun DetailSpecRow(label: String, value: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(text = label, fontSize = 12.sp, color = TextSecondary)
        Text(text = value, fontSize = 12.sp, fontWeight = FontWeight.SemiBold, color = TextPrimary, fontFamily = FontFamily.Monospace)
    }
}
