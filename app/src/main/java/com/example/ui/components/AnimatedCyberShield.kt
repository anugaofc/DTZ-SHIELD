package com.example.ui.components

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Security
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.rotate
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.example.ui.theme.NeonAmber
import com.example.ui.theme.NeonCyan
import com.example.ui.theme.NeonEmerald
import com.example.ui.theme.NeonRed
import kotlin.math.cos
import kotlin.math.sin

enum class ShieldStatus {
    PROTECTED,
    SCANNING,
    THREAT_DETECTED,
    RESOLVED
}

@Composable
fun AnimatedCyberShield(
    status: ShieldStatus,
    modifier: Modifier = Modifier,
    sizeDp: Dp = 190.dp
) {
    val infiniteTransition = rememberInfiniteTransition(label = "shield_anim")

    // Rotation for scanning radar ring
    val radarRotation by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 360f,
        animationSpec = infiniteRepeatable(
            animation = tween(2200, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "radar_sweep"
    )

    // Pulse scale for glow
    val pulseAlpha by infiniteTransition.animateFloat(
        initialValue = 0.25f,
        targetValue = 0.85f,
        animationSpec = infiniteRepeatable(
            animation = tween(1400, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "shield_pulse"
    )

    // Primary aura color by state
    val auraColor by animateColorAsState(
        targetValue = when (status) {
            ShieldStatus.PROTECTED -> NeonEmerald
            ShieldStatus.SCANNING -> NeonCyan
            ShieldStatus.THREAT_DETECTED -> NeonRed
            ShieldStatus.RESOLVED -> NeonEmerald
        },
        animationSpec = tween(500),
        label = "aura_color"
    )

    Box(
        modifier = modifier.size(sizeDp),
        contentAlignment = Alignment.Center
    ) {
        Canvas(modifier = Modifier.fillMaxSize()) {
            val center = Offset(size.width / 2f, size.height / 2f)
            val baseRadius = size.minDimension / 2f - 20f

            // 1. Ambient Outer Glow
            drawCircle(
                brush = Brush.radialGradient(
                    colors = listOf(
                        auraColor.copy(alpha = pulseAlpha * 0.45f),
                        auraColor.copy(alpha = 0.05f),
                        Color.Transparent
                    ),
                    center = center,
                    radius = baseRadius * 1.35f
                ),
                center = center,
                radius = baseRadius * 1.35f
            )

            // 2. Concentric tech boundary rings
            drawCircle(
                color = auraColor.copy(alpha = 0.3f),
                center = center,
                radius = baseRadius,
                style = Stroke(width = 2f)
            )

            drawCircle(
                color = auraColor.copy(alpha = 0.15f),
                center = center,
                radius = baseRadius * 0.85f,
                style = Stroke(width = 1.5f)
            )

            // 3. Scanning Radar Ring & Sweep
            if (status == ShieldStatus.SCANNING) {
                rotate(degrees = radarRotation, pivot = center) {
                    drawArc(
                        brush = Brush.sweepGradient(
                            colors = listOf(
                                Color.Transparent,
                                NeonCyan.copy(alpha = 0.1f),
                                NeonCyan.copy(alpha = 0.8f)
                            ),
                            center = center
                        ),
                        startAngle = 0f,
                        sweepAngle = 140f,
                        useCenter = true
                    )
                    // Radar leading edge line
                    drawLine(
                        color = NeonCyan,
                        start = center,
                        end = Offset(center.x + baseRadius, center.y),
                        strokeWidth = 3f,
                        cap = StrokeCap.Round
                    )
                }

                // Cyber tick markers along ring
                val numTicks = 24
                for (i in 0 until numTicks) {
                    val angle = (i * (360.0 / numTicks)) * (Math.PI / 180.0)
                    val r1 = baseRadius - 8f
                    val r2 = baseRadius
                    val p1 = Offset(center.x + (r1 * cos(angle)).toFloat(), center.y + (r1 * sin(angle)).toFloat())
                    val p2 = Offset(center.x + (r2 * cos(angle)).toFloat(), center.y + (r2 * sin(angle)).toFloat())
                    drawLine(
                        color = NeonCyan.copy(alpha = 0.6f),
                        start = p1,
                        end = p2,
                        strokeWidth = 2f
                    )
                }
            }

            // 4. Central Shield Silhouette drawing
            val shieldPath = Path().apply {
                val w = size.width
                val h = size.height
                val topY = h * 0.22f
                val bottomY = h * 0.80f
                val leftX = w * 0.28f
                val rightX = w * 0.72f
                val midX = w * 0.50f

                moveTo(midX, topY)
                lineTo(rightX, topY + (h * 0.08f))
                lineTo(rightX, topY + (h * 0.32f))
                quadraticBezierTo(rightX, bottomY * 0.82f, midX, bottomY)
                quadraticBezierTo(leftX, bottomY * 0.82f, leftX, topY + (h * 0.32f))
                lineTo(leftX, topY + (h * 0.08f))
                close()
            }

            // Draw shield fill & cyber border
            drawPath(
                path = shieldPath,
                color = Color(0xFF0A1224).copy(alpha = 0.88f)
            )
            drawPath(
                path = shieldPath,
                brush = Brush.linearGradient(
                    colors = listOf(auraColor, auraColor.copy(alpha = 0.3f)),
                    start = Offset(0f, 0f),
                    end = Offset(size.width, size.height)
                ),
                style = Stroke(width = 3.5f)
            )
        }

        // Central Icon / Emblem
        val icon = when (status) {
            ShieldStatus.PROTECTED -> Icons.Default.Security
            ShieldStatus.SCANNING -> Icons.Default.Security
            ShieldStatus.THREAT_DETECTED -> Icons.Default.Warning
            ShieldStatus.RESOLVED -> Icons.Default.CheckCircle
        }

        Icon(
            imageVector = icon,
            contentDescription = "DTZ Security Shield",
            modifier = Modifier.size(sizeDp * 0.30f),
            tint = auraColor
        )
    }
}
