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
import androidx.compose.material.icons.filled.Assessment
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.Security
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
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
import com.example.ui.theme.NeonCyan
import com.example.ui.theme.NeonEmerald
import com.example.ui.theme.NeonRed
import com.example.ui.theme.TextPrimary
import com.example.ui.theme.TextSecondary
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun ReportsScreen(
    viewModel: MainViewModel,
    modifier: Modifier = Modifier
) {
    val reports by viewModel.scanReports.collectAsStateWithLifecycle()
    val alerts by viewModel.alerts.collectAsStateWithLifecycle()

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
                text = "SECURITY REPORTS & AUDIT LOGS",
                fontSize = 19.sp,
                fontWeight = FontWeight.Black,
                color = TextPrimary,
                letterSpacing = 0.8.sp
            )
            Text(
                text = "Comprehensive history of scans, intercepted threats & system events",
                fontSize = 12.sp,
                color = TextSecondary
            )
        }

        // Summary Statistics Card
        item {
            GlassCard(modifier = Modifier.fillMaxWidth()) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    ReportStatItem("Total Scans", "${reports.size}", NeonCyan)
                    ReportStatItem("Security Events", "${alerts.size}", NeonEmerald)
                    val resolvedCount = alerts.count { it.isResolved }
                    ReportStatItem("Resolved", "$resolvedCount", NeonEmerald)
                }
            }
        }

        // Scan History Section
        item {
            Text(
                text = "SCAN EXECUTION HISTORY (${reports.size})",
                fontSize = 12.sp,
                fontWeight = FontWeight.Bold,
                color = TextSecondary,
                letterSpacing = 0.8.sp
            )
        }

        if (reports.isEmpty()) {
            item {
                GlassCard(modifier = Modifier.fillMaxWidth()) {
                    Column(
                        modifier = Modifier.fillMaxWidth().padding(vertical = 12.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Icon(imageVector = Icons.Default.History, contentDescription = null, tint = TextSecondary, modifier = Modifier.size(32.dp))
                        Spacer(modifier = Modifier.height(6.dp))
                        Text(text = "No Scans Recorded Yet", fontSize = 13.sp, fontWeight = FontWeight.Bold, color = TextPrimary)
                        Text(text = "Run a Smart Scan from the Dashboard to record reports.", fontSize = 11.sp, color = TextSecondary)
                    }
                }
            }
        } else {
            items(reports) { r ->
                val dateStr = SimpleDateFormat("MMM d, yyyy HH:mm:ss", Locale.getDefault()).format(Date(r.startTime))
                val durationSec = ((r.endTime - r.startTime) / 1000).coerceAtLeast(1)
                GlassCard(modifier = Modifier.fillMaxWidth()) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(text = r.scanType, fontSize = 14.sp, fontWeight = FontWeight.Bold, color = TextPrimary)
                        StatusPill(severity = if (r.threatsFound == 0) "Clean" else "Warning")
                    }
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(text = dateStr, fontSize = 11.sp, color = TextSecondary)
                    Spacer(modifier = Modifier.height(6.dp))
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        Text(text = "Items checked: ${r.itemsChecked}", fontSize = 11.sp, color = TextPrimary)
                        Text(text = "Duration: ${durationSec}s", fontSize = 11.sp, color = TextSecondary)
                        Text(
                            text = "Threats: ${r.threatsFound}",
                            fontSize = 11.sp,
                            color = if (r.threatsFound == 0) NeonEmerald else NeonRed,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }
        }

        // Security Events Timeline Section
        item {
            Text(
                text = "SECURITY EVENTS TIMELINE (${alerts.size})",
                fontSize = 12.sp,
                fontWeight = FontWeight.Bold,
                color = TextSecondary,
                letterSpacing = 0.8.sp
            )
        }

        if (alerts.isEmpty()) {
            item {
                GlassCard(modifier = Modifier.fillMaxWidth()) {
                    Text(text = "No security events recorded.", fontSize = 12.sp, color = TextSecondary)
                }
            }
        } else {
            items(alerts) { alert ->
                val dateStr = SimpleDateFormat("MMM d, HH:mm", Locale.getDefault()).format(Date(alert.timestamp))
                GlassCard(modifier = Modifier.fillMaxWidth()) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            StatusPill(severity = alert.severity)
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(text = alert.source, fontSize = 11.sp, color = TextSecondary)
                        }
                        Text(text = dateStr, fontSize = 10.sp, color = TextSecondary)
                    }
                    Spacer(modifier = Modifier.height(6.dp))
                    Text(text = alert.title, fontSize = 13.sp, fontWeight = FontWeight.Bold, color = TextPrimary)
                    Text(text = alert.description, fontSize = 11.sp, color = TextSecondary, lineHeight = 15.sp)
                    if (alert.recommendedAction.isNotEmpty()) {
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(text = "Action: ${alert.recommendedAction}", fontSize = 11.sp, color = NeonCyan)
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
private fun ReportStatItem(label: String, value: String, color: androidx.compose.ui.graphics.Color) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(text = value, fontSize = 20.sp, fontWeight = FontWeight.Black, color = color)
        Text(text = label, fontSize = 11.sp, color = TextSecondary)
    }
}
