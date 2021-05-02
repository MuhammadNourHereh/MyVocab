package com.nourtech.wordpress.myvocab.ui.fragments

import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.speech.tts.TextToSpeech
import android.util.Log
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContract
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.MutableLiveData
import androidx.navigation.fragment.findNavController
import com.firebase.ui.auth.AuthUI
import com.firebase.ui.auth.IdpResponse
import com.google.firebase.auth.FirebaseAuth
import com.nourtech.wordpress.myvocab.R
import com.nourtech.wordpress.myvocab.databinding.FragmentWordsBinding
import com.nourtech.wordpress.myvocab.dialogs.AddDialog
import com.nourtech.wordpress.myvocab.ui.viewmodels.MainViewModel
import dagger.hilt.android.AndroidEntryPoint
import java.util.*

@AndroidEntryPoint
class WordsFragment : Fragment(R.layout.fragment_words) {

    private lateinit var binding: FragmentWordsBinding
    private val viewModel: MainViewModel by activityViewModels()

    private val auth = FirebaseAuth.getInstance()
    private lateinit var authUIRegister: ActivityResultLauncher<Intent>
    private val userName = MutableLiveData("")

    private lateinit var tts: TextToSpeech

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        authUIRegister = registerForActivityResult(
            object : ActivityResultContract<Intent, IdpResponse>() {
                override fun createIntent(context: Context, input: Intent) = input
                override fun parseResult(resultCode: Int, intent: Intent?): IdpResponse? =
                    IdpResponse.fromResultIntent(intent)
            }
        ) {
            val user = auth.currentUser
            user?.let {
                Log.d("auth", "auth success: " + user.email)
                val email = user.email
                userName.postValue(email)
            }
        }
        // setup login
        auth.currentUser.let {
            val login = if (it == null)
                resources.getString(R.string.login)
            else
                it.email
            userName.postValue(login)
        }
    }

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
        binding.apply {
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

        // setup option menu
        setHasOptionsMenu(true)
    }


    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.mainactivity_menu, menu)

        userName.observe(viewLifecycleOwner) {
            menu.findItem(R.id.menuItem_login).title = it
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        when (item.itemId) {
            R.id.menuItem_add -> {
                val dialog = AddDialog()
                dialog.show(parentFragmentManager, null)
            }
            R.id.menuItem_filter -> {
                item.isChecked = !item.isChecked
                viewModel.filter(item.isChecked)
            }
            R.id.menuItem_list -> {
                findNavController().navigate(
                    R.id.action_wordsFragment_to_addFragment
                )
            }
            R.id.menuItem_clear -> {
                viewModel.clear()
            }
            R.id.menuItem_shuffle -> {
                viewModel.shuffle()
            }
            R.id.menuItem_login -> {
                if (auth.currentUser == null) {
                    logIn()
                } else {
                    signOut()
                }
            }
            R.id.menuItem_exit -> {
                requireActivity().finish()
            }
        }

        return super.onOptionsItemSelected(item)
    }

    private fun signOut() {
        auth.signOut()
        val login = resources.getString(R.string.login)
        userName.postValue(login)
    }

    private fun logIn() {
        // Choose authentication providers
        val providers = arrayListOf(
            AuthUI.IdpConfig.EmailBuilder().build(),
            AuthUI.IdpConfig.GoogleBuilder().build()
        )

        // Create and launch sign-in intent
        val intent = AuthUI.getInstance()
            .createSignInIntentBuilder()
            .setAvailableProviders(providers)
            .build()

        authUIRegister.launch(intent)
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