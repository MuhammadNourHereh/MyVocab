package com.nourtech.wordpress.myvocab.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase


@Database(entities = [WordEntity::class], version = 1, exportSchema = false)
abstract class WordsDatabase : RoomDatabase() {

    abstract val dao: WordsDAO

    companion object {
        @Volatile
        private var Instance: WordsDatabase? = null

        @Synchronized
        fun getInstance(context: Context): WordsDatabase {

            var instance = Instance
            if (instance == null) {
                instance = Room.databaseBuilder(
                    context.applicationContext,
                    WordsDatabase::class.java,
                    "words"
                )
                    .fallbackToDestructiveMigration()
                    .build()
                Instance = instance
            }
            return instance
        }
    }
}


