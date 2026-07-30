mport androidx.compose.foundation.layout.height
package com.huagugu.timememorial2.ui
mport androidx.compose.foundation.layout.height

mport androidx.compose.foundation.layout.height
import androidx.compose.foundation.background
mport androidx.compose.foundation.layout.height
import androidx.compose.foundation.clickable
mport androidx.compose.foundation.layout.height
import androidx.compose.foundation.interaction.MutableInteractionSource
mport androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.Arrangement
mport androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.Box
mport androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.Row
mport androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.fillMaxSize
mport androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
mport androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
mport androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.fillMaxWidth
mport androidx.compose.foundation.layout.height
import androidx.compose.foundation.shape.RoundedCornerShape
mport androidx.compose.foundation.layout.height
import androidx.compose.material.icons.Icons
mport androidx.compose.foundation.layout.height
import androidx.compose.material.icons.filled.DateRange
mport androidx.compose.foundation.layout.height
import androidx.compose.material.icons.filled.Home
mport androidx.compose.foundation.layout.height
import androidx.compose.material.icons.filled.Settings
mport androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
mport androidx.compose.foundation.layout.height
import androidx.compose.runtime.collectAsState
mport androidx.compose.foundation.layout.height
import androidx.compose.runtime.getValue
mport androidx.compose.foundation.layout.height
import androidx.compose.runtime.mutableIntStateOf
mport androidx.compose.foundation.layout.height
import androidx.compose.runtime.mutableStateOf
mport androidx.compose.foundation.layout.height
import androidx.compose.runtime.remember
mport androidx.compose.foundation.layout.height
import androidx.compose.runtime.setValue
mport androidx.compose.foundation.layout.height
import androidx.compose.ui.Alignment
mport androidx.compose.foundation.layout.height
import androidx.compose.ui.Modifier
mport androidx.compose.foundation.layout.height
import androidx.compose.ui.draw.clip
mport androidx.compose.foundation.layout.height
import androidx.compose.ui.graphics.Color
mport androidx.compose.foundation.layout.height
import androidx.compose.ui.graphics.vector.ImageVector
mport androidx.compose.foundation.layout.height
import androidx.compose.ui.text.font.FontWeight
mport androidx.compose.foundation.layout.height
import androidx.compose.ui.unit.dp
mport androidx.compose.foundation.layout.height
import androidx.compose.ui.unit.sp
mport androidx.compose.foundation.layout.height
import androidx.lifecycle.viewmodel.compose.viewModel
mport androidx.compose.foundation.layout.height
import com.huagugu.timememorial2.data.Memorial
mport androidx.compose.foundation.layout.height
import com.huagugu.timememorial2.ui.screens.AddMemorialSheet
mport androidx.compose.foundation.layout.height
import com.huagugu.timememorial2.ui.screens.CalendarScreen
mport androidx.compose.foundation.layout.height
import com.huagugu.timememorial2.ui.screens.HomeScreen
mport androidx.compose.foundation.layout.height
import com.huagugu.timememorial2.ui.screens.SettingsScreen
mport androidx.compose.foundation.layout.height
import com.huagugu.timememorial2.ui.theme.FabBg
mport androidx.compose.foundation.layout.height
import com.huagugu.timememorial2.ui.theme.FabOn
mport androidx.compose.foundation.layout.height
import com.huagugu.timememorial2.ui.theme.OnBackground
mport androidx.compose.foundation.layout.height
import com.huagugu.timememorial2.ui.theme.OnSurfaceVariant
mport androidx.compose.foundation.layout.height
import com.huagugu.timememorial2.viewmodel.MemorialViewModel
mport androidx.compose.foundation.layout.height
import top.yukonga.miuix.kmp.basic.Scaffold
mport androidx.compose.foundation.layout.height
import top.yukonga.miuix.kmp.basic.Surface
mport androidx.compose.foundation.layout.height
import top.yukonga.miuix.kmp.extra.SuperDialog
mport androidx.compose.foundation.layout.height
import top.yukonga.miuix.kmp.theme.MiuixTheme
mport androidx.compose.foundation.layout.height

mport androidx.compose.foundation.layout.height
data class NavItem(val icon: ImageVector, val label: String)
mport androidx.compose.foundation.layout.height

mport androidx.compose.foundation.layout.height
@Composable
mport androidx.compose.foundation.layout.height
fun MainApp(viewModel: MemorialViewModel = viewModel()) {
mport androidx.compose.foundation.layout.height
    var selectedTab by remember { mutableIntStateOf(0) }
mport androidx.compose.foundation.layout.height
    var fabExpanded by remember { mutableStateOf(false) }
mport androidx.compose.foundation.layout.height
    val editingMemorial by viewModel.editingMemorial.collectAsState()
mport androidx.compose.foundation.layout.height
    val isEditing = editingMemorial != null
mport androidx.compose.foundation.layout.height
n    // 弹窗控制
mport androidx.compose.foundation.layout.height
    var showDeleteDialog by remember { mutableStateOf(false) }
mport androidx.compose.foundation.layout.height
    var memorialToDelete by remember { mutableStateOf<Memorial?>(null) }
mport androidx.compose.foundation.layout.height

mport androidx.compose.foundation.layout.height
    Scaffold(
mport androidx.compose.foundation.layout.height
        bottomBar = {
mport androidx.compose.foundation.layout.height
            Surface(
mport androidx.compose.foundation.layout.height
                modifier = Modifier
mport androidx.compose.foundation.layout.height
                    .fillMaxSize()
mport androidx.compose.foundation.layout.height
                    .navigationBarsPadding()
mport androidx.compose.foundation.layout.height
                    .padding(horizontal = 16.dp, vertical = 10.dp)
mport androidx.compose.foundation.layout.height
                    .clip(RoundedCornerShape(22.dp)),
mport androidx.compose.foundation.layout.height
                color = MiuixTheme.colors.surface, tonalElevation = 4.dp
mport androidx.compose.foundation.layout.height
            ) {
mport androidx.compose.foundation.layout.height
                Row(
mport androidx.compose.foundation.layout.height
                    modifier = Modifier
mport androidx.compose.foundation.layout.height
                        .fillMaxSize()
mport androidx.compose.foundation.layout.height
                        .padding(horizontal = 12.dp, vertical = 8.dp),
mport androidx.compose.foundation.layout.height
                    horizontalArrangement = Arrangement.SpaceAround,
mport androidx.compose.foundation.layout.height
                    verticalAlignment = Alignment.CenterVertically
mport androidx.compose.foundation.layout.height
                ) {
mport androidx.compose.foundation.layout.height
                    BottomNavItem(
mport androidx.compose.foundation.layout.height
                        icon = Icons.Default.Home,
mport androidx.compose.foundation.layout.height
                        label = "纪念日",
mport androidx.compose.foundation.layout.height
                        selected = selectedTab == 0,
mport androidx.compose.foundation.layout.height
                        onClick = { selectedTab = 0 }
mport androidx.compose.foundation.layout.height
                    )
mport androidx.compose.foundation.layout.height
                    Box(
mport androidx.compose.foundation.layout.height
                        modifier = Modifier
mport androidx.compose.foundation.layout.height
                            .size(44.dp)
mport androidx.compose.foundation.layout.height
                            .clip(RoundedCornerShape(14.dp))
mport androidx.compose.foundation.layout.height
                            .background(FabBg)
mport androidx.compose.foundation.layout.height
                            .clickable { fabExpanded = !fabExpanded },
mport androidx.compose.foundation.layout.height
                        contentAlignment = Alignment.Center
mport androidx.compose.foundation.layout.height
                    ) {
mport androidx.compose.foundation.layout.height
                        Text("+", fontSize = 24.sp, color = FabOn, fontWeight = FontWeight.Bold)
mport androidx.compose.foundation.layout.height
                    }
mport androidx.compose.foundation.layout.height
                    BottomNavItem(
mport androidx.compose.foundation.layout.height
                        icon = Icons.Default.DateRange,
mport androidx.compose.foundation.layout.height
                        label = "日历",
mport androidx.compose.foundation.layout.height
                        selected = selectedTab == 2,
mport androidx.compose.foundation.layout.height
                        onClick = { selectedTab = 2 }
mport androidx.compose.foundation.layout.height
                    )
mport androidx.compose.foundation.layout.height
                    BottomNavItem(
mport androidx.compose.foundation.layout.height
                        icon = Icons.Default.Settings,
mport androidx.compose.foundation.layout.height
                        label = "设置",
mport androidx.compose.foundation.layout.height
                        selected = selectedTab == 3,
mport androidx.compose.foundation.layout.height
                        onClick = { selectedTab = 3 }
mport androidx.compose.foundation.layout.height
                    )
mport androidx.compose.foundation.layout.height
                }
mport androidx.compose.foundation.layout.height
            }
mport androidx.compose.foundation.layout.height
        },
mport androidx.compose.foundation.layout.height
        floatingActionButton = {
mport androidx.compose.foundation.layout.height
            SuperDialog(
mport androidx.compose.foundation.layout.height
                title = "新建纪念日",
mport androidx.compose.foundation.layout.height
                summary = "选择一个方式添加",
mport androidx.compose.foundation.layout.height
                show = fabExpanded,
mport androidx.compose.foundation.layout.height
                modify = { fabExpanded = false }
mport androidx.compose.foundation.layout.height
            ) {
mport androidx.compose.foundation.layout.height
                Box(
mport androidx.compose.foundation.layout.height
                    modifier = Modifier
mport androidx.compose.foundation.layout.height
                        .fillMaxSize()
mport androidx.compose.foundation.layout.height
                        .padding(horizontal = 8.dp, vertical = 8.dp)
mport androidx.compose.foundation.layout.height
                        .clip(RoundedCornerShape(16.dp))
mport androidx.compose.foundation.layout.height
                        .background(FabBg)
mport androidx.compose.foundation.layout.height
                        .clickable {
mport androidx.compose.foundation.layout.height
                            fabExpanded = false
mport androidx.compose.foundation.layout.height
                            selectedTab = 1
mport androidx.compose.foundation.layout.height
                        },
mport androidx.compose.foundation.layout.height
                    contentAlignment = Alignment.Center
mport androidx.compose.foundation.layout.height
                ) { Text("空白纪念日", color = Color.White, fontWeight = FontWeight.Bold) }
mport androidx.compose.foundation.layout.height
            }
mport androidx.compose.foundation.layout.height
        }
mport androidx.compose.foundation.layout.height
    ) {
mport androidx.compose.foundation.layout.height
        when (selectedTab) {
mport androidx.compose.foundation.layout.height
            0 -> HomeScreen(
mport androidx.compose.foundation.layout.height
                viewModel = viewModel,
mport androidx.compose.foundation.layout.height
                onAddClick = { selectedTab = 1 },
mport androidx.compose.foundation.layout.height
                onEditMemorial = { memorial ->
mport androidx.compose.foundation.layout.height
                    viewModel.startEdit(memorial)
mport androidx.compose.foundation.layout.height
                    selectedTab = 1
mport androidx.compose.foundation.layout.height
                },
mport androidx.compose.foundation.layout.height
                onDeleteMemorial = { memorial ->
mport androidx.compose.foundation.layout.height
                    memorialToDelete = memorial
mport androidx.compose.foundation.layout.height
                    showDeleteDialog = true
mport androidx.compose.foundation.layout.height
                }
mport androidx.compose.foundation.layout.height
            )
mport androidx.compose.foundation.layout.height
            1 -> {
mport androidx.compose.foundation.layout.height
                AddMemorialSheet(
mport androidx.compose.foundation.layout.height
                    editMemorial = editingMemorial,
mport androidx.compose.foundation.layout.height
                    onSave = { title, date, category, note ->
mport androidx.compose.foundation.layout.height
                        if (isEditing) {
mport androidx.compose.foundation.layout.height
                            viewModel.updateMemorial(editingMemorial!!, title, date, category, note)
mport androidx.compose.foundation.layout.height
                        } else {
mport androidx.compose.foundation.layout.height
                            viewModel.addMemorial(title, date, category, note)
mport androidx.compose.foundation.layout.height
                        }
mport androidx.compose.foundation.layout.height
                        viewModel.clearEdit()
mport androidx.compose.foundation.layout.height
                        selectedTab = 0
mport androidx.compose.foundation.layout.height
                    },
mport androidx.compose.foundation.layout.height
                    onDismiss = {
mport androidx.compose.foundation.layout.height
                        viewModel.clearEdit()
mport androidx.compose.foundation.layout.height
                        selectedTab = 0
mport androidx.compose.foundation.layout.height
                    }
mport androidx.compose.foundation.layout.height
                )
mport androidx.compose.foundation.layout.height
            }
mport androidx.compose.foundation.layout.height
            2 -> CalendarScreen(viewModel)
mport androidx.compose.foundation.layout.height
            3 -> SettingsScreen()
mport androidx.compose.foundation.layout.height
        }
mport androidx.compose.foundation.layout.height
    }
mport androidx.compose.foundation.layout.height

mport androidx.compose.foundation.layout.height
    // 删除确认弹窗
mport androidx.compose.foundation.layout.height
    if (showDeleteDialog && memorialToDelete != null) {
mport androidx.compose.foundation.layout.height
        SuperDialog(
mport androidx.compose.foundation.layout.height
            title = "删除纪念日",
mport androidx.compose.foundation.layout.height
            summary = "确定要删除「${memorialToDelete!!.title}」吗？\n删除后无法恢复。",
mport androidx.compose.foundation.layout.height
            show = showDeleteDialog,
mport androidx.compose.foundation.layout.height
            modify = {
mport androidx.compose.foundation.layout.height
                showDeleteDialog = false
mport androidx.compose.foundation.layout.height
                memorialToDelete = null
mport androidx.compose.foundation.layout.height
            }
mport androidx.compose.foundation.layout.height
        ) {
mport androidx.compose.foundation.layout.height
            Row(
mport androidx.compose.foundation.layout.height
                modifier = Modifier.fillMaxSize(),
mport androidx.compose.foundation.layout.height
                horizontalArrangement = Arrangement.spacedBy(12.dp)
mport androidx.compose.foundation.layout.height
            ) {
mport androidx.compose.foundation.layout.height
                Box(
mport androidx.compose.foundation.layout.height
                    modifier = Modifier
mport androidx.compose.foundation.layout.height
                        .weight(1f)
mport androidx.compose.foundation.layout.height
                        .height(46.dp)
mport androidx.compose.foundation.layout.height
                        .clip(RoundedCornerShape(12.dp))
mport androidx.compose.foundation.layout.height
                        .background(OnSurfaceVariant.copy(alpha = 0.12f))
mport androidx.compose.foundation.layout.height
                        .clickable {
mport androidx.compose.foundation.layout.height
                            showDeleteDialog = false
mport androidx.compose.foundation.layout.height
                            memorialToDelete = null
mport androidx.compose.foundation.layout.height
                        },
mport androidx.compose.foundation.layout.height
                    contentAlignment = Alignment.Center
mport androidx.compose.foundation.layout.height
                ) { Text("取消", color = OnBackground, fontWeight = FontWeight.Bold) }
mport androidx.compose.foundation.layout.height
                Box(
mport androidx.compose.foundation.layout.height
                    modifier = Modifier
mport androidx.compose.foundation.layout.height
                        .weight(1f)
mport androidx.compose.foundation.layout.height
                        .height(46.dp)
mport androidx.compose.foundation.layout.height
                        .clip(RoundedCornerShape(12.dp))
mport androidx.compose.foundation.layout.height
                        .background(androidx.compose.ui.graphics.Color(0xFFE53935))
mport androidx.compose.foundation.layout.height
                        .clickable {
mport androidx.compose.foundation.layout.height
                            memorialToDelete?.let { viewModel.deleteMemorial(it) }
mport androidx.compose.foundation.layout.height
                            showDeleteDialog = false
mport androidx.compose.foundation.layout.height
                            memorialToDelete = null
mport androidx.compose.foundation.layout.height
                        },
mport androidx.compose.foundation.layout.height
                    contentAlignment = Alignment.Center
mport androidx.compose.foundation.layout.height
                ) { Text("删除", color = Color.White, fontWeight = FontWeight.Bold) }
mport androidx.compose.foundation.layout.height
            }
mport androidx.compose.foundation.layout.height
        }
mport androidx.compose.foundation.layout.height
    }
mport androidx.compose.foundation.layout.height
}
mport androidx.compose.foundation.layout.height

mport androidx.compose.foundation.layout.height
@Composable
mport androidx.compose.foundation.layout.height
private fun BottomNavItem(
mport androidx.compose.foundation.layout.height
    icon: ImageVector,
mport androidx.compose.foundation.layout.height
    label: String,
mport androidx.compose.foundation.layout.height
    selected: Boolean,
mport androidx.compose.foundation.layout.height
    onClick: () -> Unit
mport androidx.compose.foundation.layout.height
) {
mport androidx.compose.foundation.layout.height
    val color = if (selected) OnBackground else OnSurfaceVariant
mport androidx.compose.foundation.layout.height
    androidx.compose.foundation.layout.Column(
mport androidx.compose.foundation.layout.height
        modifier = Modifier
mport androidx.compose.foundation.layout.height
            .clickable(
mport androidx.compose.foundation.layout.height
                interactionSource = remember { MutableInteractionSource() },
mport androidx.compose.foundation.layout.height
                indication = null,
mport androidx.compose.foundation.layout.height
                onClick = onClick
mport androidx.compose.foundation.layout.height
            )
mport androidx.compose.foundation.layout.height
            .padding(horizontal = 16.dp, vertical = 4.dp),
mport androidx.compose.foundation.layout.height
        horizontalAlignment = Alignment.CenterHorizontally
mport androidx.compose.foundation.layout.height
    ) {
mport androidx.compose.foundation.layout.height
        top.yukonga.miuix.kmp.basic.Icon(
mport androidx.compose.foundation.layout.height
            imageVector = icon,
mport androidx.compose.foundation.layout.height
            tint = color,
mport androidx.compose.foundation.layout.height
            modifier = Modifier.size(24.dp)
mport androidx.compose.foundation.layout.height
        )
mport androidx.compose.foundation.layout.height
        Text(label, fontSize = 11.sp, color = color)
mport androidx.compose.foundation.layout.height
    }
mport androidx.compose.foundation.layout.height
}
