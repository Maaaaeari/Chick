package com.example.chick

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
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

    // db 테이블 생성 및 변경
    inner class myDBHelper(context: Context) : SQLiteOpenHelper(context, "drugDB", null, 1) {
        override fun onCreate(db: SQLiteDatabase?) {
            db!!.execSQL("CREATE TABLE drugDB ( medId INTEGER PRIMARY KEY, medName CHAR(20), ampm CHAR(10), alarmHour INTEGER, alarmMin INTEGER, daysOfWeek CHAR(10), eatNumber INTEGER, totalNumber INTEGER, currentNumber INTEGER, medIcon INTEGER);")
        }
        override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
            db!!.execSQL("DROP TABLE IF EXISTS drugDB")
            onCreate(db)
        }
    }

}