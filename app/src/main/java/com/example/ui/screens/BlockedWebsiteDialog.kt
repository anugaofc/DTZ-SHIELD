package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
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
import com.example.domain.scanner.WebCheckResult
import com.example.ui.components.GlassCard
import com.example.ui.components.StatusPill
import com.example.ui.theme.CyberCardBorder
import com.example.ui.theme.CyberSurface
import com.example.ui.theme.NeonCyan
import com.example.ui.theme.NeonRed
import com.example.ui.theme.TextPrimary
import com.example.ui.theme.TextSecondary

@Composable
fun BlockedWebsiteDialog(
    warning: WebCheckResult,
    onDismiss: () -> Unit
) {
    Dialog(onDismissRequest = onDismiss) {
        Surface(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(20.dp))
                .border(1.5.dp, NeonRed, RoundedCornerShape(20.dp)),
            color = CyberSurface,
            tonalElevation = 8.dp
        ) {
            Column(
                modifier = Modifier
                    .padding(22.dp)
                    .verticalScroll(rememberScrollState()),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // Header Warning Icon
                Box(
                    modifier = Modifier
                        .size(64.dp)
                        .clip(CircleShape)
                        .background(NeonRed.copy(alpha = 0.15f))
                        .border(2.dp, NeonRed, CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.Block,
                        contentDescription = "Threat Blocked",
                        tint = NeonRed,
                        modifier = Modifier.size(34.dp)
                    )
                }

                Spacer(modifier = Modifier.height(14.dp))

                Text(
                    text = "DTZ SHIELD BLOCKED THIS WEBSITE",
                    fontSize = 17.sp,
                    fontWeight = FontWeight.Black,
                    color = NeonRed,
                    letterSpacing = 0.5.sp
                )

                Text(
                    text = "High-Risk Connection Intercepted",
                    fontSize = 12.sp,
                    color = TextSecondary,
                    modifier = Modifier.padding(top = 2.dp)
                )

                Spacer(modifier = Modifier.height(18.dp))

                // Detail Cards
                GlassCard(
                    modifier = Modifier.fillMaxWidth(),
                    borderColor = CyberCardBorder
                ) {
                    DetailRow(label = "Target Domain", value = warning.domain, isMono = true)
                    Spacer(modifier = Modifier.height(8.dp))
                    DetailRow(label = "Threat Category", value = warning.threatCategory)
                    Spacer(modifier = Modifier.height(8.dp))
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "Risk Level:",
                            fontSize = 13.sp,
                            color = TextSecondary,
                            modifier = Modifier.width(110.dp)
                        )
                        StatusPill(severity = warning.riskLevel)
                    }
                }

                Spacer(modifier = Modifier.height(14.dp))

                // Reason Box
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(12.dp))
                        .background(Color(0xFF1E1420))
                        .border(1.dp, NeonRed.copy(alpha = 0.4f), RoundedCornerShape(12.dp))
                        .padding(14.dp)
                ) {
                    Column {
                        Text(
                            text = "Reason for Blocking:",
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold,
                            color = NeonRed
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = warning.reason,
                            fontSize = 13.sp,
                            color = TextPrimary,
                            lineHeight = 18.sp
                        )
                    }
                }

                Spacer(modifier = Modifier.height(14.dp))

                // Recommended Action
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
                            text = "Recommended Action:",
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold,
                            color = NeonCyan
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = warning.recommendedAction,
                            fontSize = 13.sp,
                            color = TextPrimary,
                            lineHeight = 18.sp
                        )
                    }
                }

                Spacer(modifier = Modifier.height(20.dp))

                Button(
                    onClick = onDismiss,
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("dismiss_blocked_site_button"),
                    colors = ButtonDefaults.buttonColors(containerColor = NeonCyan, contentColor = Color(0xFF001F29)),
                    shape = RoundedCornerShape(10.dp)
                ) {
                    Text(
                        text = "Close & Stay Safe",
                        fontWeight = FontWeight.Bold,
                        fontSize = 14.sp
                    )
                }
            }
        }
    }
}

@Composable
private fun DetailRow(
    label: String,
    value: String,
    isMono: Boolean = false
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = "$label:",
            fontSize = 13.sp,
            color = TextSecondary,
            modifier = Modifier.width(110.dp)
        )
        Text(
            text = value,
            fontSize = 13.sp,
            color = TextPrimary,
            fontWeight = FontWeight.SemiBold,
            fontFamily = if (isMono) FontFamily.Monospace else FontFamily.Default
        )
    }
}
