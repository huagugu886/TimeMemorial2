package com.huagugu.timememorial2.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.huagugu.timememorial2.data.Category
import com.huagugu.timememorial2.ui.theme.CategoryFestival
import com.huagugu.timememorial2.ui.theme.CategoryLife
import com.huagugu.timememorial2.ui.theme.CategoryLove
import com.huagugu.timememorial2.ui.theme.CategoryStudy
import com.huagugu.timememorial2.ui.theme.CategoryWork
import com.huagugu.timememorial2.ui.theme.OnBackground
import com.huagugu.timememorial2.ui.theme.OnSurfaceVariant
import com.huagugu.timememorial2.ui.theme.SecondaryVariant
import com.huagugu.timememorial2.viewmodel.MemorialViewModel
import top.yukonga.miuix.kmp.basic.Text
import java.util.Calendar

private fun categoryColor(name: String): Color = when (name) {
    "LOVE" -> CategoryLove
    "WORK" -> CategoryWork
    "LIFE" -> CategoryLife
    "STUDY" -> CategoryStudy
    "FESTIVAL" -> CategoryFestival
    else -> Color.Gray
}

@Composable
fun CalendarScreen(viewModel: MemorialViewModel) {
    val memorials by viewModel.allMemorials.collectAsState()
    val now = Calendar.getInstance()
    var year by remember { mutableIntStateOf(now.get(Calendar.YEAR)) }
    var month by remember { mutableIntStateOf(now.get(Calendar.MONTH)) }

    val daysInMonth = remember(year, month) {
        Calendar.getInstance().apply { set(year, month, 1) }
            .getActualMaximum(Calendar.DAY_OF_MONTH)
    }
    val firstDayOfWeek = remember(year, month) {
        Calendar.getInstance().apply { set(year, month, 1) }
            .get(Calendar.DAY_OF_WEEK) - 1
    }
    val today = remember {
        Pair(now.get(Calendar.YEAR), now.get(Calendar.MONTH))
    }

    // Which days in this month have memorials → map<dayOfMonth, Memorial>
    val memorialDays = remember(memorials, year, month) {
        val cal = Calendar.getInstance()
        memorials.filter {
            cal.timeInMillis = it.date
            cal.get(Calendar.YEAR) == year && cal.get(Calendar.MONTH) == month
        }.groupBy {
            cal.timeInMillis = it.date
            cal.get(Calendar.DAY_OF_MONTH)
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(top = 12.dp)
    ) {
        // Header
        Text(
            text = "日历",
            fontSize = 34.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(start = 22.dp, bottom = 16.dp)
        )

        // Month navigation
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 22.dp)
                .padding(bottom = 20.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(36.dp)
                    .clip(RoundedCornerShape(12.dp))
                    .background(SurfaceContainer)
                    .clickable {
                        if (month == 0) { month = 11; year-- } else month--
                    },
                contentAlignment = Alignment.Center
            ) {
                Text("\u2039", fontSize = 22.sp, fontWeight = FontWeight.Bold, color = OnSurfaceVariant)
            }
            Text(
                text = "${year}年${month + 1}月",
                fontSize = 17.sp,
                fontWeight = FontWeight.SemiBold
            )
            Box(
                modifier = Modifier
                    .size(36.dp)
                    .clip(RoundedCornerShape(12.dp))
                    .background(SurfaceContainer)
                    .clickable {
                        if (month == 11) { month = 0; year++ } else month++
                    },
                contentAlignment = Alignment.Center
            ) {
                Text("\u203A", fontSize = 22.sp, fontWeight = FontWeight.Bold, color = OnSurfaceVariant)
            }
        }

        // Week headers
        Row(modifier = Modifier.padding(horizontal = 22.dp)) {
            listOf("日", "一", "二", "三", "四", "五", "六").forEach { day ->
                Text(
                    text = day,
                    modifier = Modifier.weight(1f),
                    textAlign = TextAlign.Center,
                    fontSize = 13.sp,
                    color = OnSurfaceVariant,
                    fontWeight = FontWeight.Medium
                )
            }
        }

        Spacer(modifier = Modifier.height(4.dp))

        // Calendar grid
        val totalCells = firstDayOfWeek + daysInMonth
        val rows = (totalCells + 6) / 7

        Column(modifier = Modifier.padding(horizontal = 22.dp)) {
            for (row in 0 until rows) {
                Row(modifier = Modifier.fillMaxWidth()) {
                    for (col in 0..6) {
                        val dayIndex = row * 7 + col
                        val day = dayIndex - firstDayOfWeek + 1

                        if (day in 1..daysInMonth) {
                            val isToday = year == today.first && month == today.second && day == now.get(Calendar.DAY_OF_MONTH)
                            val hasMemorial = memorialDays.containsKey(day)
                            val mColor = if (hasMemorial) categoryColor(memorialDays[day]!!.first().category) else Color.Transparent

                            Box(
                                modifier = Modifier
                                    .weight(1f)
                                    .padding(2.dp)
                                    .height(40.dp)
                                    .clip(RoundedCornerShape(12.dp))
                                    .background(
                                        when {
                                            isToday -> OnBackground
                                            hasMemorial -> mColor.copy(alpha = 0.07f)
                                            else -> Color.Transparent
                                        }
                                    ),
                                contentAlignment = Alignment.Center
                            ) {
                                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                    Text(
                                        text = "$day",
                                        fontSize = 15.sp,
                                        color = if (isToday) Color.White else OnBackground,
                                        fontWeight = if (isToday) FontWeight.SemiBold else FontWeight.Normal
                                    )
                                    if (hasMemorial && !isToday) {
                                        Box(
                                            modifier = Modifier
                                                .size(4.dp)
                                                .clip(CircleShape)
                                                .background(mColor)
                                        )
                                    }
                                }
                            }
                        } else {
                            Spacer(
                                modifier = Modifier
                                    .weight(1f)
                                    .height(40.dp)
                            )
                        }
                    }
                }
            }
        }

        // Monthly memorials section
        Spacer(modifier = Modifier.height(24.dp))
        Text(
            text = "本月纪念日",
            fontSize = 15.sp,
            fontWeight = FontWeight.SemiBold,
            color = SecondaryVariant,
            modifier = Modifier.padding(start = 22.dp, bottom = 12.dp)
        )

        val monthMems = memorialDays.values.flatten()
        if (monthMems.isEmpty()) {
            Text(
                text = "本月暂无纪念日",
                fontSize = 14.sp,
                color = OnSurfaceVariant,
                modifier = Modifier.padding(start = 22.dp)
            )
        } else {
            monthMems.forEach { m ->
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 22.dp)
                        .padding(vertical = 14.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .size(8.dp)
                            .clip(CircleShape)
                            .background(categoryColor(m.category))
                    )
                    Spacer(modifier = Modifier.width(10.dp))
                    Text(text = m.title, fontSize = 15.sp, modifier = Modifier.weight(1f))
                    val cal2 = Calendar.getInstance().apply { timeInMillis = m.date }
                    val mDay = cal2.get(Calendar.DAY_OF_MONTH)
                    Text(
                        text = "${cal2.get(Calendar.MONTH) + 1}月${mDay}日",
                        fontSize = 13.sp,
                        color = OnSurfaceVariant
                    )
                }
            }
        }
    }
}

// Re-import needed for SurfaceContainer
private val SurfaceContainer = com.huagugu.timememorial2.ui.theme.SurfaceContainer
