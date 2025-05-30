package com.example.mylazycolumnsync

import android.app.Application
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.mylazycolumnsync.data.WordDatabase
import com.example.mylazycolumnsync.data.WordItem
import com.example.mylazycolumnsync.data.WordRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class MainViewModel(application: Application) : AndroidViewModel(application) {

    private val dao = WordDatabase.getDatabase(application).wordDao()
    private val repository = WordRepository(dao)
    var itemValue by mutableStateOf("")

    var isError by mutableStateOf(false)
    var errorMessage by mutableStateOf("")

    private val _isAscending = MutableStateFlow(true)
    val isAscending: StateFlow<Boolean> = _isAscending

    private val words: StateFlow<List<WordItem>> = repository.allWords.stateIn(
        viewModelScope,
        SharingStarted.WhileSubscribed(5000),
        emptyList()
    )

    val sortedWords: StateFlow<List<WordItem>> = combine(words, _isAscending) { wordList, ascending ->
        if (ascending) {
            wordList.sortedBy { it.word.lowercase() }
        } else {
            wordList.sortedByDescending { it.word.lowercase() }
        }
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    fun onValueChange(newValue: String) {
        if (words.value.any { it.word.lowercase() == newValue.lowercase() }) {
            isError = true
            errorMessage = "This word already exists."
        } else {
            isError = false
            errorMessage = ""
        }
        itemValue = newValue
    }

    fun addItem() {
        val formatted = itemValue.lowercase().replaceFirstChar { it.uppercase() }
        viewModelScope.launch {
            try {
                repository.insert(WordItem(word = formatted))
                itemValue = ""
                isError = false
            } catch (e: Exception) {
                isError = true
            }
        }
    }

    fun deleteItem(item: String) {
        viewModelScope.launch {
            repository.delete(WordItem(word = item))
        }
    }
    fun toggleSortOrder() {
        _isAscending.value = !_isAscending.value
    }
}
