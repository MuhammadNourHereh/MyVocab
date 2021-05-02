package com.nourtech.wordpress.myvocab.db

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface WordsDAO   {

    @Query("SELECT * FROM Words_List")
    fun getAll(): List<WordEntity>

    @Query("SELECT * FROM Words_List")
    fun getAllLive(): LiveData<List<WordEntity>>

    @Query("SELECT * FROM Words_List WHERE memorized = 0")
    fun getAllFiltered(): List<WordEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun add(word: WordEntity): Long

    @Update
    fun memorize(word: WordEntity)

    @Delete
    fun delete(word: WordEntity)

    @Query("DELETE FROM Words_List")
    fun clear()
}