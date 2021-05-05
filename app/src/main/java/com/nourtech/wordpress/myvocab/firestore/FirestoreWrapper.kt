package com.nourtech.wordpress.myvocab.firestore

import android.content.SharedPreferences
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.nourtech.wordpress.myvocab.db.WordEntity
import com.nourtech.wordpress.myvocab.others.KEY_LANG1
import com.nourtech.wordpress.myvocab.others.KEY_LANG2
import com.nourtech.wordpress.myvocab.others.KEY_MEMORIZED
import com.nourtech.wordpress.myvocab.others.KEY_REALTIME_UPDATE

class FirestoreWrapper constructor(private val sharedPref: SharedPreferences) {

    private val firestore = FirebaseFirestore.getInstance()
    private val auth = FirebaseAuth.getInstance()

    fun addWord(word: WordEntity) {
        if (getNotRealtimeUpdate())
            return
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

    fun memorizeWord(word: WordEntity) {
        if (getNotRealtimeUpdate())
            return
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

    fun deleteWord(word: WordEntity) {
        if (getNotRealtimeUpdate())
            return
        auth.currentUser?.let {
            val docPath = "/users/${it.uid}/words/${word.id}"
            firestore.document(docPath).delete()
        }
    }

    private fun getNotRealtimeUpdate() = !sharedPref.getBoolean(KEY_REALTIME_UPDATE, false)
}