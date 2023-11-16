package com.example.deviceinfo

import android.app.Notification
import android.app.Service
import android.content.Intent
import android.os.Build
import android.os.Handler
import android.os.IBinder
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat

class WebSocketService : Service() {
    private val webSocketServer = WebSocketServer(this)
    private val TIMEOUT_DELAY = 60000 // Timeout duration in milliseconds (e.g., 60 seconds)

    private val timeoutHandler = Handler()
    private val timeoutRunnable = Runnable {
        stopSelf()
    }

    override fun onBind(intent: Intent?): IBinder? {
        return null
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        if (intent != null) {
            when (intent.action) {
                Actions.START.toString() -> start()
                Actions.STOP.toString() -> stop()
            }
        }

        // Schedule the timeout
        timeoutHandler.postDelayed(timeoutRunnable, TIMEOUT_DELAY.toLong())

        return super.onStartCommand(intent, flags, startId)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun start() {
        val notification = NotificationCompat.Builder(this, "websocket_service")
            .setSmallIcon(R.drawable.ic_launcher_foreground)
            .setContentTitle("Run is Active")
            .setContentText("Running SSP Service")
            .build()
        startForeground(1, notification)
        webSocketServer.startServer()
    }

    private fun stop(){
        stopSelf()
        webSocketServer.stopServer()

    }

    override fun onDestroy() {
        // Remove the timeout callback when the service is destroyed
        timeoutHandler.removeCallbacks(timeoutRunnable)
        super.onDestroy()
    }

    enum class Actions {
        START, STOP
    }
}
