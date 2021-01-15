package com.nourtech.wordpress.myvocab.list

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.nourtech.wordpress.myvocab.db.WordEntity
import com.nourtech.wordpress.myvocab.db.WordsDAO
import com.nourtech.wordpress.myvocab.db.WordsDatabase
import kotlinx.coroutines.*

@InternalCoroutinesApi
class ListViewModel(private var datasource: WordsDAO, application: Application) : ViewModel() {


    // view model factory
    class ViewModelFactory(
        private val application: Application
    ) : ViewModelProvider.Factory {

        @InternalCoroutinesApi
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(ListViewModel::class.java)) {

                return ListViewModel(WordsDatabase.getInstance(application).dao, application) as T
            }
            throw IllegalArgumentException("Unknown ViewModel class")
        }
    }

    // for access io thread
    private val job = Job()
    private val ioScope = CoroutineScope(Dispatchers.IO + job)

    // live list of words
    val list: LiveData<List<WordEntity>> = WordsDatabase
        .getInstance(application).dao.getAllLive()

    fun deleteWord(word: WordEntity) {
        ioScope.launch {
            datasource.delete(word)
        }
    }

}