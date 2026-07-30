package com.huagugu.timememorial2.ui.components

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
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
    "LOVE" to "❤️",
    "WORK" to "💼",
    "LIFE" to "🎂",
    "STUDY" to "📚",
    "FESTIVAL" to "🧧"
)

private val categoryColors = mapOf(
    "LOVE" to CategoryLove,
    "WORK" to CategoryWork,
    "LIFE" to CategoryLife,
    "STUDY" to CategoryStudy,
    "FESTIVAL" to CategoryFestival
)

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun MemorialCard(
    memorial: Memorial,
    onClick: () -> Unit,
    onEdit: () -> Unit,
    onDelete: () -> Unit,
    modifier: Modifier = Modifier
) {
    val now = System.currentTimeMillis()
    val diff = memorial.date - now
    val daysLeft = TimeUnit.MILLISECONDS.toDays(diff)
    val sdf = remember { SimpleDateFormat("yyyy/MM/dd", Locale.getDefault()) }
    val emoji = categoryEmoji[memorial.category] ?: "📅"
    val accentColor = categoryColors[memorial.category] ?: OnSurfaceVariant

    var showMenu by remember { mutableStateOf(false) }

    Box(modifier = modifier.fillMaxWidth()) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(14.dp))
                .background(accentColor.copy(alpha = 0.06f))
                .combinedClickable(
                    onClick = onClick,
                    onLongClick = { showMenu = true }
                )
                .padding(horizontal = 16.dp, vertical = 14.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Left: emoji circle
            Box(
                modifier = Modifier
                    .size(48.dp)
                    .clip(RoundedCornerShape(12.dp))
                    .background(accentColor.copy(alpha = 0.12f)),
                contentAlignment = Alignment.Center
            ) {
                Text(text = emoji, fontSize = 22.sp)
            }

            // Middle: title + date
            Column(
                modifier = Modifier
                    .weight(1f)
                    .padding(horizontal = 12.dp),
                verticalArrangement = Arrangement.Center
            ) {
                Text(
                    text = memorial.title,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Medium,
                    color = OnBackground
                )
                Text(
                    text = sdf.format(Date(memorial.date)),
                    fontSize = 12.sp,
                    color = OnSurfaceVariant,
                    modifier = Modifier.padding(top = 2.dp)
                )
                if (memorial.note.isNotBlank()) {
                    Text(
                        text = memorial.note,
                        fontSize = 11.sp,
                        color = OnSurfaceVariant,
                        maxLines = 1,
                        modifier = Modifier.padding(top = 1.dp)
                    )
                }
            }

            // Right: countdown
            Text(
                text = if (daysLeft > 0) "${daysLeft}天" else if (daysLeft == 0L) "今天" else "已过${-daysLeft}天",
                fontSize = 14.sp,
                fontWeight = FontWeight.SemiBold,
                color = accentColor,
                textAlign = TextAlign.End,
                modifier = Modifier
                    .padding(start = 8.dp)
                    .weight(0.6f, fill = false)
            )
        }

        // Long press context menu
        DropdownMenu(
            expanded = showMenu,
            onDismissRequest = { showMenu = false }
        ) {
            DropdownMenuItem(
                text = { Text("编辑") },
                leadingIcon = { Icon(Icons.Default.Edit, contentDescription = null) },
                onClick = {
                    showMenu = false
                    onEdit()
                }
            )
            DropdownMenuItem(
                text = { Text("删除") },
                leadingIcon = {
                    Icon(
                        Icons.Default.Delete,
                        contentDescription = null,
                        tint = androidx.compose.material3.MaterialTheme.colorScheme.error
                    )
                },
                onClick = {
                    showMenu = false
                    onDelete()
                }
            )
        }
    }
}
