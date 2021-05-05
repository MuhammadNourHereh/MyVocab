package com.nourtech.wordpress.myvocab.dialogs

import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.activityViewModels
import com.nourtech.wordpress.myvocab.databinding.DialogAddBinding
import com.nourtech.wordpress.myvocab.db.WordEntity
import com.nourtech.wordpress.myvocab.ui.viewmodels.MainViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
open class AddDialog : DialogFragment() {
    private lateinit var binding: DialogAddBinding

    private val viewModel: MainViewModel by activityViewModels()

    var isNewWordAdd = false
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        super.onCreateDialog(savedInstanceState)

        // setup data binding
        binding = DialogAddBinding.inflate(LayoutInflater.from(context))

        // set on click Listeners
        initListeners()

        // build dialog
        val builder = AlertDialog.Builder(context)
        builder.setTitle("Add a new word :")
        builder.setView(binding.root)


        return builder.create()
    }


    private fun initListeners() {
        binding.buttonAdd.setOnClickListener {
            add()
        }
        binding.buttonCancel.setOnClickListener {
            dismiss()
        }
        binding.buttonOk.setOnClickListener {
            add()
            dismiss()
        }
    }

    private fun add() {

        val s1 = binding.editTextFirstLanguage.text.toString()
        val s2 = binding.editTextSecondLanguage.text.toString()

        if (s1.isEmpty() || s2.isEmpty())
            return

        viewModel.addWord(WordEntity(s1, s2))
        isNewWordAdd = true

        binding.editTextFirstLanguage.text.clear()
        binding.editTextSecondLanguage.text.clear()
    }

}