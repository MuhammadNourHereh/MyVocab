package com.nourtech.wordpress.myvocab.di

import android.content.Context
import androidx.room.Room
import com.nourtech.wordpress.myvocab.db.WordsDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object Module {

    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext app: Context) =
        Room.databaseBuilder(
            app.applicationContext,
            WordsDatabase::class.java,
            "words"
        )
            .fallbackToDestructiveMigration()
            .build()

    @Provides
    @Singleton
    fun provideDao(db: WordsDatabase) =
        db.dao

}