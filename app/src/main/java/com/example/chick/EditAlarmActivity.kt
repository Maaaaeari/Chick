package com.example.chick

import android.app.Dialog
import android.app.TimePickerDialog
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.icu.util.Calendar
import android.os.Build
import android.os.Bundle
import android.widget.*
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContentProviderCompat.requireContext
import java.text.SimpleDateFormat

class EditAlarmActivity : AppCompatActivity() {

    //무슨 약인가요?
    lateinit var medName: String
    lateinit var medNameData: EditText

    //약 아이콘
    var medIcon: Int = 0

    //몇 시에 복용하나요?
    lateinit var ampm: String
    var alarmHour: Int = 0
    var alarmMin: Int = 0
    lateinit var choiceAmPm: Button

    //언제마다 복용하나요?
    lateinit var daysOfWeek: String
    lateinit var btnMon: Button
    lateinit var btnTue: Button
    lateinit var btnWed: Button
    lateinit var btnThu: Button
    lateinit var btnFri: Button
    lateinit var btnSat: Button
    lateinit var btnSun: Button

    //1회당 몇 정씩 복용하나요?
    lateinit var eatNumberData: EditText
    //var eatNumber: Int = 0
    //총 목표 복용 개수는 몇 정인가요?
    lateinit var totalNumberData: EditText
    //var totalNumber: Int = 0
    //확인 버튼
    lateinit var medEditConfirm: Button

    //DB
    lateinit var sqlDB: SQLiteDatabase
    lateinit var dbManager: DBManager

    @RequiresApi(Build.VERSION_CODES.N)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_alarm)

        //무슨 약인가요?
        medNameData = findViewById<EditText>(R.id.medName)
        medName = medNameData.text.toString()

        //약 아이콘
        medIcon = 1

        //몇 시에 복용하나요?
        fun getTime(button: Button, context: Context){
            val cal = Calendar.getInstance()

            val timeSetListener = TimePickerDialog.OnTimeSetListener {timePicker, hour, minute ->
                cal.set(Calendar.HOUR_OF_DAY, hour)
                cal.set(Calendar.MINUTE, minute)

                alarmHour = hour.toInt()
                alarmMin = minute.toInt()

                if(hour >= 12) {
                    ampm = "오후"
                    button.text = SimpleDateFormat("오후                HH       :       mm").format(cal.time)
                } else {
                    ampm = "오전"
                    button.text = SimpleDateFormat("오전                HH       :       mm").format(cal.time)
                }
            }

            val timeP = TimePickerDialog(context, android.R.style.Theme_Holo_Light_Dialog_NoActionBar, timeSetListener, cal.get(Calendar.HOUR_OF_DAY), cal.get(Calendar.MINUTE), false)
            timeP.getWindow()?.setBackgroundDrawableResource(android.R.color.transparent)
            timeP.show()
        }

        choiceAmPm = findViewById<Button>(R.id.choiceAmPm)
        choiceAmPm.setOnClickListener {
            getTime(choiceAmPm, this)
        }

        //언제마다 복용하나요?
        btnMon = findViewById<Button>(R.id.btnMon)
        btnTue = findViewById<Button>(R.id.btnTue)
        btnWed = findViewById<Button>(R.id.btnWed)
        btnThu = findViewById<Button>(R.id.btnThu)
        btnFri = findViewById<Button>(R.id.btnFri)
        btnSat = findViewById<Button>(R.id.btnSat)
        btnSun = findViewById<Button>(R.id.btnSun)

        btnMon?.setOnClickListener {
            btnMon?.isSelected = btnMon?.isSelected != true
        }
        btnTue?.setOnClickListener {
            btnTue?.isSelected = btnTue?.isSelected != true
        }
        btnWed?.setOnClickListener {
            btnWed?.isSelected = btnWed?.isSelected != true
        }
        btnThu?.setOnClickListener {
            btnThu?.isSelected = btnThu?.isSelected != true
        }
        btnFri?.setOnClickListener {
            btnFri?.isSelected = btnFri?.isSelected != true
        }
        btnSat?.setOnClickListener {
            btnSat?.isSelected = btnSat?.isSelected != true
        }
        btnSun?.setOnClickListener {
            btnSun?.isSelected = btnSun?.isSelected != true
        }

        if(btnMon.isSelected == true) {
            daysOfWeek += "월"
        } else{}
        if(btnTue.isSelected == true) {
            daysOfWeek += "화"
        } else{}
        if(btnWed.isSelected == true) {
            daysOfWeek += "수"
        } else{}
        if(btnThu.isSelected == true) {
            daysOfWeek += "목"
        } else{}
        if(btnFri.isSelected == true) {
            daysOfWeek += "금"
        } else{}
        if(btnSat.isSelected == true) {
            daysOfWeek += "토"
        } else{}
        if(btnSun.isSelected == true) {
            daysOfWeek += "일"
        } else{}

        //1회당 몇 정씩 복용하나요?
        eatNumberData = findViewById<EditText>(R.id.choiceEatNumber)
        var eatNumber: Int = Integer.parseInt(eatNumberData.text.toString())
            //eatNumberData.text.toString().toInt()

        //총 목표 복용 개수는 몇 정인가요?
        totalNumberData = findViewById<EditText>(R.id.editTotalNumber)
        var totalNumber: Int = Integer.parseInt(totalNumberData.text.toString())
            //totalNumberData.text.toString().toInt()

        //확인 버튼
        medEditConfirm = findViewById<Button>(R.id.medEditConfirm)
        medEditConfirm.setOnClickListener {
            // DB 생성
            dbManager = DBManager(this, "drugDB", null, 1)
            sqlDB = dbManager.writableDatabase
            //sqlDB.execSQL("INSERT INTO drugTBL VALUES (20605, ${medName},${ampm},${alarmHour},${alarmMin},${daysOfWeek},${eatNumber},${totalNumber},0,${medIcon},0,0)")
            sqlDB.execSQL("INSERT INTO drugTBL VALUES (1234694, '비타민c','오전', 9,00,'월수금',1,50,10,1,0,0)")
            sqlDB.close()
        }
    }
}