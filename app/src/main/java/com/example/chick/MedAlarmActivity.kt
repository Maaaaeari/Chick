package com.example.chick

import android.annotation.SuppressLint
import android.content.Intent
import android.database.sqlite.SQLiteDatabase
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.TextView
import androidx.core.view.isInvisible
import androidx.core.view.isVisible
import java.text.SimpleDateFormat
import java.util.*

class MedAlarmActivity : AppCompatActivity() {

    lateinit var txtMedDrugName: TextView
    lateinit var medAlarmCancel: Button
    lateinit var medAlarmConfirm: Button

    lateinit var dbManager: DBManager
    lateinit var sqlDB: SQLiteDatabase

    lateinit var alarmTodayName : String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_med_alarm)

        // 바인딩
        txtMedDrugName = findViewById(R.id.txtMedDrugName)
        medAlarmCancel = findViewById(R.id.medAlarmCancel)
        medAlarmConfirm = findViewById(R.id.medAlarmConfirm)

        // DB 생성
        dbManager = DBManager(this, "drugDB", null, 1)

        // 오늘 약 리스트 가져오기
        alarmTodayName = selectTodayDrug()
        txtMedDrugName.text = alarmTodayName

        // 나중에 먹기 버튼
        medAlarmCancel.setOnClickListener {
            // 약 알람 리스트로 이동
            val intent = Intent(this, MainActivity::class.java)
            this.startActivity(intent)
        }

        // 복용 완료 버튼
        medAlarmConfirm.setOnClickListener {

        }

    }

    // 현재 요일의 복용약 이름 조회
    @SuppressLint("Range")
    private fun selectTodayDrug(): String{
        // 현재 요일 구하기
        val long_now = System.currentTimeMillis()
        val t_date = Date(long_now)
        val tDaysOfWeek_dateFormat = SimpleDateFormat("E", Locale("ko", "KR"))
        val tDaysOfWeek = tDaysOfWeek_dateFormat.format(t_date)     // 요일

        // 현재 요일의 알람 조회
        val selectTodayDrug = "select * from drugTBL where goalDone=0 AND daysOfWeek LIKE '%${tDaysOfWeek}%';"
        // 읽기전용 데이터베이스 변수
        sqlDB = dbManager.readableDatabase

        // 데이터를 받아줌
        var cursor = sqlDB.rawQuery(selectTodayDrug,null)

        // 쉼표로 문자열 구분하기
        var input = mutableListOf<String>()
        val separator = ", "

        // 반복문을 사용하여 list 에 데이터를 넘겨 줍시다.
        while(cursor.moveToNext()){
            var medName = cursor.getString(cursor.getColumnIndex("medName")).toString()
            input.add(medName)
        }

//        if(cursor != null){
//            if (cursor.moveToFirst()){
//                do {
//                    var medName = cursor.getString(cursor.getColumnIndex("medName")).toString()
//                    input.add(medName)
//                } while (cursor.moveToNext())
//            }
//            cursor.close()
//            alarmTodayName =""
//        }

        // 오늘 먹어야 할 약 이름
        alarmTodayName = java.lang.String.join(separator, input)

        Log.d("abcdef", alarmTodayName)

        cursor.close()
        sqlDB.close()

        return alarmTodayName
    }
}