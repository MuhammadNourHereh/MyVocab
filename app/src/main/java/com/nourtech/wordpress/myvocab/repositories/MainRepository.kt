package com.nourtech.wordpress.myvocab.repositories

import androidx.lifecycle.LiveData
import com.nourtech.wordpress.myvocab.db.WordEntity
import com.nourtech.wordpress.myvocab.db.WordsDAO
import javax.inject.Inject

class MainRepository @Inject constructor(private val dao: WordsDAO) : AbstractRepo {
    override fun getAllWords(): List<WordEntity> =
        dao.getAll()

    override fun getAllLiveWords(): LiveData<List<WordEntity>> =
        dao.getAllLive()

    override fun getAllFilteredWords(): List<WordEntity> =
        dao.getAllFiltered()

    override fun addWord(word: WordEntity) {
        dao.add(word)
    }

    override fun memorizeWord(word: WordEntity) {
        dao.memorize(word)
    }

    override fun deleteWord(word: WordEntity) {
        dao.delete(word)
    }

    override fun clearWords() {
        dao.clear()
    }
}