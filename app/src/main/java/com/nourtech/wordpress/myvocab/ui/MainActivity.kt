package com.nourtech.wordpress.myvocab.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.nourtech.wordpress.myvocab.R
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }

}