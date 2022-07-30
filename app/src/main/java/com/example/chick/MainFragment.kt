package com.example.chick

import DrugViewAdapter
import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.database.sqlite.SQLiteDatabase
import android.icu.lang.UCharacter.GraphemeClusterBreak.T
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
import androidx.core.view.isInvisible
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class MainFragment : Fragment() {

    lateinit var btnPlus : FloatingActionButton
    lateinit var txtMainBannerContent1 : TextView
    lateinit var txtMainBannerContent2 : TextView
    lateinit var imgMainBanner : ImageView
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
        txtMainBannerContent1 = view.findViewById(R.id.txtMainBannerContent1)
        txtMainBannerContent2 = view.findViewById(R.id.txtMainBannerContent2)
        imgMainBanner = view.findViewById(R.id.imgMainBanner)
        txtBannerName = view.findViewById(R.id.txtMainBannerName)
        txtBannerTime = view.findViewById(R.id.txtMainBannerTime)
        recyclerViewDrugAll = view.findViewById(R.id.recyclerViewMain)

        // DB 생성
        dbManager = DBManager(requireContext(), "drugDB", null, 1)

        // 약 알람 조회
        drugAllList = arrayListOf<DrugAll>()
        selectDrug()

        // 배너 업데이트
        updateBanner()

        // 레이아웃 매니저 등록
        recyclerViewDrugAll.layoutManager = LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false)
        recyclerViewDrugAll.setHasFixedSize(true)
        // 리사이클러 뷰 어댑터 연결
        recyclerViewDrugAll.adapter = DrugViewAdapter(drugAllList)


        // 알람생성 버튼
        btnPlus.setOnClickListener{
            val intent = Intent(activity, AddAlarmActivity::class.java)
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

        //반복문을 사용하여 list 에 데이터를 넘겨 줍시다.
        while(cursor.moveToNext()){
            var medId = cursor.getLong(cursor.getColumnIndex("medId"))
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

    // 배너 변경 메소드
    @SuppressLint("Range")
    private fun updateBanner(){

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
        val tKKString = tKK_dateFormat.format(t_date)  // 시간
        val tMMString = tMM_dateFormat.format(t_date)    // 분
        val tKKMMString = tKKString+tMMString
        val tKKMM = tKKMMString.toInt()

        Log.d("44444", tKKMMString)


        // 현재 요일의 알람 조회
        val selectAll = "select * from drugTBL where goalDone=0 AND daysOfWeek LIKE '%${tDaysOfWeek}%';"
        // 읽기전용 데이터베이스 변수
        sqlDB = dbManager.readableDatabase
        // 데이터를 받아줌
        var cursor = sqlDB.rawQuery(selectAll,null)

        var gapHour : Int = 24
        var gapMin : Int = 60

        var gapAlaram : Int = 2500
        var doEatName : String = ""

        //반복문을 사용하여 list 에 데이터를 넘겨 줍시다.
        while(cursor.moveToNext()){
            var medId = cursor.getLong(cursor.getColumnIndex("medId"))
            var medName = cursor.getString(cursor.getColumnIndex("medName")).toString()
            var ampm = cursor.getString(cursor.getColumnIndex("ampm"))
            var alarmHour = cursor.getInt(cursor.getColumnIndex("alarmHour"))
            var alarmMin = cursor.getInt(cursor.getColumnIndex("alarmMin"))

            // 알람 시간분
            var alaramHourMinString = alarmHour.toString()+alarmMin.toString()
            var alaramHourMin = alaramHourMinString.toInt()

            Log.d("44444", alaramHourMinString)

            // 오후라면 시간에 12 더함
            if(ampm == "pm") {alarmHour = alarmHour+12}

            if(tKKMM<=alaramHourMin){
                // 현재 시간 이후의 알람이 있으면
                if(gapAlaram>=(alaramHourMin-tKKMM)){
                    gapAlaram=alaramHourMin-tKKMM
                    // 기존 시간 갭보다 작다면
                    // 배너 이름 변경
                    txtMainBannerContent1.text = "곧 "
                    txtBannerName.text = medName
                    txtMainBannerContent2.text = "를 복용할 시간이에요."
                    // 배너 시간 변경
                    var allTime : String
                    if(alarmMin < 10 ){
                        allTime = ampm+" "+alarmHour+":0"+alarmMin
                    }
                    else{
                        allTime = ampm+" "+alarmHour+":"+alarmMin
                    }
                    txtBannerTime.text = allTime
                }
            }else{
                // 현재 시간 이후의 알람이 없으면
                txtMainBannerContent1.text = "병아리가 먹는 약은?"
                txtBannerName.text = ""
                txtMainBannerContent2.text = ""
                txtBannerTime.text="삐약삐약"
            }



//            if(gapHour-alarmHour < 24){
//                // 알람이 있다면
//                if(tKK <= alarmHour){
//                    // 현재 시간보다 알람이 후라면
//                    if(gapHour>(alarmHour - tKK)){
//                        // 기존 시간 갭보다 작다면
//                        gapHour = alarmHour - tKK
//                    }else if(gapHour==(alarmHour - tKK)){
//                        // 기존 시간 갭과 같다면
//                        if(tMM <= alarmMin){
//                            // 현재 분보다 알람이 후라면
//                        }
//                    }else{
//                        // 기존 시간 갭보다 크다면
//                        gapHour = gapHour
//                    }
//                }
//            }


//            // 배너 이름 변경
//            txtBannerName.text = medName
//            // 배너 시간 변경
//            var allTime : String
//            if(alarmMin < 10 ){
//                allTime = ampm+" "+alarmHour+":0"+alarmMin
//            }
//            else{
//                allTime = ampm+" "+alarmHour+":"+alarmMin
//            }
//            txtBannerTime.text = allTime
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
        fun eatDrug(medId : Long, preStatus : Int, preNumber : Int){
            var dbManager: DBManager=DBManager(MainFragment.ApplicationContext(), "drugDB", null, 1)
            var sqlDB: SQLiteDatabase
            var preNum : Int

            // 복용완료
            preNum = preNumber+1
            val eatUpdate = "update drugTBL set eatDone=1, currentNumber=${preNum} where medId="+medId+";"
            // 복용취소
            preNum = preNumber-1
            val unEatUpdate = "update drugTBL set eatDone=0, currentNumber=${preNum} where medId="+medId+";"
            // 목표달성
            val goalUpdate = "update drugTBL set goalDone=1 where medId="+medId+" AND totalNumber==currentNumber;"

            // 쓰기전용 데이터베이스 변수
            sqlDB = dbManager.writableDatabase
            // 데이터 수정
            if(preStatus==0){
                sqlDB.execSQL(eatUpdate)
                sqlDB.execSQL(goalUpdate)
            }else if(preStatus==1){
                sqlDB.execSQL(unEatUpdate)
            }


        }
    }


}