package com.example.mylazycolumnsync.data

import kotlinx.coroutines.flow.Flow

class WordRepository(private val wordDao: WordDao) {
    val allWords: Flow<List<WordItem>> = wordDao.getAllWords()

    suspend fun insert(word: WordItem) = wordDao.insert(word)
    suspend fun delete(word: WordItem) = wordDao.delete(word)
}
