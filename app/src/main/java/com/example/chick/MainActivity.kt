package com.example.chick

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.os.Build.VERSION_CODES.S
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.core.text.set
import androidx.core.text.toSpannable
import com.google.android.material.bottomnavigation.BottomNavigationView

class MainActivity : AppCompatActivity() {
    lateinit var bottom_navigation: BottomNavigationView

    private val br: BroadcastReceiver = ResetEatdone()
    // 뒤로가기 버튼 눌렀던 시간 저장
    private var backKeyPressedTime : Long= 0

    override fun onCreate(savedInstanceState: Bundle?) {
       //setTheme(R.style.ThemeChick)
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

    // 브로드캐스트리시버 필터 추가 & 등록
    override fun onResume() {
        super.onResume()
        var filter = IntentFilter()
        filter.addAction(Intent.ACTION_DATE_CHANGED)
        registerReceiver(br, filter)
    }

    // 뒤로가기 버튼 두번 누를 시 종료
    override fun onBackPressed(){
        // 기존의 뒤로가기 기능 제거
        // super.onBackPressed()

        if(System.currentTimeMillis() > backKeyPressedTime + 2000){
            backKeyPressedTime = System.currentTimeMillis()
            return
        }

        if (System.currentTimeMillis() <= backKeyPressedTime + 2000){
            finishAffinity()
        }
    }
}