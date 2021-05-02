package com.nourtech.wordpress.myvocab.db

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.firebase.firestore.DocumentSnapshot
import com.nourtech.wordpress.myvocab.others.KEY_LANG1
import com.nourtech.wordpress.myvocab.others.KEY_LANG2
import com.nourtech.wordpress.myvocab.others.KEY_MEMORIZED
import com.nourtech.wordpress.myvocab.pojo.Word

@Entity(tableName = "Words_List")
data class WordEntity(
    @PrimaryKey(autoGenerate = true)
    var id: Int = 0,
    @ColumnInfo(name = "first_lang")
    var lang1: String,
    @ColumnInfo(name = "second_lang")
    var lang2: String,
    @ColumnInfo(name = "memorized")
    var memorized: Boolean = false
) {
    constructor(lang1: String, lang2: String) : this(0, lang1, lang2, false)
    constructor(word: Word) : this(word.id, word.lang1, word.lang2, word.memorized)
    constructor(wordDoc: DocumentSnapshot) : this(
        wordDoc.id.toInt(),
        wordDoc[KEY_LANG1] as String,
        wordDoc[KEY_LANG2] as String,
        wordDoc[KEY_MEMORIZED] as Boolean,
    )

}

