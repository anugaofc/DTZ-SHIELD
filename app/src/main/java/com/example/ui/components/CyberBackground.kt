package com.example.ui.components

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import com.example.ui.theme.CyberBackground
import com.example.ui.theme.CyberVoid
import com.example.ui.theme.NeonCyan

@Composable
fun CyberBackground(
    modifier: Modifier = Modifier,
    performanceMode: Boolean = false
) {
    if (performanceMode) {
        Canvas(modifier = modifier.fillMaxSize()) {
            drawRect(
                brush = Brush.verticalGradient(
                    colors = listOf(CyberVoid, CyberBackground, CyberVoid)
                )
            )
        }
        return
    }

    val infiniteTransition = rememberInfiniteTransition(label = "cyber_bg")
    val offsetY by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 60f,
        animationSpec = infiniteRepeatable(
            animation = tween(4000, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "grid_anim"
    )
    val glowAlpha by infiniteTransition.animateFloat(
        initialValue = 0.05f,
        targetValue = 0.16f,
        animationSpec = infiniteRepeatable(
            animation = tween(2600, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "glow_anim"
    )

    Canvas(modifier = modifier.fillMaxSize()) {
        // Base dark cyber gradient
        drawRect(
            brush = Brush.verticalGradient(
                colors = listOf(CyberVoid, CyberBackground, CyberVoid)
            )
        )

        // Subtle digital grid
        val gridSize = 60f
        val startY = offsetY % gridSize
        val width = size.width
        val height = size.height

        val gridColor = NeonCyan.copy(alpha = 0.035f)
        var y = startY
        while (y < height) {
            drawLine(
                color = gridColor,
                start = Offset(0f, y),
                end = Offset(width, y),
                strokeWidth = 1f
            )
            y += gridSize
        }

        var x = 0f
        while (x < width) {
            drawLine(
                color = gridColor,
                start = Offset(x, 0f),
                end = Offset(x, height),
                strokeWidth = 1f
            )
            x += gridSize
        }

        // Ambient cyber glows (top center & bottom right)
        drawCircle(
            brush = Brush.radialGradient(
                colors = listOf(NeonCyan.copy(alpha = glowAlpha), Color.Transparent),
                center = Offset(width * 0.5f, height * 0.15f),
                radius = width * 0.7f
            ),
            center = Offset(width * 0.5f, height * 0.15f),
            radius = width * 0.7f
        )
    }
}
