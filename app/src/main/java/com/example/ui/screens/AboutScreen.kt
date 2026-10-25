package com.example.ui.screens

import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.Image
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
import androidx.compose.material.icons.filled.Chat
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Code
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Policy
import androidx.compose.material.icons.filled.Security
import androidx.compose.material.icons.filled.Shield
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.R
import com.example.ui.components.GlassCard
import com.example.ui.theme.CyberCardBorder
import com.example.ui.theme.CyberSurfaceVariant
import com.example.ui.theme.NeonCyan
import com.example.ui.theme.NeonEmerald
import com.example.ui.theme.TextPrimary
import com.example.ui.theme.TextSecondary

@Composable
fun AboutScreen(
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Hero Brand Section
        item {
            Spacer(modifier = Modifier.height(4.dp))
            GlassCard(
                modifier = Modifier.fillMaxWidth(),
                borderColor = NeonCyan.copy(alpha = 0.5f)
            ) {
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Box(
                        modifier = Modifier
                            .size(70.dp)
                            .clip(CircleShape)
                            .background(NeonCyan.copy(alpha = 0.15f))
                            .border(2.dp, NeonCyan, CircleShape),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.Shield,
                            contentDescription = "DTZ Shield Logo",
                            tint = NeonCyan,
                            modifier = Modifier.size(38.dp)
                        )
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    Text(
                        text = "DTZ SHIELD",
                        fontSize = 22.sp,
                        fontWeight = FontWeight.Black,
                        color = TextPrimary,
                        letterSpacing = 1.sp
                    )

                    Text(
                        text = "«Free. Powerful. Protected.»",
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Bold,
                        color = NeonCyan,
                        letterSpacing = 0.5.sp
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    Text(
                        text = "DTZ SHIELD is a free security suite developed by UltraDev by Anuga Senithu and the Dark Tech Zone team.",
                        fontSize = 12.sp,
                        color = TextSecondary,
                        lineHeight = 16.sp,
                        textAlign = androidx.compose.ui.text.style.TextAlign.Center
                    )
                }
            }
        }

        // 👨‍💻 Primary Developer Contact Button
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
                    .height(52.dp)
                    .testTag("about_contact_developer_whatsapp_button"),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFF25D366),
                    contentColor = Color.White
                ),
                shape = RoundedCornerShape(12.dp)
            ) {
                Icon(imageVector = Icons.Default.Chat, contentDescription = null, modifier = Modifier.size(20.dp))
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "Contact Developer on WhatsApp",
                    fontWeight = FontWeight.Bold,
                    fontSize = 14.sp
                )
            }
        }

        // Developer & Team Information
        item {
            GlassCard(modifier = Modifier.fillMaxWidth()) {
                Text(
                    text = "DEVELOPER & ORGANIZATION",
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold,
                    color = NeonCyan,
                    letterSpacing = 0.8.sp
                )
                Spacer(modifier = Modifier.height(10.dp))

                AboutInfoRow("Lead Developer", "Anuga Senithu")
                AboutInfoRow("Engineering Team", "Dark Tech Zone")
                AboutInfoRow("Organization", "UltraDev")
                AboutInfoRow("Software Version", "2.0.1 Premium Release (100% Free)")
                AboutInfoRow("Product Type", "Free Security & Privacy Suite")
            }
        }

        // 100% Free Guarantee Card
        item {
            GlassCard(
                modifier = Modifier.fillMaxWidth(),
                borderColor = NeonEmerald.copy(alpha = 0.4f)
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(imageVector = Icons.Default.CheckCircle, contentDescription = null, tint = NeonEmerald, modifier = Modifier.size(22.dp))
                    Spacer(modifier = Modifier.width(10.dp))
                    Text(
                        text = "100% Free Security Guarantee",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold,
                        color = NeonEmerald
                    )
                }
                Spacer(modifier = Modifier.height(6.dp))
                Text(
                    text = "All core DTZ SHIELD features are available completely free. No artificial paywalls, no fake locks, no subscriptions, and no paid unlock prompts for core security.",
                    fontSize = 12.sp,
                    color = TextPrimary,
                    lineHeight = 16.sp
                )
            }
        }

        // Security Architecture & Integrity
        item {
            GlassCard(modifier = Modifier.fillMaxWidth()) {
                Text(
                    text = "SECURITY & INTEGRITY COMMITMENT",
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold,
                    color = NeonCyan,
                    letterSpacing = 0.8.sp
                )
                Spacer(modifier = Modifier.height(8.dp))
                BulletItem("Zero telemetry selling or unauthorized data collection")
                BulletItem("Legitimate local heuristic and threat cloud engine")
                BulletItem("Zero destructive behavior or silent file deletion")
                BulletItem("Built strictly in accordance with Android Platform Security Standards")
            }
        }

        // Legal & Open Source
        item {
            GlassCard(modifier = Modifier.fillMaxWidth()) {
                Text(text = "LEGAL & LICENSES", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = TextSecondary)
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "Licensed under Apache License 2.0 & MIT libraries. Material 3 Design components by Google Android Open Source Project.",
                    fontSize = 11.sp,
                    color = TextSecondary,
                    lineHeight = 15.sp
                )
                Spacer(modifier = Modifier.height(6.dp))
                Text(
                    text = "© 2026 UltraDev • Dark Tech Zone • All Rights Reserved.",
                    fontSize = 10.sp,
                    color = TextSecondary,
                    fontFamily = FontFamily.Monospace
                )
            }
        }

        item {
            Spacer(modifier = Modifier.height(24.dp))
        }
    }
}

@Composable
private fun AboutInfoRow(label: String, value: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(text = label, fontSize = 12.sp, color = TextSecondary)
        Text(text = value, fontSize = 12.sp, fontWeight = FontWeight.Bold, color = TextPrimary)
    }
}

@Composable
private fun BulletItem(text: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 3.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(4.dp)
                .clip(CircleShape)
                .background(NeonCyan)
        )
        Spacer(modifier = Modifier.width(8.dp))
        Text(text = text, fontSize = 11.sp, color = TextPrimary)
    }
}
