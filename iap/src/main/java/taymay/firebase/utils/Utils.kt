package taymay.firebase.utils

import android.app.Application
import android.content.Context
import android.util.Log
import com.google.gson.Gson
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.engine.android.Android
import io.ktor.client.plugins.HttpTimeout
import io.ktor.client.request.get
import io.ktor.client.request.post
import io.ktor.client.request.request
import io.ktor.client.request.setBody
import io.ktor.client.statement.HttpResponse
import io.ktor.client.statement.bodyAsText
import io.ktor.http.ContentType
import io.ktor.http.HttpMethod
import io.ktor.http.HttpStatusCode
import io.ktor.http.contentType
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlinx.coroutines.withTimeoutOrNull
import org.apache.commons.io.FileUtils
import org.json.JSONObject
import taymay.containers.RepositoryContainer
import taymay.firebase.utils.MyConnection.isOnline
import taymay.frameworks.utils.AppEnvironment
import taymay.frameworks.utils.AppEnvironment.IS_TESTING
import taymay.frameworks.utils.AppEnvironment.httpClient
import taymay.frameworks.utils.AppEnvironmentEnum
import java.io.BufferedReader
import java.io.File
import java.io.InputStreamReader
import java.net.Inet4Address
import java.net.NetworkInterface
import java.net.SocketException
import java.net.URL
import kotlin.coroutines.CoroutineContext


fun setupIAP(
    context: Application,
    isTesting: Boolean,
    onDone: (Boolean) -> Unit = {}
) {
    if (!isTesting) {
        AppEnvironment.setup(context.applicationContext as Application, AppEnvironmentEnum.Main)
    } else {
        AppEnvironment.setup(context.applicationContext as Application, AppEnvironmentEnum.Develop)
    }
    var repositoryContainer = RepositoryContainer(context)


//    if (Singletons.dataRemotes.isNotEmpty() && Singletons.adRemotes.isNotEmpty()) {
//        onDone(true)
//        return
//    }

    if (isOnline(context)) {
        onDone(true)
//        runWithTimeoutCallback(
//            scope = GlobalScope, timeoutMs = 15000L,
//            defaultValue = mutableListOf(),
//            context = Dispatchers.IO,
//            block = {
//                mutableListOf()
//            }) { results ->
//            elog("Kết quả: ${results.size}")
//            results.forEach {
//            }
//            onDone(results.isNotEmpty())
//        }
    } else onDone(false)
}

suspend fun getJSON(url: String, defaultVault: String): String {
    try {
        val client = HttpClient(Android) {
            install(HttpTimeout) {
                requestTimeoutMillis = 5000
                connectTimeoutMillis = 5000
                socketTimeoutMillis = 5000
            }
            engine {
                pipelining = true
            }
            expectSuccess = false
        }
        val response: HttpResponse = client.request(url) {
            method = HttpMethod.Get
        }
        if (response.status == HttpStatusCode.OK) {
            return response.body()
        }
    } catch (e: Exception) {
        e.message?.let { elog("getJSON error", it) }
    }
    return defaultVault
}

suspend fun postJSON(
    url: String, json: String, defaultVault: String
): String {
    try {
        val client = HttpClient(Android)
        val response: HttpResponse = client.post(url) {
            contentType(ContentType.Application.Json)
            setBody(json)
        }
        if (response.status == HttpStatusCode.OK) {
            return response.body()
        }
    } catch (e: Exception) {
        e.message?.let { elog("postJSON error", it) }

    }
    return (defaultVault)
}

fun elog(vararg massage: Any) {
    try {
        var log = ""
        for (obj in massage) {
            log = "$log   $obj"
        }
        if (IS_TESTING) {
            Log.e("Taymay-Log", log)
        }
    } catch (ez: Exception) {
    }
}

fun dlog(vararg massage: Any) {
    try {
        var log = ""
        for (obj in massage) {
            log = "$log   $obj"
        }
        if (IS_TESTING) {
            Log.d("Taymay-Log", log)
        }
    } catch (ez: Exception) {
    }
}

fun Context.getFile(
    urlStr: String, file: String, timout: Int = 12000, callback: (b: Boolean) -> Unit
) {
    GlobalScope.launch {
        try {
            FileUtils.copyURLToFile(
                URL(urlStr), File(file), timout, timout
            )
            MainScope().launch {
                callback(true)
            }
            return@launch
        } catch (e: Exception) {
            elog("getFile Error:", e.message!!)
        }
        MainScope().launch {
            callback(false)
        }

    }
}


fun getLocalIpV4Address(): String {
    try {
        val en = NetworkInterface.getNetworkInterfaces()
        while (en.hasMoreElements()) {
            val intf = en.nextElement()
            val enumIpAddr = intf.inetAddresses
            while (enumIpAddr.hasMoreElements()) {
                val inetAddress = enumIpAddr.nextElement()
                if (!inetAddress.isLoopbackAddress && inetAddress is Inet4Address) {
                    return inetAddress.getHostAddress() ?: ""
                }
            }
        }
    } catch (ex: SocketException) {
        ex.printStackTrace()
    }
    return ""
}

suspend fun getPublicIpV4AddressInfo(): Map<String, Any>? {
    val response = httpClient.get("https://ipinfo.io/json")
    if (response.status == HttpStatusCode.OK) {
        val jsonObject = JSONObject(response.bodyAsText())
        val jsonMap: Map<String, Any> =
            jsonObject.keys().asSequence().associateWith { jsonObject.get(it) }
        elog("getPublicIpV4AddressInfo", "--> Response body: " + jsonMap)
        return jsonMap
    } else {
        return null
    }
}

suspend fun getIpInfo(): Map<String, Any>? {
    val response = httpClient.get("https://ipinfo.io/json")
    if (response.status == HttpStatusCode.OK) {
        val jsonObject = JSONObject(response.bodyAsText())
        val jsonMap: Map<String, Any> =
            jsonObject.keys().asSequence().associateWith { jsonObject.get(it) }
        elog("ipInfo", "--> Response body: " + jsonMap.toString())
        return jsonMap
    } else {
        return null
    }
}

suspend fun getIpAPI(): Map<String, Any>? {
    val response = httpClient.get("http://ip-api.com/json/")
    if (response.status == HttpStatusCode.OK) {
        val jsonObject = JSONObject(response.bodyAsText())
        val jsonMap: Map<String, Any> =
            jsonObject.keys().asSequence().associateWith { jsonObject.get(it) }
        elog("IpAPI", "--> Response body: " + jsonMap.toString())
        return jsonMap
    } else {
        return null
    }
}

data class UserGeoIP(
    var country_code: String = "-",
    var country_name: String = "-",
    var city: String = "-",
    var postal: String = "-",
    var latitude: String = "-",
    var longitude: String = "-",
    var IPv4: String = "-",
    var state: String = "-"
)

suspend fun getUserGeoIP(): UserGeoIP {
    if (Singletons.userGeoIP == null) {
        var userGeoIP = UserGeoIP()
        try {
            var ipInfo = getIpInfo()
            var ipAPI = getIpAPI()
            val response =
                httpClient.get("https://geolocation-db.com/json/67273a00-5c4b-11ed-9204-d161c2da74ce")
            if (response.status == HttpStatusCode.OK) {
                userGeoIP = Gson().fromJson(
                    response.bodyAsText(), UserGeoIP::class.java
                )
            }
            if (userGeoIP.country_code.equals("-")) {
                if (ipAPI != null) {
                    userGeoIP.country_name = ipAPI.get("country").toString()
                    userGeoIP.country_code = ipAPI.get("country").toString()
                    userGeoIP.latitude = ipAPI.get("lat").toString()
                    userGeoIP.longitude = ipAPI.get("lon").toString()
                    userGeoIP.IPv4 = ipAPI.get("query").toString()
                } else if (ipInfo != null) {
                    userGeoIP.country_name = "-"
                    userGeoIP.country_code = ipInfo.get("country").toString()
                    userGeoIP.latitude = ipInfo.get("loc").toString().split(",")[0]
                    userGeoIP.longitude = ipInfo.get("loc").toString().split(",")[1]
                    userGeoIP.IPv4 = ipInfo.get("ip").toString()
                }
            }
            elog("geoIP", "--> Response body: " + userGeoIP.toString())
            Singletons.userGeoIP = userGeoIP
            return userGeoIP
        } catch (e: Exception) {
            e.message?.let { elog("getGeoIP Exception", it) }
        }
        elog("geoIP", "--> Response body: " + userGeoIP.toString())
        return UserGeoIP()
    } else return Singletons.userGeoIP!!

}


fun <T> runWithTimeoutCallback(
    scope: CoroutineScope,
    timeoutMs: Long,
    defaultValue: T,
    context: CoroutineContext = Dispatchers.Default,
    block: suspend CoroutineScope.() -> T,
    callback: (T) -> Unit
) {

    /**
    // Ví dụ gọi từ ViewModel (viewModelScope) hoặc Activity (lifecycleScope)
    runWithTimeoutCallback(
    scope = viewModelScope,            // hoặc lifecycleScope
    timeoutMs = 1500L,
    defaultValue = -1
    ) {
    // Chạy 2 tác vụ song song
    val job1 = async { heavyTaskA() }          // ví dụ: tính toán nặng
    val job2 = async { heavyTaskB() }          // ví dụ: I/O tính giả lập
    // Gộp kết quả khi cả hai xong
    val a = job1.await()
    val b = job2.await()
    a + b                                      // Kết quả cuối cùng (Int)
    } { result ->
    // Callback nhận kết quả (UI thread)
    // Nếu timeout/lỗi -> result = -1
    println("Kết quả: $result")
    }

    // Ví dụ các task nặng giả lập
    suspend fun heavyTaskA(): Int {
    delay(800)      // mô phỏng tải
    return 10
    }
    suspend fun heavyTaskB(): Int {
    delay(1000)     // mô phỏng tải
    return 32
    }



     */

    scope.launch(context) {
        val result: T = try {
            // Hủy toàn bộ job con nếu quá hạn
            withTimeoutOrNull(timeoutMs) {
                // block chạy trong scope hiện tại => có thể dùng async song song bên trong
                block()
            } ?: defaultValue
        } catch (e: CancellationException) {
            // bị hủy (ví dụ scope bị hủy) -> trả default
            defaultValue
        } catch (e: Throwable) {
            // lỗi bất ngờ -> trả default
            defaultValue
        }
        // Trả kết quả về Main (UI) nếu cần
        withContext(Dispatchers.Main) {
            callback(result)
        }
    }
}


fun Context.loadJsonFromAssets(fileName: String): String {
    val inputStream = assets.open(fileName)
    val bufferedReader = BufferedReader(InputStreamReader(inputStream))
    val jsonString = bufferedReader.use { it.readText() }
    return jsonString
}


