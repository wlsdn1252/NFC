package com.example.fragmentpractice3

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.ContextWrapper
import android.graphics.Color
import android.os.Build
import android.os.Build.VERSION_CODES
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat

class NotificationHelper(base: Context?) : ContextWrapper(base) {
    private val channelID = "channelID"
    private val channelNm = "channelNm"

    init{
        // 안드로이드 버전이 오레오이상이면 채널생성
        if(Build.VERSION.SDK_INT >= VERSION_CODES.O){
            // 채널생성
            createChannel()
        }
    }

    //채널 생성
    @RequiresApi(VERSION_CODES.O)
    private fun createChannel(){
        var channel = NotificationChannel(channelID,channelNm,
        NotificationManager.IMPORTANCE_DEFAULT)

        channel.enableLights(true)
        channel.enableVibration(true)
        channel.lightColor = Color.GREEN
        channel.lockscreenVisibility = Notification.VISIBILITY_PRIVATE

        getManager().createNotificationChannel(channel)
    }


    // NotificationManager생성
    fun getManager(): NotificationManager{
        return getSystemService(NOTIFICATION_SERVICE) as NotificationManager
    }
    
    //norification 설정
    fun getChannelNotification(time: String?):NotificationCompat.Builder{
        return NotificationCompat.Builder(applicationContext, channelID)
            //.setContentTitle(time)
            .setContentText("약먹어라!!!!!!!!!!!!!!!")
            .setSmallIcon(R.drawable.ic_launcher_background)
    }
}