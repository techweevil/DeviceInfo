package com.example.deviceinfo

import android.content.Context
import io.ktor.server.application.Application
import io.ktor.server.application.call
import io.ktor.server.application.install
import io.ktor.server.cio.CIO
import io.ktor.server.engine.embeddedServer
import io.ktor.server.response.*
import io.ktor.server.routing.get
import io.ktor.server.routing.routing
import io.ktor.server.websocket.WebSockets
import io.ktor.server.websocket.pingPeriod
import io.ktor.server.websocket.timeout
import io.ktor.server.websocket.webSocket
import io.ktor.websocket.CloseReason
import io.ktor.websocket.Frame
import io.ktor.websocket.close
import io.ktor.websocket.readText
import java.time.Duration
import android.provider.Settings.Secure
import io.ktor.server.response.respond
import io.ktor.server.application.*
import io.ktor.server.plugins.contentnegotiation.*
import io.ktor.serialization.kotlinx.json.*
import kotlinx.serialization.json.Json
import io.ktor.http.*
import io.ktor.serialization.kotlinx.*
import io.ktor.server.websocket.sendSerialized
import kotlinx.serialization.json.*


fun Application.module(context: Context) {


    val devices = mutableListOf<Device>()

    fun getDeviceId(): String{
        return Secure.getString(context.contentResolver, Secure.ANDROID_ID)
    }


    install(WebSockets) {
        pingPeriod = Duration.ofSeconds(15)
        timeout = Duration.ofSeconds(15)
        maxFrameSize = Long.MAX_VALUE
        masking = false
        contentConverter = KotlinxWebsocketSerializationConverter(Json)
    }

    install(ContentNegotiation) {
        json(Json {
            prettyPrint = true
            isLenient = true
        })
    }


    routing {
        get("/") {
            call.respondText("Hello World!")
        }

        get("/hi") {
            call.respondText("Hello ${call.request.queryParameters["name"]}")
        }

        get("/getId") {
            try {
                val deviceId = getDeviceId()
                devices.add(Device(deviceID = deviceId))
//                call.respondText { deviceId }
                call.respond(Device(deviceID = deviceId))
            } catch (e: Exception) {
                // Log the exception for debugging purposes
                e.printStackTrace()

                // Respond with a 500 Internal Server Error and a descriptive error message
                call.respondText("Internal Server Error: ${e.message}", status = HttpStatusCode.InternalServerError)
            }
        }

        webSocket("/echo") {
            val deviceId = getDeviceId()

            // Convert the device ID to JSON format

            send(Frame.Text(deviceId))

            for (frame in incoming) {
                frame as? Frame.Text ?: continue
                val receivedText = frame.readText()
                if (receivedText.equals("bye", ignoreCase = true)) {
                    close(CloseReason(CloseReason.Codes.NORMAL, "Client said BYE"))
                } else {
                    // You can choose to send something else here if needed
                }
            }
        }
}

fun main() {
}
}




class WebSocketServer(context: Context) {

    private val socketServer = embeddedServer(CIO, host = "127.0.0.1", port = 8083, module = { module(context = context) })

    fun startServer() {
        try {
            socketServer.start(wait = false)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun stopServer() {
        socketServer.stop(1000, 10000)
    }
}
