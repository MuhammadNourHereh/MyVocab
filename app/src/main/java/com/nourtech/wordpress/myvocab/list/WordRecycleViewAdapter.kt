package com.nourtech.wordpress.myvocab.list

import android.app.AlertDialog
import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.FragmentManager
import androidx.recyclerview.widget.RecyclerView
import com.nourtech.wordpress.myvocab.R
import com.nourtech.wordpress.myvocab.db.WordEntity
import kotlinx.coroutines.InternalCoroutinesApi


@InternalCoroutinesApi
class WordRecycleViewAdapter(
    private val wordList: List<WordEntity>,
    private val viewModel: ListViewModel,
    private val supportFragmentManager: FragmentManager
) : RecyclerView.Adapter<WordRecycleViewAdapter.WordViewHolder>() {

    class WordViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
         val lang1TextView : TextView = itemView.findViewById(R.id.lang1TextView)
         val lang2TextView : TextView = itemView.findViewById(R.id.lang2TextView)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): WordViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.content_word_recycler_view, parent, false)
        return WordViewHolder(view)
    }

    override fun onBindViewHolder(holder: WordViewHolder, position: Int) {
        holder.lang1TextView.text = wordList[position].lang1
        holder.lang2TextView.text = wordList[position].lang2
        holder.lang1TextView.setOnLongClickListener {
            deleteDialog(position)
            true
        }
        holder.lang2TextView.setOnLongClickListener {
            deleteDialog(position)
            true
        }
    }

    private fun deleteDialog(position: Int) {

        object : DialogFragment() {

            override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
                return activity?.let {
                    // Use the Builder class for convenient dialog construction
                    val builder = AlertDialog.Builder(it)
                    builder.setMessage("Delete ?")
                        .setPositiveButton("delete"
                        ) { _, _ ->
                            viewModel.deleteWord(wordList[position])
                        }
                        .setNegativeButton(R.string.cancel
                        ) { _, _ ->
                            dismiss()
                        }
                    // Create the AlertDialog object and return it
                    builder.create()
                } ?: throw IllegalStateException("Activity cannot be null")
            }
        }.show(supportFragmentManager, null)


    }

    override fun getItemCount() = wordList.size

}