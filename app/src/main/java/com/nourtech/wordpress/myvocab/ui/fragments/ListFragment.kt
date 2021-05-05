package com.nourtech.wordpress.myvocab.ui.fragments

import android.content.DialogInterface
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar
import com.nourtech.wordpress.myvocab.R
import com.nourtech.wordpress.myvocab.adapters.WordRecycleViewAdapter
import com.nourtech.wordpress.myvocab.databinding.FragmentListBinding
import com.nourtech.wordpress.myvocab.db.WordEntity
import com.nourtech.wordpress.myvocab.dialogs.AddDialog
import com.nourtech.wordpress.myvocab.ui.viewmodels.MainViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

@AndroidEntryPoint
class ListFragment : Fragment(R.layout.fragment_list) {

    private lateinit var binding: FragmentListBinding
    private lateinit var recyclerView: RecyclerView

    private val viewModel: MainViewModel by activityViewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentListBinding.bind(view)
        binding.lifecycleOwner = this

        // set recyclerView
        recyclerView = binding.recyclerViewWords
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        refreshList()

        binding.fab.setOnClickListener {
            // extends the dialog to refresh recycler view as list changed
            class Dialog : AddDialog() {
                override fun onDismiss(dialog: DialogInterface) {
                    super.onDismiss(dialog)
                    if (isNewWordAdd)
                        refreshList()
                }
            }
            Dialog().show(parentFragmentManager, null)
        }

        // set swipe for recycler view
        setSwipe()
    }

    private fun refreshList() {
        CoroutineScope(Dispatchers.IO).launch {
            val words = viewModel.getAllWords() as MutableList<WordEntity>
            withContext(Dispatchers.Main) {
                recyclerView.adapter = WordRecycleViewAdapter(words, viewModel)
            }
        }
    }

    private fun setSwipe() {
        ItemTouchHelper(simpleCallback).attachToRecyclerView(binding.recyclerViewWords)
    }

    private val simpleCallback = object : ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.RIGHT) {
        override fun onMove(
            recyclerView: RecyclerView,
            viewHolder: RecyclerView.ViewHolder,
            target: RecyclerView.ViewHolder
        ): Boolean = false

        override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {

            // cast adapter to WordRecycleViewAdapter
            val adapter = (binding.recyclerViewWords.adapter as WordRecycleViewAdapter)
            val word = adapter.getItem(viewHolder.adapterPosition)
            viewModel.deleteWord(word)
            adapter.updateList(viewHolder.adapterPosition)
            Snackbar.make(
                requireView(),
                "word " + word.lang1 + " deleted",
                Snackbar.LENGTH_SHORT
            ).show()
        }
    }


}