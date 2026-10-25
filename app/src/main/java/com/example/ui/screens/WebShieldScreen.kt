package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Download
import androidx.compose.material.icons.filled.GppGood
import androidx.compose.material.icons.filled.Language
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Security
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
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
fun WebShieldScreen(
    viewModel: MainViewModel,
    modifier: Modifier = Modifier
) {
    val settings by viewModel.settings.collectAsStateWithLifecycle()
    val checkResult by viewModel.webCheckResult.collectAsStateWithLifecycle()

    var inputUrl by remember { mutableStateOf("https://bank-login-secure.xyz") }

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
                text = "WEB & INTERNET SHIELD",
                fontSize = 20.sp,
                fontWeight = FontWeight.Black,
                color = TextPrimary,
                letterSpacing = 0.8.sp
            )
            Text(
                text = "Real-time phishing, malicious website, and scam prevention",
                fontSize = 12.sp,
                color = TextSecondary
            )
        }

        // Main Web Shield Toggle Card
        item {
            GlassCard(
                modifier = Modifier.fillMaxWidth(),
                borderColor = if (settings.webShield) NeonCyan.copy(alpha = 0.5f) else NeonAmber.copy(alpha = 0.3f)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .size(46.dp)
                            .clip(CircleShape)
                            .background(if (settings.webShield) NeonCyan.copy(alpha = 0.15f) else CyberSurfaceVariant),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.Language,
                            contentDescription = null,
                            tint = if (settings.webShield) NeonCyan else TextSecondary,
                            modifier = Modifier.size(26.dp)
                        )
                    }

                    Spacer(modifier = Modifier.width(14.dp))

                    Column(modifier = Modifier.weight(1f)) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(
                                text = "Web Protection",
                                fontSize = 15.sp,
                                fontWeight = FontWeight.Bold,
                                color = TextPrimary
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            StatusPill(severity = if (settings.webShield) "Protected" else "Warning")
                        }
                        Text(
                            text = if (settings.webShield) "Shield active: Filtering deceptive domains & malicious scripts"
                            else "Web Shield disabled. Browser navigation is unprotected.",
                            fontSize = 11.sp,
                            color = TextSecondary,
                            lineHeight = 15.sp
                        )
                    }

                    Switch(
                        checked = settings.webShield,
                        onCheckedChange = { viewModel.toggleWebShield(it) },
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

        // Feature Matrix Badges
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                WebFeatureBadge("Phishing Filter", Icons.Default.Security, Modifier.weight(1f))
                WebFeatureBadge("Dangerous URLs", Icons.Default.Warning, Modifier.weight(1f))
                WebFeatureBadge("Download Guard", Icons.Default.Download, Modifier.weight(1f))
                WebFeatureBadge("HTTPS Enforce", Icons.Default.Lock, Modifier.weight(1f))
            }
        }

        // Live URL & Threat Intelligence Inspector
        item {
            GlassCard(modifier = Modifier.fillMaxWidth()) {
                Text(
                    text = "URL & DOMAIN REPUTATION INSPECTOR",
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold,
                    color = NeonCyan,
                    letterSpacing = 0.8.sp
                )
                Spacer(modifier = Modifier.height(6.dp))
                Text(
                    text = "Verify domains against the DTZ Threat Intelligence Cloud & heuristic engine.",
                    fontSize = 11.sp,
                    color = TextSecondary
                )

                Spacer(modifier = Modifier.height(12.dp))

                OutlinedTextField(
                    value = inputUrl,
                    onValueChange = { inputUrl = it },
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("url_inspector_input"),
                    placeholder = { Text("https://example.com", color = Color(0xFF64748B)) },
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = NeonCyan,
                        unfocusedBorderColor = CyberCardBorder,
                        focusedTextColor = TextPrimary,
                        unfocusedTextColor = TextPrimary,
                        focusedContainerColor = CyberSurfaceVariant.copy(alpha = 0.5f),
                        unfocusedContainerColor = CyberSurfaceVariant.copy(alpha = 0.5f)
                    ),
                    singleLine = true,
                    shape = RoundedCornerShape(10.dp)
                )

                Spacer(modifier = Modifier.height(10.dp))

                // Test presets chips
                Text(text = "Quick Test Samples:", fontSize = 11.sp, color = TextSecondary)
                Spacer(modifier = Modifier.height(6.dp))
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .horizontalScroll(rememberScrollState()),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    SampleChip("https://bank-login-secure.xyz") { inputUrl = it; viewModel.checkUrl(it) }
                    SampleChip("https://evil-phishing.test") { inputUrl = it; viewModel.checkUrl(it) }
                    SampleChip("http://192.168.1.10/verify-account") { inputUrl = it; viewModel.checkUrl(it) }
                    SampleChip("https://google.com") { inputUrl = it; viewModel.checkUrl(it) }
                }

                Spacer(modifier = Modifier.height(14.dp))

                Button(
                    onClick = { viewModel.checkUrl(inputUrl) },
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("check_url_button"),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = NeonCyan,
                        contentColor = Color(0xFF001F29)
                    ),
                    shape = RoundedCornerShape(10.dp)
                ) {
                    Icon(imageVector = Icons.Default.Search, contentDescription = null, modifier = Modifier.size(18.dp))
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(text = "Analyze Website Safety", fontWeight = FontWeight.Bold, fontSize = 13.sp)
                }
            }
        }

        // Analysis Result Card
        if (checkResult != null) {
            val res = checkResult!!
            item {
                GlassCard(
                    modifier = Modifier.fillMaxWidth(),
                    borderColor = if (res.isBlocked) NeonRed else NeonEmerald
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = if (res.isBlocked) "⚠️ THREAT DETECTED" else "✅ DOMAIN VERIFIED SAFE",
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Bold,
                            color = if (res.isBlocked) NeonRed else NeonEmerald
                        )
                        StatusPill(severity = res.riskLevel)
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    Text(
                        text = "Domain: ${res.domain}",
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Bold,
                        color = TextPrimary,
                        fontFamily = FontFamily.Monospace
                    )
                    Text(
                        text = "Category: ${res.threatCategory}",
                        fontSize = 12.sp,
                        color = TextSecondary
                    )

                    Spacer(modifier = Modifier.height(6.dp))

                    Text(
                        text = res.reason,
                        fontSize = 12.sp,
                        color = TextPrimary,
                        lineHeight = 16.sp
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                        for (d in res.details) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Box(
                                    modifier = Modifier
                                        .size(4.dp)
                                        .clip(CircleShape)
                                        .background(if (res.isBlocked) NeonRed else NeonEmerald)
                                )
                                Spacer(modifier = Modifier.width(6.dp))
                                Text(text = d, fontSize = 11.sp, color = TextSecondary)
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
private fun WebFeatureBadge(
    title: String,
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .clip(RoundedCornerShape(8.dp))
            .background(CyberSurfaceVariant.copy(alpha = 0.6f))
            .border(1.dp, CyberCardBorder, RoundedCornerShape(8.dp))
            .padding(vertical = 10.dp, horizontal = 4.dp),
        contentAlignment = Alignment.Center
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Icon(imageVector = icon, contentDescription = null, tint = NeonCyan, modifier = Modifier.size(18.dp))
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = title,
                fontSize = 9.sp,
                fontWeight = FontWeight.SemiBold,
                color = TextPrimary,
                maxLines = 1
            )
        }
    }
}

@Composable
private fun SampleChip(url: String, onSelect: (String) -> Unit) {
    Box(
        modifier = Modifier
            .clip(RoundedCornerShape(16.dp))
            .background(CyberSurfaceVariant)
            .border(1.dp, CyberCardBorder, RoundedCornerShape(16.dp))
            .clickable { onSelect(url) }
            .padding(horizontal = 10.dp, vertical = 6.dp)
    ) {
        Text(
            text = url.removePrefix("https://").removePrefix("http://"),
            fontSize = 10.sp,
            color = NeonCyan,
            fontWeight = FontWeight.Medium
        )
    }
}
