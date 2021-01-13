package com.nourtech.wordpress.myvocab.main

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.nourtech.wordpress.myvocab.db.WordsDatabase
import kotlinx.coroutines.InternalCoroutinesApi

class WordViewModelFactory(private val application: Application)
    : ViewModelProvider.Factory {

    @InternalCoroutinesApi
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(WordViewModel::class.java)) {

            return WordViewModel(WordsDatabase.getInstance(application).dao, application) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }

}