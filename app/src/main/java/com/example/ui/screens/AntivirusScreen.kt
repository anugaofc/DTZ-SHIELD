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
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Bolt
import androidx.compose.material.icons.filled.Cancel
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Folder
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.PowerSettingsNew
import androidx.compose.material.icons.filled.Restore
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Security
import androidx.compose.material.icons.filled.Usb
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.domain.scanner.ScanMode
import com.example.ui.MainViewModel
import com.example.ui.components.AnimatedCyberShield
import com.example.ui.components.GlassCard
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
fun AntivirusScreen(
    viewModel: MainViewModel,
    modifier: Modifier = Modifier
) {
    val isScanning by viewModel.isScanning.collectAsStateWithLifecycle()
    val scanProgress by viewModel.scanProgress.collectAsStateWithLifecycle()
    val scanMode by viewModel.activeScanMode.collectAsStateWithLifecycle()
    val quarantined by viewModel.quarantinedItems.collectAsStateWithLifecycle()

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
                text = "ANTIVIRUS ENGINE",
                fontSize = 20.sp,
                fontWeight = FontWeight.Black,
                color = TextPrimary,
                letterSpacing = 0.8.sp
            )
            Text(
                text = "Advanced heuristic scanner & quarantined threat vault",
                fontSize = 12.sp,
                color = TextSecondary
            )
        }

        // Active Scanning Progress Card
        if (isScanning) {
            item {
                GlassCard(
                    modifier = Modifier.fillMaxWidth(),
                    borderColor = NeonCyan
                ) {
                    Column(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        AnimatedCyberShield(
                            status = ShieldStatus.SCANNING,
                            sizeDp = 140.dp
                        )

                        Spacer(modifier = Modifier.height(12.dp))

                        Text(
                            text = scanMode.title.uppercase(),
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Black,
                            color = NeonCyan,
                            letterSpacing = 1.sp
                        )

                        Text(
                            text = "${scanProgress.percentage}% COMPLETE",
                            fontSize = 22.sp,
                            fontWeight = FontWeight.Black,
                            color = TextPrimary
                        )

                        Spacer(modifier = Modifier.height(10.dp))

                        LinearProgressIndicator(
                            progress = { scanProgress.percentage / 100f },
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(8.dp)
                                .clip(RoundedCornerShape(4.dp)),
                            color = NeonCyan,
                            trackColor = Color(0xFF1E293B),
                            strokeCap = StrokeCap.Round
                        )

                        Spacer(modifier = Modifier.height(10.dp))

                        Text(
                            text = scanProgress.currentItem,
                            fontSize = 11.sp,
                            color = TextSecondary,
                            maxLines = 1,
                            fontFamily = FontFamily.Monospace
                        )

                        Spacer(modifier = Modifier.height(4.dp))

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(
                                text = "Items scanned: ${scanProgress.itemsScanned}",
                                fontSize = 11.sp,
                                color = TextSecondary
                            )
                            Text(
                                text = "Threats found: ${scanProgress.threatsFound.size}",
                                fontSize = 11.sp,
                                color = if (scanProgress.threatsFound.isEmpty()) NeonEmerald else NeonRed,
                                fontWeight = FontWeight.Bold
                            )
                        }

                        Spacer(modifier = Modifier.height(14.dp))

                        Button(
                            onClick = { viewModel.cancelScan() },
                            colors = ButtonDefaults.buttonColors(
                                containerColor = NeonRed.copy(alpha = 0.2f),
                                contentColor = NeonRed
                            ),
                            shape = RoundedCornerShape(8.dp),
                            modifier = Modifier
                                .fillMaxWidth()
                                .testTag("cancel_scan_button")
                        ) {
                            Icon(imageVector = Icons.Default.Cancel, contentDescription = null, modifier = Modifier.size(16.dp))
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(text = "Stop Scanning", fontWeight = FontWeight.Bold, fontSize = 12.sp)
                        }
                    }
                }
            }
        }

        // Scanning Modes Grid
        if (!isScanning) {
            item {
                Text(
                    text = "SELECT SCAN MODE",
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold,
                    color = TextSecondary,
                    letterSpacing = 0.8.sp
                )
                Spacer(modifier = Modifier.height(8.dp))

                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    ScanModeCard(
                        mode = ScanMode.SMART,
                        icon = Icons.Default.Bolt,
                        isPrimary = true,
                        onScan = { viewModel.startScan(ScanMode.SMART) }
                    )
                    ScanModeCard(
                        mode = ScanMode.QUICK,
                        icon = Icons.Default.PlayArrow,
                        isPrimary = false,
                        onScan = { viewModel.startScan(ScanMode.QUICK) }
                    )
                    ScanModeCard(
                        mode = ScanMode.FULL,
                        icon = Icons.Default.Search,
                        isPrimary = false,
                        onScan = { viewModel.startScan(ScanMode.FULL) }
                    )
                    ScanModeCard(
                        mode = ScanMode.CUSTOM,
                        icon = Icons.Default.Folder,
                        isPrimary = false,
                        onScan = { viewModel.startScan(ScanMode.CUSTOM) }
                    )
                    ScanModeCard(
                        mode = ScanMode.USB,
                        icon = Icons.Default.Usb,
                        isPrimary = false,
                        onScan = { viewModel.startScan(ScanMode.USB) }
                    )
                    ScanModeCard(
                        mode = ScanMode.BOOT,
                        icon = Icons.Default.PowerSettingsNew,
                        isPrimary = false,
                        onScan = { viewModel.startScan(ScanMode.BOOT) }
                    )
                }
            }
        }

        // Quarantine Manager Section
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        text = "QUARANTINE MANAGER",
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Bold,
                        color = if (quarantined.isEmpty()) TextSecondary else NeonRed,
                        letterSpacing = 0.8.sp
                    )
                    Text(
                        text = "${quarantined.size} isolated threats securely contained",
                        fontSize = 11.sp,
                        color = TextSecondary
                    )
                }

                if (quarantined.isNotEmpty()) {
                    OutlinedButton(
                        onClick = { viewModel.clearAllQuarantine() },
                        colors = ButtonDefaults.outlinedButtonColors(contentColor = NeonRed),
                        shape = RoundedCornerShape(8.dp),
                        modifier = Modifier.testTag("clear_quarantine_button")
                    ) {
                        Text(text = "Clear All", fontSize = 11.sp, fontWeight = FontWeight.Bold)
                    }
                }
            }
        }

        if (quarantined.isEmpty()) {
            item {
                GlassCard(
                    modifier = Modifier.fillMaxWidth(),
                    borderColor = CyberCardBorder
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 14.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Icon(
                            imageVector = Icons.Default.CheckCircle,
                            contentDescription = null,
                            tint = NeonEmerald,
                            modifier = Modifier.size(36.dp)
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = "Quarantine Vault is Empty",
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Bold,
                            color = TextPrimary
                        )
                        Text(
                            text = "No active threats or quarantined malicious files detected.",
                            fontSize = 12.sp,
                            color = TextSecondary
                        )
                    }
                }
            }
        } else {
            items(quarantined) { item ->
                val dateStr = SimpleDateFormat("MMM d, yyyy HH:mm", Locale.getDefault()).format(Date(item.detectionDate))
                GlassCard(
                    modifier = Modifier.fillMaxWidth(),
                    borderColor = NeonRed.copy(alpha = 0.4f)
                ) {
                    Column(modifier = Modifier.fillMaxWidth()) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = item.threatName,
                                fontSize = 13.sp,
                                fontWeight = FontWeight.Bold,
                                color = NeonRed
                            )
                            StatusPill(severity = item.severity)
                        }

                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = "File: ${item.fileName}",
                            fontSize = 12.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = TextPrimary,
                            fontFamily = FontFamily.Monospace
                        )
                        Text(
                            text = "Path: ${item.originalLocation}",
                            fontSize = 11.sp,
                            color = TextSecondary,
                            maxLines = 1
                        )
                        Text(
                            text = "Detected: $dateStr • Action: ${item.actionTaken}",
                            fontSize = 10.sp,
                            color = TextSecondary
                        )
                        if (item.riskDescription.isNotEmpty()) {
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = item.riskDescription,
                                fontSize = 11.sp,
                                color = NeonAmber,
                                lineHeight = 15.sp
                            )
                        }

                        Spacer(modifier = Modifier.height(10.dp))

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.End
                        ) {
                            Button(
                                onClick = { viewModel.restoreQuarantineItem(item) },
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = CyberSurfaceVariant,
                                    contentColor = NeonCyan
                                ),
                                shape = RoundedCornerShape(8.dp),
                                modifier = Modifier.testTag("restore_threat_${item.id}")
                            ) {
                                Icon(imageVector = Icons.Default.Restore, contentDescription = null, modifier = Modifier.size(14.dp))
                                Spacer(modifier = Modifier.width(4.dp))
                                Text(text = "Restore", fontSize = 11.sp, fontWeight = FontWeight.Bold)
                            }
                            Spacer(modifier = Modifier.width(8.dp))
                            Button(
                                onClick = { viewModel.deleteQuarantineItem(item) },
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = NeonRed.copy(alpha = 0.2f),
                                    contentColor = NeonRed
                                ),
                                shape = RoundedCornerShape(8.dp),
                                modifier = Modifier.testTag("delete_threat_${item.id}")
                            ) {
                                Icon(imageVector = Icons.Default.Delete, contentDescription = null, modifier = Modifier.size(14.dp))
                                Spacer(modifier = Modifier.width(4.dp))
                                Text(text = "Delete", fontSize = 11.sp, fontWeight = FontWeight.Bold)
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
private fun ScanModeCard(
    mode: ScanMode,
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    isPrimary: Boolean,
    onScan: () -> Unit
) {
    GlassCard(
        modifier = Modifier.fillMaxWidth(),
        borderColor = if (isPrimary) NeonCyan.copy(alpha = 0.6f) else CyberCardBorder,
        onClick = onScan
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(40.dp)
                    .clip(CircleShape)
                    .background(if (isPrimary) NeonCyan.copy(alpha = 0.2f) else CyberSurfaceVariant),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    tint = if (isPrimary) NeonCyan else TextPrimary,
                    modifier = Modifier.size(20.dp)
                )
            }

            Spacer(modifier = Modifier.width(14.dp))

            Column(modifier = Modifier.weight(1f)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = mode.title,
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Bold,
                        color = TextPrimary
                    )
                    if (isPrimary) {
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "RECOMMENDED",
                            fontSize = 9.sp,
                            fontWeight = FontWeight.Bold,
                            color = NeonCyan
                        )
                    }
                }
                Text(
                    text = mode.description,
                    fontSize = 11.sp,
                    color = TextSecondary,
                    lineHeight = 15.sp
                )
            }

            Button(
                onClick = onScan,
                colors = ButtonDefaults.buttonColors(
                    containerColor = if (isPrimary) NeonCyan else CyberSurfaceVariant,
                    contentColor = if (isPrimary) Color(0xFF001F29) else TextPrimary
                ),
                shape = RoundedCornerShape(8.dp)
            ) {
                Text(text = "Scan", fontSize = 11.sp, fontWeight = FontWeight.Bold)
            }
        }
    }
}
