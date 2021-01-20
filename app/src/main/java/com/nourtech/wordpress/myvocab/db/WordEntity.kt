package com.nourtech.wordpress.myvocab.db

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

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

    companion object {
        val EmptyWord = WordEntity("Word list is empty", "Word list is empty")
    }

}

