package com.nourtech.wordpress.myvocab.repositories

import androidx.lifecycle.LiveData
import com.nourtech.wordpress.myvocab.db.WordEntity


interface AbstractRepo {
    fun getAllWords(): List<WordEntity>

    fun getAllLiveWords(): LiveData<List<WordEntity>>

    fun getAllFilteredWords(): List<WordEntity>

    fun addWord(word: WordEntity)

    fun memorizeWord(word: WordEntity)

    fun deleteWord(word: WordEntity)

    fun clearWords()

}