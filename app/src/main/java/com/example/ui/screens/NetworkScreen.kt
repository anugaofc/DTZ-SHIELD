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
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material.icons.filled.Computer
import androidx.compose.material.icons.filled.Devices
import androidx.compose.material.icons.filled.NetworkCheck
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Router
import androidx.compose.material.icons.filled.Security
import androidx.compose.material.icons.filled.Smartphone
import androidx.compose.material.icons.filled.Tv
import androidx.compose.material.icons.filled.Wifi
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.domain.scanner.NetworkDeviceInfo
import com.example.ui.MainViewModel
import com.example.ui.components.GlassCard
import com.example.ui.components.SecurityScoreGauge
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
fun NetworkScreen(
    viewModel: MainViewModel,
    modifier: Modifier = Modifier
) {
    val settings by viewModel.settings.collectAsStateWithLifecycle()
    val report by viewModel.networkReport.collectAsStateWithLifecycle()

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Section Header
        item {
            Spacer(modifier = Modifier.height(4.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        text = "NETWORK SHIELD & FIREWALL",
                        fontSize = 19.sp,
                        fontWeight = FontWeight.Black,
                        color = TextPrimary,
                        letterSpacing = 0.8.sp
                    )
                    Text(
                        text = "Wi-Fi vulnerability analysis, ARP guard & device map",
                        fontSize = 12.sp,
                        color = TextSecondary
                    )
                }

                Button(
                    onClick = { viewModel.runNetworkScan() },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = CyberSurfaceVariant,
                        contentColor = NeonCyan
                    ),
                    shape = RoundedCornerShape(8.dp),
                    modifier = Modifier.testTag("rescan_network_button")
                ) {
                    Icon(imageVector = Icons.Default.Refresh, contentDescription = null, modifier = Modifier.size(14.dp))
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(text = "Rescan", fontSize = 11.sp, fontWeight = FontWeight.Bold)
                }
            }
        }

        // Firewall Toggle Card
        item {
            GlassCard(
                modifier = Modifier.fillMaxWidth(),
                borderColor = if (settings.networkShield) NeonEmerald.copy(alpha = 0.4f) else NeonAmber.copy(alpha = 0.4f)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .size(46.dp)
                            .clip(CircleShape)
                            .background(if (settings.networkShield) NeonEmerald.copy(alpha = 0.15f) else CyberSurfaceVariant),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.Security,
                            contentDescription = null,
                            tint = if (settings.networkShield) NeonEmerald else TextSecondary,
                            modifier = Modifier.size(24.dp)
                        )
                    }

                    Spacer(modifier = Modifier.width(14.dp))

                    Column(modifier = Modifier.weight(1f)) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(
                                text = "Intrusion Shield & Firewall",
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Bold,
                                color = TextPrimary
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            StatusPill(severity = if (settings.networkShield) "Safe" else "Warning")
                        }
                        Text(
                            text = if (settings.networkShield) "Monitoring ARP packets & packet flood anomalies"
                            else "Firewall paused. Device is exposed to local subnet scans.",
                            fontSize = 11.sp,
                            color = TextSecondary,
                            lineHeight = 15.sp
                        )
                    }

                    Switch(
                        checked = settings.networkShield,
                        onCheckedChange = { viewModel.toggleNetworkShield(it) },
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

        if (report != null) {
            val r = report!!

            // Network Map Visualizer
            item {
                GlassCard(modifier = Modifier.fillMaxWidth()) {
                    Text(
                        text = "LIVE NETWORK MAP",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        color = NeonCyan,
                        letterSpacing = 0.8.sp
                    )
                    Spacer(modifier = Modifier.height(14.dp))

                    // Internet -> Router -> Devices -> DTZ SHIELD
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        NetworkNodeItem("Internet", Icons.Default.Wifi, NeonCyan)
                        Icon(imageVector = Icons.Default.ArrowForward, contentDescription = null, tint = TextSecondary, modifier = Modifier.size(16.dp))
                        NetworkNodeItem("Router", Icons.Default.Router, NeonCyan)
                        Icon(imageVector = Icons.Default.ArrowForward, contentDescription = null, tint = TextSecondary, modifier = Modifier.size(16.dp))
                        NetworkNodeItem("Devices (${r.devices.size})", Icons.Default.Devices, NeonCyan)
                        Icon(imageVector = Icons.Default.ArrowForward, contentDescription = null, tint = TextSecondary, modifier = Modifier.size(16.dp))
                        NetworkNodeItem("DTZ SHIELD", Icons.Default.Security, NeonEmerald)
                    }
                }
            }

            // Wi-Fi Diagnostics & Details
            item {
                GlassCard(modifier = Modifier.fillMaxWidth()) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column {
                            Text(text = "Current Connection:", fontSize = 11.sp, color = TextSecondary)
                            Text(text = r.networkName, fontSize = 16.sp, fontWeight = FontWeight.Bold, color = TextPrimary)
                            Text(text = r.networkType, fontSize = 11.sp, color = NeonCyan)
                        }
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(8.dp))
                                .background(CyberSurfaceVariant)
                                .padding(horizontal = 10.dp, vertical = 6.dp)
                        ) {
                            Text(text = "Score: ${r.securityScore}/100", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = NeonEmerald)
                        }
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        DetailChip("Local IP", r.localIp, Modifier.weight(1f))
                        DetailChip("Gateway", r.gatewayIp, Modifier.weight(1f))
                        DetailChip("Signal", "${r.signalStrengthPercent}%", Modifier.weight(0.7f))
                    }
                }
            }

            // Connected Devices Discovery List
            item {
                Text(
                    text = "DISCOVERED NETWORK DEVICES (${r.devices.size})",
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold,
                    color = TextSecondary,
                    letterSpacing = 0.8.sp
                )
            }

            items(r.devices) { dev ->
                GlassCard(modifier = Modifier.fillMaxWidth()) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        val icon = when (dev.deviceType) {
                            "Router" -> Icons.Default.Router
                            "Mobile" -> Icons.Default.Smartphone
                            "IoT" -> Icons.Default.Tv
                            else -> Icons.Default.Computer
                        }

                        Box(
                            modifier = Modifier
                                .size(38.dp)
                                .clip(CircleShape)
                                .background(if (dev.status == "This Device") NeonEmerald.copy(alpha = 0.15f) else CyberSurfaceVariant),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = icon,
                                contentDescription = null,
                                tint = if (dev.status == "This Device") NeonEmerald else NeonCyan,
                                modifier = Modifier.size(20.dp)
                            )
                        }

                        Spacer(modifier = Modifier.width(12.dp))

                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = dev.deviceName,
                                fontSize = 13.sp,
                                fontWeight = FontWeight.Bold,
                                color = TextPrimary
                            )
                            Text(
                                text = "${dev.ipAddress} • ${dev.macAddress}",
                                fontSize = 11.sp,
                                color = TextSecondary,
                                fontFamily = FontFamily.Monospace
                            )
                        }

                        StatusPill(severity = dev.risk)
                    }
                }
            }

            // Open Ports Scan Section
            item {
                Text(
                    text = "PORT VULNERABILITY INSPECTOR",
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold,
                    color = TextSecondary,
                    letterSpacing = 0.8.sp
                )
            }

            item {
                GlassCard(modifier = Modifier.fillMaxWidth()) {
                    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                        for (port in r.openPorts) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Text(
                                        text = "Port ${port.port}",
                                        fontSize = 12.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = TextPrimary,
                                        fontFamily = FontFamily.Monospace,
                                        modifier = Modifier.width(70.dp)
                                    )
                                    Text(
                                        text = port.serviceName,
                                        fontSize = 12.sp,
                                        color = TextSecondary
                                    )
                                }
                                StatusPill(severity = if (port.isOpen) port.riskLevel else "Safe")
                            }
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
private fun NetworkNodeItem(
    title: String,
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    color: Color
) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Box(
            modifier = Modifier
                .size(38.dp)
                .clip(CircleShape)
                .background(color.copy(alpha = 0.15f))
                .border(1.dp, color, CircleShape),
            contentAlignment = Alignment.Center
        ) {
            Icon(imageVector = icon, contentDescription = null, tint = color, modifier = Modifier.size(18.dp))
        }
        Spacer(modifier = Modifier.height(4.dp))
        Text(text = title, fontSize = 9.sp, fontWeight = FontWeight.Bold, color = TextPrimary)
    }
}

@Composable
private fun DetailChip(label: String, value: String, modifier: Modifier = Modifier) {
    Box(
        modifier = modifier
            .clip(RoundedCornerShape(8.dp))
            .background(CyberSurfaceVariant)
            .padding(horizontal = 8.dp, vertical = 6.dp)
    ) {
        Column {
            Text(text = label, fontSize = 9.sp, color = TextSecondary)
            Text(
                text = value,
                fontSize = 11.sp,
                fontWeight = FontWeight.Bold,
                color = TextPrimary,
                fontFamily = FontFamily.Monospace,
                maxLines = 1
            )
        }
    }
}
