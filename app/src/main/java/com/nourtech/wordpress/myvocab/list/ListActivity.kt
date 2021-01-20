package com.nourtech.wordpress.myvocab.list

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
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
            recyclerView.adapter = WordRecycleViewAdapter(newList, viewModel, supportFragmentManager)
        })

        binding.fab.setOnClickListener {
            val dialog = AddDialog()
            dialog.show(supportFragmentManager,null)
        }



    }


    }

