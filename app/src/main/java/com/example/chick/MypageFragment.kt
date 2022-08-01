package com.example.chick

import android.annotation.SuppressLint
import android.content.Context
import android.content.Context.MODE_PRIVATE
import android.content.Intent
import android.content.SharedPreferences
import android.database.sqlite.SQLiteDatabase
import android.os.Build.VERSION_CODES.S
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Switch
import android.widget.TextView
import android.widget.Toast
import com.example.chick.MainFragment.Companion.instance
import java.text.SimpleDateFormat
import java.util.*


class MypageFragment : Fragment() {

    lateinit var switchAlaram : Switch
    lateinit var txtAlramOnOff : TextView

    lateinit var dbManager: DBManager
    lateinit var sqlDB: SQLiteDatabase
    // Data에 있는 DrugAll
    lateinit var alramAllList: ArrayList<AlramAll>

    // 다른 액티비티 함수 호출
    lateinit var mainActivity : MainActivity


    init{
        MypageFragment.instance = this
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        mainActivity = context as MainActivity
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        var view = LayoutInflater.from(activity).inflate(R.layout.fragment_mypage, container, false)

        // 바인딩
        switchAlaram = view.findViewById(R.id.switchAlaram)
        txtAlramOnOff = view.findViewById(R.id.txtAlramOnOff)

        // 저장되었던 스위치 값을 가져옴
        loadData()

        // DB 생성
        dbManager = DBManager(applicationContext(), "drugDB", null, 1)

        // 약 알람 조회
        alramAllList = arrayListOf<AlramAll>()

        // 스위치 체크 상태 변화
        switchAlaram.setOnCheckedChangeListener { button, isChecked ->
            if(isChecked){
                Toast.makeText(context, "약 알람이 켜졌어요.", Toast.LENGTH_SHORT).show()
                txtAlramOnOff.text = "현재 전체 약 알람이 켜져있어요."
                // 요일별 알람 리스트 조회
                selectAlram()

//                mainActivity.onTimeSet()
            }else{
                Toast.makeText(context, "약 알람이 꺼졌어요.", Toast.LENGTH_SHORT).show()
                txtAlramOnOff.text = "현재 전체 약 알람이 꺼져있어요."
                if(alramAllList != null){
                    mainActivity.stopAlarm()
                }
            }
        }


        return view
    }

    // 요일별 알람 select 메소드
    @SuppressLint("Range")
    private fun selectAlram(){
        // 현재시간을 가져오기
        val long_now = System.currentTimeMillis()
        // 현재 시간을 Date 타입으로 변환
        val t_date = Date(long_now)
        // 날짜, 시간을 가져오고 싶은 형태 선언
        val tDaysOfWeek_dateFormat = SimpleDateFormat("E", Locale("ko", "KR"))
        val tKK_dateFormat = SimpleDateFormat("kk", Locale("ko", "KR"))
        val tMM_dateFormat = SimpleDateFormat("mm", Locale("ko", "KR"))
        // 현재 시간을 dateFormat 에 선언한 형태의 String 으로 변환
        val tDaysOfWeek = tDaysOfWeek_dateFormat.format(t_date)     // 요일
        var tKKString = tKK_dateFormat.format(t_date)  // 시간
        val tMMString = tMM_dateFormat.format(t_date)    // 분
        if(tKKString=="24"){
            tKKString = "00"
        }
        val tKKMMString = tKKString+tMMString
        val tKKMM = tKKMMString.toInt()

        // 현재 요일 알람 조회
        val selectAlarm = "select * from drugTBL where goalDone=0 AND ${tKKMM} <= alarmTime AND daysOfWeek LIKE '%${tDaysOfWeek}%' order by alarmTime;"
        // 읽기전용 데이터베이스 변수
        sqlDB = dbManager.readableDatabase
        // 데이터를 받아줌
        var cursor = sqlDB.rawQuery(selectAlarm,null)

        // 리스트에 가장 최신 데이터 넘겨줌
        cursor.moveToNext()
        var medId = cursor.getLong(cursor.getColumnIndex("medId"))
        var medName = cursor.getString(cursor.getColumnIndex("medName")).toString()
        var alarmHour = cursor.getInt(cursor.getColumnIndex("alarmHour"))
        var alarmMin = cursor.getInt(cursor.getColumnIndex("alarmMin"))

        if(alarmHour==0){
            alarmHour = 24
        }
        mainActivity.onTimeSet(alarmHour, alarmMin, medName)

        cursor.close()
        sqlDB.close()
    }

    // 앱이 종료되는 시점이 다가올 때 호출
    override fun onDestroy() {
        super.onDestroy()

        saveData()  // 값을 저장하는 함수
    }

    // 앱 종료 전 스위치 값 저장
    fun saveData(){
        val pref = mainActivity.getSharedPreferences("pref", 0)
        val edit = pref.edit()  // 수정모드

        var switchStatus = "ON"
        if (switchAlaram.isChecked == true){
            switchStatus = "ON"
        }else{
            switchStatus = "OFF"
        }

        edit.putString("switch", switchStatus)
        edit.apply()        // 저장완료
    }

    // 앱 로딩 후 이전의 스위치 값 불러옴
    fun loadData(){
        val pref = mainActivity.getSharedPreferences("pref", 0)
        if(pref.getString("switch", "OFF")=="ON"){
            switchAlaram.isChecked = true
        }else{
            switchAlaram.isChecked = false
        }
    }


    companion object{
        lateinit var instance: MypageFragment

        fun applicationContext() : Context {
            return instance.requireContext()
        }
    }

}