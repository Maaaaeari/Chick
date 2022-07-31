package com.example.chick

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log
import android.widget.Toast
import com.example.chick.MainFragment.Companion.resetEatdoneBtn

// 날이 바뀌면 eatDone 버튼 초기화
class ResetEatdone : BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent?) {
        if(Intent.ACTION_DATE_CHANGED == intent!!.action) {
            Toast.makeText(context!!, "날이 바뀌었다.",  Toast.LENGTH_SHORT).show()
            resetEatdoneBtn()
        }
    }
}