package com.example.deviceinfo

import android.app.Application
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.annotation.RequiresApi

class RunningWebSocket: Application() {
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate() {
        super.onCreate()
        Intent(applicationContext, WebSocketService::class.java).also {
            it.action = WebSocketService.Actions.START.toString()
            startService(it)
        }
        val channel = NotificationChannel(
            "websocket_service",
            "Running SSP Service",
            NotificationManager.IMPORTANCE_HIGH
        )

        val notifcationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notifcationManager.createNotificationChannel(channel)
    }


}