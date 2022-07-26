package com.example.chick

import android.os.Bundle
import android.view.Menu
import androidx.appcompat.app.AppCompatActivity

class EditAlarmActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_alarm)
        
        //supportActionBar?.setDisplayShowTitleEnabled(false)
        //supportActionBar!!.setDisplayHomeAsUpEnabled(true)
    }

    /*
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_edit_alarm, menu)
        return true
    }

     */
}