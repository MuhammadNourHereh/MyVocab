package com.nourtech.wordpress.myvocab.db

import androidx.room.Database
import androidx.room.RoomDatabase


@Database(entities = [WordEntity::class], version = 1, exportSchema = false)
abstract class WordsDatabase : RoomDatabase() {
    abstract val dao: WordsDAO
}


