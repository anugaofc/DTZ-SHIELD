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
import androidx.compose.material.icons.filled.CameraAlt
import androidx.compose.material.icons.filled.FolderSpecial
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Mic
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Security
import androidx.compose.material.icons.filled.Visibility
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
fun PrivacyScreen(
    viewModel: MainViewModel,
    modifier: Modifier = Modifier
) {
    val settings by viewModel.settings.collectAsStateWithLifecycle()
    val report by viewModel.privacyReport.collectAsStateWithLifecycle()

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
                        text = "PRIVACY & DATA SHIELD",
                        fontSize = 19.sp,
                        fontWeight = FontWeight.Black,
                        color = TextPrimary,
                        letterSpacing = 0.8.sp
                    )
                    Text(
                        text = "Ransomware folder lock, webcam protection & sensitive data",
                        fontSize = 12.sp,
                        color = TextSecondary
                    )
                }

                Button(
                    onClick = { viewModel.runPrivacyAudit() },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = CyberSurfaceVariant,
                        contentColor = NeonCyan
                    ),
                    shape = RoundedCornerShape(8.dp),
                    modifier = Modifier.testTag("audit_privacy_button")
                ) {
                    Icon(imageVector = Icons.Default.Refresh, contentDescription = null, modifier = Modifier.size(14.dp))
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(text = "Audit", fontSize = 11.sp, fontWeight = FontWeight.Bold)
                }
            }
        }

        // Privacy Score Banner
        if (report != null) {
            val r = report!!
            item {
                GlassCard(
                    modifier = Modifier.fillMaxWidth(),
                    borderColor = NeonCyan.copy(alpha = 0.4f)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        SecurityScoreGauge(
                            score = r.privacyScore,
                            sizeDp = 125.dp
                        )

                        Spacer(modifier = Modifier.width(16.dp))

                        Column {
                            Text(
                                text = "PRIVACY RATING",
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Black,
                                color = NeonCyan,
                                letterSpacing = 0.8.sp
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = "Privacy Score: ${r.privacyScore}/100",
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Bold,
                                color = TextPrimary
                            )
                            Text(
                                text = if (r.privacyScore >= 80) "Your device has good privacy protection."
                                else "Elevated privacy exposure detected across installed permissions.",
                                fontSize = 11.sp,
                                color = TextSecondary,
                                lineHeight = 15.sp
                            )
                        }
                    }
                }
            }

            // Ransomware Protection Toggle & Folders
            item {
                GlassCard(
                    modifier = Modifier.fillMaxWidth(),
                    borderColor = if (settings.ransomwareProtection) NeonEmerald.copy(alpha = 0.4f) else NeonAmber.copy(alpha = 0.4f)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Box(
                            modifier = Modifier
                                .size(42.dp)
                                .clip(CircleShape)
                                .background(NeonEmerald.copy(alpha = 0.15f)),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(imageVector = Icons.Default.Lock, contentDescription = null, tint = NeonEmerald, modifier = Modifier.size(22.dp))
                        }
                        Spacer(modifier = Modifier.width(12.dp))
                        Column(modifier = Modifier.weight(1f)) {
                            Text(text = "Ransomware Shield", fontSize = 14.sp, fontWeight = FontWeight.Bold, color = TextPrimary)
                            Text(
                                text = "Blocks unauthorized encryption & silent file alteration",
                                fontSize = 11.sp,
                                color = TextSecondary
                            )
                        }
                        Switch(
                            checked = settings.ransomwareProtection,
                            onCheckedChange = { viewModel.toggleRansomware(it) },
                            colors = SwitchDefaults.colors(
                                checkedThumbColor = Color(0xFF00220F),
                                checkedTrackColor = NeonEmerald,
                                uncheckedThumbColor = Color(0xFF64748B),
                                uncheckedTrackColor = Color(0xFF1E293B)
                            )
                        )
                    }

                    Spacer(modifier = Modifier.height(10.dp))
                    Text(text = "Protected Vault Directories:", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = TextSecondary)
                    Spacer(modifier = Modifier.height(6.dp))
                    Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                        for (folder in r.ransomwareProtectedFolders) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(imageVector = Icons.Default.FolderSpecial, contentDescription = null, tint = NeonCyan, modifier = Modifier.size(14.dp))
                                Spacer(modifier = Modifier.width(6.dp))
                                Text(text = folder, fontSize = 11.sp, color = TextPrimary, fontFamily = FontFamily.Monospace)
                            }
                        }
                    }
                }
            }

            // Webcam & Microphone Protection
            item {
                GlassCard(
                    modifier = Modifier.fillMaxWidth(),
                    borderColor = if (settings.webcamProtection) NeonCyan.copy(alpha = 0.4f) else NeonAmber.copy(alpha = 0.4f)
                ) {
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
                            Icon(imageVector = Icons.Default.CameraAlt, contentDescription = null, tint = NeonCyan, modifier = Modifier.size(22.dp))
                        }
                        Spacer(modifier = Modifier.width(12.dp))
                        Column(modifier = Modifier.weight(1f)) {
                            Text(text = "Webcam & Mic Guard", fontSize = 14.sp, fontWeight = FontWeight.Bold, color = TextPrimary)
                            Text(
                                text = "Alerts on background camera/mic recording events",
                                fontSize = 11.sp,
                                color = TextSecondary
                            )
                        }
                        Switch(
                            checked = settings.webcamProtection,
                            onCheckedChange = { viewModel.toggleWebcam(it) },
                            colors = SwitchDefaults.colors(
                                checkedThumbColor = Color(0xFF001F29),
                                checkedTrackColor = NeonCyan,
                                uncheckedThumbColor = Color(0xFF64748B),
                                uncheckedTrackColor = Color(0xFF1E293B)
                            )
                        )
                    }
                }
            }

            // Sensitive Apps Permissions Audit
            item {
                Text(
                    text = "CAMERA & MICROPHONE ACCESS APPS (${r.cameraAccessApps.size + r.micAccessApps.size})",
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold,
                    color = TextSecondary,
                    letterSpacing = 0.8.sp
                )
            }

            items(r.cameraAccessApps) { app ->
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
                            Icon(imageVector = Icons.Default.CameraAlt, contentDescription = null, tint = NeonCyan, modifier = Modifier.size(16.dp))
                        }
                        Spacer(modifier = Modifier.width(12.dp))
                        Column(modifier = Modifier.weight(1f)) {
                            Text(text = app.appName, fontSize = 13.sp, fontWeight = FontWeight.Bold, color = TextPrimary)
                            Text(text = app.packageName, fontSize = 10.sp, color = TextSecondary, fontFamily = FontFamily.Monospace)
                        }
                        StatusPill(severity = if (app.riskLevel == "High") "Warning" else "Safe")
                    }
                }
            }

            items(r.micAccessApps) { app ->
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
                            Icon(imageVector = Icons.Default.Mic, contentDescription = null, tint = NeonAmber, modifier = Modifier.size(16.dp))
                        }
                        Spacer(modifier = Modifier.width(12.dp))
                        Column(modifier = Modifier.weight(1f)) {
                            Text(text = app.appName, fontSize = 13.sp, fontWeight = FontWeight.Bold, color = TextPrimary)
                            Text(text = app.packageName, fontSize = 10.sp, color = TextSecondary, fontFamily = FontFamily.Monospace)
                        }
                        StatusPill(severity = if (app.riskLevel == "High") "Warning" else "Safe")
                    }
                }
            }
        }

        item {
            Spacer(modifier = Modifier.height(24.dp))
        }
    }
}
