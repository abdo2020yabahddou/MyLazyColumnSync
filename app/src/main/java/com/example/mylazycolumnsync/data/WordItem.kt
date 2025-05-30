package com.example.mylazycolumnsync.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "word_table")
data class WordItem(
    @PrimaryKey val word: String
)

