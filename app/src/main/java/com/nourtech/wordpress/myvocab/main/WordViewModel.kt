package com.nourtech.wordpress.myvocab.main

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.nourtech.wordpress.myvocab.db.WordEntity
import com.nourtech.wordpress.myvocab.db.WordsDAO
import kotlinx.coroutines.*

class WordViewModel(private var datasource: WordsDAO, application: Application) : androidx.lifecycle.AndroidViewModel(application) {

    private val job = Job()
    private val ioScope = CoroutineScope(Dispatchers.IO + job)

    var list :MutableList<WordEntity> = listOf(WordEntity(0,"your list is empty", "", false)).toMutableList()
    init {
        GlobalScope.launch {
            list = datasource.getAll().toMutableList()
        }
    }

    private var id = 0
    private val _word = MutableLiveData<WordEntity>()
    val word : LiveData<WordEntity>
        get () = _word


    fun next(){
        if (id == list.size - 1)
            id = 0
        else
            id++
        update()
    }

    fun previous(){
        if (id > 0 )
            id--
        // check if list is empty or has 1 element
        else if (list.size in 0 .. 1)
            return
        else
            id = list.size - 1
        update()
    }

    private fun update(){
        _word.value = list[id]
    }

    fun check(b :Boolean){
        list[id].memorized = b
        ioScope.launch {
            datasource.memorize(list[id])
        }
    }
    fun clear(){
        ioScope.launch {
            datasource.clear()
        }
    }
    fun shuffle(){
        // use Fisher - Yates shuffle algorithm
        for (i in list.size - 1 downTo  0){

            // generate a random number between 0 and i
            val r = (0 .. i).random()

            // swap i and r elements
            val element = list[r]
            list[r] = list[i]
            list[i] = element

            // reset id and update
            id = 0
            update()
        }
    }

}