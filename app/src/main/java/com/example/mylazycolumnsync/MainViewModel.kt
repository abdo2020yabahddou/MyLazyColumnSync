package com.example.mylazycolumnsync

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel

class MainViewModel : ViewModel() {
    var itemValue by mutableStateOf("")
    var itemList by mutableStateOf(
        mutableListOf("Ethics", "Integrity", "Morality", "Sincerity", "Truthfulness", "Uprightness")
    )

    var isError by mutableStateOf(false)
    var errorMessage by mutableStateOf("")

    var isAscending by mutableStateOf(true)

    fun onValueChange(newValue: String) {
        if (itemList.contains(newValue)) {
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
        if (itemList.any { it.equals(formatted, ignoreCase = true) }) {
            isError = true
        } else {
            itemList = (itemList + formatted).sortedWith(compareBy { it.lowercase() }).toMutableList()
            if (!isAscending) itemList = itemList.reversed().toMutableList()
            itemValue = ""
        }
    }

    fun deleteItem(index: Int) {
        if (index in itemList.indices) {
            itemList = itemList.toMutableList().apply { removeAt(index) }
        }
    }

    fun toggleSortOrder() {
        isAscending = !isAscending
        itemList = if (isAscending) {
            itemList.sorted().toMutableList()
        } else {
            itemList.sortedDescending().toMutableList()
        }
    }
}
