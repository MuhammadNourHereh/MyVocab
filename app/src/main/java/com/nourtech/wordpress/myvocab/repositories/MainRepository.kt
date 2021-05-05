package com.nourtech.wordpress.myvocab.repositories

import androidx.lifecycle.LiveData
import com.nourtech.wordpress.myvocab.db.WordEntity
import com.nourtech.wordpress.myvocab.db.WordsDAO
import com.nourtech.wordpress.myvocab.firestore.FirestoreWrapper
import javax.inject.Inject

class MainRepository @Inject constructor(
    private val dao: WordsDAO,
    private val firestoreWrapper: FirestoreWrapper
) : AbstractRepo {

    override fun getAllWords(): List<WordEntity> =
        dao.getAll()

    override fun getAllLiveWords(): LiveData<List<WordEntity>> =
        dao.getAllLive()

    override fun getAllFilteredWords(): List<WordEntity> =
        dao.getAllFiltered()

    override fun addWord(word: WordEntity) {
        word.id = dao.add(word).toInt()
        firestoreWrapper.addWord(word)
    }

    override fun memorizeWord(word: WordEntity) {
        dao.memorize(word)
        firestoreWrapper.memorizeWord(word)
    }

    override fun deleteWord(word: WordEntity) {
        dao.delete(word)
        firestoreWrapper.deleteWord(word)
    }

    override fun clearWords() {
        dao.clear()
    }
}