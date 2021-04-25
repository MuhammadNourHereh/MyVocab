package com.nourtech.wordpress.myvocab.ui.fragments

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
import com.nourtech.wordpress.myvocab.dialogs.AddDialog
import com.nourtech.wordpress.myvocab.ui.viewmodels.MainViewModel
import dagger.hilt.android.AndroidEntryPoint

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

        // refresh recycler view as list changed
        viewModel.lifeWordsList().observe(viewLifecycleOwner) {
            if (recyclerView.adapter == null)
                recyclerView.adapter = WordRecycleViewAdapter(it, viewModel)
            else
                (recyclerView.adapter as WordRecycleViewAdapter).updateList(it)
        }

        binding.fab.setOnClickListener {
            val dialog = AddDialog()
            dialog.show(parentFragmentManager, null)
        }

        // set swipe for recycler view
        setSwipe()
    }

    private fun setSwipe() {
        val callback = object : ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.RIGHT) {
            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean = false

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {

                val word = (binding.recyclerViewWords.adapter as WordRecycleViewAdapter)
                    .getItem(viewHolder.adapterPosition)
                viewModel.deleteWord(word)
                binding.recyclerViewWords.adapter?.notifyDataSetChanged()
                Snackbar.make(
                    requireView(),
                    "word " + word.lang1 + " deleted",
                    Snackbar.LENGTH_SHORT
                )
                    .show()
            }
        }
        ItemTouchHelper(callback).attachToRecyclerView(binding.recyclerViewWords)
    }
}