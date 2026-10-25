package com.example.ui.screens

import android.content.Intent
import android.net.Uri
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
import androidx.compose.material.icons.filled.BugReport
import androidx.compose.material.icons.filled.Chat
import androidx.compose.material.icons.filled.GraphicEq
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Security
import androidx.compose.material.icons.filled.Speed
import androidx.compose.material.icons.filled.Sync
import androidx.compose.material.icons.filled.VolumeUp
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
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
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun SettingsScreen(
    viewModel: MainViewModel,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val settings by viewModel.settings.collectAsStateWithLifecycle()

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
                text = "SUITE SETTINGS & DEMO",
                fontSize = 19.sp,
                fontWeight = FontWeight.Black,
                color = TextPrimary,
                letterSpacing = 0.8.sp
            )
            Text(
                text = "Configure engine defenses, audio feedback, updates and safe demo tests",
                fontSize = 12.sp,
                color = TextSecondary
            )
        }

        // Protection Shield Toggles
        item {
            Text(
                text = "CORE DEFENSE CONFIGURATION",
                fontSize = 12.sp,
                fontWeight = FontWeight.Bold,
                color = TextSecondary,
                letterSpacing = 0.8.sp
            )
            Spacer(modifier = Modifier.height(8.dp))

            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                SettingsSwitchRow(
                    title = "Real-Time Protection",
                    description = "Monitors installed apps & suspicious background activity",
                    checked = settings.realTimeProtection,
                    onCheckedChange = { viewModel.toggleRealTime(it) }
                )
                SettingsSwitchRow(
                    title = "Web & Phishing Shield",
                    description = "Blocks known scam domains, phishing URLs, and unencrypted portals",
                    checked = settings.webShield,
                    onCheckedChange = { viewModel.toggleWebShield(it) }
                )
                SettingsSwitchRow(
                    title = "Network Intrusion & Firewall",
                    description = "Guards against ARP spoofing, rogue devices, and open ports",
                    checked = settings.networkShield,
                    onCheckedChange = { viewModel.toggleNetworkShield(it) }
                )
                SettingsSwitchRow(
                    title = "Ransomware Folder Guard",
                    description = "Protects documents & photos from unauthorized alteration",
                    checked = settings.ransomwareProtection,
                    onCheckedChange = { viewModel.toggleRansomware(it) }
                )
            }
        }

        // Audio & Performance
        item {
            Text(
                text = "AUDIO & PERFORMANCE CONTROLS",
                fontSize = 12.sp,
                fontWeight = FontWeight.Bold,
                color = TextSecondary,
                letterSpacing = 0.8.sp
            )
            Spacer(modifier = Modifier.height(8.dp))

            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                SettingsSwitchRow(
                    title = "Cyber Audio & Sound Effects",
                    description = "Play high-tech audio tones on scan start, completion & threat alerts",
                    checked = settings.soundEffects,
                    onCheckedChange = { viewModel.toggleSound(it) }
                )
                SettingsSwitchRow(
                    title = "Performance Mode",
                    description = "Reduces canvas animations & digital grid for battery optimization",
                    checked = settings.performanceMode,
                    onCheckedChange = { viewModel.togglePerformanceMode(it) }
                )
            }
        }

        // Database Update Section
        item {
            GlassCard(modifier = Modifier.fillMaxWidth()) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .size(42.dp)
                            .clip(CircleShape)
                            .background(NeonCyan.copy(alpha = 0.15f)),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(imageVector = Icons.Default.Sync, contentDescription = null, tint = NeonCyan, modifier = Modifier.size(22.dp))
                    }
                    Spacer(modifier = Modifier.width(12.dp))
                    Column(modifier = Modifier.weight(1f)) {
                        Text(text = "Protection Database", fontSize = 14.sp, fontWeight = FontWeight.Bold, color = TextPrimary)
                        Text(text = "Version: ${settings.databaseVersion} (Up to date)", fontSize = 11.sp, color = NeonEmerald)
                        val dateStr = SimpleDateFormat("MMM d, HH:mm", Locale.getDefault()).format(Date(settings.lastDefinitionsUpdate))
                        Text(text = "Last synced: $dateStr", fontSize = 10.sp, color = TextSecondary)
                    }

                    Button(
                        onClick = { viewModel.updateDefinitions() },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = CyberSurfaceVariant,
                            contentColor = NeonCyan
                        ),
                        shape = RoundedCornerShape(8.dp),
                        modifier = Modifier.testTag("update_definitions_button")
                    ) {
                        Text(text = "Update", fontSize = 11.sp, fontWeight = FontWeight.Bold)
                    }
                }
            }
        }

        // 🧪 Safe Demo / Test Mode Section
        item {
            GlassCard(
                modifier = Modifier.fillMaxWidth(),
                borderColor = NeonAmber.copy(alpha = 0.5f)
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(imageVector = Icons.Default.BugReport, contentDescription = null, tint = NeonAmber, modifier = Modifier.size(22.dp))
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "DTZ SHIELD SECURITY DEMO",
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Black,
                        color = NeonAmber,
                        letterSpacing = 0.5.sp
                    )
                }

                Spacer(modifier = Modifier.height(6.dp))
                Text(
                    text = "Safely test the antivirus quarantine and threat warning workflows without downloading any actual malware. Simulated detections are clearly labeled with «DEMO — NOT A REAL THREAT».",
                    fontSize = 11.sp,
                    color = TextSecondary,
                    lineHeight = 15.sp
                )

                Spacer(modifier = Modifier.height(12.dp))

                Button(
                    onClick = { viewModel.triggerSafeDemo() },
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("trigger_safe_demo_button"),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = NeonAmber,
                        contentColor = Color(0xFF1E1400)
                    ),
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Icon(imageVector = Icons.Default.PlayArrow, contentDescription = null, modifier = Modifier.size(16.dp))
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = "Simulate Threat Detection & Quarantine",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }

        // Developer WhatsApp Contact Button
        item {
            Button(
                onClick = {
                    try {
                        val intent = Intent(Intent.ACTION_VIEW, Uri.parse("https://reference-url-citation.invalid/0"))
                        context.startActivity(intent)
                    } catch (_: Exception) {}
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(48.dp)
                    .testTag("contact_developer_whatsapp_button"),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFF25D366),
                    contentColor = Color.White
                ),
                shape = RoundedCornerShape(10.dp)
            ) {
                Icon(imageVector = Icons.Default.Chat, contentDescription = null, modifier = Modifier.size(18.dp))
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "Contact Developer on WhatsApp",
                    fontWeight = FontWeight.Bold,
                    fontSize = 13.sp
                )
            }
        }

        item {
            Spacer(modifier = Modifier.height(24.dp))
        }
    }
}

@Composable
private fun SettingsSwitchRow(
    title: String,
    description: String,
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit
) {
    GlassCard(modifier = Modifier.fillMaxWidth()) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(text = title, fontSize = 13.sp, fontWeight = FontWeight.Bold, color = TextPrimary)
                Spacer(modifier = Modifier.height(2.dp))
                Text(text = description, fontSize = 11.sp, color = TextSecondary, lineHeight = 15.sp)
            }
            Switch(
                checked = checked,
                onCheckedChange = onCheckedChange,
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
