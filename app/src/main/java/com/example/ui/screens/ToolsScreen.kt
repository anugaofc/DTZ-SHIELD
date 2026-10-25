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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CleaningServices
import androidx.compose.material.icons.filled.DeleteForever
import androidx.compose.material.icons.filled.Face
import androidx.compose.material.icons.filled.Fingerprint
import androidx.compose.material.icons.filled.MarkEmailRead
import androidx.compose.material.icons.filled.Medication
import androidx.compose.material.icons.filled.PhonelinkLock
import androidx.compose.material.icons.filled.Psychology
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Security
import androidx.compose.material.icons.filled.Send
import androidx.compose.material.icons.filled.Thermostat
import androidx.compose.material.icons.filled.VpnKey
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.ui.MainViewModel
import com.example.ui.ScreenNav
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
fun ToolsScreen(
    viewModel: MainViewModel,
    modifier: Modifier = Modifier
) {
    var scamInputText by remember { mutableStateOf("URGENT: Your bank account is suspended. Verify login at http://secure-update-bank.xyz") }
    val scamResult by viewModel.scamScanResult.collectAsStateWithLifecycle()
    val cyberCaptureResult by viewModel.cyberCaptureResult.collectAsStateWithLifecycle()
    val shredResult by viewModel.lastShredResult.collectAsStateWithLifecycle()

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
                        text = "10 NEXT-GEN TOOLS",
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Black,
                        color = TextPrimary,
                        letterSpacing = 0.8.sp
                    )
                    Text(
                        text = "Full Cybersecurity Intelligence & Defense Suite",
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
                    Text(text = "ALL FREE", fontSize = 10.sp, fontWeight = FontWeight.Bold, color = NeonEmerald)
                }
            }
        }

        // Tool 1: AI Scam, SMS & Email Phish Detector
        item {
            GlassCard(modifier = Modifier.fillMaxWidth()) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(modifier = Modifier.size(36.dp).clip(CircleShape).background(NeonCyan.copy(alpha = 0.15f)), contentAlignment = Alignment.Center) {
                        Icon(imageVector = Icons.Default.MarkEmailRead, contentDescription = null, tint = NeonCyan, modifier = Modifier.size(20.dp))
                    }
                    Spacer(modifier = Modifier.width(10.dp))
                    Column {
                        Text(text = "1. AI Scam, SMS & Mail Shield", fontSize = 14.sp, fontWeight = FontWeight.Bold, color = TextPrimary)
                        Text(text = "Detects smishing, fake invoices, banking scams & deepfakes", fontSize = 11.sp, color = TextSecondary)
                    }
                }

                Spacer(modifier = Modifier.height(10.dp))

                OutlinedTextField(
                    value = scamInputText,
                    onValueChange = { scamInputText = it },
                    modifier = Modifier.fillMaxWidth().testTag("scam_input_field"),
                    placeholder = { Text("Paste suspicious message, SMS or email content...", color = Color(0xFF64748B)) },
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = NeonCyan,
                        unfocusedBorderColor = CyberCardBorder,
                        focusedTextColor = TextPrimary,
                        unfocusedTextColor = TextPrimary,
                        focusedContainerColor = CyberSurfaceVariant.copy(alpha = 0.5f),
                        unfocusedContainerColor = CyberSurfaceVariant.copy(alpha = 0.5f)
                    ),
                    maxLines = 3,
                    shape = RoundedCornerShape(10.dp)
                )

                Spacer(modifier = Modifier.height(8.dp))

                Button(
                    onClick = { viewModel.scanScamMessage(scamInputText) },
                    modifier = Modifier.fillMaxWidth().testTag("analyze_scam_button"),
                    colors = ButtonDefaults.buttonColors(containerColor = NeonCyan, contentColor = Color(0xFF001F29)),
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Text(text = "Analyze with AI Scam Guard", fontWeight = FontWeight.Bold, fontSize = 12.sp)
                }

                if (scamResult != null) {
                    val res = scamResult!!
                    Spacer(modifier = Modifier.height(10.dp))
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(8.dp))
                            .background(if (res.isScamDetected) Color(0xFF260D12) else Color(0xFF0D2616))
                            .border(1.dp, if (res.isScamDetected) NeonRed else NeonEmerald, RoundedCornerShape(8.dp))
                            .padding(10.dp)
                    ) {
                        Column {
                            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                                Text(text = res.scamCategory, fontSize = 12.sp, fontWeight = FontWeight.Bold, color = if (res.isScamDetected) NeonRed else NeonEmerald)
                                StatusPill(severity = res.riskLevel)
                            }
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(text = res.reason, fontSize = 11.sp, color = TextPrimary)
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(text = "Action: ${res.recommendedAction}", fontSize = 11.sp, color = NeonCyan)
                        }
                    }
                }
            }
        }

        // Tool 2: CyberCapture Zero-Day Cloud Sandbox
        item {
            GlassCard(modifier = Modifier.fillMaxWidth()) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(modifier = Modifier.size(36.dp).clip(CircleShape).background(NeonEmerald.copy(alpha = 0.15f)), contentAlignment = Alignment.Center) {
                        Icon(imageVector = Icons.Default.Psychology, contentDescription = null, tint = NeonEmerald, modifier = Modifier.size(20.dp))
                    }
                    Spacer(modifier = Modifier.width(10.dp))
                    Column {
                        Text(text = "2. CyberCapture & Zero-Day Sandbox", fontSize = 14.sp, fontWeight = FontWeight.Bold, color = TextPrimary)
                        Text(text = "Automated behavioral execution analysis in cloud sandbox", fontSize = 11.sp, color = TextSecondary)
                    }
                }
                Spacer(modifier = Modifier.height(8.dp))
                Button(
                    onClick = { viewModel.testCyberCapture("unknown_sample_payload.apk") },
                    modifier = Modifier.fillMaxWidth().testTag("cyber_capture_button"),
                    colors = ButtonDefaults.buttonColors(containerColor = CyberSurfaceVariant, contentColor = NeonEmerald),
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Text(text = "Test Zero-Day Cloud Sandbox Analysis", fontSize = 12.sp, fontWeight = FontWeight.Bold)
                }

                if (cyberCaptureResult != null) {
                    val c = cyberCaptureResult!!
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(text = "Sandbox Report: ${c.behavioralSummary}", fontSize = 11.sp, color = TextPrimary)
                    Text(text = "Reputation Score: ${c.reputationScore}/100 • Execution: ${c.sandboxExecutionTimeMs}ms", fontSize = 10.sp, color = NeonCyan)
                }
            }
        }

        // Tool 3: Data Shredder (DOD 5220.22-M Multi-pass)
        item {
            GlassCard(modifier = Modifier.fillMaxWidth()) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(modifier = Modifier.size(36.dp).clip(CircleShape).background(NeonRed.copy(alpha = 0.15f)), contentAlignment = Alignment.Center) {
                        Icon(imageVector = Icons.Default.DeleteForever, contentDescription = null, tint = NeonRed, modifier = Modifier.size(20.dp))
                    }
                    Spacer(modifier = Modifier.width(10.dp))
                    Column {
                        Text(text = "3. Data Shredder (DOD 5220.22-M)", fontSize = 14.sp, fontWeight = FontWeight.Bold, color = TextPrimary)
                        Text(text = "Permanent file destruction with multi-pass random bit overwrite", fontSize = 11.sp, color = TextSecondary)
                    }
                }
                Spacer(modifier = Modifier.height(8.dp))
                Button(
                    onClick = { viewModel.runDataShredderDemo() },
                    modifier = Modifier.fillMaxWidth().testTag("run_shredder_button"),
                    colors = ButtonDefaults.buttonColors(containerColor = CyberSurfaceVariant, contentColor = NeonRed),
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Text(text = "Test Secure Multi-Pass File Shredder", fontSize = 12.sp, fontWeight = FontWeight.Bold)
                }
                if (shredResult != null) {
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(text = "Shredder: ${shredResult!!.status} (${shredResult!!.passesCompleted} passes)", fontSize = 11.sp, color = NeonEmerald, fontWeight = FontWeight.Bold)
                }
            }
        }

        // Tools 4-10 Grid / Cards
        item {
            Text(text = "ADDITIONAL DEFENSE PROTOCOLS", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = TextSecondary, letterSpacing = 0.8.sp)
            Spacer(modifier = Modifier.height(8.dp))

            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                NextGenToolRow("4. SecureLine VPN", "Fast, low-ping encrypted tunneling & virtual IP masking", Icons.Default.VpnKey, NeonCyan) {
                    viewModel.navigateTo(ScreenNav.VPN)
                }
                NextGenToolRow("5. AntiTrack Premium", "Browser fingerprint cloaker & WebRTC IP leak shield", Icons.Default.Fingerprint, NeonEmerald) {
                    viewModel.navigateTo(ScreenNav.ANTITRACK)
                }
                NextGenToolRow("6. Cleanup Premium", "Deep junk-file removal, RAM booster & cache optimization", Icons.Default.CleaningServices, NeonCyan) {
                    viewModel.navigateTo(ScreenNav.CLEANUP)
                }
                NextGenToolRow("7. Remote Access & RAT Shield", "Blocks unauthorized screen mirroring, remote ADB & spyware", Icons.Default.PhonelinkLock, NeonEmerald) {
                    viewModel.triggerRemoteAccessAudit()
                }
                NextGenToolRow("8. AI Deepfake Media Detector", "Analyzes synthetic voice clone patterns and facial distortions", Icons.Default.Face, NeonCyan) {
                    viewModel.scanScamMessage("Incoming emergency call with synthetic voice clone requesting urgent wire transfer")
                }
                NextGenToolRow("9. Rescue Disk & Offline Disinfection", "Emergency offline boot scanning and system recovery tool", Icons.Default.Medication, NeonEmerald) {
                    viewModel.runRescueDisk()
                }
                NextGenToolRow("10. Device Health & Thermal Guard", "CPU temperature, power throttling & memory drain watchdog", Icons.Default.Thermostat, NeonCyan) {
                    viewModel.navigateTo(ScreenNav.SYSTEM)
                }
            }
        }

        item {
            Spacer(modifier = Modifier.height(24.dp))
        }
    }
}

@Composable
private fun NextGenToolRow(
    title: String,
    desc: String,
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    accentColor: Color,
    onClick: () -> Unit
) {
    GlassCard(
        modifier = Modifier.fillMaxWidth(),
        onClick = onClick
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier.size(34.dp).clip(CircleShape).background(accentColor.copy(alpha = 0.15f)),
                contentAlignment = Alignment.Center
            ) {
                Icon(imageVector = icon, contentDescription = null, tint = accentColor, modifier = Modifier.size(18.dp))
            }
            Spacer(modifier = Modifier.width(12.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(text = title, fontSize = 13.sp, fontWeight = FontWeight.Bold, color = TextPrimary)
                Text(text = desc, fontSize = 11.sp, color = TextSecondary)
            }
            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(6.dp))
                    .background(CyberSurfaceVariant)
                    .padding(horizontal = 6.dp, vertical = 3.dp)
            ) {
                Text(text = "OPEN", fontSize = 10.sp, fontWeight = FontWeight.Bold, color = accentColor)
            }
        }
    }
}
