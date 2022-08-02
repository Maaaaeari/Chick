package com.example.chick

import android.annotation.SuppressLint
import android.content.Intent
import android.database.sqlite.SQLiteDatabase
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.TextView
import androidx.core.view.isVisible
import java.text.SimpleDateFormat
import java.util.*

class MedAlarmActivity : AppCompatActivity() {

    lateinit var txtMedDrugName1: TextView
    lateinit var txtMedDrugName2: TextView
    lateinit var medAlarmCancel: Button
    lateinit var medAlarmConfirm: Button

    lateinit var dbManager: DBManager
    lateinit var sqlDB: SQLiteDatabase

    lateinit var alarmTodayName : String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_med_alarm)

        // 바인딩
        txtMedDrugName1 = findViewById(R.id.txtMedDrugName1)
        txtMedDrugName2 = findViewById(R.id.txtMedDrugName2)
        medAlarmCancel = findViewById(R.id.medAlarmCancel)
        medAlarmConfirm = findViewById(R.id.medAlarmConfirm)

        // DB 생성
        dbManager = DBManager(this, "drugDB", null, 1)

        // 현재 요일 구하기
        val long_now = System.currentTimeMillis()
        val t_date = Date(long_now)
        val tDaysOfWeek_dateFormat = SimpleDateFormat("E", Locale("ko", "KR"))
        val tDaysOfWeek = tDaysOfWeek_dateFormat.format(t_date)     // 요일

        // 오늘 약 리스트 가져오기
        alarmTodayName = selectTodayDrug(tDaysOfWeek)
        if(alarmTodayName != ""){
            // 오늘 먹을 약이 있다면
            txtMedDrugName1.text = alarmTodayName
            txtMedDrugName2.text = "모두 복용하셨나요?"
            medAlarmCancel.setText("나중에 먹기")
            medAlarmConfirm.setVisibility(View.VISIBLE)
        }else{
            // 오늘 먹을 약이 없다면
            txtMedDrugName1.text = ""
            txtMedDrugName2.text = "오늘 복용할 약이 없었군요!"
            medAlarmCancel.setText("확인")
            medAlarmConfirm.setVisibility(View.GONE)
        }

        // 나중에 먹기 버튼
        medAlarmCancel.setOnClickListener {
            // 약 알람 리스트로 이동
            val intent = Intent(this, MainActivity::class.java)
            this.startActivity(intent)
        }

        // 복용 완료 버튼
        medAlarmConfirm.setOnClickListener {
            // 현재 요일 약 모두 복용완료
            eatTodayDrugAll(tDaysOfWeek)
            // 약 알람 리스트로 이동
            val intent = Intent(this, MainActivity::class.java)
            this.startActivity(intent)
        }

    }

    // 현재 요일의 복용약 이름 조회
    @SuppressLint("Range")
    private fun selectTodayDrug(tDaysOfWeek: String): String{

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

        // 오늘 먹어야 할 약 이름
        alarmTodayName = java.lang.String.join(separator, input)

        cursor.close()
        sqlDB.close()

        return alarmTodayName
    }

    // 현재 요일 약 모두 복용완료
    private fun eatTodayDrugAll(tDaysOfWeek: String){

        // 현재 요일의 약 복용완료 업데이트
        val eatDoneTodayUpdate = "update drugTBL set eatDone=1, currentNumber=(currentNumber+eatNumber) where daysOfWeek LIKE '%${tDaysOfWeek}%' AND eatDone=0 AND totalNumber>(currentNumber+eatNumber);"
        val goalDoneTodayUpdate = "update drugTBL set eatDone=1, currentNumber=(currentNumber+eatNumber), goalDone=1 where daysOfWeek LIKE '%${tDaysOfWeek}%' AND eatDone=0 AND totalNumber<=(currentNumber+eatNumber);"

        // 쓰기전용 데이터베이스 변수
        sqlDB = dbManager.writableDatabase

        sqlDB.execSQL(eatDoneTodayUpdate)
        sqlDB.execSQL(goalDoneTodayUpdate)

        sqlDB.close()
        dbManager.close()
    }
}