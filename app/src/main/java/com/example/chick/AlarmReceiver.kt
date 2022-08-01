// 리시브 클래스 (Notification 호출)

package com.example.chick

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log
import android.widget.Toast
import androidx.core.app.NotificationCompat
import androidx.core.content.ContextCompat
import androidx.core.content.ContextCompat.getSystemService
import java.util.*

class AlarmReceiver: BroadcastReceiver(){
    override fun onReceive(context: Context?, intent: Intent?) {

        var notificationHelper: NotificationHelper = NotificationHelper(context)
        var nb : NotificationCompat.Builder = notificationHelper.getChannelNotification()

        // 알림 호출
        notificationHelper.getManager().notify(1, nb.build())
    }
}