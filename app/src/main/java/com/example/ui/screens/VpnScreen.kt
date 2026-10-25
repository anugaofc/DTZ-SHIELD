package com.example.ui.screens

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
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
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.PowerSettingsNew
import androidx.compose.material.icons.filled.Security
import androidx.compose.material.icons.filled.Speed
import androidx.compose.material.icons.filled.VpnKey
import androidx.compose.material.icons.filled.VpnLock
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
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
import com.example.domain.scanner.VpnServer
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
fun VpnScreen(
    viewModel: MainViewModel,
    modifier: Modifier = Modifier
) {
    val vpnStatus by viewModel.vpnStatus.collectAsStateWithLifecycle()
    val availableServers = viewModel.vpnEngine.availableServers

    val infiniteTransition = rememberInfiniteTransition(label = "vpn_pulse")
    val pulseGlow by infiniteTransition.animateFloat(
        initialValue = 0.3f,
        targetValue = 0.85f,
        animationSpec = infiniteRepeatable(
            animation = tween(1400, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "vpn_glow"
    )

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
                        text = "SECURELINE VPN",
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Black,
                        color = TextPrimary,
                        letterSpacing = 0.8.sp
                    )
                    Text(
                        text = "Free Premium, Fast & Low-Ping Encrypted Tunnel",
                        fontSize = 12.sp,
                        color = NeonCyan
                    )
                }
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(8.dp))
                        .background(NeonEmerald.copy(alpha = 0.15f))
                        .border(1.dp, NeonEmerald, RoundedCornerShape(8.dp))
                        .padding(horizontal = 8.dp, vertical = 4.dp)
                ) {
                    Text(text = "100% FREE", fontSize = 10.sp, fontWeight = FontWeight.Bold, color = NeonEmerald)
                }
            }
        }

        // Connection Control Hero Card
        item {
            val isConnected = vpnStatus.isConnected
            val isConnecting = vpnStatus.isConnecting
            val statusColor = if (isConnected) NeonEmerald else if (isConnecting) NeonAmber else Color(0xFF64748B)

            GlassCard(
                modifier = Modifier.fillMaxWidth(),
                borderColor = if (isConnected) NeonEmerald.copy(alpha = pulseGlow) else CyberCardBorder
            ) {
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    // Big Circular Toggle Button
                    Box(
                        modifier = Modifier
                            .size(110.dp)
                            .clip(CircleShape)
                            .background(statusColor.copy(alpha = 0.15f))
                            .border(3.dp, statusColor, CircleShape)
                            .clickable { viewModel.toggleVpn() },
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.PowerSettingsNew,
                            contentDescription = "VPN Toggle",
                            tint = statusColor,
                            modifier = Modifier.size(52.dp)
                        )
                    }

                    Spacer(modifier = Modifier.height(14.dp))

                    Text(
                        text = if (isConnected) "VPN CONNECTED & ENCRYPTED"
                        else if (isConnecting) "SECURING TUNNEL..."
                        else "VPN DISCONNECTED",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Black,
                        color = statusColor,
                        letterSpacing = 0.8.sp
                    )

                    Text(
                        text = if (isConnected) "Your network traffic is encrypted via WireGuard AES-256"
                        else "Tap above to hide your IP address and encrypt public Wi-Fi",
                        fontSize = 11.sp,
                        color = TextSecondary,
                        modifier = Modifier.padding(top = 2.dp)
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    // IP Masking Comparison
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(12.dp))
                            .background(CyberSurfaceVariant)
                            .padding(12.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column {
                            Text(text = "Real IP (Original)", fontSize = 10.sp, color = TextSecondary)
                            Text(
                                text = if (isConnected) "HIDDEN & BLOCKED" else vpnStatus.realIp,
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Bold,
                                color = if (isConnected) NeonEmerald else TextPrimary,
                                fontFamily = FontFamily.Monospace
                            )
                        }

                        Icon(
                            imageVector = Icons.Default.VpnLock,
                            contentDescription = null,
                            tint = statusColor,
                            modifier = Modifier.size(22.dp)
                        )

                        Column(horizontalAlignment = Alignment.End) {
                            Text(text = "Virtual Protected IP", fontSize = 10.sp, color = TextSecondary)
                            Text(
                                text = if (isConnected) vpnStatus.virtualIp else "Unprotected",
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Bold,
                                color = if (isConnected) NeonCyan else Color(0xFF94A3B8),
                                fontFamily = FontFamily.Monospace
                            )
                        }
                    }

                    if (isConnected) {
                        Spacer(modifier = Modifier.height(14.dp))
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            VpnMetricChip("Ping Latency", "${vpnStatus.currentPingMs} ms", NeonEmerald)
                            VpnMetricChip("Download", "${vpnStatus.downloadSpeedMbps} Mbps", NeonCyan)
                            VpnMetricChip("Upload", "${vpnStatus.uploadSpeedMbps} Mbps", NeonCyan)
                        }
                    }
                }
            }
        }

        // Global Low-Ping Servers Selection
        item {
            Text(
                text = "SELECT HIGH-SPEED LOW-PING SERVER",
                fontSize = 12.sp,
                fontWeight = FontWeight.Bold,
                color = TextSecondary,
                letterSpacing = 0.8.sp
            )
        }

        items(availableServers) { server ->
            val isCurrent = vpnStatus.currentServer.id == server.id
            GlassCard(
                modifier = Modifier.fillMaxWidth(),
                borderColor = if (isCurrent) NeonCyan else CyberCardBorder,
                onClick = { viewModel.selectVpnServer(server) }
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(text = server.flagEmoji, fontSize = 24.sp)
                    Spacer(modifier = Modifier.width(12.dp))
                    Column(modifier = Modifier.weight(1f)) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(
                                text = "${server.city}, ${server.country}",
                                fontSize = 13.sp,
                                fontWeight = FontWeight.Bold,
                                color = TextPrimary
                            )
                            if (isCurrent) {
                                Spacer(modifier = Modifier.width(6.dp))
                                Text(text = "ACTIVE", fontSize = 9.sp, fontWeight = FontWeight.Bold, color = NeonCyan)
                            }
                        }
                        Text(
                            text = "${server.protocol} • Load: ${server.loadPercent}%",
                            fontSize = 11.sp,
                            color = TextSecondary
                        )
                    }

                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(8.dp))
                            .background(CyberSurfaceVariant)
                            .padding(horizontal = 8.dp, vertical = 4.dp)
                    ) {
                        Text(
                            text = "⚡ ${server.pingMs}ms",
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            color = if (server.pingMs < 30) NeonEmerald else NeonCyan
                        )
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
private fun VpnMetricChip(label: String, value: String, color: Color) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(text = value, fontSize = 14.sp, fontWeight = FontWeight.Black, color = color)
        Text(text = label, fontSize = 10.sp, color = TextSecondary)
    }
}
