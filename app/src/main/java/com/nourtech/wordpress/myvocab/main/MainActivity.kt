package com.nourtech.wordpress.myvocab.main

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.View
import com.nourtech.wordpress.myvocab.R
import com.nourtech.wordpress.myvocab.list.ListActivity
import com.nourtech.wordpress.myvocab.databinding.ActivityMainBinding
import com.nourtech.wordpress.myvocab.list.add.AddDialog
import kotlinx.coroutines.InternalCoroutinesApi

class MainActivity : AppCompatActivity() {

    private lateinit var viewModel: WordViewModel
    private lateinit var binding:ActivityMainBinding

    @InternalCoroutinesApi
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // setup view model
        viewModel= WordViewModelFactory(application)
            .create(WordViewModel::class.java)

        // setup data binding
        binding = ActivityMainBinding.inflate(layoutInflater)
        binding.lifecycleOwner = this
        binding.viewModel = viewModel

        // inflate layout
        setContentView(binding.root)

        // make textView2 invisible
        binding.textView2.visibility = View.INVISIBLE

        // set listeners
        setListeners()
    }

    @InternalCoroutinesApi
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.mainactivity_meny, menu)
        menu?.findItem(R.id.menuItem_add)?.setOnMenuItemClickListener {
            val dialog = AddDialog()
            dialog.show(supportFragmentManager,null)
            true
        }
        menu?.findItem(R.id.menuItem_list)?.setOnMenuItemClickListener {
            startActivity(Intent(this, ListActivity::class.java))
            true
        }
        menu?.findItem(R.id.menuItem_clear)?.setOnMenuItemClickListener {
            viewModel.clear()
            true
        }
        menu?.findItem(R.id.menuItem_shuffle)?.setOnMenuItemClickListener {
            viewModel.shuffle()
            true
        }
        menu?.findItem(R.id.menuItem_exit)?.setOnMenuItemClickListener {
            finish()
            true
        }
        return super.onCreateOptionsMenu(menu)
    }
    private fun setListeners(){
        binding.buttonNext.setOnClickListener {
            viewModel.next()
            binding.textView2.visibility = View.INVISIBLE
        }
        binding.buttonPrevious.setOnClickListener {
            viewModel.previous()
            binding.textView2.visibility = View.INVISIBLE
        }
        binding.checkBoxMemorized.setOnCheckedChangeListener{
                _, isChecked -> viewModel.check(isChecked)
        }
        binding.buttonShow.setOnClickListener {
            binding.textView2.visibility = View.VISIBLE
        }
    }
}