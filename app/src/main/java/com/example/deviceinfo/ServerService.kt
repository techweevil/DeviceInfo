import android.app.Notification
import android.app.PendingIntent
import android.app.Service
import android.content.Context
import android.content.Intent
import android.os.IBinder
import androidx.core.app.NotificationCompat
import com.example.deviceinfo.MainActivity
import com.example.deviceinfo.R
import com.example.deviceinfo.WebSocketServer
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class ServerService : Service() {

    private val webSocketServer = WebSocketServer(this)

    override fun onCreate() {
        super.onCreate()
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        startServer()
        return START_STICKY
    }

    override fun onDestroy() {
        stopServer()
        super.onDestroy()
    }

    private fun startServer() {
        GlobalScope.launch {
            val status = webSocketServer.startServer()
            showNotification("Server is running", status.toString())
        }
    }

    private fun stopServer() {
        webSocketServer.stopServer()
        stopForeground(true)
        stopSelf()
    }

    override fun onBind(intent: Intent?): IBinder? {
        return null
    }

    private fun showNotification(title: String, content: String) {
        val notificationIntent = Intent(this, MainActivity::class.java)
        val pendingIntent = PendingIntent.getActivity(this, 0, notificationIntent,
            PendingIntent.FLAG_IMMUTABLE)

        val notification: Notification = NotificationCompat.Builder(this, CHANNEL_ID)
            .setContentTitle(title)
            .setContentText(content)
            .setSmallIcon(R.drawable.ic_launcher_foreground)
            .setContentIntent(pendingIntent)
            .build()

        startForeground(FOREGROUND_SERVICE_ID, notification)
    }

    companion object {
        const val CHANNEL_ID = "ServerChannel"
        const val FOREGROUND_SERVICE_ID = 1
    }
}
