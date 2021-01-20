package com.nourtech.wordpress.myvocab.main

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.nourtech.wordpress.myvocab.db.WordEntity
import com.nourtech.wordpress.myvocab.db.WordsDAO
import kotlinx.coroutines.*

class WordViewModel(private var datasource: WordsDAO, application: Application) :
    androidx.lifecycle.AndroidViewModel(application) {

    private val ioScope = CoroutineScope(Dispatchers.IO)
    private var filter = false
    private lateinit var list: MutableList<WordEntity>

    // is list empty ?
    private val _empty = MutableLiveData<Boolean>()
    val empty: LiveData<Boolean>
        get() {
            return _empty
        }

    private var index = 0 // current word index
    private val _word = MutableLiveData<WordEntity>()
    val word: LiveData<WordEntity>
        get() = _word

    init {
        updateList()
    }

    private fun update() {
        if (list.isNotEmpty()) {
            _word.value = list[index]
            _empty.value = list.isEmpty()
        } else {
            _word.value = WordEntity.EmptyWord
            _empty.value = list.isEmpty()
        }
    }

    fun updateList() {
        runBlocking {
            withContext(Dispatchers.Default) {
                list = if (filter)
                    datasource.getAllFiltered().toMutableList()
                else
                    datasource.getAll().toMutableList()
            }

            update()
        }

    }

    fun next() {
        when {
            // check if list is empty or has 1 element
            (list.size in 0..1) -> {
            }
            (index <= list.size - 1) -> index = 0
            else -> index++

        }
        update()
    }

    fun previous() {
        when {
            (index > 0) -> index--
            // check if list is empty or has 1 element
            (list.size in 0..1) -> {
            }
            else -> index = list.size - 1
        }
        update()
        _empty.value = list.isEmpty()
    }


    fun check(b: Boolean) {
        if (list.isEmpty())
            return
        list[index].memorized = b
        val item = list[index]
        ioScope.launch {
            datasource.memorize(item)
        }
        if (filter && b) {
            list.remove(list[index])
        }
    }

    fun clear() {
        ioScope.launch {
            datasource.clear()
        }
    }

    fun shuffle() {
        // use Fisher - Yates shuffle algorithm
        for (i in list.size - 1 downTo 0) {

            // generate a random number between 0 and i
            val r = (0..i).random()

            // swap i and r elements
            val element = list[r]
            list[r] = list[i]
            list[i] = element

            // reset id and update
            index = 0
            update()
        }
    }

    fun filter(checked: Boolean) {
        filter = checked
        // remove all memorized words
        if (checked) {
            val shiftList = listOf<WordEntity>().toMutableList()
            for (x in list) {
                if (!x.memorized) {
                    shiftList.add(x)
                }
            }
            list = shiftList
        }

        // show all words
        else {
            updateList()
        }
        index = 0
        update()
    }

}