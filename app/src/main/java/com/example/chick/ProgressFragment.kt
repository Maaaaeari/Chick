package com.example.chick

import android.annotation.SuppressLint
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.chick.MainFragment.Companion.ApplicationContext

class ProgressFragment : Fragment() {
    lateinit var txtProGoal: TextView
    lateinit var recyclerViewDrugAll: RecyclerView

    lateinit var dbManager: DBManager
    lateinit var sqlDB: SQLiteDatabase

    var goalDoneData: Int = 0

    // ProData에 있는 ProDrugAll
    lateinit var drugProList: ArrayList<ProDrugAll>

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        var view =
            LayoutInflater.from(activity).inflate(R.layout.fragment_progress, container, false)

        // 바인딩
        txtProGoal = view.findViewById(R.id.txtTopGoal)
        recyclerViewDrugAll = view.findViewById(R.id.recyclerViewPro)

        // DB 생성
        dbManager = DBManager(requireContext(), "drugDB", null, 1)

        // 약 알람 리스트 불러오기
        drugProList = arrayListOf<ProDrugAll>()
        selectDrug()

        // 레이아웃 매니저 등록
        recyclerViewDrugAll.layoutManager = LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false)
        recyclerViewDrugAll.setHasFixedSize(true)
        // 리사이클러 뷰 어댑터 연결
        recyclerViewDrugAll.adapter = ProgressAdapter(drugProList)

        // 목표개수 변경
        goalDrug()
        txtProGoal.text = goalDoneData.toString() + "개"

        return view
    }


    @SuppressLint("Range")
    private fun selectDrug() {
        //읽기전용 데이터베이스 변수
        sqlDB = dbManager.readableDatabase
        //데이터를 받아줌
        var cursor = sqlDB.rawQuery("select * from drugTBL ORDER BY goalDone, medId DESC;", null)

        //반복문을 사용하여 list 에 데이터를 넘겨 줍시다.
        while (cursor.moveToNext()) {
            var medId = cursor.getInt(cursor.getColumnIndex("medId"))
            var medName = cursor.getString(cursor.getColumnIndex("medName")).toString()
            var ampm = cursor.getString(cursor.getColumnIndex("ampm")).toString()
            var alarmHour = cursor.getInt(cursor.getColumnIndex("alarmHour"))
            var alarmMin = cursor.getInt(cursor.getColumnIndex("alarmMin"))
            var daysOfWeek = cursor.getString(cursor.getColumnIndex("daysOfWeek")).toString()
            var eatNumber = cursor.getInt(cursor.getColumnIndex("eatNumber"))
            var medIcon = cursor.getInt(cursor.getColumnIndex("medIcon"))
            var currentNumber = cursor.getInt(cursor.getColumnIndex("currentNumber"))
            var totalNumber = cursor.getInt(cursor.getColumnIndex("totalNumber"))
            var eatDone = cursor.getInt(cursor.getColumnIndex("eatDone"))
            var goalDone = cursor.getInt(cursor.getColumnIndex("goalDone"))

            drugProList.add(ProDrugAll(medId, medName, ampm, alarmHour, alarmMin, daysOfWeek, eatNumber, medIcon, currentNumber, totalNumber, eatDone, goalDone))
        }
        cursor.close()
        sqlDB.close()
    }

    @SuppressLint("Range")
    private fun goalDrug() {
        //DB읽기
        sqlDB = dbManager.readableDatabase

        var cursor = sqlDB.rawQuery("SELECT goalDone from drugTBL", null)
        var goalcursor = sqlDB.rawQuery("SELECT goalDone FROM drugTBL where goalDone = 1", null).count
        while(cursor.moveToNext()){
            goalDoneData = goalcursor
        }
        cursor.close()
        sqlDB.close()
    }
}