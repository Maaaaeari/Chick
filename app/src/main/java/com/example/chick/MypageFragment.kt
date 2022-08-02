package com.example.chick

import android.annotation.SuppressLint
import android.app.TimePickerDialog
import kotlin.concurrent.timer
import android.content.Context
import android.content.Context.MODE_PRIVATE
import android.content.Intent
import android.content.SharedPreferences
import android.database.sqlite.SQLiteDatabase
import android.icu.util.Calendar
import android.os.Build
import android.os.Build.VERSION_CODES.S
import android.os.Bundle
import android.os.Handler
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.FragmentTransaction
import com.google.android.material.floatingactionbutton.FloatingActionButton
import android.widget.Switch
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.RequiresApi
import com.example.chick.MainFragment.Companion.instance
import java.text.SimpleDateFormat
import java.util.*
import kotlin.concurrent.fixedRateTimer


class MypageFragment : Fragment() {
    lateinit var btnhelper : Button

    lateinit var switchAlaram : Switch
    lateinit var txtAlramOnOff : TextView

    // 다른 액티비티 함수 호출
    lateinit var mainActivity : MainActivity

    init{
        MypageFragment.instance = this
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        mainActivity = context as MainActivity
    }

    @RequiresApi(Build.VERSION_CODES.N)
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        var view = LayoutInflater.from(activity).inflate(R.layout.fragment_mypage, container, false)

        //바인딩
        btnhelper = view.findViewById(R.id.textView4)

        //도움말클릭시 화면 넘어가기
        btnhelper.setOnClickListener{
            val intent = Intent(activity, HelperActivity::class.java)
            startActivity(intent)
        }

        // 바인딩
        switchAlaram = view.findViewById(R.id.switchAlaram)
        txtAlramOnOff = view.findViewById(R.id.txtAlramOnOff)

        // 앱 종료 전 저장되었던 스위치 값을 가져옴
        loadData()

        // 스위치 체크 상태 변화
        switchAlaram.setOnCheckedChangeListener { button, isChecked ->
            if(isChecked){
                Toast.makeText(context, "알람이 켜졌어요.", Toast.LENGTH_SHORT).show()
                // 타임피커
                getTimeP()
            }else{
                Toast.makeText(context, "알람이 꺼졌어요.", Toast.LENGTH_SHORT).show()
                txtAlramOnOff.text = "알람이 꺼져있어요."
                mainActivity.stopAlarm()
            }
        }

        return view
    }

    @RequiresApi(Build.VERSION_CODES.N)
    fun getTimeP() {
        val cal = Calendar.getInstance()

        val timeSetListener = TimePickerDialog.OnTimeSetListener {timePicker, hour, minute ->
            cal.set(Calendar.HOUR_OF_DAY, hour)
            cal.set(Calendar.MINUTE, minute)

            var hTime = hour.toInt()
            var mTime = minute.toInt()

            var hTimeT: String = ""
            var mTimeT: String = ""

            if(hTime < 10) {
                hTimeT = "0"+hTime.toString()
            } else {
                hTimeT = hTime.toString()
            }
            if(mTime < 10) {
                mTimeT = "0"+mTime.toString()
            } else {
                mTimeT = mTime.toString()
            }

            txtAlramOnOff.text = hTimeT+":"+mTimeT+"에 알람이 설정되었어요."

            mainActivity.onTimeSet(hTime, mTime)
        }

        val timeP = TimePickerDialog(context, android.R.style.Theme_Holo_Light_Dialog_NoActionBar, timeSetListener, cal.get(Calendar.HOUR_OF_DAY), cal.get(Calendar.MINUTE), false)
        timeP.getWindow()?.setBackgroundDrawableResource(android.R.color.transparent)
        timeP.show()
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
            txtAlramOnOff.text = "현재 저녁 약 알람이 켜져있어요."
        }else{
            switchAlaram.isChecked = false
            txtAlramOnOff.text = "현재 저녁 약 알람이 꺼져있어요."
        }
    }

//    fun run(){
//        fixedRateTimer(period = 10000, initialDelay = 1000){
//            // SomethingToDo..
//            selectAlram()
//            if (switchAlaram.isChecked == false)
//                cancel()
//        }
//    }

//    private val mDelayHandler: Handler by lazy {
//        Handler()
//    }
//
//    private fun waitGuest(){
//        mDelayHandler.postDelayed(::showGuest, 10000) // 10초 후에 showGuest 함수를 실행한다.
//    }
//
//    private fun showGuest(){
//        selectAlram()
//        Log.d("infinity", "으으아ㅡ아ㅡ아으ㅏ으아으")
//        waitGuest() // 코드 실행뒤에 계속해서 반복하도록 작업한다.
//    }


    companion object{
        lateinit var instance: MypageFragment

        fun applicationContext() : Context {
            return instance.requireContext()
        }
    }

}