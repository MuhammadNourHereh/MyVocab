package com.nourtech.wordpress.myvocab.ui.fragments

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.nourtech.wordpress.myvocab.R
import com.nourtech.wordpress.myvocab.databinding.FragmentSettingsBinding
import com.nourtech.wordpress.myvocab.others.USERNAME
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SettingsFragment : Fragment(R.layout.fragment_settings) {

    private lateinit var binding: FragmentSettingsBinding
    private val firestore = FirebaseFirestore.getInstance()
    private val auth = FirebaseAuth.getInstance()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentSettingsBinding.bind(view)

        binding.apply {
            buttonChange.setOnClickListener {
                if (auth.currentUser != null || etUserName.text.isNotEmpty())
                    changeName(etUserName.text.toString())
            }
        }
    }

    private fun changeName(userName: String) {
        val docPath = "/users/" + auth.currentUser?.uid
        firestore.document(docPath).update(
            mapOf(Pair(USERNAME, userName))
        ).addOnSuccessListener {
            Snackbar.make(
                requireView(),
                "userName Changed $userName",
                Snackbar.LENGTH_SHORT
            ).show()
        }
    }
}