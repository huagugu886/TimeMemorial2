package com.huagugu.timememorial2.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
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
import com.huagugu.timememorial2.ui.theme.CategoryFestival
import com.huagugu.timememorial2.ui.theme.CategoryLife
import com.huagugu.timememorial2.ui.theme.CategoryLove
import com.huagugu.timememorial2.ui.theme.CategoryStudy
import com.huagugu.timememorial2.ui.theme.CategoryWork
import com.huagugu.timememorial2.ui.theme.OnBackground
import com.huagugu.timememorial2.ui.theme.OnSurfaceVariant
import com.huagugu.timememorial2.ui.theme.SurfaceContainer
import top.yukonga.miuix.kmp.basic.Text

@Composable
fun CategoryChip(
    label: String,
    selected: Boolean,
    activeColor: Color = OnBackground,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .clip(RoundedCornerShape(20.dp))
            .background(if (selected) activeColor else SurfaceContainer)
            .clickable { onClick() }
            .padding(horizontal = 18.dp, vertical = 8.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = label,
            color = if (selected) Color.White else OnSurfaceVariant,
            fontSize = 14.sp,
            fontWeight = FontWeight.Medium
        )
    }
}

@Composable
fun CategoryChipFromEnum(
    category: Category,
    selected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val color = when (category) {
        Category.LOVE -> CategoryLove
        Category.WORK -> CategoryWork
        Category.LIFE -> CategoryLife
        Category.STUDY -> CategoryStudy
        Category.FESTIVAL -> CategoryFestival
    }
    CategoryChip(
        label = category.label,
        selected = selected,
        activeColor = color,
        onClick = onClick,
        modifier = modifier
    )
}
