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
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.DeviceThermostat
import androidx.compose.material.icons.filled.Memory
import androidx.compose.material.icons.filled.Storage
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.ui.MainViewModel
import com.example.ui.components.GlassCard
import com.example.ui.theme.CyberCardBorder
import com.example.ui.theme.CyberSurfaceVariant
import com.example.ui.theme.NeonAmber
import com.example.ui.theme.NeonCyan
import com.example.ui.theme.NeonEmerald
import com.example.ui.theme.NeonRed
import com.example.ui.theme.TextPrimary
import com.example.ui.theme.TextSecondary

@Composable
fun CleanupScreen(
    viewModel: MainViewModel,
    modifier: Modifier = Modifier
) {
    val health by viewModel.systemHealth.collectAsStateWithLifecycle()

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
                        text = "CLEANUP PREMIUM",
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Black,
                        color = TextPrimary,
                        letterSpacing = 0.8.sp
                    )
                    Text(
                        text = "Deep Junk File, Cache, Storage & RAM Booster",
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

        // 1-Click Clean Everything Hero Button
        item {
            GlassCard(
                modifier = Modifier.fillMaxWidth(),
                borderColor = NeonEmerald.copy(alpha = 0.5f)
            ) {
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Box(
                        modifier = Modifier
                            .size(72.dp)
                            .clip(CircleShape)
                            .background(NeonEmerald.copy(alpha = 0.15f))
                            .border(2.dp, NeonEmerald, CircleShape),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.CleaningServices,
                            contentDescription = null,
                            tint = NeonEmerald,
                            modifier = Modifier.size(38.dp)
                        )
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    Text(
                        text = "DEEP SYSTEM CLEANUP",
                        fontSize = 17.sp,
                        fontWeight = FontWeight.Black,
                        color = TextPrimary
                    )

                    Text(
                        text = "Purge application caches, obsolete installation leftovers & optimize RAM memory",
                        fontSize = 11.sp,
                        color = TextSecondary,
                        modifier = Modifier.padding(top = 2.dp, bottom = 14.dp)
                    )

                    Button(
                        onClick = { viewModel.optimizeCache() },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(48.dp)
                            .testTag("one_click_cleanup_button"),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = NeonEmerald,
                            contentColor = Color(0xFF00220F)
                        ),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Icon(imageVector = Icons.Default.CleaningServices, contentDescription = null, modifier = Modifier.size(18.dp))
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(text = "Boost Device & Clean Junk Now", fontWeight = FontWeight.Bold, fontSize = 13.sp)
                    }
                }
            }
        }

        // Memory & Storage Diagnostics Breakdown
        if (health != null) {
            val h = health!!
            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    GlassCard(modifier = Modifier.weight(1f)) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(imageVector = Icons.Default.Memory, contentDescription = null, tint = NeonCyan, modifier = Modifier.size(16.dp))
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(text = "RAM Memory", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = TextPrimary)
                        }
                        Spacer(modifier = Modifier.height(6.dp))
                        Text(text = "${h.ramUsagePercent}% Used", fontSize = 17.sp, fontWeight = FontWeight.Black, color = NeonCyan)
                        Text(text = "${h.availableRamMb} MB Available", fontSize = 10.sp, color = TextSecondary)
                        Spacer(modifier = Modifier.height(6.dp))
                        LinearProgressIndicator(
                            progress = { h.ramUsagePercent / 100f },
                            modifier = Modifier.fillMaxWidth().height(6.dp).clip(RoundedCornerShape(3.dp)),
                            color = NeonCyan,
                            trackColor = Color(0xFF1E293B),
                            strokeCap = StrokeCap.Round
                        )
                    }

                    GlassCard(modifier = Modifier.weight(1f)) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(imageVector = Icons.Default.Storage, contentDescription = null, tint = NeonEmerald, modifier = Modifier.size(16.dp))
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(text = "Storage Space", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = TextPrimary)
                        }
                        Spacer(modifier = Modifier.height(6.dp))
                        Text(text = "${h.storageUsagePercent}% Used", fontSize = 17.sp, fontWeight = FontWeight.Black, color = NeonEmerald)
                        Text(text = "${h.freeStorageGb} GB Free", fontSize = 10.sp, color = TextSecondary)
                        Spacer(modifier = Modifier.height(6.dp))
                        LinearProgressIndicator(
                            progress = { h.storageUsagePercent / 100f },
                            modifier = Modifier.fillMaxWidth().height(6.dp).clip(RoundedCornerShape(3.dp)),
                            color = NeonEmerald,
                            trackColor = Color(0xFF1E293B),
                            strokeCap = StrokeCap.Round
                        )
                    }
                }
            }
        }

        // Cleanup Categories
        item {
            Text(
                text = "CLEANUP MODULES",
                fontSize = 12.sp,
                fontWeight = FontWeight.Bold,
                color = TextSecondary,
                letterSpacing = 0.8.sp
            )
            Spacer(modifier = Modifier.height(8.dp))

            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                CleanupCategoryCard("Application Cache & Junk", "Temporary cached images, network caches & thumbnails", "380 MB Recoverable", true)
                CleanupCategoryCard("Residual Uninstalled Files", "Leftover files from previously removed applications", "145 MB Cleanable", true)
                CleanupCategoryCard("Obsolete Download Packages", "Stale APK installers in Downloads folder", "92 MB Found", false)
                CleanupCategoryCard("Battery & Thermal Guard", "CPU temperature: 34°C (Normal) • Background throttled", "Optimized", false)
            }
        }

        item {
            Spacer(modifier = Modifier.height(24.dp))
        }
    }
}

@Composable
private fun CleanupCategoryCard(title: String, desc: String, sizeLabel: String, isActionable: Boolean) {
    GlassCard(modifier = Modifier.fillMaxWidth()) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(text = title, fontSize = 13.sp, fontWeight = FontWeight.Bold, color = TextPrimary)
                Text(text = desc, fontSize = 11.sp, color = TextSecondary)
            }
            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(8.dp))
                    .background(CyberSurfaceVariant)
                    .padding(horizontal = 8.dp, vertical = 4.dp)
            ) {
                Text(text = sizeLabel, fontSize = 11.sp, fontWeight = FontWeight.Bold, color = if (isActionable) NeonEmerald else NeonCyan)
            }
        }
    }
}
