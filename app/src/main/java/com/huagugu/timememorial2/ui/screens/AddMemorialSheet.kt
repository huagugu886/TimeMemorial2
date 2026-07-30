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
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.huagugu.timememorial2.data.Category
import com.huagugu.timememorial2.data.Memorial
import com.huagugu.timememorial2.ui.theme.Background
import com.huagugu.timememorial2.ui.theme.CategoryFestival
import com.huagugu.timememorial2.ui.theme.CategoryLife
import com.huagugu.timememorial2.ui.theme.CategoryLove
import com.huagugu.timememorial2.ui.theme.CategoryStudy
import com.huagugu.timememorial2.ui.theme.CategoryWork
import com.huagugu.timememorial2.ui.theme.Disabled
import com.huagugu.timememorial2.ui.theme.OnBackground
import com.huagugu.timememorial2.ui.theme.OnSurfaceVariant
import com.huagugu.timememorial2.ui.theme.SecondaryVariant
import com.huagugu.timememorial2.ui.theme.SurfaceContainer
import top.yukonga.miuix.kmp.basic.Text
import top.yukonga.miuix.kmp.extra.SuperDialog
import java.util.Calendar

private val categoryColors = mapOf(
    Category.LOVE to CategoryLove,
    Category.WORK to CategoryWork,
    Category.LIFE to CategoryLife,
    Category.STUDY to CategoryStudy,
    Category.FESTIVAL to CategoryFestival
)

/**
 * 获取指定年月的最大天数
 */
private fun getMaxDay(year: Int, month: Int): Int {
    val cal = Calendar.getInstance()
    cal.set(year, month - 1, 1)
    return cal.getActualMaximum(Calendar.DAY_OF_MONTH)
}

@Composable
fun AddMemorialSheet(
    onSave: (title: String, date: Long, category: String, note: String) -> Unit,
    editMemorial: Memorial? = null,
    onDismiss: () -> Unit
) {
    val isEdit = editMemorial != null
    var title by remember(editMemorial) { mutableStateOf(editMemorial?.title ?: "") }
    var note by remember(editMemorial) { mutableStateOf(editMemorial?.note ?: "") }

    // 解析初始日期
    val initCal = remember(editMemorial) {
        Calendar.getInstance().apply {
            editMemorial?.let { timeInMillis = it.date }
        }
    }
    var year by remember(editMemorial) { mutableIntStateOf(initCal.get(Calendar.YEAR)) }
    var month by remember(editMemorial) { mutableIntStateOf(initCal.get(Calendar.MONTH) + 1) }
    var day by remember(editMemorial) { mutableIntStateOf(initCal.get(Calendar.DAY_OF_MONTH)) }
    val initialCategory = remember(editMemorial) {
        Category.entries.find { it.name == editMemorial?.category } ?: Category.LIFE
    }
    var selectedCategory by remember(editMemorial) { mutableStateOf(initialCategory) }
    var selectedTab by remember(editMemorial) { mutableIntStateOf(Category.entries.indexOf(initialCategory)) }
    var showEmptyDialog by remember { mutableStateOf(false) }

    // 根据当前年月计算合法天数范围
    val maxDay = remember(year, month) { getMaxDay(year, month) }
    // 如果当前 day 超过 maxDay，自动修正
    LaunchedEffect(maxDay) {
        if (day > maxDay) day = maxDay
    }

    // 年/月变化时，如果 day 超出范围则自动修正
    fun clampDay() {
        val max = getMaxDay(year, month)
        if (day > max) day = max
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Background)
            .navigationBarsPadding()
            .padding(horizontal = 22.dp, vertical = 24.dp)
    ) {
        // Title
        Text(
            text = if (isEdit) "编辑纪念日" else "新建纪念日",
            fontSize = 34.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(bottom = 24.dp)
        )

        // Date picker
        Text("选择日期", fontSize = 13.sp, color = OnSurfaceVariant)
        Spacer(modifier = Modifier.height(6.dp))
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(14.dp))
                .background(SurfaceContainer)
                .padding(horizontal = 16.dp, vertical = 12.dp),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Year
            Text(
                text = "◀",
                color = OnSurfaceVariant,
                modifier = Modifier.clickable {
                    year--
                    clampDay()
                }.padding(horizontal = 4.dp)
            )
            Text(
                text = "$year 年",
                fontSize = 17.sp,
                fontWeight = FontWeight.Medium,
                modifier = Modifier.padding(horizontal = 4.dp)
            )
            Text(
                text = "▶",
                color = OnSurfaceVariant,
                modifier = Modifier.clickable {
                    year++
                    clampDay()
                }.padding(horizontal = 4.dp)
            )

            Spacer(modifier = Modifier.width(12.dp))

            // Month
            Text(
                text = "◀",
                color = OnSurfaceVariant,
                modifier = Modifier.clickable {
                    if (month == 1) { month = 12; year-- } else month--
                    clampDay()
                }.padding(horizontal = 4.dp)
            )
            Text(
                text = String.format("%02d 月", month),
                fontSize = 17.sp,
                fontWeight = FontWeight.Medium,
                modifier = Modifier.padding(horizontal = 4.dp)
            )
            Text(
                text = "▶",
                color = OnSurfaceVariant,
                modifier = Modifier.clickable {
                    if (month == 12) { month = 1; year++ } else month++
                    clampDay()
                }.padding(horizontal = 4.dp)
            )

            Spacer(modifier = Modifier.width(12.dp))

            // Day
            Text(
                text = "◀",
                color = OnSurfaceVariant,
                modifier = Modifier.clickable {
                    if (day > 1) day--
                }.padding(horizontal = 4.dp)
            )
            Text(
                text = String.format("%02d 日", day),
                fontSize = 17.sp,
                fontWeight = FontWeight.Medium,
                modifier = Modifier.padding(horizontal = 4.dp)
            )
            Text(
                text = "▶",
                color = OnSurfaceVariant,
                modifier = Modifier.clickable {
                    if (day < maxDay) day++
                }.padding(horizontal = 4.dp)
            )
        }

        Spacer(modifier = Modifier.height(20.dp))

        // Title input
        Text("标题", fontSize = 13.sp, color = OnSurfaceVariant)
        Spacer(modifier = Modifier.height(6.dp))
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(14.dp))
                .background(SurfaceContainer)
                .padding(horizontal = 16.dp, vertical = 14.dp)
        ) {
            if (title.isEmpty()) {
                Text("例如：结婚纪念日", color = Disabled, fontSize = 15.sp)
            }
            androidx.compose.foundation.text.BasicTextField(
                value = title,
                onValueChange = { title = it },
                textStyle = androidx.compose.ui.text.TextStyle(
                    fontSize = 15.sp,
                    color = OnBackground
                ),
                modifier = Modifier.fillMaxWidth(),
                singleLine = true
            )
        }

        Spacer(modifier = Modifier.height(20.dp))

        // Category
        Text("分类", fontSize = 13.sp, color = OnSurfaceVariant)
        Spacer(modifier = Modifier.height(6.dp))
        androidx.compose.material3.ScrollableTabRow(
            selectedTabIndex = selectedTab,
            containerColor = SurfaceContainer,
            contentColor = OnBackground,
            edgePadding = 0.dp,
            divider = {},
            indicator = {},
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(14.dp))
        ) {
            Category.entries.forEachIndexed { index, cat ->
                val color = categoryColors[cat] ?: OnSurfaceVariant
                val isSelected = selectedTab == index
                Box(
                    modifier = Modifier
                        .padding(horizontal = 4.dp, vertical = 8.dp)
                        .clip(RoundedCornerShape(12.dp))
                        .background(if (isSelected) color.copy(alpha = 0.12f) else OnSurfaceVariant.copy(alpha = 0.06f))
                        .clickable {
                            selectedTab = index
                            selectedCategory = cat
                        }
                        .padding(horizontal = 16.dp, vertical = 8.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = cat.label,
                        fontSize = 14.sp,
                        fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                        color = if (isSelected) color else OnSurfaceVariant
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(20.dp))

        // Note input
        Text("备注", fontSize = 13.sp, color = OnSurfaceVariant)
        Spacer(modifier = Modifier.height(6.dp))
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(80.dp)
                .clip(RoundedCornerShape(14.dp))
                .background(SurfaceContainer)
                .padding(horizontal = 16.dp, vertical = 14.dp)
        ) {
            if (note.isEmpty()) {
                Text("可选备注信息", color = Disabled, fontSize = 15.sp)
            }
            androidx.compose.foundation.text.BasicTextField(
                value = note,
                onValueChange = { note = it },
                textStyle = androidx.compose.ui.text.TextStyle(
                    fontSize = 15.sp,
                    color = OnBackground
                ),
                modifier = Modifier.fillMaxWidth()
            )
        }

        Spacer(modifier = Modifier.weight(1f))

        // Save
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp)
                .clip(RoundedCornerShape(14.dp))
                .background(SecondaryVariant)
                .clickable {
                    if (title.isBlank()) {
                        showEmptyDialog = true
                    } else {
                        val cal = Calendar.getInstance()
                        cal.set(year, month - 1, day, 0, 0, 0)
                        cal.set(Calendar.MILLISECOND, 0)
                        onSave(title.trim(), cal.timeInMillis, selectedCategory.name, note.trim())
                    }
                },
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = if (isEdit) "保存修改" else "保存纪念日",
                color = androidx.compose.ui.graphics.Color.White,
                fontWeight = FontWeight.Bold,
                fontSize = 16.sp
            )
        }
    }

    if (showEmptyDialog) {
        SuperDialog(
            title = "请输入标题",
            summary = "纪念日标题不能为空哦~",
            show = showEmptyDialog,
            modify = { showEmptyDialog = false }
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(46.dp)
                    .clip(RoundedCornerShape(12.dp))
                    .background(SecondaryVariant)
                    .clickable { showEmptyDialog = false },
                contentAlignment = Alignment.Center
            ) {
                Text("确定", color = androidx.compose.ui.graphics.Color.White, fontWeight = FontWeight.Bold)
            }
        }
    }
}
