package com.example.mylazycolumnsync.data

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface WordDao {
    @Query("SELECT * FROM word_table ORDER BY word ASC")
    fun getAllWords(): Flow<List<WordItem>>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(word: WordItem)

    @Delete
    suspend fun delete(word: WordItem)

    @Query("DELETE FROM word_table")
    suspend fun deleteAll()
}
