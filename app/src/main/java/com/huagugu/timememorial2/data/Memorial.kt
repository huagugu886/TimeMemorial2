package com.huagugu.timememorial2.data

import androidx.room.Entity
import androidx.room.PrimaryKey

enum class Category(val label: String) {
    LOVE("爱情"),
    WORK("工作"),
    LIFE("生活"),
    STUDY("学习"),
    FESTIVAL("节日")
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
