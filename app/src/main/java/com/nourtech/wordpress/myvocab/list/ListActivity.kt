package com.nourtech.wordpress.myvocab.list

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.nourtech.wordpress.myvocab.R
import com.nourtech.wordpress.myvocab.list.add.AddDialog
import com.nourtech.wordpress.myvocab.databinding.ActivityListBinding
import com.nourtech.wordpress.myvocab.db.WordEntity
import com.nourtech.wordpress.myvocab.db.WordsDatabase
import kotlinx.coroutines.*


@InternalCoroutinesApi
class ListActivity : AppCompatActivity() {
    private lateinit var binding :ActivityListBinding
    private lateinit var recyclerView: RecyclerView
    private val job = Job()
    private val scope = CoroutineScope(job + Dispatchers.IO)
    private lateinit var list  : LiveData<List<WordEntity>>

    @InternalCoroutinesApi
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityListBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setSupportActionBar(findViewById(R.id.toolbar))
        recyclerView = binding.recyclerViewWords
        list = WordsDatabase.getInstance(application).dao.getAllLive()
        list.observe(this, Observer<List<WordEntity>>{ newList ->
            recyclerView.adapter = WordRecycleViewAdapter(newList)
        })

        recyclerView.layoutManager = LinearLayoutManager(this)
        binding.fab.setOnClickListener {
            val dialog = AddDialog()
            dialog.show(supportFragmentManager,null)
        }

    }


}

