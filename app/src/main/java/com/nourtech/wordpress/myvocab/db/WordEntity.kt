package com.nourtech.wordpress.myvocab.db

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "Words_List")
data class WordEntity(
    @PrimaryKey (autoGenerate = true)
    var id: Int,
    @ColumnInfo(name = "first_lang")
    var lang1: String,
    @ColumnInfo(name = "second_lang")
    var lang2: String,
    @ColumnInfo(name = "memorized_lang")
    var memorized: Boolean)