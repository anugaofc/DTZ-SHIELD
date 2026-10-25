package com.example.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.SeverityCritical
import com.example.ui.theme.SeverityHighRisk
import com.example.ui.theme.SeverityInfo
import com.example.ui.theme.SeveritySafe
import com.example.ui.theme.SeverityWarning

@Composable
fun StatusPill(
    severity: String,
    modifier: Modifier = Modifier
) {
    val (bgColor, textColor, dotColor) = when (severity.lowercase()) {
        "safe", "clean", "protected" -> Triple(SeveritySafe.copy(alpha = 0.15f), SeveritySafe, SeveritySafe)
        "info" -> Triple(SeverityInfo.copy(alpha = 0.15f), SeverityInfo, SeverityInfo)
        "warning" -> Triple(SeverityWarning.copy(alpha = 0.15f), SeverityWarning, SeverityWarning)
        "high risk" -> Triple(SeverityHighRisk.copy(alpha = 0.15f), SeverityHighRisk, SeverityHighRisk)
        "critical" -> Triple(SeverityCritical.copy(alpha = 0.18f), SeverityCritical, SeverityCritical)
        else -> Triple(Color(0xFF334155), Color(0xFF94A3B8), Color(0xFF94A3B8))
    }

    Row(
        modifier = modifier
            .clip(RoundedCornerShape(12.dp))
            .background(bgColor)
            .border(1.dp, dotColor.copy(alpha = 0.4f), RoundedCornerShape(12.dp))
            .padding(horizontal = 8.dp, vertical = 4.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(6.dp)
                .clip(CircleShape)
                .background(dotColor)
        )
        Spacer(modifier = Modifier.width(6.dp))
        Text(
            text = severity.uppercase(),
            color = textColor,
            fontSize = 11.sp,
            fontWeight = FontWeight.Bold,
            letterSpacing = 0.5.sp
        )
    }
}
