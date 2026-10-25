package com.example.ui.components

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.CyberVoid
import com.example.ui.theme.NeonCyan
import com.example.ui.theme.NeonEmerald
import com.example.ui.theme.TextPrimary
import com.example.ui.theme.TextSecondary

@Composable
fun SplashLoadingScreen(
    statusMessage: String,
    modifier: Modifier = Modifier
) {
    val infiniteTransition = rememberInfiniteTransition(label = "splash")
    val pulseAlpha by infiniteTransition.animateFloat(
        initialValue = 0.4f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(800, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "pulse"
    )

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(CyberVoid),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
            modifier = Modifier.padding(24.dp)
        ) {
            AnimatedCyberShield(
                status = ShieldStatus.SCANNING,
                sizeDp = 180.dp
            )

            Spacer(modifier = Modifier.height(28.dp))

            Text(
                text = "DTZ SHIELD",
                fontSize = 28.sp,
                fontWeight = FontWeight.Black,
                color = TextPrimary,
                letterSpacing = 2.sp
            )

            Text(
                text = "«Free. Powerful. Protected.»",
                fontSize = 13.sp,
                fontWeight = FontWeight.Bold,
                color = NeonCyan,
                letterSpacing = 0.8.sp
            )

            Text(
                text = "VERSION 2.0.1 • PREMIUM SECURITY SUITE",
                fontSize = 11.sp,
                fontWeight = FontWeight.Bold,
                color = NeonEmerald,
                letterSpacing = 1.sp,
                modifier = Modifier.padding(top = 4.dp, bottom = 28.dp)
            )

            LinearProgressIndicator(
                modifier = Modifier
                    .width(220.dp)
                    .height(6.dp)
                    .clip(RoundedCornerShape(3.dp)),
                color = NeonCyan,
                trackColor = Color(0xFF1E293B),
                strokeCap = StrokeCap.Round
            )

            Spacer(modifier = Modifier.height(14.dp))

            Text(
                text = statusMessage,
                fontSize = 12.sp,
                color = TextSecondary.copy(alpha = pulseAlpha),
                fontFamily = FontFamily.Monospace,
                fontWeight = FontWeight.Medium
            )
        }
    }
}
