package com.nourtech.wordpress.myvocab.ui.fragments

import android.content.SharedPreferences
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.userProfileChangeRequest
import com.google.firebase.firestore.FirebaseFirestore
import com.nourtech.wordpress.myvocab.R
import com.nourtech.wordpress.myvocab.databinding.FragmentSettingsBinding
import com.nourtech.wordpress.myvocab.db.WordEntity
import com.nourtech.wordpress.myvocab.others.KEY_LANG1
import com.nourtech.wordpress.myvocab.others.KEY_LANG2
import com.nourtech.wordpress.myvocab.others.KEY_MEMORIZED
import com.nourtech.wordpress.myvocab.others.KEY_REALTIME_UPDATE
import com.nourtech.wordpress.myvocab.ui.viewmodels.MainViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject


@AndroidEntryPoint
class SettingsFragment : Fragment(R.layout.fragment_settings) {

    private lateinit var binding: FragmentSettingsBinding
    private val firestore = FirebaseFirestore.getInstance()
    private val user = FirebaseAuth.getInstance().currentUser

    @Inject
    lateinit var sharedPref: SharedPreferences

    private val viewModel: MainViewModel by activityViewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentSettingsBinding.bind(view)

        binding.apply {
            buttonChange.setOnClickListener {
                if (user != null || etUserName.text.isNotEmpty())
                    changeName(etUserName.text.toString())
            }
            btnBackup.setOnClickListener {
                CoroutineScope(Dispatchers.IO).launch {
                    user?.let {
                        backup(viewModel.getAllWords())
                    }
                }
            }
            btnRestore.setOnClickListener {
                user?.let {
                    restore()
                }
            }
            cbRealtimeUpdate.isChecked = sharedPref.getBoolean(KEY_REALTIME_UPDATE, false)
            cbRealtimeUpdate.setOnCheckedChangeListener { _, isChecked ->
                sharedPref.edit().putBoolean(KEY_REALTIME_UPDATE, isChecked)
                    .apply()
            }
        }
    }

    private fun changeName(userName: String) {
        user!!.updateProfile(
            userProfileChangeRequest { displayName = userName }
        ).addOnCompleteListener {
            if (it.isSuccessful) {
                snackMassage("userName Changed $userName")
            } else
                snackMassage("failure")
        }
    }

    private fun backup(list: List<WordEntity>) {
        for (w in list) {
            val docPath = "/users/${user!!.uid}/words/${w.id}"

            val wordMap = hashMapOf<String, Any>(
                Pair(KEY_LANG1, w.lang1),
                Pair(KEY_LANG2, w.lang2),
                Pair(KEY_MEMORIZED, w.memorized)
            )
            firestore.document(docPath).set(wordMap)
        }
    }

    private fun restore() {
        val colPath = "/users/${user!!.uid}/words/"
        firestore.collection(colPath).get().addOnSuccessListener {
            for (document in it.documents) {
                viewModel.addWord(WordEntity(document))
            }
            snackMassage("success")

        }.addOnFailureListener {
            snackMassage("Failure")
        }
    }

    private fun snackMassage(msg: String) {
        Snackbar.make(requireView(), msg, Snackbar.LENGTH_SHORT).show()
    }

}