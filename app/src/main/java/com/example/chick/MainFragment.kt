package com.example.chick

import DrugViewAdapter
import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.database.sqlite.SQLiteDatabase
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton

class MainFragment : Fragment() {

    lateinit var btnPlus : FloatingActionButton
    lateinit var txtBannerName : TextView
    lateinit var txtBannerTime : TextView
    lateinit var recyclerViewDrugAll : RecyclerView

    lateinit var dbManager: DBManager
    lateinit var sqlDB: SQLiteDatabase

    // Data에 있는 PostAll
    lateinit var drugAllList: ArrayList<DrugAll>

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        var view = LayoutInflater.from(activity).inflate(R.layout.fragment_main, container, false)

        // 바인딩
        btnPlus = view.findViewById(R.id.btnMainAdd)
        txtBannerName = view.findViewById(R.id.txtMainBannerName)
        txtBannerTime = view.findViewById(R.id.txtMainBannerTime)
        recyclerViewDrugAll = view.findViewById(R.id.recyclerViewMain)

        // DB 생성
        dbManager = DBManager(requireActivity()!!.applicationContext, "drugDB", null, 1)

        // 약 알람 리스트
        //drugAllList = arrayListOf<DrugAll>()
        //selectDrug()
        // 전체 약 리스트 불러오기
//        drugAllList.addAll(selectDrug())
        drugAllList = arrayListOf<DrugAll>(
            DrugAll(1, "vitamin", "am", 2, 30 ,"월수", 2, 1) ,
            DrugAll(2, "d이노시톨", "pm", 4, 30 ,"금토일", 1, 8),
            DrugAll(3, "유산균", "am", 2, 0,"화수목" ,3 ,2),
            DrugAll(4, "vitamin", "pm", 8, 30, "월수", 4, 3),
            DrugAll(5, "하하하", "am", 2, 30, "월화수목금토일", 2, 4),
            DrugAll(6, "vitamin", "am", 10, 30 ,"월수", 2, 6)
        )

        // 레이아웃 매니저 등록
        recyclerViewDrugAll.layoutManager = LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false)
        recyclerViewDrugAll.setHasFixedSize(true)
        // 리사이클러 뷰 어댑터 연결
        recyclerViewDrugAll.adapter = DrugViewAdapter(drugAllList)

        // 알람생성 버튼
        btnPlus.setOnClickListener{
            val intent = Intent(activity, EditAlarmActivity::class.java)
            startActivity(intent)
        }

        // Inflate the layout for this fragment
        //return inflater.inflate(R.layout.fragment_main, container, false)

        return view
    }



    // select 메소드
    @SuppressLint("Range")
    private fun selectDrug() : ArrayList<DrugAll>{
        //전체조회
        val selectAll = "select * from drug"
        Log.d("11","111")
        //읽기전용 데이터베이스 변수
        sqlDB = dbManager.readableDatabase
        Log.d("222","22222")
        //데이터를 받아줌
        val cursor = sqlDB.rawQuery(selectAll,null)

        //반복문을 사용하여 list 에 데이터를 넘겨 줍시다.
        while(cursor.moveToNext()){
            val medId = cursor.getInt(cursor.getColumnIndex("medId"))
            val medName = cursor.getString(cursor.getColumnIndex("medName"))
            val ampm = cursor.getString(cursor.getColumnIndex("ampm"))
            val alarmHour = cursor.getInt(cursor.getColumnIndex("alarmHour"))
            val alarmMin = cursor.getInt(cursor.getColumnIndex("alarmMin"))
            val daysOfWeek = cursor.getString(cursor.getColumnIndex("daysOfWeek"))
            val eatNumber = cursor.getInt(cursor.getColumnIndex("eatNumber"))
            val medIcon = cursor.getInt(cursor.getColumnIndex("medIcon"))

            drugAllList.add(DrugAll(medId,medName,ampm,alarmHour,alarmMin,daysOfWeek,eatNumber,medIcon))
        }
        cursor.close()
        sqlDB.close()

        return drugAllList
    }


}