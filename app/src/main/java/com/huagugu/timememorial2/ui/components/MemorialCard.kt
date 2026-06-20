package com.huagugu.timememorial2.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.huagugu.timememorial2.data.Category
import com.huagugu.timememorial2.data.Memorial
import com.huagugu.timememorial2.ui.theme.CategoryFestival
import com.huagugu.timememorial2.ui.theme.CategoryLife
import com.huagugu.timememorial2.ui.theme.CategoryLove
import com.huagugu.timememorial2.ui.theme.CategoryStudy
import com.huagugu.timememorial2.ui.theme.CategoryWork
import com.huagugu.timememorial2.ui.theme.OnBackground
import com.huagugu.timememorial2.ui.theme.OnSurfaceVariant
import top.yukonga.miuix.kmp.basic.Text
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.concurrent.TimeUnit

private val categoryEmoji = mapOf(
    "LOVE" to "\u2764\uFE0F",
    "WORK" to "\uD83D\uDCBC",
    "LIFE" to "\uD83C\uDF82",
    "STUDY" to "\uD83D\uDCDA",
    "FESTIVAL" to "\uD83E\uDDE7"
)

@Composable
fun MemorialCard(
    memorial: Memorial,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val now = System.currentTimeMillis()
    val diffMs = memorial.date - now
    val diffDays = TimeUnit.MILLISECONDS.toDays(diffMs)
    val isPast = diffMs < 0
    val absDays = kotlin.math.abs(diffDays)

    val categoryColor = when (memorial.category) {
        "LOVE" -> CategoryLove
        "WORK" -> CategoryWork
        "LIFE" -> CategoryLife
        "STUDY" -> CategoryStudy
        "FESTIVAL" -> CategoryFestival
        else -> Color(0xFF7C4DFF)
    }

    val emoji = categoryEmoji[memorial.category] ?: "\uD83D\uDCC5"
    val dateFormat = SimpleDateFormat("yyyy.MM.dd", Locale.getDefault())
    val dateStr = dateFormat.format(Date(memorial.date))
    val noteStr = if (memorial.note.isNotEmpty()) " \u00B7 ${memorial.note}" else ""

    Row(
        modifier = modifier
            .fillMaxWidth()
            .clickable { onClick() }
            .padding(horizontal = 18.dp, vertical = 14.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Emoji icon
        Box(
            modifier = Modifier
                .size(36.dp)
                .clip(RoundedCornerShape(12.dp))
                .background(categoryColor.copy(alpha = 0.07f)),
            contentAlignment = Alignment.Center
        ) {
            Text(text = emoji, fontSize = 18.sp)
        }

        // Center: title + date
        Column(
            modifier = Modifier
                .weight(1f)
                .padding(start = 14.dp)
        ) {
            Text(
                text = memorial.title,
                fontSize = 16.sp,
                fontWeight = FontWeight.Medium,
                color = OnBackground
            )
            Text(
                text = "$dateStr$noteStr",
                fontSize = 13.sp,
                color = OnSurfaceVariant,
                modifier = Modifier.padding(top = 3.dp)
            )
        }

        // Right: number + label
        Column(
            horizontalAlignment = Alignment.End,
            modifier = Modifier.padding(start = 12.dp)
        ) {
            Text(
                text = "$absDays",
                fontSize = 28.sp,
                fontWeight = FontWeight.Bold,
                color = categoryColor,
                lineHeight = 30.sp
            )
            Text(
                text = if (isPast) "\u5DF2\u8FC7\u5929" else "\u5269\u4F59\u5929",
                fontSize = 11.sp,
                color = OnSurfaceVariant,
                fontWeight = FontWeight.Medium
            )
        }
    }
}
