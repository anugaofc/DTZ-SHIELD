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
import androidx.compose.material.icons.filled.Block
import androidx.compose.material.icons.filled.Fingerprint
import androidx.compose.material.icons.filled.Radar
import androidx.compose.material.icons.filled.Security
import androidx.compose.material.icons.filled.VisibilityOff
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
fun AntiTrackScreen(
    viewModel: MainViewModel,
    modifier: Modifier = Modifier
) {
    val antiTrack by viewModel.antiTrackStatus.collectAsStateWithLifecycle()
    var webrtcGuard by remember { mutableStateOf(true) }
    var canvasCloak by remember { mutableStateOf(true) }

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
                        text = "ANTITRACK PREMIUM",
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Black,
                        color = TextPrimary,
                        letterSpacing = 0.8.sp
                    )
                    Text(
                        text = "Browser Fingerprint Masking & Behavioral Anti-Tracking",
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

        // Active Fingerprint Disguise Hero Card
        item {
            GlassCard(
                modifier = Modifier.fillMaxWidth(),
                borderColor = NeonEmerald.copy(alpha = 0.4f)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .size(52.dp)
                            .clip(CircleShape)
                            .background(NeonEmerald.copy(alpha = 0.15f)),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(imageVector = Icons.Default.Fingerprint, contentDescription = null, tint = NeonEmerald, modifier = Modifier.size(30.dp))
                    }
                    Spacer(modifier = Modifier.width(14.dp))
                    Column(modifier = Modifier.weight(1f)) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(text = "Digital Fingerprint Cloaked", fontSize = 14.sp, fontWeight = FontWeight.Bold, color = TextPrimary)
                            Spacer(modifier = Modifier.width(6.dp))
                            StatusPill(severity = "Safe")
                        }
                        Text(
                            text = "Websites cannot build a tracking profile of your hardware or canvas rendering",
                            fontSize = 11.sp,
                            color = TextSecondary
                        )
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))

                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(8.dp))
                        .background(CyberSurfaceVariant)
                        .padding(10.dp)
                ) {
                    Column {
                        Text(text = "Simulated Cloaked Profile:", fontSize = 10.sp, color = TextSecondary)
                        Text(
                            text = antiTrack.activeFingerprintId,
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            color = NeonCyan,
                            fontFamily = FontFamily.Monospace
                        )
                    }
                }
            }
        }

        // Defense Toggles
        item {
            Text(text = "TRACKING DEFENSE SHIELDS", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = TextSecondary, letterSpacing = 0.8.sp)
            Spacer(modifier = Modifier.height(8.dp))

            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                GlassCard(modifier = Modifier.fillMaxWidth()) {
                    Row(modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
                        Column(modifier = Modifier.weight(1f)) {
                            Text(text = "WebRTC IP Leak Protection", fontSize = 13.sp, fontWeight = FontWeight.Bold, color = TextPrimary)
                            Text(text = "Prevents browser WebRTC API from exposing your true local IPv4/IPv6", fontSize = 11.sp, color = TextSecondary)
                        }
                        Switch(
                            checked = webrtcGuard,
                            onCheckedChange = { webrtcGuard = it },
                            colors = SwitchDefaults.colors(checkedThumbColor = Color(0xFF00220F), checkedTrackColor = NeonEmerald)
                        )
                    }
                }

                GlassCard(modifier = Modifier.fillMaxWidth()) {
                    Row(modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
                        Column(modifier = Modifier.weight(1f)) {
                            Text(text = "Canvas & Audio Noise Injection", fontSize = 13.sp, fontWeight = FontWeight.Bold, color = TextPrimary)
                            Text(text = "Randomizes pixel and frequency hashes to prevent hardware fingerprinting", fontSize = 11.sp, color = TextSecondary)
                        }
                        Switch(
                            checked = canvasCloak,
                            onCheckedChange = { canvasCloak = it },
                            colors = SwitchDefaults.colors(checkedThumbColor = Color(0xFF00220F), checkedTrackColor = NeonEmerald)
                        )
                    }
                }
            }
        }

        // Intercepted Trackers List
        item {
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
                Text(text = "INTERCEPTED TRACKERS (${antiTrack.recentBlockedTrackers.size})", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = TextSecondary, letterSpacing = 0.8.sp)
                Text(text = "${antiTrack.totalTrackersBlocked} Total Blocked", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = NeonEmerald)
            }
        }

        items(antiTrack.recentBlockedTrackers) { tracker ->
            GlassCard(modifier = Modifier.fillMaxWidth()) {
                Row(modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier.size(34.dp).clip(CircleShape).background(CyberSurfaceVariant),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(imageVector = Icons.Default.Block, contentDescription = null, tint = NeonRed, modifier = Modifier.size(16.dp))
                    }
                    Spacer(modifier = Modifier.width(12.dp))
                    Column(modifier = Modifier.weight(1f)) {
                        Text(text = tracker.trackerName, fontSize = 13.sp, fontWeight = FontWeight.Bold, color = TextPrimary)
                        Text(text = "${tracker.targetDomain} • ${tracker.trackerCategory}", fontSize = 11.sp, color = TextSecondary, fontFamily = FontFamily.Monospace)
                    }
                    StatusPill(severity = "Safe")
                }
            }
        }

        item {
            Spacer(modifier = Modifier.height(24.dp))
        }
    }
}
