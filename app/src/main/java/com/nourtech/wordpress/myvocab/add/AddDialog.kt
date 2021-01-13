package com.nourtech.wordpress.myvocab.list.add

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
    lateinit var binding: DialogAddBinding

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
    private suspend fun initListeners() {
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
        val job = Job()
        val scope = CoroutineScope(Dispatchers.IO + job)
        scope.launch {
            WordsDatabase.getInstance(requireContext()).dao
                .add(
                    WordEntity(
                        0,
                        binding.editTextFirstLanguage.text.toString(),
                        binding.editTextSecondLanguage.text.toString(),
                        false
                    )
                )
        }

    }

}