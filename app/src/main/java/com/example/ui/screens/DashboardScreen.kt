package com.example.ui.screens

import androidx.compose.animation.AnimatedVisibility
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Bolt
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.CleaningServices
import androidx.compose.material.icons.filled.Fingerprint
import androidx.compose.material.icons.filled.GppGood
import androidx.compose.material.icons.filled.Language
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Router
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Security
import androidx.compose.material.icons.filled.Shield
import androidx.compose.material.icons.filled.VpnKey
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.domain.scanner.ScanMode
import com.example.ui.MainViewModel
import com.example.ui.ScreenNav
import com.example.ui.components.AnimatedCyberShield
import com.example.ui.components.GlassCard
import com.example.ui.components.SecurityScoreGauge
import com.example.ui.components.ShieldStatus
import com.example.ui.components.StatusPill
import com.example.ui.theme.CyberCardBorder
import com.example.ui.theme.CyberSurfaceVariant
import com.example.ui.theme.NeonAmber
import com.example.ui.theme.NeonCyan
import com.example.ui.theme.NeonEmerald
import com.example.ui.theme.NeonRed
import com.example.ui.theme.TextPrimary
import com.example.ui.theme.TextSecondary
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun DashboardScreen(
    viewModel: MainViewModel,
    modifier: Modifier = Modifier
) {
    val settings by viewModel.settings.collectAsStateWithLifecycle()
    val shieldStatus by viewModel.shieldStatus.collectAsStateWithLifecycle()
    val securityScore by viewModel.securityScore.collectAsStateWithLifecycle()
    val quarantined by viewModel.quarantinedItems.collectAsStateWithLifecycle()
    val alerts by viewModel.alerts.collectAsStateWithLifecycle()
    val isScanning by viewModel.isScanning.collectAsStateWithLifecycle()

    val unresolvedAlerts = alerts.filter { !it.isResolved }

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // 1. Branding Header
        item {
            Spacer(modifier = Modifier.height(6.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Default.Shield,
                            contentDescription = null,
                            tint = NeonCyan,
                            modifier = Modifier.size(24.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "DTZ SHIELD",
                            fontSize = 22.sp,
                            fontWeight = FontWeight.Black,
                            color = TextPrimary,
                            letterSpacing = 1.sp
                        )
                    }
                    Text(
                        text = "Free. Powerful. Protected.",
                        fontSize = 12.sp,
                        color = NeonCyan,
                        fontWeight = FontWeight.Medium
                    )
                }

                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(8.dp))
                        .background(CyberSurfaceVariant)
                        .border(1.dp, CyberCardBorder, RoundedCornerShape(8.dp))
                        .padding(horizontal = 10.dp, vertical = 5.dp)
                ) {
                    Text(
                        text = "v2.0.1 FREE",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        color = NeonEmerald
                    )
                }
            }
        }

        // 2. Central Animated Cyber Shield Hero
        item {
            GlassCard(
                modifier = Modifier.fillMaxWidth(),
                borderColor = when (shieldStatus) {
                    ShieldStatus.PROTECTED -> NeonEmerald.copy(alpha = 0.4f)
                    ShieldStatus.SCANNING -> NeonCyan.copy(alpha = 0.5f)
                    ShieldStatus.THREAT_DETECTED -> NeonRed.copy(alpha = 0.6f)
                    ShieldStatus.RESOLVED -> NeonEmerald.copy(alpha = 0.5f)
                }
            ) {
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    AnimatedCyberShield(
                        status = shieldStatus,
                        sizeDp = 180.dp
                    )

                    Spacer(modifier = Modifier.height(14.dp))

                    val statusTitle = when (shieldStatus) {
                        ShieldStatus.PROTECTED -> "YOU ARE PROTECTED"
                        ShieldStatus.SCANNING -> "SCANNING IN PROGRESS..."
                        ShieldStatus.THREAT_DETECTED -> "THREAT DETECTED"
                        ShieldStatus.RESOLVED -> "SYSTEM SECURED"
                    }
                    val statusColor = when (shieldStatus) {
                        ShieldStatus.PROTECTED, ShieldStatus.RESOLVED -> NeonEmerald
                        ShieldStatus.SCANNING -> NeonCyan
                        ShieldStatus.THREAT_DETECTED -> NeonRed
                    }

                    Text(
                        text = statusTitle,
                        fontSize = 19.sp,
                        fontWeight = FontWeight.Black,
                        color = statusColor,
                        letterSpacing = 0.8.sp
                    )

                    Text(
                        text = if (isScanning) "Inspecting packages and system storage..."
                        else if (shieldStatus == ShieldStatus.THREAT_DETECTED) "Active threat isolated in Quarantine"
                        else "Real-time AI malware & web shield actively guarding device",
                        fontSize = 12.sp,
                        color = TextSecondary,
                        modifier = Modifier.padding(top = 4.dp, bottom = 16.dp)
                    )

                    // Primary Scan CTA Button
                    Button(
                        onClick = {
                            if (isScanning) {
                                viewModel.navigateTo(ScreenNav.ANTIVIRUS)
                            } else {
                                viewModel.startScan(ScanMode.SMART)
                                viewModel.navigateTo(ScreenNav.ANTIVIRUS)
                            }
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(52.dp)
                            .testTag("primary_smart_scan_button"),
                        shape = RoundedCornerShape(12.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = if (isScanning) NeonAmber else NeonCyan,
                            contentColor = Color(0xFF001F29)
                        )
                    ) {
                        Icon(
                            imageVector = Icons.Default.Bolt,
                            contentDescription = null,
                            modifier = Modifier.size(20.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = if (isScanning) "VIEW SCAN PROGRESS" else "SMART SCAN",
                            fontSize = 15.sp,
                            fontWeight = FontWeight.Bold,
                            letterSpacing = 0.5.sp
                        )
                    }
                }
            }
        }

        // 3. Quick Action Scanning Shortcuts
        item {
            Text(
                text = "SECURITY SCANS",
                fontSize = 12.sp,
                fontWeight = FontWeight.Bold,
                color = TextSecondary,
                letterSpacing = 0.8.sp
            )
            Spacer(modifier = Modifier.height(8.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                QuickScanButton(
                    title = "Quick Scan",
                    icon = Icons.Default.Bolt,
                    modifier = Modifier.weight(1f)
                ) {
                    viewModel.startScan(ScanMode.QUICK)
                    viewModel.navigateTo(ScreenNav.ANTIVIRUS)
                }
                QuickScanButton(
                    title = "Full Scan",
                    icon = Icons.Default.Search,
                    modifier = Modifier.weight(1f)
                ) {
                    viewModel.startScan(ScanMode.FULL)
                    viewModel.navigateTo(ScreenNav.ANTIVIRUS)
                }
                QuickScanButton(
                    title = "Network",
                    icon = Icons.Default.Router,
                    modifier = Modifier.weight(1f)
                ) {
                    viewModel.navigateTo(ScreenNav.NETWORK)
                }
                QuickScanButton(
                    title = "Privacy",
                    icon = Icons.Default.Lock,
                    modifier = Modifier.weight(1f)
                ) {
                    viewModel.navigateTo(ScreenNav.PRIVACY)
                }
            }

            Spacer(modifier = Modifier.height(10.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                QuickScanButton(
                    title = "VPN",
                    icon = Icons.Default.VpnKey,
                    modifier = Modifier.weight(1f)
                ) {
                    viewModel.navigateTo(ScreenNav.VPN)
                }
                QuickScanButton(
                    title = "Cleanup",
                    icon = Icons.Default.CleaningServices,
                    modifier = Modifier.weight(1f)
                ) {
                    viewModel.navigateTo(ScreenNav.CLEANUP)
                }
                QuickScanButton(
                    title = "AntiTrack",
                    icon = Icons.Default.Fingerprint,
                    modifier = Modifier.weight(1f)
                ) {
                    viewModel.navigateTo(ScreenNav.ANTITRACK)
                }
                QuickScanButton(
                    title = "10 Tools",
                    icon = Icons.Default.Security,
                    modifier = Modifier.weight(1f)
                ) {
                    viewModel.navigateTo(ScreenNav.TOOLS)
                }
            }
        }

        // 4. Central DTZ Security Center Score Card
        item {
            GlassCard(
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    SecurityScoreGauge(
                        score = securityScore,
                        sizeDp = 130.dp
                    )

                    Spacer(modifier = Modifier.width(16.dp))

                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = "DTZ SECURITY CENTER",
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Black,
                            color = NeonCyan,
                            letterSpacing = 0.5.sp
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = "Comprehensive multi-layered protection score calculated in real-time.",
                            fontSize = 11.sp,
                            color = TextSecondary,
                            lineHeight = 15.sp
                        )

                        Spacer(modifier = Modifier.height(10.dp))

                        Button(
                            onClick = { viewModel.fixAllIssues() },
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(38.dp)
                                .testTag("fix_all_issues_button"),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = NeonEmerald,
                                contentColor = Color(0xFF00220F)
                            ),
                            shape = RoundedCornerShape(8.dp)
                        ) {
                            Text(
                                text = "Fix All Issues",
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                }
            }
        }

        // 5. Active Protection Shields
        item {
            Text(
                text = "DEFENSE SHIELDS",
                fontSize = 12.sp,
                fontWeight = FontWeight.Bold,
                color = TextSecondary,
                letterSpacing = 0.8.sp
            )
            Spacer(modifier = Modifier.height(8.dp))

            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                ShieldToggleCard(
                    title = "Real-Time Antivirus",
                    subtitle = "Continuous malware & process monitor",
                    enabled = settings.realTimeProtection,
                    onToggle = { viewModel.toggleRealTime(it) }
                )
                ShieldToggleCard(
                    title = "Web & Phishing Shield",
                    subtitle = "Blocks malicious domains & dangerous URLs",
                    enabled = settings.webShield,
                    onToggle = { viewModel.toggleWebShield(it) }
                )
                ShieldToggleCard(
                    title = "Network Shield & Firewall",
                    subtitle = "ARP spoofing, Wi-Fi & port vulnerability guard",
                    enabled = settings.networkShield,
                    onToggle = { viewModel.toggleNetworkShield(it) }
                )
                ShieldToggleCard(
                    title = "Ransomware Folder Shield",
                    subtitle = "Locks sensitive folders against unauthorized encryption",
                    enabled = settings.ransomwareProtection,
                    onToggle = { viewModel.toggleRansomware(it) }
                )
            }
        }

        // 6. Security Status Highlights
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                GlassCard(
                    modifier = Modifier.weight(1f),
                    onClick = { viewModel.navigateTo(ScreenNav.ANTIVIRUS) }
                ) {
                    Text(text = "Quarantine", fontSize = 12.sp, color = TextSecondary)
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "${quarantined.size} Items",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = if (quarantined.isEmpty()) NeonEmerald else NeonRed
                    )
                }

                GlassCard(
                    modifier = Modifier.weight(1f),
                    onClick = { viewModel.navigateTo(ScreenNav.REPORTS) }
                ) {
                    val dateStr = SimpleDateFormat("MMM d, HH:mm", Locale.getDefault()).format(Date(settings.lastScanTime))
                    Text(text = "Last Scan", fontSize = 12.sp, color = TextSecondary)
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = dateStr,
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Bold,
                        color = TextPrimary
                    )
                }
            }
        }

        // 7. Unresolved Security Alerts
        if (unresolvedAlerts.isNotEmpty()) {
            item {
                Text(
                    text = "ACTIVE SECURITY ALERTS (${unresolvedAlerts.size})",
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold,
                    color = NeonAmber,
                    letterSpacing = 0.8.sp
                )
                Spacer(modifier = Modifier.height(8.dp))
            }

            items(unresolvedAlerts.take(4).size) { index ->
                val alert = unresolvedAlerts[index]
                GlassCard(
                    modifier = Modifier.fillMaxWidth(),
                    borderColor = NeonAmber.copy(alpha = 0.4f)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column(modifier = Modifier.weight(1f)) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                StatusPill(severity = alert.severity)
                                Spacer(modifier = Modifier.width(8.dp))
                                Text(
                                    text = alert.source,
                                    fontSize = 11.sp,
                                    color = TextSecondary
                                )
                            }
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = alert.title,
                                fontSize = 13.sp,
                                fontWeight = FontWeight.Bold,
                                color = TextPrimary
                            )
                            Text(
                                text = alert.description,
                                fontSize = 11.sp,
                                color = TextSecondary,
                                maxLines = 2
                            )
                        }

                        Button(
                            onClick = { viewModel.resolveAlert(alert.id) },
                            colors = ButtonDefaults.buttonColors(
                                containerColor = CyberSurfaceVariant,
                                contentColor = NeonCyan
                            ),
                            shape = RoundedCornerShape(8.dp),
                            modifier = Modifier.testTag("resolve_alert_${alert.id}")
                        ) {
                            Text(text = "Resolve", fontSize = 11.sp, fontWeight = FontWeight.Bold)
                        }
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
private fun QuickScanButton(
    title: String,
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    GlassCard(
        modifier = modifier,
        onClick = onClick
    ) {
        Column(
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Icon(
                imageVector = icon,
                contentDescription = title,
                tint = NeonCyan,
                modifier = Modifier.size(22.dp)
            )
            Spacer(modifier = Modifier.height(6.dp))
            Text(
                text = title,
                fontSize = 11.sp,
                fontWeight = FontWeight.SemiBold,
                color = TextPrimary,
                maxLines = 1
            )
        }
    }
}

@Composable
private fun ShieldToggleCard(
    title: String,
    subtitle: String,
    enabled: Boolean,
    onToggle: (Boolean) -> Unit
) {
    GlassCard(
        modifier = Modifier.fillMaxWidth(),
        borderColor = if (enabled) CyberCardBorder else NeonAmber.copy(alpha = 0.3f)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .size(8.dp)
                            .clip(CircleShape)
                            .background(if (enabled) NeonEmerald else NeonAmber)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = title,
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Bold,
                        color = TextPrimary
                    )
                }
                Spacer(modifier = Modifier.height(2.dp))
                Text(
                    text = subtitle,
                    fontSize = 11.sp,
                    color = TextSecondary
                )
            }

            Switch(
                checked = enabled,
                onCheckedChange = onToggle,
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
