package com.example.chick

import android.app.Dialog
import android.app.TimePickerDialog
import android.content.Context
import android.content.Intent
import android.database.sqlite.SQLiteDatabase
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.icu.util.Calendar
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.WindowManager
import android.widget.*
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import java.text.SimpleDateFormat
import java.util.*

class EditAlarmActivity : AppCompatActivity() {
    //아이디
    var medId: Long = 0
    //무슨 약인가요?
    var medName: String = ""
    lateinit var medNameData: EditText
    //약 아이콘
    var medIcon: Int = 1
    lateinit var medChoice: ImageButton
    //몇 시에 복용하나요?
    var ampm: String =""
    var alarmTime: Int = 0
    var alarmHour: Int = -1
    var alarmMin: Int = -1
    lateinit var choiceAmPm: Button
    //언제마다 복용하나요?
    var daysOfWeek: String = ""
    lateinit var btnMon: Button
    lateinit var btnTue: Button
    lateinit var btnWed: Button
    lateinit var btnThu: Button
    lateinit var btnFri: Button
    lateinit var btnSat: Button
    lateinit var btnSun: Button
    //1회당 몇 정씩 복용하나요?
    lateinit var eatNumberData: EditText
    var eatNumber: Int = -1
    //총 목표 복용 개수는 몇 정인가요?
    lateinit var totalNumberData: EditText
    var totalNumber: Int = -1
    //이전 버튼
    lateinit var medEditBack: ImageButton
    //확인 버튼
    lateinit var medEditConfirm: Button
    //취소 버튼
    lateinit var medEditCancel: Button

    //DB
    lateinit var sqlDB: SQLiteDatabase
    lateinit var dbManager: DBManager

    @RequiresApi(Build.VERSION_CODES.N)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_alarm)

        //무슨 약인가요?
        medNameData = findViewById<EditText>(R.id.medName)
        //약 아이콘
        medChoice = findViewById<ImageButton>(R.id.medChoice)
        //1회당 몇 정씩 복용하나요?
        eatNumberData = findViewById<EditText>(R.id.choiceEatNumber)
        //총 목표 복용 개수는 몇 정인가요?
        totalNumberData = findViewById<EditText>(R.id.editTotalNumber)
        //몇 시에 복용하나요?
        choiceAmPm = findViewById<Button>(R.id.choiceAmPm)
        //언제마다 복용하나요?
        btnMon = findViewById<Button>(R.id.btnMon)
        btnTue = findViewById<Button>(R.id.btnTue)
        btnWed = findViewById<Button>(R.id.btnWed)
        btnThu = findViewById<Button>(R.id.btnThu)
        btnFri = findViewById<Button>(R.id.btnFri)
        btnSat = findViewById<Button>(R.id.btnSat)
        btnSun = findViewById<Button>(R.id.btnSun)
        //이전 버튼
        medEditBack = findViewById(R.id.medEditBack)
        //확인 버튼
        medEditConfirm = findViewById(R.id.medEditConfirm)
        //취소 버튼
        medEditCancel = findViewById(R.id.medEditCancel)

        //약 아이콘
        val dialog = CustomDialogEdit(this)
        medChoice.setOnClickListener {
            dialog.medDig()
        }
        dialog.setOnClickedListener(object : CustomDialogEdit.ButtonClickListener {
            override fun onClicked(myName: Int) {
                when(myName) {
                    1 -> medChoice.setImageResource(R.drawable.size_drugs_b_1)
                    2 -> medChoice.setImageResource(R.drawable.size_drugs_b_2)
                    3 -> medChoice.setImageResource(R.drawable.size_drugs_b_3)
                    4 -> medChoice.setImageResource(R.drawable.size_drugs_b_4)
                    5 -> medChoice.setImageResource(R.drawable.size_drugs_b_5)
                    6 -> medChoice.setImageResource(R.drawable.size_drugs_b_6)
                    7 -> medChoice.setImageResource(R.drawable.size_drugs_b_7)
                    8 -> medChoice.setImageResource(R.drawable.size_drugs_b_8)
                }
                medIcon = myName
            }
        })

        fun getTimeP(button: Button, context: Context){
            val cal = Calendar.getInstance()

            val timeSetListener = TimePickerDialog.OnTimeSetListener {timePicker, hour, minute ->
                cal.set(Calendar.HOUR_OF_DAY, hour)
                cal.set(Calendar.MINUTE, minute)

                alarmHour = hour.toInt()
                alarmMin = minute.toInt()

                if(hour >= 13) {
                    ampm = "오후"
                    var textPmH = alarmHour - 12
                    button.text = "오후                  " + textPmH.toString() + "       :       " + alarmMin.toString()
                } else {
                    ampm = "오전"
                    button.text = "오전                  " + alarmHour.toString() + "       :       " + alarmMin.toString()
                }
            }

            val timeP = TimePickerDialog(context, android.R.style.Theme_Holo_Light_Dialog_NoActionBar, timeSetListener, cal.get(Calendar.HOUR_OF_DAY), cal.get(Calendar.MINUTE), false)
            timeP.getWindow()?.setBackgroundDrawableResource(android.R.color.transparent)
            timeP.show()
        }

        choiceAmPm.setOnClickListener {
            getTimeP(choiceAmPm, this)
        }

        //언제마다 복용하나요?
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

        //이전 버튼
        medEditBack.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }

        //확인 버튼
        medEditConfirm.setOnClickListener {
            //아이디
            val currentTime : Long = System.currentTimeMillis()
            val dateFormat = SimpleDateFormat("yyyyMMddHHmmss")
            val curTime: String = dateFormat.format(Date(currentTime)).toString()
            val numberTime = curTime.replace("[^0-9]".toRegex(), "")
            medId = numberTime.toLong()
            //무슨 약인가요?
            medName = medNameData.text.toString()
            //언제마다 복용하나요?
            if(btnMon.isSelected) {
                daysOfWeek += "월"
            } else{}
            if(btnTue.isSelected) {
                daysOfWeek += "화"
            } else{}
            if(btnWed.isSelected) {
                daysOfWeek += "수"
            } else{}
            if(btnThu.isSelected) {
                daysOfWeek += "목"
            } else{}
            if(btnFri.isSelected) {
                daysOfWeek += "금"
            } else{}
            if(btnSat.isSelected) {
                daysOfWeek += "토"
            } else{}
            if(btnSun.isSelected) {
                daysOfWeek += "일"
            } else{}
            //1회당 몇 정씩 복용하나요?
            val eatNumberS = eatNumberData.text.toString()
            //총 목표 복용 개수는 몇 정인가요?
            val totalNumberS = totalNumberData.text.toString()

            if(medName == "") {
                daysOfWeek = ""
                Toast.makeText(this, "약 이름을 입력해주세요.", Toast.LENGTH_LONG).show()
            } else if(ampm == "") {
                daysOfWeek = ""
                Toast.makeText(this, "알람 시간을 입력해주세요.", Toast.LENGTH_LONG).show()
            } else if(alarmHour == -1) {
                daysOfWeek = ""
                Toast.makeText(this, "알람 시간을 입력해주세요.", Toast.LENGTH_LONG).show()
            } else if(alarmMin == -1) {
                daysOfWeek = ""
                Toast.makeText(this, "알람 시간을 입력해주세요.", Toast.LENGTH_LONG).show()
            } else if(daysOfWeek == "") {
                daysOfWeek = ""
                Toast.makeText(this, "알람 요일을 입력해주세요.", Toast.LENGTH_LONG).show()
            } else if(eatNumberS == "") {
                daysOfWeek = ""
                Toast.makeText(this, "1회당 복용 개수를 입력해주세요.", Toast.LENGTH_LONG).show()
            } else if(totalNumberS == "") {
                daysOfWeek = ""
                Toast.makeText(this, "총 목표 개수를 입력해주세요.", Toast.LENGTH_LONG).show()
            } else {
                //1회당 몇 정씩 복용하나요?
                eatNumber = eatNumberS.toInt()
                //총 목표 복용 개수는 몇 정인가요?
                totalNumber = totalNumberS.toInt()

                if(eatNumber > totalNumber) {
                    daysOfWeek = ""
                    Toast.makeText(this, "총 목표 개수를 1회당 복용 개수 이상으로\n입력해주세요.", Toast.LENGTH_LONG).show()
                } else {
                    Log.d("testmedId", medId.toString())
                    Log.d("testmedName", medName)
                    Log.d("testampm", ampm)
                    Log.d("testalarmHour", alarmHour.toString())
                    Log.d("testalarmMin", alarmMin.toString())
                    Log.d("testdaysOfWeek", daysOfWeek)
                    Log.d("testeatNumber", eatNumber.toString())
                    Log.d("testtotalNumber", totalNumber.toString())
                    Log.d("testmedIcon", medIcon.toString())

                    alarmTime = (alarmHour.toString()+alarmMin.toString()).toInt()
                    if(alarmHour>=13){
                        alarmHour = alarmHour-12
                    }

                    //DB 생성
                    dbManager = DBManager(this, "drugDB", null, 1)
                    sqlDB = dbManager.writableDatabase
                    sqlDB.execSQL("INSERT INTO drugTBL VALUES ('"+medId+"', '"+medName+"','"+ampm+"','"+alarmTime+"','"+alarmHour+"','"+alarmMin+"','"+daysOfWeek+"','"+eatNumber+"','"+totalNumber+"',0,'"+medIcon+"',0,0);")
                    sqlDB.close()

                    //화면 전환
                    val intent = Intent(this, MainActivity::class.java)
                    startActivity(intent)
                }
            }
        }

        //취소 버튼
        medEditCancel.setOnClickListener {
            dialog.cancleDig(this)
        }
    }
}

class CustomDialogEdit(context: Context) {
    private val dialog = Dialog(context)

    fun medDig() {
        dialog.setContentView(R.layout.dialog_med_choice)

        dialog.window!!.setLayout(
            WindowManager.LayoutParams.MATCH_PARENT,
            WindowManager.LayoutParams.MATCH_PARENT)
        dialog.getWindow()?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.getWindow()?.clearFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND)
        dialog.setCanceledOnTouchOutside(false)
        dialog.setCancelable(true)

        dialog.show()

        val drugs1 = dialog.findViewById<ImageButton>(R.id.drugs1)
        val drugs2 = dialog.findViewById<ImageButton>(R.id.drugs2)
        val drugs3 = dialog.findViewById<ImageButton>(R.id.drugs3)
        val drugs4 = dialog.findViewById<ImageButton>(R.id.drugs4)
        val drugs5 = dialog.findViewById<ImageButton>(R.id.drugs5)
        val drugs6 = dialog.findViewById<ImageButton>(R.id.drugs6)
        val drugs7 = dialog.findViewById<ImageButton>(R.id.drugs7)
        val drugs8 = dialog.findViewById<ImageButton>(R.id.drugs8)

        drugs1.setOnClickListener {
            onClickedListener.onClicked(1)
            dialog.dismiss()
        }
        drugs2.setOnClickListener {
            onClickedListener.onClicked(2)
            dialog.dismiss()
        }
        drugs3.setOnClickListener {
            onClickedListener.onClicked(3)
            dialog.dismiss()
        }
        drugs4.setOnClickListener {
            onClickedListener.onClicked(4)
            dialog.dismiss()
        }
        drugs5.setOnClickListener {
            onClickedListener.onClicked(5)
            dialog.dismiss()
        }
        drugs6.setOnClickListener {
            onClickedListener.onClicked(6)
            dialog.dismiss()
        }
        drugs7.setOnClickListener {
            onClickedListener.onClicked(7)
            dialog.dismiss()
        }
        drugs8.setOnClickListener {
            onClickedListener.onClicked(8)
            dialog.dismiss()
        }
    }

    fun cancleDig(context: Context) {
        dialog.setContentView(R.layout.dialog_cancle)

        dialog.window!!.setLayout(
            WindowManager.LayoutParams.MATCH_PARENT,
            WindowManager.LayoutParams.WRAP_CONTENT)
        dialog.getWindow()?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.setCanceledOnTouchOutside(false)
        dialog.setCancelable(true)

        dialog.show()

        val cancleN = dialog.findViewById<Button>(R.id.btnDialogCancleN)
        val cancleY = dialog.findViewById<Button>(R.id.btnDialogCancleY)

        cancleN.setOnClickListener {
            dialog.dismiss()
        }
        cancleY.setOnClickListener {
            val intent = Intent(context, MainActivity::class.java)
            context.startActivity(intent)
        }
    }

    interface ButtonClickListener {
        fun onClicked(myName: Int)
    }

    private lateinit var onClickedListener: ButtonClickListener

    fun setOnClickedListener(listener: ButtonClickListener) {
        onClickedListener = listener
    }
}