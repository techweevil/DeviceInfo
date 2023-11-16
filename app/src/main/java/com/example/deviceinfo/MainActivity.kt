package com.example.deviceinfo

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.example.deviceinfo.ui.theme.DeviceInfoTheme

class MainActivity : ComponentActivity() {
    private val webSocketServer = WebSocketServer(this)
    private var deviceId by mutableStateOf("")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        handleDeepLink(intent)

        setContent {
            DeviceInfoTheme {
                // A surface container using the 'background' color from the theme
                Column(
                    modifier = Modifier.fillMaxSize(),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Text("Please wait...")
//                    Button(onClick = {
//                        Intent(applicationContext, WebSocketService::class.java).also {
//                            it.action = WebSocketService.Actions.START.toString()
//                            startService(it)
//                        }
//                    }) {
//                        Text(text = "Start")
//                    }
//
//                    Button(onClick = {
//                        Intent(applicationContext, WebSocketService::class.java).also {
//                            it.action = WebSocketService.Actions.STOP.toString()
//                            startService(it)
//                        }
//                    }) {
//                        Text(text = "Stop")
//                    }
                }
            }
        }
    }

    override fun onDestroy() {
        // Stop the WebSocket server when the activity is destroyed
        webSocketServer.stopServer()
        super.onDestroy()
    }

    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)
        handleDeepLink(intent)
    }

    private fun handleDeepLink(intent: Intent?) {
        println("clicked")
        if (intent != null && intent.action == Intent.ACTION_VIEW) {
            val deepLink: Uri? = intent.data
            if (deepLink != null) {
                // Check if the deep link matches your expected format
                if (deepLink.scheme == "device-wss" && deepLink.host == "deviceinfo.com") {
                    // Add a delay of 1 second before minimizing the app
                    Handler(Looper.getMainLooper()).postDelayed({
                        minimizeApp()
                        println("Performed!")
                    }, 2000)

                }
            }
        }
    }

    private fun minimizeApp() {
        // Code to minimize the app goes here
        // You may use custom logic based on your app's architecture
        // For example, you can use moveTaskToBack(true) to minimize the app
        moveTaskToBack(true)
    }
}
