package com.example.chick

import android.app.TimePickerDialog
import android.content.Context
import android.icu.util.Calendar
import android.os.Build
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.Spinner
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import java.text.SimpleDateFormat

class EditAlarmActivity : AppCompatActivity() {
    @RequiresApi(Build.VERSION_CODES.N)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_alarm)

        fun getTime(button: Button, context: Context){
            val cal = Calendar.getInstance()

            val timeSetListener = TimePickerDialog.OnTimeSetListener {timePicker, hour, minute ->
                cal.set(Calendar.HOUR, hour)
                cal.set(Calendar.MINUTE, minute)

                button.text = SimpleDateFormat("HH  :  mm").format(cal.time)
            }

            val timeP = TimePickerDialog(context, android.R.style.Theme_Holo_Light_Dialog_NoActionBar, timeSetListener, cal.get(Calendar.HOUR_OF_DAY), cal.get(Calendar.MINUTE), true)
            timeP.getWindow()?.setBackgroundDrawableResource(android.R.color.transparent)
            timeP.show()
        }

        val choiceAmPm = findViewById<Button>(R.id.choiceAmPm)

        choiceAmPm.setOnClickListener {
            getTime(choiceAmPm, this)
        }
    }
}