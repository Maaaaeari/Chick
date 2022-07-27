package com.example.chick

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.core.text.set
import androidx.core.text.toSpannable
import com.google.android.material.bottomnavigation.BottomNavigationView

class MainActivity : AppCompatActivity() {

    lateinit var bottom_navigation: BottomNavigationView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        bottom_navigation = findViewById(R.id.bottomNavigationView)

        // bottomNavigationView 설정
        bottom_navigation.setOnItemSelectedListener { item->
            when(item.itemId){
                R.id.tab_main -> {
                    val mainFragment = MainFragment()
                    supportFragmentManager.beginTransaction().replace(R.id.main_content, mainFragment).commit()
                    return@setOnItemSelectedListener true
                }
                R.id.tab_progress -> {
                    val progressFragment = ProgressFragment()
                    supportFragmentManager.beginTransaction().replace(R.id.main_content, progressFragment).commit()
                    return@setOnItemSelectedListener true
                }
                R.id.tab_mypage -> {
                    val mypageFragment = MypageFragment()
                    supportFragmentManager.beginTransaction().replace(R.id.main_content, mypageFragment).commit()
                    return@setOnItemSelectedListener true
                }
            }
            return@setOnItemSelectedListener false
        }

        val mainFragment = MainFragment()
        supportFragmentManager.beginTransaction().add(R.id.main_content, mainFragment).commit()
    }
}