package com.nourtech.wordpress.myvocab.db;

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import kotlinx.coroutines.*
import kotlinx.coroutines.internal.synchronized


@Database(entities = [WordEntity::class], version = 1)
abstract class WordsDatabase : RoomDatabase() {

       
        abstract val dao: WordsDAO
        companion object{
            @Volatile
            private var Instance: WordsDatabase? = null

            @InternalCoroutinesApi
             fun getInstance(context: Context) : WordsDatabase {
                synchronized(this){
                    var instance = Instance
                    if (instance == null){
                        instance = Room.databaseBuilder(context.applicationContext, WordsDatabase::class.java, "words")
                            .fallbackToDestructiveMigration()
                            .build()
                        Instance = instance
                    }
                    val job = Job()
                    val scope = CoroutineScope(Dispatchers.IO + job)
                    scope.launch {
                        if (instance.dao.getAll().isEmpty()) {
                            instance.dao.add(WordEntity(0, "", "", false))
                        }
                    }
                    return instance
                }

            }
        }
}

