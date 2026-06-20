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
import com.huagugu.timememorial2.viewmodel.MemorialViewModel
import top.yukonga.miuix.kmp.basic.Button
import top.yukonga.miuix.kmp.basic.NumberPicker
import top.yukonga.miuix.kmp.basic.Text
import top.yukonga.miuix.kmp.basic.TextField
import top.yukonga.miuix.kmp.theme.MiuixTheme
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
fun AddMemorialSheet(
    viewModel: MemorialViewModel,
    onDismiss: () -> Unit
) {
    var title by remember { mutableStateOf("") }
    var category by remember { mutableStateOf(Category.LOVE) }

    val cal = remember { Calendar.getInstance() }
    var year by remember { mutableIntStateOf(cal.get(Calendar.YEAR)) }
    var month by remember { mutableIntStateOf(cal.get(Calendar.MONTH) + 1) }
    var day by remember { mutableIntStateOf(cal.get(Calendar.DAY_OF_MONTH)) }

    // Dim background + bottom sheet
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0x66000000))
            .clickable(
                indication = null,
                interactionSource = remember { androidx.compose.foundation.interaction.MutableInteractionSource() }
            ) { onDismiss() },
        contentAlignment = Alignment.BottomCenter
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(topStart = 28.dp, topEnd = 28.dp))
                .background(Background)
                .clickable(
                    indication = null,
                    interactionSource = remember { androidx.compose.foundation.interaction.MutableInteractionSource() }
                ) { /* consume click */ }
                .navigationBarsPadding()
                .padding(horizontal = 28.dp)
                .padding(top = 24.dp, bottom = 16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Handle bar
            Box(
                modifier = Modifier
                    .width(36.dp)
                    .height(4.dp)
                    .clip(RoundedCornerShape(2.dp))
                    .background(OnSurfaceVariant.copy(alpha = 0.3f))
            )

            Text(
                text = "添加纪念日",
                fontSize = 20.sp,
                fontWeight = FontWeight.SemiBold,
                modifier = Modifier.padding(top = 20.dp, bottom = 20.dp)
            )

            // Title input
            TextField(
                value = title,
                onValueChange = { title = it },
                modifier = Modifier.fillMaxWidth(),
                label = "纪念日名称",
                singleLine = true
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Category selector
            Text(
                text = "分类",
                fontSize = 14.sp,
                fontWeight = FontWeight.Medium,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 8.dp),
                textAlign = TextAlign.Start,
                color = OnSurfaceVariant
            )

            Row(
                horizontalArrangement = Arrangement.spacedBy(10.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Category.entries.forEach { cat ->
                    val isSelected = category == cat
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(24.dp))
                            .background(
                                if (isSelected) categoryColor(cat.name)
                                else SurfaceContainer
                            )
                            .clickable { category = cat }
                            .padding(horizontal = 16.dp, vertical = 8.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = when (cat) {
                                Category.LOVE -> "爱情"
                                Category.WORK -> "工作"
                                Category.LIFE -> "生活"
                                Category.STUDY -> "学习"
                                Category.FESTIVAL -> "节日"
                            },
                            fontSize = 14.sp,
                            color = if (isSelected) Color.White else OnBackground,
                            fontWeight = if (isSelected) FontWeight.Medium else FontWeight.Normal
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            // Date picker - MIUIX NumberPicker × 3
            Text(
                text = "选择日期",
                fontSize = 14.sp,
                fontWeight = FontWeight.Medium,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 12.dp),
                textAlign = TextAlign.Start,
                color = OnSurfaceVariant
            )

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Year
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    NumberPicker(
                        value = year,
                        onValueChange = { year = it },
                        range = 1900..2100,
                        label = { "${it}年" },
                        visibleItemCount = 5
                    )
                }

                Spacer(modifier = Modifier.width(12.dp))

                // Month
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    NumberPicker(
                        value = month,
                        onValueChange = { month = it },
                        range = 1..12,
                        label = { "${it}月" },
                        visibleItemCount = 5
                    )
                }

                Spacer(modifier = Modifier.width(12.dp))

                // Day
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    NumberPicker(
                        value = day,
                        onValueChange = { day = it },
                        range = 1..31,
                        label = { "${it}日" },
                        visibleItemCount = 5
                    )
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Save button
            Button(
                onClick = {
                    val selectedCal = Calendar.getInstance().apply {
                        set(year, month - 1, day, 0, 0, 0)
                        set(Calendar.MILLISECOND, 0)
                    }
                    viewModel.addMemorial(
                        Memorial(
                            title = title.ifBlank { "未命名纪念日" },
                            date = selectedCal.timeInMillis,
                            category = category.name
                        )
                    )
                    onDismiss()
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = "保存",
                    fontSize = 16.sp,
                    modifier = Modifier.padding(vertical = 4.dp)
                )
            }
        }
    }
}
