package com.nourtech.wordpress.myvocab.ui.fragments

import android.os.Build
import android.os.Bundle
import android.speech.tts.TextToSpeech
import android.util.Log
import android.view.Menu
import android.view.MenuInflater
import android.view.View
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.nourtech.wordpress.myvocab.R
import com.nourtech.wordpress.myvocab.databinding.FragmentWordsBinding
import com.nourtech.wordpress.myvocab.dialogs.AddDialog
import com.nourtech.wordpress.myvocab.ui.viewmodels.MainViewModel
import dagger.hilt.android.AndroidEntryPoint
import java.util.*
import javax.inject.Inject

@AndroidEntryPoint
class WordsFragment : Fragment(R.layout.fragment_words) {

    private lateinit var binding: FragmentWordsBinding
    private lateinit var tts: TextToSpeech

    @Inject
    lateinit var viewModel: MainViewModel

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

        // setup option menu
        setHasOptionsMenu(true)
    }


    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.mainactivity_menu, menu)
        menu.findItem(R.id.menuItem_add).setOnMenuItemClickListener {
            val dialog = AddDialog()
            dialog.show(parentFragmentManager, null)
            true
        }
        menu.findItem(R.id.menuItem_filter).setOnMenuItemClickListener {
            it.isChecked = !it.isChecked
            viewModel.filter(it.isChecked)
            true
        }
        menu.findItem(R.id.menuItem_list).setOnMenuItemClickListener {
            findNavController().navigate(
                R.id.action_wordsFragment_to_addFragment
            )
            true
        }
        menu.findItem(R.id.menuItem_clear).setOnMenuItemClickListener {
            viewModel.clear()
            true
        }
        menu.findItem(R.id.menuItem_shuffle).setOnMenuItemClickListener {
            viewModel.shuffle()
            true
        }
        menu.findItem(R.id.menuItem_settings).setOnMenuItemClickListener {
            true
        }
        menu.findItem(R.id.menuItem_exit).setOnMenuItemClickListener {
            requireActivity().finish()
            true
        }

    }

    private fun setListeners() = binding.apply {
        buttonNext.setOnClickListener {
            viewModel?.next()
            binding.textView2.visibility = View.INVISIBLE
        }
        buttonPrevious.setOnClickListener {
            viewModel?.previous()
            textView2.visibility = View.INVISIBLE
        }
        checkBoxMemorized.setOnCheckedChangeListener { _, isChecked ->
            viewModel?.check(isChecked)
        }
        buttonShow.setOnClickListener {
            textView2.visibility = View.VISIBLE
        }
        imageButtonSpeaker.setOnClickListener {
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
            @Suppress("DEPRECATION")
            tts.speak(text, TextToSpeech.QUEUE_FLUSH, null)
        }
    }
}