package com.nourtech.wordpress.myvocab.main

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.speech.tts.TextToSpeech
import android.util.Log
import android.view.Menu
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.nourtech.wordpress.myvocab.R
import com.nourtech.wordpress.myvocab.add.AddDialog
import com.nourtech.wordpress.myvocab.databinding.ActivityMainBinding
import com.nourtech.wordpress.myvocab.list.ListActivity
import com.nourtech.wordpress.myvocab.settings.SettingsActivity
import kotlinx.coroutines.runBlocking
import java.util.*

class MainActivity : AppCompatActivity(), TextToSpeech.OnInitListener {

    private lateinit var viewModel: WordViewModel
    private lateinit var binding: ActivityMainBinding
    private lateinit var tts: TextToSpeech


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // setup view model
        viewModel = WordViewModelFactory(application)
            .create(WordViewModel::class.java)

        // setup data binding
        binding = ActivityMainBinding.inflate(layoutInflater)
        binding.lifecycleOwner = this
        binding.viewModel = viewModel

        // inflate layout
        setContentView(binding.root)

        // init tts
        tts = TextToSpeech(this, this)

        // make textView2 invisible
        binding.textView2.visibility = View.INVISIBLE

        // observe for check box
        viewModel.empty.observe(this) {
            binding.checkBoxMemorized.visibility = if (!it) View.VISIBLE else View.INVISIBLE
            binding.imageButtonSpeaker.visibility = if (!it) View.VISIBLE else View.INVISIBLE
        }

        // set listeners
        setListeners()
    }

    override fun onResume() {
        super.onResume()
        viewModel.updateList()
    }


    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.mainactivity_menu, menu)
        menu?.findItem(R.id.menuItem_add)?.setOnMenuItemClickListener {
            runBlocking {
                val dialog = AddDialog()
                dialog.show(supportFragmentManager, null)
            }

            viewModel.updateList()
            true
        }
        menu?.findItem(R.id.menuItem_filter)?.setOnMenuItemClickListener {
            it.isChecked = !it.isChecked
            viewModel.filter(it.isChecked)
            true
        }
        menu?.findItem(R.id.menuItem_list)?.setOnMenuItemClickListener {
            startActivity(Intent(this, ListActivity::class.java))
            true
        }
        menu?.findItem(R.id.menuItem_clear)?.setOnMenuItemClickListener {
            viewModel.clear()
            viewModel.updateList()
            true
        }
        menu?.findItem(R.id.menuItem_shuffle)?.setOnMenuItemClickListener {
            viewModel.shuffle()
            viewModel.updateList()
            true
        }
        menu?.findItem(R.id.menuItem_settings)?.setOnMenuItemClickListener {
            startActivity(Intent(this, SettingsActivity::class.java))
            true
        }
        menu?.findItem(R.id.menuItem_exit)?.setOnMenuItemClickListener {
            finish()
            true
        }
        return super.onCreateOptionsMenu(menu)
    }

    private fun setListeners() {
        binding.buttonNext.setOnClickListener {
            viewModel.next()
            binding.textView2.visibility = View.INVISIBLE
        }
        binding.buttonPrevious.setOnClickListener {
            viewModel.previous()
            binding.textView2.visibility = View.INVISIBLE
        }
        binding.checkBoxMemorized.setOnCheckedChangeListener { _, isChecked ->
            viewModel.check(
                isChecked
            )
        }
        binding.buttonShow.setOnClickListener {
            binding.textView2.visibility = View.VISIBLE
        }
        binding.imageButtonSpeaker.setOnClickListener {
            speakOut()
        }
    }

    // for tts
    override fun onInit(status: Int) {

        if (status == TextToSpeech.SUCCESS) {
            val result = tts.setLanguage(Locale.US)

            if (result == TextToSpeech.LANG_MISSING_DATA
                || result == TextToSpeech.LANG_NOT_SUPPORTED
            ) {
                Log.e("TTS", "Language is not supported")
            } else {
                binding.imageButtonSpeaker.isEnabled = true
            }
        } else {
            Log.e("TTS", "Initialization Failed")
        }
    }


    private fun speakOut() {
        val text: String = viewModel.word.value?.lang1.toString()
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            tts.speak(text,TextToSpeech.QUEUE_FLUSH,null,null)
        } else {
            tts.speak(text, TextToSpeech.QUEUE_FLUSH, null)
        }
    }
}