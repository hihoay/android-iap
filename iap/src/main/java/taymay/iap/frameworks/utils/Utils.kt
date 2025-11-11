package taymay.iap.frameworks.utils

import android.app.Application
import android.content.Context
import com.android.billingclient.api.PurchasesUpdatedListener
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.engine.android.Android
import io.ktor.client.plugins.HttpTimeout
import io.ktor.client.request.post
import io.ktor.client.request.request
import io.ktor.client.request.setBody
import io.ktor.client.statement.HttpResponse
import io.ktor.http.ContentType
import io.ktor.http.HttpMethod
import io.ktor.http.HttpStatusCode
import io.ktor.http.contentType
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlinx.coroutines.withTimeoutOrNull
import taymay.frameworks.utils.AppEnvironment
import taymay.frameworks.utils.AppEnvironmentEnum
import taymay.frameworks.utils.dlog
import taymay.frameworks.utils.elog
import taymay.iap.frameworks.DialogRemoveAd
import taymay.iap.frameworks.DialogRemoveAd.Companion.IS_PREMIUM
import taymay.iap.frameworks.iap.DataWrappers
import taymay.iap.frameworks.iap.IapConnector
import taymay.iap.frameworks.iap.PurchaseServiceListener
import taymay.iap.frameworks.utils.Singletons.trackingFunction
import java.io.BufferedReader
import java.io.InputStreamReader
import java.util.Arrays
import kotlin.coroutines.CoroutineContext

fun Context.showDialogRemoveAd(idProducts: String, clasHome: Class<*>) {
    val dialogRemoveAd = DialogRemoveAd(this)
    dialogRemoveAd.showDialogRemoveAd(
        idProducts, clasHome
    )
}

fun Context.isPayRemoveAd(): Boolean {
    try {
        return MyCache.getBooleanValueByName(
            this, IS_PREMIUM
        )
    } catch (e: Exception) {
        return MyCache.getBooleanValueByName(this, IS_PREMIUM)

    }
}





fun setupIAP(
    context: Context,
    isTesting: Boolean,
    products: String,
    onPricesUpdated: () -> Unit = {},
    onProductPurchased: () -> Unit = {},
    onProductRestored: () -> Unit = {},
    onPurchaseFailed: () -> Unit = {},
) {

    if (!isTesting) {
        AppEnvironment.setup(context.applicationContext as Application, AppEnvironmentEnum.Main)
    } else {
        AppEnvironment.setup(context.applicationContext as Application, AppEnvironmentEnum.Develop)
    }

    var listNonCons: List<String>
    var listCons: List<String>
    var listSubs: List<String>
    listNonCons = products.split(',').map { it.trim() }
    listCons = Arrays.asList()
    listSubs = Arrays.asList()

    val iapConnector: IapConnector = IapConnector(context, listNonCons, listCons, listSubs)

    val purchaseServiceListener: PurchaseServiceListener = object : PurchaseServiceListener {

        override fun onPricesUpdated(iapKeyPrices: Map<String, DataWrappers.ProductDetails>) {
            dlog("onPricesUpdated")
            onPricesUpdated()
        }

        override fun onProductPurchased(purchaseInfo: DataWrappers.PurchaseInfo) {
            dlog("onProductPurchased")

            onProductPurchased()
            try {
                trackingFunction(
                    "purchased_${purchaseInfo.sku}", mapOf(
                        "orderId" to "${purchaseInfo.orderId}",
                        "purchaseToken" to "${purchaseInfo.purchaseToken}",
                        "signature" to "${purchaseInfo.signature}",
                        "sku" to "${purchaseInfo.sku}",
                    )
                )


            } catch (e: Exception) {
            }
            MyCache.putBooleanValueByName(
                context, IS_PREMIUM, true
            )
        }

        override fun onProductRestored(purchaseInfo: DataWrappers.PurchaseInfo) {
            dlog("onProductRestored")

            onProductRestored()
            try {
                trackingFunction(
                    "restored_${purchaseInfo.sku}", mapOf(
                        "orderId" to "${purchaseInfo.orderId}",
                        "purchaseToken" to "${purchaseInfo.purchaseToken}",
                        "signature" to "${purchaseInfo.signature}",
                        "sku" to "${purchaseInfo.sku}",
                    )
                )


            } catch (e: Exception) {

            }


            if (purchaseInfo.sku in listNonCons) MyCache.putBooleanValueByName(
                context, IS_PREMIUM, true
            )
        }

        override fun onPurchaseFailed(
            purchaseInfo: DataWrappers.PurchaseInfo?, billingResponseCode: Int?
        ) {
            dlog("onPurchaseFailed")

            onPurchaseFailed()

        }
    }
    iapConnector.addPurchaseListener(purchaseServiceListener)


//    if (isOnline(context)) {
//        onDone(true)
////        runWithTimeoutCallback(
////            scope = GlobalScope, timeoutMs = 15000L,
////            defaultValue = mutableListOf(),
////            context = Dispatchers.IO,
////            block = {
////                mutableListOf()
////            }) { results ->
////            elog("Kết quả: ${results.size}")
////            results.forEach {
////            }
////            onDone(results.isNotEmpty())
////        }
//    } else onDone(false)
}

private val purchasesUpdatedListener = PurchasesUpdatedListener { billingResult, purchases ->
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


