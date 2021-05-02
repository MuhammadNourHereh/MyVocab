package com.nourtech.wordpress.myvocab.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.nourtech.wordpress.myvocab.R
import com.nourtech.wordpress.myvocab.databinding.ItemWordRecyclerViewBinding
import com.nourtech.wordpress.myvocab.db.WordEntity
import com.nourtech.wordpress.myvocab.ui.viewmodels.MainViewModel

class WordRecycleViewAdapter(
    private var wordList: List<WordEntity>,
    private val viewModel: MainViewModel
) : RecyclerView.Adapter<ViewHolder>() {

    private lateinit var binding: ItemWordRecyclerViewBinding

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_word_recycler_view, parent, false)
        binding = ItemWordRecyclerViewBinding.bind(view)

        return object : ViewHolder(view) {}
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        binding.tvLang1.text = wordList[position].lang1
        binding.tvLang2.text = wordList[position].lang2
        binding.cbMemorized.isChecked = wordList[position].memorized

        binding.cbMemorized.setOnCheckedChangeListener { _, isChecked ->
            viewModel.check(wordList[position].id, isChecked)
        }

    }

    override fun getItemCount() = wordList.size

    fun getItem(position: Int): WordEntity = wordList[position]

    fun updateList(list: List<WordEntity>) {
        wordList = list
        notifyDataSetChanged()
    }
}