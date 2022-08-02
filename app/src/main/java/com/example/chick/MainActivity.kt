package com.example.chick

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.os.Build.VERSION_CODES.S
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.core.text.set
import androidx.core.text.toSpannable
import com.example.chick.MainFragment.Companion.instance
import com.google.android.material.bottomnavigation.BottomNavigationView
import java.text.DateFormat
import java.util.*

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

    // 시간 정하는 함수
    fun onTimeSet(hTime: Int, mTime: Int){
        var c = Calendar.getInstance()

        c.set(Calendar.HOUR_OF_DAY, hTime)  //시간
        c.set(Calendar.MINUTE, mTime)  //분
        c.set(Calendar.SECOND, 0)  //초

        Log.d("testCal", hTime.toString())
        Log.d("testCal", mTime.toString())

        startAlarm(c)
    }

    // 전체 알람 켜기
    fun startAlarm(c: Calendar){

        // 알람매니저 선언
        var alarmManager : AlarmManager = getSystemService(Context.ALARM_SERVICE) as AlarmManager
        var intent = Intent(this, AlarmReceiver::class.java)

        var pendingIntent = PendingIntent.getBroadcast(this, 1, intent, PendingIntent.FLAG_MUTABLE)

        // 설정 시간이 현재 시간보다 이전이면 +1일
        if(c.before(Calendar.getInstance())){
            c.add(Calendar.DATE, 1)
        }

        alarmManager.setExact(AlarmManager.RTC_WAKEUP, c.timeInMillis, pendingIntent)
    }

    // 전체 알람 끄기
    fun stopAlarm(){
        // 알람매니저 선언
        var alarmManager : AlarmManager = getSystemService(Context.ALARM_SERVICE) as AlarmManager
        var intent = Intent(this, AlarmReceiver::class.java)
        var pendingIntent = PendingIntent.getBroadcast(this, 1, intent, PendingIntent.FLAG_IMMUTABLE)

        alarmManager.cancel(pendingIntent)
    }


}