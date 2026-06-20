package com.huagugu.timememorial2.data

import kotlinx.coroutines.flow.Flow

class MemorialRepository(private val dao: MemorialDao) {

    fun getAll(): Flow<List<Memorial>> = dao.getAll()

    fun getByCategory(category: String): Flow<List<Memorial>> = dao.getByCategory(category)

    suspend fun insert(memorial: Memorial): Long = dao.insert(memorial)

    suspend fun update(memorial: Memorial) = dao.update(memorial)

    suspend fun deleteById(id: Long) = dao.deleteById(id)
}
