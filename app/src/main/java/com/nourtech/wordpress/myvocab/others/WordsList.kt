package com.nourtech.wordpress.myvocab.others

import com.nourtech.wordpress.myvocab.db.WordEntity
import com.nourtech.wordpress.myvocab.pojo.Word

class WordsList {

    private var list: MutableList<Word> = mutableListOf()
    private var index = 0
    var filter = false

    fun next() {
        when {
            // check if list is empty or has 1 element
            (list.size in 0..1) -> return
            (index >= list.size - 1) -> index = 0
            else -> index++
        }
    }

    fun previous() {
        when {
            (index > 0) -> index--
            // check if list is empty or has 1 element
            (list.size in 0..1) -> return
            else -> index = list.size - 1
        }
    }

    fun check(b: Boolean) {
        if (list.isEmpty())
            return

        list[index].memorized = b
        val item = list[index]

        if (filter && b) {
            list.remove(item)
            previous()
        }
    }

    fun clear() {
        list.clear()
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
        }
    }

    fun isEmpty() = list.isEmpty()
    fun isNotEmpty() = list.isNotEmpty()
    fun get() = list[index]
    fun setList(list: List<WordEntity>) {

        this.list = list.map {
            Word(it.id, it.lang1, it.lang2, it.memorized)
        } as MutableList<Word>

        index = 0
    }

}