package com.nourtech.wordpress.myvocab.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.nourtech.wordpress.myvocab.R
import com.nourtech.wordpress.myvocab.ui.viewModels.WordViewModel
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    @Inject
    lateinit var viewModel: WordViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

    }





}