package com.example.chick

import DrugViewAdapter
import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.database.sqlite.SQLiteDatabase
import android.os.Build.VERSION_CODES.M
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.core.content.ContentProviderCompat.requireContext
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

    // Data에 있는 DrugAll
    lateinit var drugAllList: ArrayList<DrugAll>

    init{
        instance = this
    }

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
        dbManager = DBManager(requireContext(), "drugDB", null, 1)

        // 약 알람 조회
        drugAllList = arrayListOf<DrugAll>()
        selectDrug()

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

        return view
    }


    // select 메소드
    @SuppressLint("Range")
    private fun selectDrug(){
        // 알람 조회
        val selectAll = "select * from drugTBL where goalDone=0;"
        // 읽기전용 데이터베이스 변수
        sqlDB = dbManager.readableDatabase
        // 데이터를 받아줌
        var cursor = sqlDB.rawQuery(selectAll,null)

        //SQL 삽입
//        sqlDB = dbManager.writableDatabase
//        sqlDB.execSQL("INSERT INTO drugTBL VALUES (1, '비타민c','오전', 9,00,'월수금',1,50,10,1,0,0)")
//        sqlDB.execSQL("INSERT INTO drugTBL VALUES (2, '이노시톨','오후', 3,30,'월화수목금토일',2,100,60,2,0,0)")
//        sqlDB.execSQL("INSERT INTO drugTBL VALUES (3, '마그네슘','오전', 4,40,'화목',3,300,150,3,1,0)")
//        sqlDB.execSQL("INSERT INTO drugTBL VALUES (4, '철분','오후', 5,50,'수',1,100,10,4,0,0)")
//        sqlDB.execSQL("INSERT INTO drugTBL VALUES (5, '유산균','오전', 9,10,'월화수목금토일',2,400,380,5,1,0)")
//        sqlDB.execSQL("INSERT INTO drugTBL VALUES (6, '텐텐','오후', 8,05,'월수금',4,400,10,6,1,0)")
//        sqlDB.execSQL("INSERT INTO drugTBL VALUES (5, '유산균','오전', 9,10,'월화수목금토일',2,400,380,5,0,0)")
//        sqlDB.execSQL("INSERT INTO drugTBL VALUES (6, '텐텐','오후', 8, 05,'월수금',4,400,400,6,0,0)")
//        sqlDB.execSQL("INSERT INTO drugTBL VALUES (7, '글루콤','오후', 11,20,'월화수목금토일',1,30,30,7,0,1)")
//        sqlDB.execSQL("INSERT INTO drugTBL VALUES (8, '비타민d','오전', 10,00,'금토일',2,40,40,8,0,1)")

        //반복문을 사용하여 list 에 데이터를 넘겨 줍시다.
        while(cursor.moveToNext()){
            var medId = cursor.getInt(cursor.getColumnIndex("medId"))
            var medName = cursor.getString(cursor.getColumnIndex("medName")).toString()
            var ampm = cursor.getString(cursor.getColumnIndex("ampm")).toString()
            var alarmHour = cursor.getInt(cursor.getColumnIndex("alarmHour"))
            var alarmMin = cursor.getInt(cursor.getColumnIndex("alarmMin"))
            var daysOfWeek = cursor.getString(cursor.getColumnIndex("daysOfWeek")).toString()
            var eatNumber = cursor.getInt(cursor.getColumnIndex("eatNumber"))
            var currentNumber = cursor.getInt(cursor.getColumnIndex("currentNumber"))
            var medIcon = cursor.getInt(cursor.getColumnIndex("medIcon"))
            var eatDone = cursor.getInt(cursor.getColumnIndex("eatDone"))

            drugAllList.add(DrugAll(medId,medName,ampm,alarmHour,alarmMin,daysOfWeek,eatNumber,currentNumber,medIcon,eatDone))
        }
        cursor.close()
        sqlDB.close()
    }

    companion object{

        lateinit var instance: MainFragment

        fun ApplicationContext() : Context {
            return instance.requireContext()
        }

        // 복용 완료 update 메소드
        fun eatDrug(medId : Int, preStatus : Int, preNumber : Int){
            var dbManager: DBManager=DBManager(MainFragment.ApplicationContext(), "drugDB", null, 1)
            var sqlDB: SQLiteDatabase
            var preNum : Int

            // 복용완료
            preNum = preNumber+1
            val eatUpdate = "update drugTBL set eatDone=1, currentNumber=${preNum} where medId="+medId+";"
            // 복용취소
            preNum = preNumber-1
            val unEatUpdate = "update drugTBL set eatDone=0, currentNumber=${preNum} where medId="+medId+";"
            // 쓰기전용 데이터베이스 변수
            sqlDB = dbManager.writableDatabase
            // 데이터 수정
            if(preStatus==0){
                sqlDB.execSQL(eatUpdate)
            }else if(preStatus==1){
                sqlDB.execSQL(unEatUpdate)
            }


        }
    }

//    // 복용 완료 update 메소드
//    fun eatDrug(medId : Int, preStatus : Int){
//        // 복용완료
//        val eatUpdate = "update drugTBL set eatDone=1 where medId="+medId+";"
//        // 복용취소
//        val unEatUpdate = "update drugTBL set eatDone=0 where medId="+medId+";"
//        // 쓰기전용 데이터베이스 변수
//        sqlDB = dbManager.writableDatabase
//        // 데이터 수정
//        if(preStatus==0){
//            sqlDB.execSQL(eatUpdate)
//        }else{
//            sqlDB.execSQL(unEatUpdate)
//        }
//
//    }


}