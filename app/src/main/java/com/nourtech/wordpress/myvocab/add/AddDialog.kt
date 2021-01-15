package com.nourtech.wordpress.myvocab.add

import android.app.AlertDialog
import android.app.Dialog
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import androidx.annotation.RequiresApi
import androidx.fragment.app.DialogFragment
import com.nourtech.wordpress.myvocab.databinding.DialogAddBinding
import com.nourtech.wordpress.myvocab.db.WordEntity
import com.nourtech.wordpress.myvocab.db.WordsDatabase
import kotlinx.coroutines.*


class AddDialog : DialogFragment() {
    private lateinit var binding: DialogAddBinding

    @InternalCoroutinesApi
    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        super.onCreateDialog(savedInstanceState)
        binding = DialogAddBinding.inflate(LayoutInflater.from(context))
        val builder = AlertDialog.Builder(context)
        builder.setView(binding.root)
        GlobalScope.launch {
            initListeners()
        }

        return builder.create()
    }

    @InternalCoroutinesApi
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

    @InternalCoroutinesApi
    fun add() {
        val s1 = binding.editTextFirstLanguage.text.toString()
        val s2 = binding.editTextSecondLanguage.text.toString()
        val job = Job()
        val scope = CoroutineScope(Dispatchers.IO + job)
        scope.launch {
            WordsDatabase.getInstance(requireContext()).dao
                .add(
                    WordEntity(0, s1, s2, false)
                )
            binding.editTextFirstLanguage.text.clear()
            binding.editTextSecondLanguage.text.clear()
        }

    }

}