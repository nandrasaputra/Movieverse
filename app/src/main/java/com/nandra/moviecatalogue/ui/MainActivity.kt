package com.nandra.moviecatalogue.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.nandra.moviecatalogue.R

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }

    /*override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.main_menu, menu)
        return true
    }*/

    /*override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val id = item.itemId
        if (id == R.id.main_menu_action_settings) {
            findNavController(R.id.main_fragment_container).navigate(R.id.action_mainFragment_to_settingsPreferenceFragment)
        }
        return super.onOptionsItemSelected(item)
    }*/
}
