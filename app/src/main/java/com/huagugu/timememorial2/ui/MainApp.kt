package com.huagugu.timememorial2.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Settings
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.huagugu.timememorial2.ui.screens.AddMemorialSheet
import com.huagugu.timememorial2.ui.screens.CalendarScreen
import com.huagugu.timememorial2.ui.screens.HomeScreen
import com.huagugu.timememorial2.ui.screens.SettingsScreen
import com.huagugu.timememorial2.ui.theme.FabBg
import com.huagugu.timememorial2.ui.theme.FabOn
import com.huagugu.timememorial2.ui.theme.OnBackground
import com.huagugu.timememorial2.ui.theme.OnSurfaceVariant
import com.huagugu.timememorial2.viewmodel.MemorialViewModel
import top.yukonga.miuix.kmp.basic.Icon
import top.yukonga.miuix.kmp.basic.Text

@Composable
fun MainApp(viewModel: MemorialViewModel = viewModel()) {
    var currentTab by remember { mutableIntStateOf(0) }
    var showAddSheet by remember { mutableStateOf(false) }

    Box(modifier = Modifier.fillMaxSize().background(Color(0xFFF7F7F7))) {
        when (currentTab) {
            0 -> HomeScreen(viewModel = viewModel, onAddClick = { showAddSheet = true })
            1 -> CalendarScreen(viewModel = viewModel)
            2 -> SettingsScreen()
        }

        // FAB - bottom right
        Box(
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(end = 24.dp, bottom = 100.dp)
                .size(52.dp)
                .clip(RoundedCornerShape(16.dp))
                .background(FabBg)
                .clickable { showAddSheet = true },
            contentAlignment = Alignment.Center
        ) {
            Text("+", fontSize = 26.sp, fontWeight = FontWeight.Bold, color = FabOn)
        }

        // Floating bottom navigation bar
        FloatingNavBar(
            modifier = Modifier.align(Alignment.BottomCenter),
            currentTab = currentTab,
            onTabClick = { currentTab = it }
        )
    }

    if (showAddSheet) {
        AddMemorialSheet(
            viewModel = viewModel,
            onDismiss = { showAddSheet = false }
        )
    }
}

@Composable
private fun FloatingNavBar(
    modifier: Modifier = Modifier,
    currentTab: Int,
    onTabClick: (Int) -> Unit
) {
    val items = listOf(
        NavItem(Icons.Default.Home, 0),
        NavItem(Icons.Default.DateRange, 1),
        NavItem(Icons.Default.Settings, 2)
    )

    Row(
        modifier = modifier
            .navigationBarsPadding()
            .padding(bottom = 16.dp)
            .shadow(
                elevation = 12.dp,
                shape = RoundedCornerShape(28.dp),
                ambientColor = Color.Black.copy(alpha = 0.06f),
                spotColor = Color.Black.copy(alpha = 0.08f)
            )
            .clip(RoundedCornerShape(28.dp))
            .background(Color(0xCCF5F5F7))
            .border(
                width = 0.5.dp,
                color = Color.White.copy(alpha = 0.35f),
                shape = RoundedCornerShape(28.dp)
            )
            .padding(horizontal = 24.dp, vertical = 8.dp),
        horizontalArrangement = Arrangement.spacedBy(20.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        items.forEach { item ->
            val isSelected = currentTab == item.index
            Box(
                modifier = Modifier
                    .size(40.dp)
                    .clip(RoundedCornerShape(12.dp))
                    .clickable(
                        indication = null,
                        interactionSource = remember { MutableInteractionSource() }
                    ) { onTabClick(item.index) },
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = item.icon,
                    contentDescription = null,
                    modifier = Modifier.size(24.dp),
                    tint = if (isSelected) OnBackground else OnSurfaceVariant.copy(alpha = 0.4f)
                )
            }
        }
    }
}

private data class NavItem(val icon: ImageVector, val index: Int)
