package com.example.ui.components

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.NeonAmber
import com.example.ui.theme.NeonCyan
import com.example.ui.theme.NeonEmerald
import com.example.ui.theme.NeonRed
import com.example.ui.theme.TextPrimary
import com.example.ui.theme.TextSecondary

@Composable
fun SecurityScoreGauge(
    score: Int,
    modifier: Modifier = Modifier,
    sizeDp: Dp = 160.dp
) {
    val animatedProgress by animateFloatAsState(
        targetValue = (score / 100f).coerceIn(0f, 1f),
        animationSpec = tween(1200),
        label = "score_progress"
    )

    val scoreColor = when {
        score >= 85 -> NeonEmerald
        score >= 65 -> NeonCyan
        score >= 45 -> NeonAmber
        else -> NeonRed
    }

    val scoreStatusLabel = when {
        score >= 90 -> "EXCELLENT PROTECTION"
        score >= 75 -> "GOOD PROTECTION"
        score >= 50 -> "ATTENTION REQUIRED"
        else -> "CRITICAL VULNERABILITIES"
    }

    Box(
        modifier = modifier.size(sizeDp),
        contentAlignment = Alignment.Center
    ) {
        Canvas(modifier = Modifier.fillMaxSize()) {
            val center = Offset(size.width / 2f, size.height / 2f)
            val radius = size.minDimension / 2f - 14f

            // Background track
            drawArc(
                color = Color(0xFF1E293B),
                startAngle = 135f,
                sweepAngle = 270f,
                useCenter = false,
                style = Stroke(width = 10.dp.toPx(), cap = StrokeCap.Round)
            )

            // Active glowing progress arc
            drawArc(
                brush = Brush.sweepGradient(
                    colors = listOf(NeonCyan, scoreColor),
                    center = center
                ),
                startAngle = 135f,
                sweepAngle = 270f * animatedProgress,
                useCenter = false,
                style = Stroke(width = 10.dp.toPx(), cap = StrokeCap.Round)
            )
        }

        Column(
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "$score",
                fontSize = 38.sp,
                fontWeight = FontWeight.Black,
                color = TextPrimary
            )
            Text(
                text = "/ 100",
                fontSize = 12.sp,
                color = TextSecondary,
                fontWeight = FontWeight.Medium
            )
            Spacer(modifier = Modifier.height(2.dp))
            Text(
                text = scoreStatusLabel,
                fontSize = 10.sp,
                fontWeight = FontWeight.Bold,
                color = scoreColor,
                letterSpacing = 0.5.sp
            )
        }
    }
}
