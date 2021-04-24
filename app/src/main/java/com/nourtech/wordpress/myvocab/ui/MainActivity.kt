package com.nourtech.wordpress.myvocab.ui

import android.os.Bundle
import android.view.Menu
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import com.nourtech.wordpress.myvocab.R
import com.nourtech.wordpress.myvocab.dialogs.AddDialog
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


    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.mainactivity_menu, menu)
        menu?.findItem(R.id.menuItem_add)?.setOnMenuItemClickListener {

            val dialog = AddDialog()
            dialog.show(supportFragmentManager, null)

            true
        }
        menu?.findItem(R.id.menuItem_filter)?.setOnMenuItemClickListener {
            it.isChecked = !it.isChecked
            viewModel.filter(it.isChecked)
            true
        }
        menu?.findItem(R.id.menuItem_list)?.setOnMenuItemClickListener {
            findNavController(R.id.fragment_container_view).navigate(
                R.id.action_wordsFragment_to_addFragment
            )
            true
        }
        menu?.findItem(R.id.menuItem_clear)?.setOnMenuItemClickListener {
            viewModel.clear()
            viewModel.updateList()
            true
        }
        menu?.findItem(R.id.menuItem_shuffle)?.setOnMenuItemClickListener {
            viewModel.shuffle()
            viewModel.updateList()
            true
        }
        menu?.findItem(R.id.menuItem_settings)?.setOnMenuItemClickListener {
            true
        }
        menu?.findItem(R.id.menuItem_exit)?.setOnMenuItemClickListener {
            finish()
            true
        }
        return super.onCreateOptionsMenu(menu)
    }


}