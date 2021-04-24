package com.nourtech.wordpress.myvocab.ui.fragments

import android.os.Build
import android.os.Bundle
import android.speech.tts.TextToSpeech
import android.util.Log
import android.view.View
import androidx.fragment.app.Fragment
import com.nourtech.wordpress.myvocab.R
import com.nourtech.wordpress.myvocab.databinding.FragmentWordsBinding
import com.nourtech.wordpress.myvocab.ui.viewModels.WordViewModel
import dagger.hilt.android.AndroidEntryPoint
import java.util.*
import javax.inject.Inject

@AndroidEntryPoint
class WordsFragment : Fragment(R.layout.fragment_words) {

    private lateinit var binding: FragmentWordsBinding


    private lateinit var tts: TextToSpeech

    @Inject
    lateinit var viewModel: WordViewModel

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // setup data binding
        binding = FragmentWordsBinding.bind(view)
        binding.lifecycleOwner = this
        binding.viewModel = viewModel

        // init tts
        tts = TextToSpeech(requireContext(), onInitListener)

        // make textView2 invisible
        binding.textView2.visibility = View.INVISIBLE

        // observe for check box
        viewModel.empty.observe(viewLifecycleOwner) {
            binding.checkBoxMemorized.visibility = if (!it) View.VISIBLE else View.INVISIBLE
            binding.imageButtonSpeaker.visibility = if (!it) View.VISIBLE else View.INVISIBLE
        }

        // set listeners
        setListeners()
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
    private val onInitListener = TextToSpeech.OnInitListener { status ->
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
            tts.speak(text, TextToSpeech.QUEUE_FLUSH, null, null)
        } else {
            tts.speak(text, TextToSpeech.QUEUE_FLUSH, null)
        }
    }
}