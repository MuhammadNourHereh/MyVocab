package com.nourtech.wordpress.myvocab.di

import android.content.Context
import android.content.Context.MODE_PRIVATE
import android.content.SharedPreferences
import androidx.room.Room
import com.nourtech.wordpress.myvocab.db.WordsDatabase
import com.nourtech.wordpress.myvocab.others.KEY_REALTIME_UPDATE
import com.nourtech.wordpress.myvocab.others.SHARED_PREFERENCES_NAME
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

    @Singleton
    @Provides
    fun provideSharedPreferences(@ApplicationContext app: Context) =
        app.getSharedPreferences(SHARED_PREFERENCES_NAME, MODE_PRIVATE)

    @Singleton
    @Provides
    fun provideRealtimeUpdateToggle(sharedPref: SharedPreferences) =
        sharedPref.getBoolean(KEY_REALTIME_UPDATE, false)

}
