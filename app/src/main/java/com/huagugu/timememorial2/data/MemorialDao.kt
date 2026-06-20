package com.huagugu.timememorial2.data

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
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
