package com.huagugu.timememorial2.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.huagugu.timememorial2.data.Category
import com.huagugu.timememorial2.ui.components.CategoryChipFromEnum
import com.huagugu.timememorial2.ui.components.MemorialCard
import com.huagugu.timememorial2.ui.theme.OnBackground
import com.huagugu.timememorial2.ui.theme.OnSurfaceVariant
import com.huagugu.timememorial2.ui.theme.SurfaceContainer
import com.huagugu.timememorial2.viewmodel.MemorialViewModel
import top.yukonga.miuix.kmp.basic.Text

@Composable
fun HomeScreen(
    viewModel: MemorialViewModel,
    onAddClick: () -> Unit,
    onEditMemorial: (com.huagugu.timememorial2.data.Memorial) -> Unit,
    onDeleteMemorial: (com.huagugu.timememorial2.data.Memorial) -> Unit
) {
    val memorials by viewModel.filteredMemorials.collectAsState()
    val selectedCategory by viewModel.selectedCategory.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .windowInsetsPadding(WindowInsets.statusBars)
            .padding(top = 12.dp)
    ) {
        Text(
            text = "纪念日",
            fontSize = 34.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(start = 22.dp, bottom = 24.dp)
        )

        // Filter chips
        LazyRow(
            contentPadding = PaddingValues(horizontal = 16.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            item {
                CategoryChipFromEnum(
                    label = "全部",
                    selected = selectedCategory == null,
                    color = OnBackground,
                    onClick = { viewModel.setCategory(null) }
                )
            }
            items(Category.entries.toList()) { cat ->
                CategoryChipFromEnum(
                    label = cat.label,
                    selected = selectedCategory == cat.name,
                    color = com.huagugu.timememorial2.ui.theme.CategoryFestival,
                    onClick = { viewModel.setCategory(cat.name) }
                )
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

        if (memorials.isEmpty()) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(32.dp)
            ) {
                Text(
                    text = "还没有纪念日，点击 + 添加",
                    color = OnSurfaceVariant,
                    fontSize = 14.sp
                )
            }
        } else {
            LazyColumn(
                contentPadding = PaddingValues(horizontal = 16.dp, vertical = 4.dp),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                items(
                    items = memorials,
                    key = { it.id }
                ) { memorial ->
                    MemorialCard(
                        memorial = memorial,
                        onClick = {},
                        onEdit = { onEditMemorial(memorial) },
                        onDelete = { onDeleteMemorial(memorial) },
                        modifier = Modifier.animateItem()
                    )
                }
            }
        }
    }
}
