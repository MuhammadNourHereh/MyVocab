package com.nourtech.wordpress.myvocab.list

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.nourtech.wordpress.myvocab.R
import com.nourtech.wordpress.myvocab.db.WordEntity

class WordRecycleViewAdapter(
    private var wordList: List<WordEntity>
) : RecyclerView.Adapter<WordRecycleViewAdapter.WordViewHolder>() {

    class WordViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val lang1TextView: TextView = itemView.findViewById(R.id.lang1TextView)
        val lang2TextView: TextView = itemView.findViewById(R.id.lang2TextView)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): WordViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.content_word_recycler_view, parent, false)
        return WordViewHolder(view)
    }

    override fun onBindViewHolder(holder: WordViewHolder, position: Int) {
        holder.lang1TextView.text = wordList[position].lang1
        holder.lang2TextView.text = wordList[position].lang2
    }

    override fun getItemCount() = wordList.size

    fun getItem(position: Int): WordEntity = wordList[position]

    fun updateList(list: List<WordEntity>) {
        wordList = list
        notifyDataSetChanged()
    }
}