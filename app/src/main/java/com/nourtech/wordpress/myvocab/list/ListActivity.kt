package com.nourtech.wordpress.myvocab.list

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.nourtech.wordpress.myvocab.R
import com.nourtech.wordpress.myvocab.add.AddDialog
import com.nourtech.wordpress.myvocab.databinding.ActivityListBinding


class ListActivity : AppCompatActivity() {
    private lateinit var binding :ActivityListBinding
    private lateinit var recyclerView: RecyclerView
    private lateinit var viewModel: ListViewModel


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // setup data binding
        binding = ActivityListBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // set action bar
        setSupportActionBar(findViewById(R.id.toolbar))

        // init view model
        viewModel = ListViewModel.ViewModelFactory(application)
            .create(ListViewModel::class.java)

        // set recyclerView
        recyclerView = binding.recyclerViewWords
        recyclerView.layoutManager = LinearLayoutManager(this)

        // refresh recycler view as list changed
        viewModel.list.observe(this, { newList ->
            recyclerView.adapter =
                WordRecycleViewAdapter(newList, viewModel, supportFragmentManager)
        })

        binding.fab.setOnClickListener {
            val dialog = AddDialog()
            dialog.show(supportFragmentManager, null)
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
                Toast.makeText(baseContext, "word " + word.lang1 + " deleted", Toast.LENGTH_SHORT)
                    .show()
            }
        }
        ItemTouchHelper(callback).attachToRecyclerView(binding.recyclerViewWords)
    }
}

