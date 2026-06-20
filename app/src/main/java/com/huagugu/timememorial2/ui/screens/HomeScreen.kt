package com.huagugu.timememorial2.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
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
import com.huagugu.timememorial2.ui.components.CategoryChip
import com.huagugu.timememorial2.ui.components.CategoryChipFromEnum
import com.huagugu.timememorial2.ui.components.MemorialCard
import com.huagugu.timememorial2.ui.theme.OnBackground
import com.huagugu.timememorial2.ui.theme.OnSurfaceVariant
import com.huagugu.timememorial2.ui.theme.SurfaceContainer
import com.huagugu.timememorial2.viewmodel.MemorialViewModel
import top.yukonga.miuix.kmp.basic.Text

@Composable
fun HomeScreen(viewModel: MemorialViewModel, onAddClick: () -> Unit) {
    val memorials by viewModel.filteredMemorials.collectAsState()
    val selectedCategory by viewModel.selectedCategory.collectAsState()
    val totalCount = memorials.size

    val now = System.currentTimeMillis()
    val upcomingCount = memorials.count { it.date > now }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(top = 12.dp)
    ) {
        // Header
        Text(
            text = "纪念日",
            fontSize = 34.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(start = 22.dp, bottom = 16.dp)
        )

        // Stats row - exactly 2 cards like preview
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 22.dp)
                .padding(bottom = 16.dp),
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            StatCard("全部纪念日", "$totalCount", Modifier.weight(1f))
            StatCard("即将到来", "$upcomingCount", Modifier.weight(1f))
        }

        // Category chips - "全部" first, then categories
        LazyRow(
            modifier = Modifier.padding(bottom = 16.dp),
            contentPadding = PaddingValues(horizontal = 22.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            item {
                CategoryChip(
                    label = "全部",
                    selected = selectedCategory == null,
                    activeColor = OnBackground,
                    onClick = { viewModel.setCategory(null) }
                )
            }
            items(Category.entries.toList()) { cat ->
                CategoryChipFromEnum(
                    category = cat,
                    selected = selectedCategory == cat.name,
                    onClick = { viewModel.setCategory(cat.name) }
                )
            }
        }

        // Memorial list inside a card container
        LazyColumn(
            modifier = Modifier
                .padding(horizontal = 22.dp)
                .clip(RoundedCornerShape(20.dp))
                .background(SurfaceContainer),
            contentPadding = PaddingValues(bottom = 120.dp)
        ) {
            items(memorials, key = { it.id }) { memorial ->
                MemorialCard(
                    memorial = memorial,
                    onClick = { /* TODO: edit/delete */ }
                )
                // Divider between items (not after last)
                if (memorial != memorials.last()) {
                    androidx.compose.foundation.layout.Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(0.5.dp)
                            .padding(horizontal = 18.dp)
                            .background(com.huagugu.timememorial2.ui.theme.DividerLine)
                    )
                }
            }
        }
    }
}

@Composable
fun StatCard(label: String, value: String, modifier: Modifier = Modifier) {
    Column(
        modifier = modifier
            .clip(RoundedCornerShape(20.dp))
            .background(SurfaceContainer)
            .padding(16.dp)
    ) {
        Text(text = label, fontSize = 12.sp, color = OnSurfaceVariant, fontWeight = FontWeight.Medium)
        Text(text = value, fontSize = 26.sp, fontWeight = FontWeight.Bold, modifier = Modifier.padding(top = 4.dp))
    }
}
