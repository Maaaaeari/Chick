package com.example.chick

import DrugViewAdapter
import android.annotation.SuppressLint
import android.app.*
import android.content.Context
import android.content.Intent
import android.database.sqlite.SQLiteDatabase
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.provider.CalendarContract
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.*
import androidx.core.app.NotificationCompat
import androidx.core.content.ContextCompat.getSystemService
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
        MainFragment.instance = this
    }

    @SuppressLint("NotifyDataSetChanged")
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
        recyclerViewDrugAll.adapter?.notifyDataSetChanged()


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
//        sqlDB = dbManager.writableDatabase
//        sqlDB.execSQL("INSERT INTO drugTBL VALUES (5, '바바ㅏㅂ', '오전', 11, 3, 30, '월수금', 3, 30, 28, 4, 0, 0)")

        // 알람 조회
        val selectAll = "select * from drugTBL where goalDone=0 order by alarmTime;"
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
            var totalNumber = cursor.getInt(cursor.getColumnIndex("totalNumber"))
            var currentNumber = cursor.getInt(cursor.getColumnIndex("currentNumber"))
            var medIcon = cursor.getInt(cursor.getColumnIndex("medIcon"))
            var eatDone = cursor.getInt(cursor.getColumnIndex("eatDone"))
            var goalDone = cursor.getInt(cursor.getColumnIndex("goalDone"))

            drugAllList.add(DrugAll(medId,medName,ampm,alarmHour,alarmMin,daysOfWeek,eatNumber,totalNumber, currentNumber,medIcon,eatDone, goalDone))
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
        var tKKString = tKK_dateFormat.format(t_date)  // 시간
        val tMMString = tMM_dateFormat.format(t_date)    // 분
        if(tKKString=="24"){
            tKKString = "00"
        }
        val tKKMMString = tKKString+tMMString
        val tKKMM = tKKMMString.toInt()

        // 현재 요일의 현재 시간 이후의 가장 최근 알람 조회
        val selectAll = "select * from drugTBL where goalDone=0 AND daysOfWeek LIKE '%${tDaysOfWeek}%' AND eatDone=0;"
        // 읽기전용 데이터베이스 변수
        sqlDB = dbManager.readableDatabase
        // 데이터를 받아줌
        var cursor = sqlDB.rawQuery(selectAll,null)

        var gapAlaram : Int = 2500
        var trial_name : String = ""
        var trial_ampm : String = ""
        var trial_hour : Int = 0
        var trial_min : Int = 0

        //반복문을 사용하여 list 에 데이터를 넘겨 줍시다.
        while(cursor.moveToNext()){
            var medName = cursor.getString(cursor.getColumnIndex("medName")).toString()
            var ampm = cursor.getString(cursor.getColumnIndex("ampm"))
            var alarmTime = cursor.getInt(cursor.getColumnIndex("alarmTime"))
            var alarmHour = cursor.getInt(cursor.getColumnIndex("alarmHour"))
            var alarmMin = cursor.getInt(cursor.getColumnIndex("alarmMin"))

            if(tKKMM<=alarmTime){
                // 현재 시간 이후의 알람이 있으면
                if(gapAlaram>=(alarmTime-tKKMM)){
                    // 기존 시간 갭보다 작다면
                    gapAlaram=alarmTime-tKKMM
                    trial_name = medName
                    trial_ampm = ampm
                    trial_hour = alarmHour
                    trial_min = alarmMin
                }else{
                    gapAlaram = gapAlaram
                }
            }else{
                // 현재 시간 이후의 알람이 없으면
                gapAlaram = gapAlaram
                txtMainBannerContent1.text = "병아리가 먹는 약은?"
                txtBannerName.text = ""
                txtMainBannerContent2.text = ""
                txtBannerTime.text="삐약삐약"
            }

        }

        if(trial_name != ""){
            // 알람이 있는 경우에만
            // 배너 이름 변경
            txtMainBannerContent1.text = "곧 "
            txtBannerName.text = trial_name
            txtMainBannerContent2.text = "을(를) 복용할 시간이에요."
            // 배너 시간 변경
            var allTime : String
            if(trial_min < 10 ){
                allTime = trial_ampm+" "+trial_hour+":0"+trial_min
            }
            else{
                allTime = trial_ampm+" "+trial_hour+":"+trial_min
            }
            txtBannerTime.text = allTime
        }

        cursor.close()
        sqlDB.close()
    }


    companion object{

        lateinit var instance: MainFragment
        lateinit var dialog : Dialog

        lateinit var Alarm : AlarmReceiver
        lateinit var notificationManager : NotificationManager
        lateinit var builder : NotificationCompat

        fun ApplicationContext() : Context {
            return instance.requireContext()
        }

        // 복용 완료 update 메소드
        fun eatDrug(medId : Long, medName:String, preStatus : Int, eatNumber: Int, preNumber : Int, totalNumber : Int){
            var dbManager: DBManager=DBManager(MainFragment.ApplicationContext(), "drugDB", null, 1)
            var sqlDB: SQLiteDatabase
            var preNum : Int

            // 복용완료
            preNum = preNumber+eatNumber
            val eatUpdate = "update drugTBL set eatDone=1, currentNumber=${preNum} where medId="+medId+";"
            // 복용취소
            preNum = preNumber-eatNumber
            if(preNum < 0) {
                preNum = 0
            }
            val unEatUpdate = "update drugTBL set eatDone=0, currentNumber=${preNum} where medId="+medId+";"
            // 목표달성
            val goalUpdate = "update drugTBL set goalDone=1 where medId="+medId+" AND totalNumber<=currentNumber;"

            // 쓰기전용 데이터베이스 변수
            sqlDB = dbManager.writableDatabase
            // 데이터 수정
            if(preStatus==0){
                sqlDB.execSQL(eatUpdate)
                // 복용률 100% 달성했다면
                if(totalNumber<=(preNumber+eatNumber)){
                    sqlDB.execSQL(goalUpdate)
                    Handler(Looper.getMainLooper()).postDelayed({
                        // 복용완료 다이얼로그 실행
                        showDialogGoalDone(medName)
                    }, 500)
                }
            }else if(preStatus==1){
                sqlDB.execSQL(unEatUpdate)
            }
            sqlDB.close()
            dbManager.close()


        }

        // 목표 복용 달성 다이얼로그
        fun showDialogGoalDone(medName: String){
            dialog = Dialog(ApplicationContext())
            dialog.setContentView(R.layout.dialog_complete)

            dialog.window!!.setLayout(
                WindowManager.LayoutParams.MATCH_PARENT,
                WindowManager.LayoutParams.MATCH_PARENT)
            //배경 투명으로 지정(모서리 둥근 배경 보이게 하기)
            dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            dialog.setCanceledOnTouchOutside(false)
            dialog.setCancelable(true)

            dialog.show()

            val txtDialogCompleteName = dialog.findViewById<TextView>(R.id.txtDialogCompleteName)
            txtDialogCompleteName.text = medName

            val btnComplete = dialog.findViewById<Button>(R.id.btnDialogComplete)
            btnComplete.setOnClickListener {
                dialog.dismiss()
            }
        }

        // 자정이 지날 시 복용 버튼 리셋
        fun resetEatdoneBtn(){
            var dbManager: DBManager=DBManager(MainFragment.ApplicationContext(), "drugDB", null, 1)
            var sqlDB: SQLiteDatabase

            // 쓰기전용 데이터베이스 변수
            sqlDB = dbManager.writableDatabase

            // 복용 초기화
            val eatDoneUpdate = "update drugTBL set eatDone=0;"
            sqlDB.execSQL(eatDoneUpdate)

            sqlDB.close()
            dbManager.close()

            val intent = Intent(ApplicationContext(), MainActivity::class.java)
            intent.addFlags (Intent.FLAG_ACTIVITY_NO_ANIMATION)
            ApplicationContext().startActivity(intent)
        }


    }


}