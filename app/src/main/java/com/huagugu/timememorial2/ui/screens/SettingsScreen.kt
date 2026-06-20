package com.huagugu.timememorial2.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.huagugu.timememorial2.ui.theme.Disabled
import com.huagugu.timememorial2.ui.theme.OnSurfaceVariant
import com.huagugu.timememorial2.ui.theme.SurfaceContainer
import top.yukonga.miuix.kmp.basic.Switch
import top.yukonga.miuix.kmp.basic.Text

@Composable
fun SettingsScreen() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(top = 12.dp)
    ) {
        Text(
            text = "设置",
            fontSize = 34.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(start = 22.dp, bottom = 24.dp)
        )

        SettingsGroupLabel("通用")
        SettingsCard {
            SwitchItem("纪念日提醒", "", initialOn = true)
            ClickItem("提前提醒天数", "提前 3 天提醒")
            ClickItem("深色模式", "跟随系统")
        }

        Spacer(modifier = Modifier.height(24.dp))

        SettingsGroupLabel("数据")
        SettingsCard {
            ClickItem("导出数据", "")
            ClickItem("导入数据", "")
        }

        Spacer(modifier = Modifier.height(24.dp))

        SettingsGroupLabel("关于")
        SettingsCard {
            ClickItem("TimeMemorial2", "版本 1.0.0")
        }
    }
}

@Composable
private fun SettingsGroupLabel(text: String) {
    Text(
        text = text,
        fontSize = 13.sp,
        color = OnSurfaceVariant,
        fontWeight = FontWeight.Medium,
        modifier = Modifier.padding(start = 4.dp, bottom = 10.dp)
    )
}

@Composable
private fun SettingsCard(content: @Composable () -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 22.dp)
            .clip(RoundedCornerShape(20.dp))
            .background(SurfaceContainer)
    ) {
        content()
    }
}

@Composable
private fun SwitchItem(title: String, subtitle: String, initialOn: Boolean = false) {
    var on by remember { mutableStateOf(initialOn) }
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 18.dp)
            .height(56.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(modifier = Modifier.weight(1f)) {
            Text(text = title, fontSize = 16.sp, fontWeight = FontWeight.Medium)
            if (subtitle.isNotEmpty()) {
                Text(text = subtitle, fontSize = 13.sp, color = OnSurfaceVariant, modifier = Modifier.padding(top = 2.dp))
            }
        }
        Switch(checked = on, onCheckedChange = { on = it })
    }
    SettingsDivider()
}

@Composable
private fun ClickItem(title: String, subtitle: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 18.dp)
            .height(56.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(modifier = Modifier.weight(1f)) {
            Text(text = title, fontSize = 16.sp, fontWeight = FontWeight.Medium)
            if (subtitle.isNotEmpty()) {
                Text(text = subtitle, fontSize = 13.sp, color = OnSurfaceVariant, modifier = Modifier.padding(top = 2.dp))
            }
        }
        Text(text = "\u203A", fontSize = 16.sp, color = Disabled)
    }
    SettingsDivider()
}

@Composable
private fun SettingsDivider() {
    Spacer(
        modifier = Modifier
            .fillMaxWidth()
            .height(0.5.dp)
            .padding(horizontal = 18.dp)
            .background(com.huagugu.timememorial2.ui.theme.DividerLine)
    )
}
