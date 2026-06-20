#!/bin/bash
set -e

BASE=/sdcard/Download/TimeMemorial2
rm -rf $BASE
mkdir -p $BASE/app/src/main/java/com/huagugu/timememorial2/{data,viewmodel,ui/{theme,screens,components}}
mkdir -p $BASE/app/src/main/res/values
mkdir -p $BASE/gradle

# ===== Gradle 配置 =====

cat > $BASE/build.gradle.kts << 'EOF'
plugins {
    alias(libs.plugins.android.application) apply false
    alias(libs.plugins.kotlin.android) apply false
    alias(libs.plugins.kotlin.compose) apply false
    alias(libs.plugins.ksp) apply false
}
EOF

cat > $BASE/settings.gradle.kts << 'EOF'
pluginManagement {
    repositories {
        google {
            content {
                includeGroupByRegex("com\\.android.*")
                includeGroupByRegex("com\\.google.*")
                includeGroupByRegex("androidx.*")
            }
        }
        mavenCentral()
        gradlePluginPortal()
    }
}
dependencyResolution {
    repositories {
        google()
        mavenCentral()
        maven("https://jitpack.io")
    }
}
rootProject.name = "TimeMemorial2"
include(":app")
EOF

cat > $BASE/.gitignore << 'EOF'
*.iml
.gradle
/local.properties
/.idea
.DS_Store
/build
/captures
/app/build
EOF

cat > $BASE/README.md << 'EOF'
# TimeMemorial2
纪念日管理 App
EOF

cat > $BASE/gradle/libs.versions.toml << 'EOF'
[versions]
agp = "8.7.3"
kotlin = "2.0.21"
ksp = "2.0.21-1.0.28"
coreKtx = "1.15.0"
lifecycleRuntime = "2.8.7"
activityCompose = "1.9.3"
composeBom = "2024.12.01"
room = "2.6.1"
navigationCompose = "2.8.5"
miuix = "0.9.2"

[libraries]
androidx-core-ktx = { group = "androidx.core", name = "core-ktx", version.ref = "coreKtx" }
androidx-lifecycle-runtime-ktx = { group = "androidx.lifecycle", name = "lifecycle-runtime-ktx", version.ref = "lifecycleRuntime" }
androidx-lifecycle-viewmodel-compose = { group = "androidx.lifecycle", name = "lifecycle-viewmodel-compose", version.ref = "lifecycleRuntime" }
androidx-activity-compose = { group = "androidx.activity", name = "activity-compose", version.ref = "activityCompose" }
androidx-compose-bom = { group = "androidx.compose", name = "compose-bom", version.ref = "composeBom" }
androidx-ui = { group = "androidx.compose.ui", name = "ui" }
androidx-ui-graphics = { group = "androidx.compose.ui", name = "ui-graphics" }
androidx-ui-tooling = { group = "androidx.compose.ui", name = "ui-tooling" }
androidx-ui-tooling-preview = { group = "androidx.compose.ui", name = "ui-tooling-preview" }
androidx-material3 = { group = "androidx.compose.material3", name = "material3" }
androidx-navigation-compose = { group = "androidx.navigation", name = "navigation-compose", version.ref = "navigationCompose" }
androidx-room-runtime = { group = "androidx.room", name = "room-runtime", version.ref = "room" }
androidx-room-ktx = { group = "androidx.room", name = "room-ktx", version.ref = "room" }
androidx-room-compiler = { group = "androidx.room", name = "room-compiler", version.ref = "room" }
miuix = { group = "top.yukonga.miuix.kmp", name = "miuix", version.ref = "miuix" }

[plugins]
android-application = { id = "com.android.application", version.ref = "agp" }
kotlin-android = { id = "org.jetbrains.kotlin.android", version.ref = "kotlin" }
kotlin-compose = { id = "org.jetbrains.kotlin.plugin.compose", version.ref = "kotlin" }
ksp = { id = "com.google.devtools.ksp", version.ref = "ksp" }
EOF

cat > $BASE/app/build.gradle.kts << 'EOF'
plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)
    alias(libs.plugins.ksp)
}
android {
    namespace = "com.huagugu.timememorial2"
    compileSdk = 35
    defaultConfig {
        applicationId = "com.huagugu.timememorial2"
        minSdk = 26
        targetSdk = 35
        versionCode = 1
        versionName = "1.0"
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    kotlinOptions { jvmTarget = "11" }
    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
        }
    }
    buildFeatures { compose = true }
}
dependencies {
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.lifecycle.viewmodel.compose)
    implementation(libs.androidx.activity.compose)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.ui)
    implementation(libs.androidx.ui.graphics)
    implementation(libs.androidx.ui.tooling.preview)
    implementation(libs.androidx.material3)
    implementation(libs.androidx.navigation.compose)
    implementation(libs.androidx.room.runtime)
    implementation(libs.androidx.room.ktx)
    ksp(libs.androidx.room.compiler)
    implementation(libs.miuix)
    debugImplementation(libs.androidx.ui.tooling)
}
EOF

cat > $BASE/app/src/main/AndroidManifest.xml << 'EOF'
<?xml version="1.0" encoding="utf-8"?>
<manifest xmlns:android="http://schemas.android.com/apk/res/android">
    <application
        android:allowBackup="true"
        android:label="纪念日"
        android:supportsRtl="true"
        android:theme="@style/Theme.TimeMemorial2">
        <activity android:name=".MainActivity" android:exported="true" android:windowSoftInputMode="adjustResize">
            <intent-filter>
                <action android:name="android.intent.action.MAIN" />
                <category android:name="android.intent.category.LAUNCHER" />
            </intent-filter>
        </activity>
    </application>
</manifest>
EOF

cat > $BASE/app/src/main/res/values/themes.xml << 'EOF'
<?xml version="1.0" encoding="utf-8"?>
<resources>
    <style name="Theme.TimeMemorial2" parent="android:Theme.Material.Light.NoActionBar" />
</resources>
EOF

# ===== Kotlin 源码 =====

cat > $BASE/app/src/main/java/com/huagugu/timememorial2/TimeMemorialApp.kt << 'EOF'
package com.huagugu.timememorial2

import android.app.Application
import com.huagugu.timememorial2.data.AppDatabase

class TimeMemorialApp : Application() {
    val database: AppDatabase by lazy { AppDatabase.getDatabase(this) }
}
EOF

cat > $BASE/app/src/main/java/com/huagugu/timememorial2/MainActivity.kt << 'EOF'
package com.huagugu.timememorial2

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.huagugu.timememorial2.ui.MainApp

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent { MainApp() }
    }
}
EOF

cat > $BASE/app/src/main/java/com/huagugu/timememorial2/data/Memorial.kt << 'EOF'
package com.huagugu.timememorial2.data

import androidx.room.*

enum class Category(val label: String) {
    LOVE("爱情"), WORK("工作"), LIFE("生活"), STUDY("学习"), FESTIVAL("节日")
}

@Entity(tableName = "memorials")
data class Memorial(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val title: String,
    val date: Long,
    val category: String,
    val repeatYearly: Boolean = true,
    val note: String = "",
    val createdAt: Long = System.currentTimeMillis()
)
EOF

cat > $BASE/app/src/main/java/com/huagugu/timememorial2/data/MemorialDao.kt << 'EOF'
package com.huagugu.timememorial2.data

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface MemorialDao {
    @Query("SELECT * FROM memorials ORDER BY date ASC")
    fun getAll(): Flow<List<Memorial>>
    @Query("SELECT * FROM memorials WHERE category = :category ORDER BY date ASC")
    fun getByCategory(category: String): Flow<List<Memorial>>
    @Insert
    suspend fun insert(memorial: Memorial): Long
    @Update
    suspend fun update(memorial: Memorial)
    @Delete
    suspend fun delete(memorial: Memorial)
    @Query("DELETE FROM memorials WHERE id = :id")
    suspend fun deleteById(id: Long)
}
EOF

cat > $BASE/app/src/main/java/com/huagugu/timememorial2/data/AppDatabase.kt << 'EOF'
package com.huagugu.timememorial2.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [Memorial::class], version = 1, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {
    abstract fun memorialDao(): MemorialDao
    companion object {
        @Volatile private var INSTANCE: AppDatabase? = null
        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                Room.databaseBuilder(context.applicationContext, AppDatabase::class.java, "timememorial_db").build().also { INSTANCE = it }
            }
        }
    }
}
EOF

cat > $BASE/app/src/main/java/com/huagugu/timememorial2/data/MemorialRepository.kt << 'EOF'
package com.huagugu.timememorial2.data

import kotlinx.coroutines.flow.Flow

class MemorialRepository(private val dao: MemorialDao) {
    fun getAll(): Flow<List<Memorial>> = dao.getAll()
    fun getByCategory(category: String): Flow<List<Memorial>> = dao.getByCategory(category)
    suspend fun insert(memorial: Memorial): Long = dao.insert(memorial)
    suspend fun update(memorial: Memorial) = dao.update(memorial)
    suspend fun deleteById(id: Long) = dao.deleteById(id)
}
EOF

cat > $BASE/app/src/main/java/com/huagugu/timememorial2/viewmodel/MemorialViewModel.kt << 'EOF'
package com.huagugu.timememorial2.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.huagugu.timememorial2.TimeMemorialApp
import com.huagugu.timememorial2.data.*
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class MemorialViewModel(application: Application) : AndroidViewModel(application) {
    private val repo = MemorialRepository((application as TimeMemorialApp).database.memorialDao())
    val allMemorials = repo.getAll().stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())
    private val _cat = MutableStateFlow<String?>(null)
    val selectedCategory: StateFlow<String?> = _cat
    val filteredMemorials = combine(allMemorials, _cat) { m, c ->
        if (c == null) m else m.filter { it.category == c }
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    fun setCategory(c: String?) { _cat.value = c }
    fun addMemorial(title: String, date: Long, category: String, note: String) {
        viewModelScope.launch { repo.insert(Memorial(title = title, date = date, category = category, note = note)) }
    }
    fun deleteMemorial(id: Long) { viewModelScope.launch { repo.deleteById(id) } }
}
EOF

cat > $BASE/app/src/main/java/com/huagugu/timememorial2/ui/theme/Color.kt << 'EOF'
package com.huagugu.timememorial2.ui.theme

import androidx.compose.ui.graphics.Color

val Purple80 = Color(0xFFCFBCFF)
val PurpleGrey80 = Color(0xFFCCC2DC)
val Pink80 = Color(0xFFEFB8C8)
val Purple40 = Color(0xFF6750A4)
val PurpleGrey40 = Color(0xFF625B71)
val Pink40 = Color(0xFF7D5260)
val PrimaryPurple = Color(0xFF7C4DFF)
val LightPurple = Color(0xFFB388FF)
val DarkPurple = Color(0xFF651FFF)
val SurfacePurple = Color(0xFFF3E8FF)
val CardPurple = Color(0xFFEDE7F6)
EOF

cat > $BASE/app/src/main/java/com/huagugu/timememorial2/ui/theme/Theme.kt << 'EOF'
package com.huagugu.timememorial2.ui.theme

import android.app.Activity
import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat

private val DarkColorScheme = darkColorScheme(primary = Purple80, secondary = PurpleGrey80, tertiary = Pink80)
private val LightColorScheme = lightColorScheme(primary = Purple40, secondary = PurpleGrey40, tertiary = Pink40)

@Composable
fun TimeMemorialTheme(darkTheme: Boolean = isSystemInDarkTheme(), dynamicColor: Boolean = true, content: @Composable () -> Unit) {
    val colorScheme = when {
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            val ctx = LocalView.current.context
            if (darkTheme) dynamicDarkColorScheme(ctx) else dynamicLightColorScheme(ctx)
        }
        darkTheme -> DarkColorScheme
        else -> LightColorScheme
    }
    val view = LocalView.current
    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as Activity).window
            window.statusBarColor = colorScheme.primary.toArgb()
            WindowCompat.getInsetsController(window, view).isAppearanceLightStatusBars = !darkTheme
        }
    }
    MaterialTheme(colorScheme = colorScheme, content = content)
}
EOF

cat > $BASE/app/src/main/java/com/huagugu/timememorial2/ui/components/CategoryChip.kt << 'EOF'
package com.huagugu.timememorial2.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.huagugu.timememorial2.data.Category

@Composable
fun CategoryChip(category: Category, selected: Boolean, onClick: () -> Unit, modifier: Modifier = Modifier) {
    val bg = when (category) {
        Category.LOVE -> Color(0xFFFF80AB); Category.WORK -> Color(0xFF82B1FF); Category.LIFE -> Color(0xFF69F0AE)
        Category.STUDY -> Color(0xFFFFD740); Category.FESTIVAL -> Color(0xFFFF6E40)
    }
    Box(modifier = modifier.clip(RoundedCornerShape(20.dp)).background(if (selected) bg else bg.copy(alpha = 0.2f))
        .clickable { onClick() }.padding(horizontal = 16.dp, vertical = 8.dp), contentAlignment = Alignment.Center) {
        Text(category.label, color = if (selected) Color.White else bg, fontSize = 14.sp, fontWeight = if (selected) FontWeight.Bold else FontWeight.Normal)
    }
}
EOF

cat > $BASE/app/src/main/java/com/huagugu/timememorial2/ui/components/MemorialCard.kt << 'EOF'
package com.huagugu.timememorial2.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.huagugu.timememorial2.data.Category
import com.huagugu.timememorial2.data.Memorial
import com.huagugu.timememorial2.ui.theme.*
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.TimeUnit

@Composable
fun MemorialCard(memorial: Memorial, onClick: () -> Unit, modifier: Modifier = Modifier) {
    val diffMs = memorial.date - System.currentTimeMillis()
    val diffDays = TimeUnit.MILLISECONDS.toDays(diffMs)
    val isPast = diffMs < 0
    val color = when (memorial.category) {
        Category.LOVE.name -> Color(0xFFFF80AB); Category.WORK.name -> Color(0xFF82B1FF); Category.LIFE.name -> Color(0xFF69F0AE)
        Category.STUDY.name -> Color(0xFFFFD740); Category.FESTIVAL.name -> Color(0xFFFF6E40)
        else -> PrimaryPurple
    }
    val fmt = SimpleDateFormat("yyyy.MM.dd", Locale.getDefault())
    Box(modifier = modifier.fillMaxWidth().clip(RoundedCornerShape(16.dp)).background(CardPurple).padding(16.dp)) {
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
            Column(modifier = Modifier.weight(1f)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(modifier = Modifier.size(8.dp).clip(RoundedCornerShape(4.dp)).background(color))
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(memorial.title, fontSize = 18.sp, fontWeight = FontWeight.Bold, color = Color(0xFF1A1A1A))
                }
                Spacer(modifier = Modifier.height(4.dp))
                Text(fmt.format(Date(memorial.date)), fontSize = 13.sp, color = Color(0xFF888888))
                if (memorial.note.isNotEmpty()) { Spacer(modifier = Modifier.height(4.dp)); Text(memorial.note, fontSize = 12.sp, color = Color(0xFFAAAAAA)) }
            }
            Column(horizontalAlignment = Alignment.End) {
                Text(if (isPast) "已过" else "剩余", fontSize = 12.sp, color = Color(0xFF888888))
                Text("${if (isPast) -diffDays else diffDays}", fontSize = 28.sp, fontWeight = FontWeight.Bold, color = color)
                Text("天", fontSize = 12.sp, color = Color(0xFF888888))
            }
        }
    }
}
EOF

cat > $BASE/app/src/main/java/com/huagugu/timememorial2/ui/screens/HomeScreen.kt << 'EOF'
package com.huagugu.timememorial2.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.huagugu.timememorial2.data.Category
import com.huagugu.timememorial2.ui.components.CategoryChip
import com.huagugu.timememorial2.ui.components.MemorialCard
import com.huagugu.timememorial2.ui.theme.*
import com.huagugu.timememorial2.viewmodel.MemorialViewModel
import java.util.*

@Composable
fun HomeScreen(viewModel: MemorialViewModel, onAddClick: () -> Unit) {
    val memorials by viewModel.filteredMemorials.collectAsState()
    val selectedCategory by viewModel.selectedCategory.collectAsState()
    val total = memorials.size
    val now = System.currentTimeMillis()
    val upcoming = memorials.count { it.date > now }
    val cal = Calendar.getInstance()
    val month = cal.get(Calendar.MONTH); val year = cal.get(Calendar.YEAR)
    val thisMonth = memorials.count { val c = Calendar.getInstance().apply { timeInMillis = it.date }; c.get(Calendar.MONTH) == month && c.get(Calendar.YEAR) == year }
    Column(modifier = Modifier.fillMaxSize().background(Color(0xFFF8F5FF)).padding(horizontal = 16.dp).padding(top = 48.dp)) {
        Text("纪念日", fontSize = 28.sp, fontWeight = FontWeight.Bold, color = PrimaryPurple, modifier = Modifier.padding(bottom = 16.dp))
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(12.dp)) {
            StatCard("总计", "$total", PrimaryPurple, Modifier.weight(1f)); StatCard("即将到来", "$upcoming", DarkPurple, Modifier.weight(1f)); StatCard("本月", "$thisMonth", LightPurple, Modifier.weight(1f))
        }
        Spacer(modifier = Modifier.height(16.dp))
        LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            item { CategoryChip(category = Category.LOVE, selected = selectedCategory == null, onClick = { viewModel.setCategory(null) }) }
            items(Category.entries.toList()) { cat -> CategoryChip(category = cat, selected = selectedCategory == cat.name, onClick = { viewModel.setCategory(cat.name) }) }
        }
        Spacer(modifier = Modifier.height(16.dp))
        if (memorials.isEmpty()) { Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) { Text("还没有纪念日\n点击 + 添加一个吧", color = Color(0xFFAAAAAA), fontSize = 16.sp) } }
        else { LazyColumn(verticalArrangement = Arrangement.spacedBy(12.dp), contentPadding = PaddingValues(bottom = 80.dp)) { items(memorials, key = { it.id }) { MemorialCard(it, onClick = {}) } } }
    }
}

@Composable
fun StatCard(label: String, value: String, color: Color, modifier: Modifier = Modifier) {
    Box(modifier = modifier.clip(RoundedCornerShape(16.dp)).background(color.copy(alpha = 0.1f)).padding(16.dp)) {
        Column { Text(label, fontSize = 13.sp, color = color); Text(value, fontSize = 24.sp, fontWeight = FontWeight.Bold, color = color) }
    }
}
EOF

cat > $BASE/app/src/main/java/com/huagugu/timememorial2/ui/screens/CalendarScreen.kt << 'EOF'
package com.huagugu.timememorial2.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.huagugu.timememorial2.ui.theme.*
import com.huagugu.timememorial2.viewmodel.MemorialViewModel
import java.util.*

@Composable
fun CalendarScreen(viewModel: MemorialViewModel) {
    val memorials by viewModel.allMemorials.collectAsState()
    val now = Calendar.getInstance()
    var year by remember { mutableIntStateOf(now.get(Calendar.YEAR)) }
    var month by remember { mutableIntStateOf(now.get(Calendar.MONTH)) }
    val daysInMonth = remember(year, month) { Calendar.getInstance().apply { set(year, month, 1) }.getActualMaximum(Calendar.DAY_OF_MONTH) }
    val firstDow = remember(year, month) { Calendar.getInstance().apply { set(year, month, 1) }.get(Calendar.DAY_OF_WEEK) - 1 }
    val memDays = remember(memorials, year, month) {
        val c = Calendar.getInstance()
        memorials.filter { c.timeInMillis = it.date; c.get(Calendar.YEAR) == year && c.get(Calendar.MONTH) == month }.associate { c.timeInMillis = it.date; c.get(Calendar.DAY_OF_MONTH) to it.title }
    }
    Column(modifier = Modifier.fillMaxSize().background(Color(0xFFF8F5FF)).padding(horizontal = 16.dp).padding(top = 48.dp)) {
        Text("日历", fontSize = 28.sp, fontWeight = FontWeight.Bold, color = PrimaryPurple, modifier = Modifier.padding(bottom = 16.dp))
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
            Text("<", fontSize = 24.sp, color = PrimaryPurple, fontWeight = FontWeight.Bold, modifier = Modifier.clip(RoundedCornerShape(12.dp)).background(PrimaryPurple.copy(alpha = 0.1f)).clickable { if (month == 0) { month = 11; year-- } else month-- }.padding(horizontal = 16.dp, vertical = 8.dp))
            Text("${year}年${month + 1}月", fontSize = 20.sp, fontWeight = FontWeight.Bold)
            Text(">", fontSize = 24.sp, color = PrimaryPurple, fontWeight = FontWeight.Bold, modifier = Modifier.clip(RoundedCornerShape(12.dp)).background(PrimaryPurple.copy(alpha = 0.1f)).clickable { if (month == 11) { month = 0; year++ } else month++ }.padding(horizontal = 16.dp, vertical = 8.dp))
        }
        Spacer(modifier = Modifier.height(16.dp))
        Row(modifier = Modifier.fillMaxWidth()) { listOf("日","一","二","三","四","五","六").forEach { d -> Text(d, modifier = Modifier.weight(1f), textAlign = TextAlign.Center, fontSize = 13.sp, color = Color(0xFF888888)) } }
        Spacer(modifier = Modifier.height(8.dp))
        val rows = (firstDow + daysInMonth + 6) / 7
        for (r in 0 until rows) { Row(modifier = Modifier.fillMaxWidth()) { for (col in 0..6) { val day = r * 7 + col - firstDow + 1; if (day in 1..daysInMonth) { val has = memDays.containsKey(day); Box(modifier = Modifier.weight(1f).padding(2.dp).height(36.dp).clip(RoundedCornerShape(8.dp)).background(if (has) PrimaryPurple.copy(alpha = 0.15f) else Color.Transparent), contentAlignment = Alignment.Center) { Text("$day", fontSize = 14.sp, color = if (has) PrimaryPurple else Color(0xFF333333), fontWeight = if (has) FontWeight.Bold else FontWeight.Normal) } } else Spacer(modifier = Modifier.weight(1f).height(36.dp)) } } }
        if (memDays.isNotEmpty()) { Spacer(modifier = Modifier.height(24.dp)); Text("本月纪念日", fontSize = 16.sp, fontWeight = FontWeight.Bold, color = PrimaryPurple); Spacer(modifier = Modifier.height(8.dp)); memDays.forEach { (d, t) -> Text("  ${month + 1}月${d}日 - $t", fontSize = 14.sp, color = Color(0xFF555555)) } }
    }
}
EOF

cat > $BASE/app/src/main/java/com/huagugu/timememorial2/ui/screens/SettingsScreen.kt << 'EOF'
package com.huagugu.timememorial2.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.huagugu.timememorial2.ui.theme.*

@Composable
fun SettingsScreen() {
    Column(modifier = Modifier.fillMaxSize().background(Color(0xFFF8F5FF)).padding(horizontal = 16.dp).padding(top = 48.dp)) {
        Text("设置", fontSize = 28.sp, fontWeight = FontWeight.Bold, color = PrimaryPurple, modifier = Modifier.padding(bottom = 24.dp))
        listOf("提醒通知" to "开启纪念日提醒", "主题" to "跟随系统", "数据备份" to "导出/导入纪念日数据", "关于" to "TimeMemorial2 v1.0").forEach { (t, s) ->
            Column(modifier = Modifier.fillMaxWidth().padding(bottom = 8.dp).clip(RoundedCornerShape(12.dp)).background(Color.White).padding(16.dp)) {
                Text(t, fontSize = 16.sp, fontWeight = FontWeight.Medium); Text(s, fontSize = 13.sp, color = Color(0xFF888888))
            }
        }
    }
}
EOF

cat > $BASE/app/src/main/java/com/huagugu/timememorial2/ui/screens/AddMemorialSheet.kt << 'EOF'
package com.huagugu.timememorial2.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.huagugu.timememorial2.data.Category
import com.huagugu.timememorial2.ui.components.CategoryChip
import com.huagugu.timememorial2.ui.theme.*
import com.huagugu.timememorial2.viewmodel.MemorialViewModel
import java.text.SimpleDateFormat
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddMemorialSheet(viewModel: MemorialViewModel, onDismiss: () -> Unit) {
    var title by remember { mutableStateOf("") }; var note by remember { mutableStateOf("") }; var selCat by remember { mutableStateOf(Category.LOVE) }
    val fmt = remember { SimpleDateFormat("yyyy年MM月dd日", Locale.getDefault()) }; var selDate by remember { mutableLongStateOf(System.currentTimeMillis()) }; var showDP by remember { mutableStateOf(false) }
    ModalBottomSheet(onDismissRequest = onDismiss, containerColor = Color.White) {
        Column(modifier = Modifier.fillMaxWidth().padding(horizontal = 24.dp).padding(bottom = 32.dp)) {
            Text("添加纪念日", fontSize = 22.sp, fontWeight = FontWeight.Bold, color = PrimaryPurple, modifier = Modifier.padding(bottom = 20.dp))
            Text("标题", fontSize = 14.sp, color = Color(0xFF888888))
            OutlinedTextField(value = title, onValueChange = { title = it }, placeholder = { Text("给纪念日起个名字") }, modifier = Modifier.fillMaxWidth(), singleLine = true, shape = RoundedCornerShape(12.dp))
            Spacer(modifier = Modifier.height(12.dp))
            Text("日期", fontSize = 14.sp, color = Color(0xFF888888))
            Box(modifier = Modifier.fillMaxWidth().clip(RoundedCornerShape(12.dp)).background(SurfacePurple).clickable { showDP = true }.padding(16.dp)) { Text(fmt.format(Date(selDate)), fontSize = 16.sp) }
            Spacer(modifier = Modifier.height(12.dp))
            Text("分类", fontSize = 14.sp, color = Color(0xFF888888)); Spacer(modifier = Modifier.height(8.dp))
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) { Category.entries.forEach { cat -> CategoryChip(category = cat, selected = selCat == cat, onClick = { selCat = cat }) } }
            Spacer(modifier = Modifier.height(12.dp))
            Text("备注（可选）", fontSize = 14.sp, color = Color(0xFF888888))
            OutlinedTextField(value = note, onValueChange = { note = it }, placeholder = { Text("添加备注信息") }, modifier = Modifier.fillMaxWidth(), maxLines = 3, shape = RoundedCornerShape(12.dp))
            Spacer(modifier = Modifier.height(20.dp))
            Button(onClick = { if (title.isNotBlank()) { viewModel.addMemorial(title = title.trim(), date = selDate, category = selCat.name, note = note.trim()); onDismiss() } }, modifier = Modifier.fillMaxWidth().height(52.dp), shape = RoundedCornerShape(14.dp), colors = ButtonDefaults.buttonColors(containerColor = PrimaryPurple)) {
                Text("添加", fontSize = 16.sp, fontWeight = FontWeight.Bold)
            }
        }
    }
    if (showDP) { val dps = rememberDatePickerState(initialSelectedDateMillis = selDate); DatePickerDialog(onDismissRequest = { showDP = false }, confirmButton = { TextButton(onClick = { dps.selectedDateMillis?.let { selDate = it }; showDP = false }) { Text("确定") } }, dismissButton = { TextButton(onClick = { showDP = false }) { Text("取消") } }) { DatePicker(state = dps) } }
}
EOF

cat > $BASE/app/src/main/java/com/huagugu/timememorial2/ui/MainApp.kt << 'EOF'
package com.huagugu.timememorial2.ui

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.text.font.FontWeight
import androidx.lifecycle.viewmodel.compose.viewModel
import com.huagugu.timememorial2.ui.theme.PrimaryPurple
import com.huagugu.timememorial2.viewmodel.MemorialViewModel

@Composable
fun MainApp(viewModel: MemorialViewModel = viewModel()) {
    var tab by remember { mutableIntStateOf(0) }; var showAdd by remember { mutableStateOf(false) }
    Scaffold(
        floatingActionButton = { FloatingActionButton(onClick = { showAdd = true }, containerColor = PrimaryPurple, contentColor = Color.White, shape = RoundedCornerShape(16.dp)) { Text("+", fontSize = 24.sp, fontWeight = FontWeight.Bold) } },
        floatingActionButtonPosition = FabPosition.Center,
        bottomBar = { NavigationBar(containerColor = Color.White) { listOf("首页","日历","设置").forEachIndexed { i, t -> NavigationBarItem(selected = tab == i, onClick = { tab = i }, icon = {}, label = { Text(t) }, colors = NavigationBarItemDefaults.colors(selectedIconColor = PrimaryPurple, selectedTextColor = PrimaryPurple, indicatorColor = PrimaryPurple.copy(alpha = 0.1f))) } } }
    ) { pv -> Box(modifier = Modifier.padding(pv)) { when (tab) { 0 -> HomeScreen(viewModel, { showAdd = true }); 1 -> CalendarScreen(viewModel); 2 -> SettingsScreen() } } }
    if (showAdd) AddMemorialSheet(viewModel, onDismiss = { showAdd = false })
}
EOF

# ===== Git =====
cd $BASE
git init
git add .
git commit -m "feat: 初始化纪念日管理 App - Kotlin + Compose + MIUIX + Room"
git remote add origin https://github.com/huagugu886/TimeMemorial2.git
git branch -M main

echo "========================================="
echo "  项目已创建在 /sdcard/Download/TimeMemorial2/"
echo "========================================="
echo "准备推送到 GitHub..."
git push -u origin main

echo "=== 全部完成 ==="
