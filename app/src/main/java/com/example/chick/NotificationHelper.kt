// 알람 클래스

package com.example.chick

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.ContextWrapper
import android.content.Intent
import android.graphics.Color
import android.media.AudioAttributes
import android.media.MediaPlayer
import android.media.Ringtone
import android.media.RingtoneManager
import android.net.Uri
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat
import com.example.chick.MainFragment.Companion.builder

class NotificationHelper(base: Context?) : ContextWrapper(base) {
    private val channelID = "channelID"
    private val channelNm = "channelNm"

    init {
        // 안드로이드 버전이 오레오 이상이면 채널 생성
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            createChannel()
        }
    }

    // 채널 생성
    @RequiresApi(Build.VERSION_CODES.O)
    private fun createChannel(){
        var channel = NotificationChannel(channelID, channelNm, NotificationManager.IMPORTANCE_DEFAULT)

        channel.enableLights(true)      // Unit 불빛 표시
        channel.enableVibration(true)   // 진동
        channel.lightColor = Color.GREEN    // 불빛 색상
        channel.lockscreenVisibility = Notification.VISIBILITY_PRIVATE
        channel.setShowBadge(false)     // 배지 아이콘 출력 없음

        getManager().createNotificationChannel(channel)
    }

    // NotificationManager 생성
    fun getManager(): NotificationManager{
        return getSystemService(NOTIFICATION_SERVICE) as NotificationManager
    }

    // Notificaiton 설정
    fun getChannelNotification(): NotificationCompat.Builder{
        val intent = Intent(this, MedAlarmActivity::class.java)
        val pendingIntent = PendingIntent.getActivity(this, 10, intent, PendingIntent.FLAG_MUTABLE)



//        val uri: Uri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)
//        val audioAttributes = AudioAttributes.Builder()
//            .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
//            .setUsage(AudioAttributes.USAGE_ALARM)
//            .build()

//        val player: MediaPlayer = MediaPlayer.create(this, R.raw.sound_chick)
//        player.start()
//
//        val builder : NotificationCompat.Builder = NotificationCompat.Builder(applicationContext, channelID)
//
//        builder.setContentTitle("삐약삐약")
//        builder.setContentText("오늘 약을 모두 복용하셨나요?")
//        builder.setSmallIcon(R.drawable.ic_alarm)
//        builder.setContentIntent(pendingIntent)
//        builder.setAutoCancel(true)
//        builder.setSound(Uri.parse("android.resource://"+this.packageName+"/"+R.raw.sound_chick))
//
//        return builder



//        val path : Uri = Uri.parse("android.resource://"+packageName+"/raw/sound_chick.mp3")
//        val r3 : Ringtone = RingtoneManager.getRingtone(this, path)
//        r3.play()


        return NotificationCompat.Builder(applicationContext, channelID)
//            .setDefaults(0)
            .setContentTitle("삐약삐약")
            .setContentText("오늘 약을 모두 복용하셨나요?")
            .setSmallIcon(R.drawable.ic_alarm)
//            .setWhen(System.currentTimeMillis())
            .setContentIntent(pendingIntent)
//            .setSound(Uri.parse("android.resource://"+packageName+"/"+R.raw.sound_chick))
//            .setSound(uri, audioAttributes)
//            .setSound(Uri.parse("android.resource://"+packageName+"/raw/sound_chick.mp3"))

            .setAutoCancel(true)
    }
}