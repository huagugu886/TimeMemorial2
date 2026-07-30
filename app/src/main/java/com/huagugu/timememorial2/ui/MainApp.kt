package com.huagugu.timememorial2.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Settings
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.huagugu.timememorial2.data.Memorial
import com.huagugu.timememorial2.ui.screens.AddMemorialSheet
import com.huagugu.timememorial2.ui.screens.CalendarScreen
import com.huagugu.timememorial2.ui.screens.HomeScreen
import com.huagugu.timememorial2.ui.screens.SettingsScreen
import com.huagugu.timememorial2.ui.theme.FabBg
import com.huagugu.timememorial2.ui.theme.FabOn
import com.huagugu.timememorial2.ui.theme.OnBackground
import com.huagugu.timememorial2.ui.theme.OnSurfaceVariant
import com.huagugu.timememorial2.viewmodel.MemorialViewModel
import top.yukonga.miuix.kmp.basic.Scaffold
import top.yukonga.miuix.kmp.basic.Surface
import top.yukonga.miuix.kmp.extra.SuperDialog
import top.yukonga.miuix.kmp.theme.MiuixTheme

data class NavItem(val icon: ImageVector, val label: String)

@Composable
fun MainApp(viewModel: MemorialViewModel = viewModel()) {
    var selectedTab by remember { mutableIntStateOf(0) }
    var fabExpanded by remember { mutableStateOf(false) }
    val editingMemorial by viewModel.editingMemorial.collectAsState()
    val isEditing = editingMemorial != null
n    // 弹窗控制
    var showDeleteDialog by remember { mutableStateOf(false) }
    var memorialToDelete by remember { mutableStateOf<Memorial?>(null) }

    Scaffold(
        bottomBar = {
            Surface(
                modifier = Modifier
                    .fillMaxSize()
                    .navigationBarsPadding()
                    .padding(horizontal = 16.dp, vertical = 10.dp)
                    .clip(RoundedCornerShape(22.dp)),
                color = MiuixTheme.colors.surface, tonalElevation = 4.dp
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(horizontal = 12.dp, vertical = 8.dp),
                    horizontalArrangement = Arrangement.SpaceAround,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    BottomNavItem(
                        icon = Icons.Default.Home,
                        label = "纪念日",
                        selected = selectedTab == 0,
                        onClick = { selectedTab = 0 }
                    )
                    Box(
                        modifier = Modifier
                            .size(44.dp)
                            .clip(RoundedCornerShape(14.dp))
                            .background(FabBg)
                            .clickable { fabExpanded = !fabExpanded },
                        contentAlignment = Alignment.Center
                    ) {
                        Text("+", fontSize = 24.sp, color = FabOn, fontWeight = FontWeight.Bold)
                    }
                    BottomNavItem(
                        icon = Icons.Default.DateRange,
                        label = "日历",
                        selected = selectedTab == 2,
                        onClick = { selectedTab = 2 }
                    )
                    BottomNavItem(
                        icon = Icons.Default.Settings,
                        label = "设置",
                        selected = selectedTab == 3,
                        onClick = { selectedTab = 3 }
                    )
                }
            }
        },
        floatingActionButton = {
            SuperDialog(
                title = "新建纪念日",
                summary = "选择一个方式添加",
                show = fabExpanded,
                modify = { fabExpanded = false }
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(horizontal = 8.dp, vertical = 8.dp)
                        .clip(RoundedCornerShape(16.dp))
                        .background(FabBg)
                        .clickable {
                            fabExpanded = false
                            selectedTab = 1
                        },
                    contentAlignment = Alignment.Center
                ) { Text("空白纪念日", color = Color.White, fontWeight = FontWeight.Bold) }
            }
        }
    ) {
        when (selectedTab) {
            0 -> HomeScreen(
                viewModel = viewModel,
                onAddClick = { selectedTab = 1 },
                onEditMemorial = { memorial ->
                    viewModel.startEdit(memorial)
                    selectedTab = 1
                },
                onDeleteMemorial = { memorial ->
                    memorialToDelete = memorial
                    showDeleteDialog = true
                }
            )
            1 -> {
                AddMemorialSheet(
                    editMemorial = editingMemorial,
                    onSave = { title, date, category, note ->
                        if (isEditing) {
                            viewModel.updateMemorial(editingMemorial!!, title, date, category, note)
                        } else {
                            viewModel.addMemorial(title, date, category, note)
                        }
                        viewModel.clearEdit()
                        selectedTab = 0
                    },
                    onDismiss = {
                        viewModel.clearEdit()
                        selectedTab = 0
                    }
                )
            }
            2 -> CalendarScreen(viewModel)
            3 -> SettingsScreen()
        }
    }

    // 删除确认弹窗
    if (showDeleteDialog && memorialToDelete != null) {
        SuperDialog(
            title = "删除纪念日",
            summary = "确定要删除「${memorialToDelete!!.title}」吗？\n删除后无法恢复。",
            show = showDeleteDialog,
            modify = {
                showDeleteDialog = false
                memorialToDelete = null
            }
        ) {
            Row(
                modifier = Modifier.fillMaxSize(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .height(46.dp)
                        .clip(RoundedCornerShape(12.dp))
                        .background(OnSurfaceVariant.copy(alpha = 0.12f))
                        .clickable {
                            showDeleteDialog = false
                            memorialToDelete = null
                        },
                    contentAlignment = Alignment.Center
                ) { Text("取消", color = OnBackground, fontWeight = FontWeight.Bold) }
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .height(46.dp)
                        .clip(RoundedCornerShape(12.dp))
                        .background(androidx.compose.ui.graphics.Color(0xFFE53935))
                        .clickable {
                            memorialToDelete?.let { viewModel.deleteMemorial(it) }
                            showDeleteDialog = false
                            memorialToDelete = null
                        },
                    contentAlignment = Alignment.Center
                ) { Text("删除", color = Color.White, fontWeight = FontWeight.Bold) }
            }
        }
    }
}

@Composable
private fun BottomNavItem(
    icon: ImageVector,
    label: String,
    selected: Boolean,
    onClick: () -> Unit
) {
    val color = if (selected) OnBackground else OnSurfaceVariant
    androidx.compose.foundation.layout.Column(
        modifier = Modifier
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = null,
                onClick = onClick
            )
            .padding(horizontal = 16.dp, vertical = 4.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        top.yukonga.miuix.kmp.basic.Icon(
            imageVector = icon,
            tint = color,
            modifier = Modifier.size(24.dp)
        )
        Text(label, fontSize = 11.sp, color = color)
    }
}
