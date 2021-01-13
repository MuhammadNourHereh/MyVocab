package com.nourtech.wordpress.myvocab.db

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface WordsDAO   {

    @Query("SELECT * FROM Words_List")
    fun getAll(): List<WordEntity>

    @Query("SELECT * FROM Words_List")
    fun getAllLive(): LiveData<List<WordEntity>>

    @Insert
    fun add(word : WordEntity)

    @Update
    fun memorize(word: WordEntity)

    @Delete
    fun delete(word: WordEntity)

    @Query("DELETE FROM Words_List")
    fun clear()
}