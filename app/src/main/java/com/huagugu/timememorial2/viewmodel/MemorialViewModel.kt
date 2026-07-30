package com.huagugu.timememorial2.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.huagugu.timememorial2.TimeMemorialApp
import com.huagugu.timememorial2.data.Memorial
import com.huagugu.timememorial2.data.MemorialRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class MemorialViewModel(application: Application) : AndroidViewModel(application) {

    private val repository: MemorialRepository
    val allMemorials: StateFlow<List<Memorial>>

    private val _selectedCategory = MutableStateFlow<String?>(null)
    val selectedCategory: StateFlow<String?> = _selectedCategory

    // 选中的纪念日（用于编辑）
    private val _editingMemorial = MutableStateFlow<Memorial?>(null)
    val editingMemorial: StateFlow<Memorial?> = _editingMemorial

    val filteredMemorials: StateFlow<List<Memorial>>

    init {
        val dao = (application as TimeMemorialApp).database.memorialDao()
        repository = MemorialRepository(dao)
        allMemorials = repository.getAll()
            .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())
        filteredMemorials = combine(allMemorials, _selectedCategory) { memorials, category ->
            if (category == null) memorials else memorials.filter { it.category == category }
        }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())
    }

    fun setCategory(category: String?) {
        _selectedCategory.value = category
    }

    fun startEdit(memorial: Memorial) {
        _editingMemorial.value = memorial
    }

    fun clearEdit() {
        _editingMemorial.value = null
    }

    fun addMemorial(title: String, date: Long, category: String, note: String) {
        viewModelScope.launch {
            repository.insert(
                Memorial(
                    title = title,
                    date = date,
                    category = category,
                    note = note
                )
            )
        }
    }

    fun updateMemorial(memorial: Memorial, title: String, date: Long, category: String, note: String) {
        viewModelScope.launch {
            repository.update(
                memorial.copy(
                    title = title,
                    date = date,
                    category = category,
                    note = note
                )
            )
        }
    }

    fun deleteMemorial(memorial: Memorial) {
        viewModelScope.launch {
            repository.deleteById(memorial.id)
        }
    }
}
