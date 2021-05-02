package com.nourtech.wordpress.myvocab.ui.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.nourtech.wordpress.myvocab.db.WordEntity
import com.nourtech.wordpress.myvocab.others.EmptyWord
import com.nourtech.wordpress.myvocab.others.WordsList
import com.nourtech.wordpress.myvocab.pojo.Word
import com.nourtech.wordpress.myvocab.repositories.MainRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private var repo: MainRepository
) : ViewModel() {

    private val wordsList = WordsList()
    private val ioScope = CoroutineScope(Dispatchers.IO)

    // is list empty ?
    private val _empty = MutableLiveData<Boolean>()
    val empty: LiveData<Boolean>
        get() {
            return _empty
        }

    private val _word = MutableLiveData<Word>()
    val word: LiveData<Word>
        get() = _word

    init {
        updateList()
    }

    fun deleteWord(word: WordEntity) {
        ioScope.launch {
            repo.deleteWord(word)
            updateList()
        }

    }

    fun addWord(wordEntity: WordEntity) {
        ioScope.launch {
            repo.addWord(wordEntity)
            updateList()
        }
    }

    fun lifeWordsList() = repo.getAllLiveWords()
    fun getAllWords() = repo.getAllWords()
    fun next() {
        wordsList.next()
        update()
    }

    fun previous() {
        wordsList.previous()
        update()
    }

    fun check(b: Boolean) {
        if (wordsList.isEmpty())
            return

        val item = WordEntity(wordsList.get())
        item.memorized = b

        wordsList.check(b)

        // update db
        ioScope.launch {
            repo.memorizeWord(item)
        }

    }

    fun check(wordId: Int, checked: Boolean) {

        val item = wordsList.get(wordId)?.let { WordEntity(it) } ?: return
        item.memorized = checked

        wordsList.check(checked)

        // update db
        ioScope.launch {
            repo.memorizeWord(item)
            updateList()
        }

    }

    fun clear() {

        wordsList.clear()
        ioScope.launch {
            repo.clearWords()
        }
        updateList()
    }

    fun shuffle() {
        wordsList.shuffle()
        update()
    }

    fun filter(checked: Boolean) {
        wordsList.filter = checked
        updateList()
    }

    private fun updateList() {
        ioScope.launch {

            val list = if (wordsList.filter)
                repo.getAllFilteredWords().toMutableList()
            else
                repo.getAllWords().toList()

            wordsList.setList(list)

            withContext(Dispatchers.Main) {
                update()
            }
        }
    }

    private fun update() {
        if (wordsList.isNotEmpty()) {
            _word.postValue(wordsList.get())
        } else {
            _word.postValue(EmptyWord)
        }

        _empty.value = wordsList.isEmpty()
    }
}