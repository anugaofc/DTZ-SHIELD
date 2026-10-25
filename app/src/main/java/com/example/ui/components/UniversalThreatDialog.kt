package com.example.ui.components

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Block
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Security
import androidx.compose.material.icons.filled.Shield
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
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
import androidx.compose.ui.window.Dialog
import com.example.ui.theme.CyberCardBorder
import com.example.ui.theme.CyberSurface
import com.example.ui.theme.NeonAmber
import com.example.ui.theme.NeonCyan
import com.example.ui.theme.NeonEmerald
import com.example.ui.theme.NeonRed
import com.example.ui.theme.TextPrimary
import com.example.ui.theme.TextSecondary

data class UniversalThreatNotice(
    val title: String,
    val threatCategory: String, // Virus, Malicious Website, Ransomware, SMS Scam, Network Intrusion, etc.
    val targetName: String,
    val targetLocation: String,
    val severity: String, // Critical, High Risk, Warning
    val reason: String,
    val recommendedAction: String,
    val actionTaken: String = "Blocked & Quarantined"
)

@Composable
fun UniversalThreatDialog(
    threat: UniversalThreatNotice,
    onDismiss: () -> Unit,
    onBlockAndQuarantine: () -> Unit
) {
    val infiniteTransition = rememberInfiniteTransition(label = "threat_pulse")
    val pulseGlow by infiniteTransition.animateFloat(
        initialValue = 0.3f,
        targetValue = 0.9f,
        animationSpec = infiniteRepeatable(
            animation = tween(1000, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "glow"
    )

    Dialog(onDismissRequest = onDismiss) {
        Surface(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(22.dp))
                .border(2.dp, NeonRed.copy(alpha = pulseGlow), RoundedCornerShape(22.dp)),
            color = CyberSurface,
            tonalElevation = 10.dp
        ) {
            Column(
                modifier = Modifier
                    .padding(22.dp)
                    .verticalScroll(rememberScrollState()),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // Top Logo Emblem Header
                Box(
                    modifier = Modifier
                        .size(68.dp)
                        .clip(CircleShape)
                        .background(NeonRed.copy(alpha = 0.18f))
                        .border(2.dp, NeonRed, CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.Shield,
                        contentDescription = "DTZ Shield Logo",
                        tint = NeonRed,
                        modifier = Modifier.size(38.dp)
                    )
                }

                Spacer(modifier = Modifier.height(12.dp))

                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = "DTZ SHIELD",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Black,
                        color = NeonRed,
                        letterSpacing = 1.sp
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = "DEFENSE ALERT",
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Bold,
                        color = TextPrimary
                    )
                }

                Text(
                    text = threat.actionTaken.uppercase(),
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold,
                    color = NeonEmerald,
                    letterSpacing = 0.8.sp,
                    modifier = Modifier.padding(top = 2.dp)
                )

                Spacer(modifier = Modifier.height(16.dp))

                // Detail Information Card
                GlassCard(
                    modifier = Modifier.fillMaxWidth(),
                    borderColor = CyberCardBorder
                ) {
                    ThreatRow(label = "Threat Target", value = threat.targetName, isMono = true)
                    Spacer(modifier = Modifier.height(6.dp))
                    ThreatRow(label = "Category", value = threat.threatCategory)
                    Spacer(modifier = Modifier.height(6.dp))
                    ThreatRow(label = "Location / Source", value = threat.targetLocation)
                    Spacer(modifier = Modifier.height(6.dp))
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "Risk Level:",
                            fontSize = 12.sp,
                            color = TextSecondary,
                            modifier = Modifier.width(115.dp)
                        )
                        StatusPill(severity = threat.severity)
                    }
                }

                Spacer(modifier = Modifier.height(14.dp))

                // Warning Reason Box
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(12.dp))
                        .background(Color(0xFF260D12))
                        .border(1.dp, NeonRed.copy(alpha = 0.5f), RoundedCornerShape(12.dp))
                        .padding(14.dp)
                ) {
                    Column {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(imageVector = Icons.Default.Warning, contentDescription = null, tint = NeonRed, modifier = Modifier.size(16.dp))
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = "REASON FOR WARNING",
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold,
                                color = NeonRed,
                                letterSpacing = 0.5.sp
                            )
                        }
                        Spacer(modifier = Modifier.height(6.dp))
                        Text(
                            text = threat.reason,
                            fontSize = 12.sp,
                            color = TextPrimary,
                            lineHeight = 17.sp
                        )
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))

                // Recommended Action Box
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(12.dp))
                        .background(Color(0xFF0F1E2A))
                        .border(1.dp, NeonCyan.copy(alpha = 0.4f), RoundedCornerShape(12.dp))
                        .padding(14.dp)
                ) {
                    Column {
                        Text(
                            text = "RECOMMENDED ACTION:",
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            color = NeonCyan,
                            letterSpacing = 0.5.sp
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = threat.recommendedAction,
                            fontSize = 12.sp,
                            color = TextPrimary,
                            lineHeight = 17.sp
                        )
                    }
                }

                Spacer(modifier = Modifier.height(20.dp))

                // Action Buttons
                Button(
                    onClick = onBlockAndQuarantine,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(48.dp)
                        .testTag("threat_quarantine_block_button"),
                    colors = ButtonDefaults.buttonColors(containerColor = NeonRed, contentColor = Color.White),
                    shape = RoundedCornerShape(10.dp)
                ) {
                    Icon(imageVector = Icons.Default.Block, contentDescription = null, modifier = Modifier.size(18.dp))
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(text = "Quarantine Threat & Block", fontWeight = FontWeight.Bold, fontSize = 13.sp)
                }

                Spacer(modifier = Modifier.height(8.dp))

                OutlinedButton(
                    onClick = onDismiss,
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("threat_dismiss_button"),
                    colors = ButtonDefaults.outlinedButtonColors(contentColor = TextSecondary),
                    shape = RoundedCornerShape(10.dp)
                ) {
                    Text(text = "Close Alert (Keep Shielded)", fontSize = 12.sp)
                }
            }
        }
    }
}

@Composable
private fun ThreatRow(label: String, value: String, isMono: Boolean = false) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = "$label:",
            fontSize = 12.sp,
            color = TextSecondary,
            modifier = Modifier.width(115.dp)
        )
        Text(
            text = value,
            fontSize = 12.sp,
            color = TextPrimary,
            fontWeight = FontWeight.SemiBold,
            fontFamily = if (isMono) FontFamily.Monospace else FontFamily.Default,
            maxLines = 1
        )
    }
}
