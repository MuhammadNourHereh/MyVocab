package com.nourtech.wordpress.myvocab.list

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.LiveData
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.nourtech.wordpress.myvocab.R
import com.nourtech.wordpress.myvocab.add.AddDialog
import com.nourtech.wordpress.myvocab.databinding.ActivityListBinding
import com.nourtech.wordpress.myvocab.db.WordEntity
import com.nourtech.wordpress.myvocab.db.WordsDatabase
import kotlinx.coroutines.*


@InternalCoroutinesApi
class ListActivity : AppCompatActivity() {
    private lateinit var binding :ActivityListBinding
    private lateinit var recyclerView: RecyclerView
    private lateinit var viewModel: ListViewModel

    @InternalCoroutinesApi
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // setup data binding
        binding = ActivityListBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // set action bar
        setSupportActionBar(findViewById(R.id.toolbar))
        recyclerView = binding.recyclerViewWords

        // init view model
        viewModel = ListViewModel.ViewModelFactory(application)
            .create(ListViewModel::class.java)

        // refresh recycler view as list changed
        viewModel.list.observe(this, { newList ->
            recyclerView.adapter = WordRecycleViewAdapter(newList, viewModel, applicationContext)
        })

        recyclerView.layoutManager = LinearLayoutManager(this)
        binding.fab.setOnClickListener {
            val dialog = AddDialog()
            dialog.show(supportFragmentManager,null)
        }

    }


}

