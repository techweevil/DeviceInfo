import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.deviceinfo.WebSocketServer
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class ServerViewModel(application: Application) : AndroidViewModel(application) {

    private val webSocketServer = WebSocketServer(application.applicationContext)


    fun startServer() {
        viewModelScope.launch(Dispatchers.IO) {
            val result = webSocketServer.startServer()
            println("resul: $result")
            // Handle the result, update UI, log status, etc.
        }
    }

//    fun stopServer() {
//        viewModelScope.launch(Dispatchers.IO) {
//            webSocketServer.sleep()
//            // Handle the result, update UI, log status, etc.
//        }
//    }
}


