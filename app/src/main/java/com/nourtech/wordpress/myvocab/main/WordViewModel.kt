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
    private var filter = false
    private lateinit var list :MutableList<WordEntity>
    init {
        updateList()
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
        if (list.isNotEmpty())
            _word.value = list[id]
        else
            _word.value = WordEntity(0, "your list is empty",
                "your list is empty", false)
    }

    fun check(b :Boolean){
        if (list.isEmpty())
            return
        list[id].memorized = b
        val item = list[id]
        ioScope.launch {
            datasource.memorize(item)
        }
        if (filter && b){
            list.remove(list[id])
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
    fun filter(checked: Boolean){
        filter = checked
        // remove all memorized words
        val _list   = listOf<WordEntity>().toMutableList()
        if (checked){
            for (x in list){
                if (!x.memorized){
                    _list.add(x)
                }
            }
            list = _list
        }

        // show all words
        else{
            updateList()
        }
        id = 0
        update()
    }
     fun updateList(){
        GlobalScope.launch {
            list = datasource.getAll().toMutableList()
        }
    }
}