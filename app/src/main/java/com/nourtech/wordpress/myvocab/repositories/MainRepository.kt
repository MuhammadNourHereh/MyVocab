package com.nourtech.wordpress.myvocab.repositories

import android.content.SharedPreferences
import androidx.lifecycle.LiveData
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.nourtech.wordpress.myvocab.db.WordEntity
import com.nourtech.wordpress.myvocab.db.WordsDAO
import com.nourtech.wordpress.myvocab.others.KEY_LANG1
import com.nourtech.wordpress.myvocab.others.KEY_LANG2
import com.nourtech.wordpress.myvocab.others.KEY_MEMORIZED
import com.nourtech.wordpress.myvocab.others.KEY_REALTIME_UPDATE
import javax.inject.Inject

class MainRepository @Inject constructor(
    private val dao: WordsDAO,
    private val sharedPref: SharedPreferences
) : AbstractRepo {

    private val firestore = FirebaseFirestore.getInstance()
    private val auth = FirebaseAuth.getInstance()

    override fun getAllWords(): List<WordEntity> =
        dao.getAll()

    override fun getAllLiveWords(): LiveData<List<WordEntity>> =
        dao.getAllLive()

    override fun getAllFilteredWords(): List<WordEntity> =
        dao.getAllFiltered()

    override fun addWord(word: WordEntity) {
        val id = dao.add(word)
        val jit = sharedPref.getBoolean(KEY_REALTIME_UPDATE, false)
        if (jit)
            auth.currentUser?.let {
                val docPath = "/users/${it.uid}/words/${id}"
                val wordMap = hashMapOf<String, Any>(
                    Pair(KEY_LANG1, word.lang1),
                    Pair(KEY_LANG2, word.lang2),
                    Pair(KEY_MEMORIZED, word.memorized)
                )
                firestore.document(docPath).set(wordMap)
            }
    }

    override fun memorizeWord(word: WordEntity) {
        dao.memorize(word)
        val jit = sharedPref.getBoolean(KEY_REALTIME_UPDATE, false)
        if (jit)
            auth.currentUser?.let {
                val docPath = "/users/${it.uid}/words/${word.id}"
                val wordMap = hashMapOf<String, Any>(
                    Pair(KEY_LANG1, word.lang1),
                    Pair(KEY_LANG2, word.lang2),
                    Pair(KEY_MEMORIZED, word.memorized)
                )
                firestore.document(docPath).set(wordMap)
            }
    }

    override fun deleteWord(word: WordEntity) {
        dao.delete(word)
        val jit = sharedPref.getBoolean(KEY_REALTIME_UPDATE, false)
        if (jit) {
            auth.currentUser?.let {
                val docPath = "/users/${it.uid}/words/${word.id}"
                firestore.document(docPath).delete()
            }
        }
    }

    override fun clearWords() {
        dao.clear()
        val jit = sharedPref.getBoolean(KEY_REALTIME_UPDATE, false)
        if (jit) {
            auth.currentUser?.let {
                val docPath = "/users/${it.uid}/words"
                firestore.collection(docPath)
            }
        }
    }
}