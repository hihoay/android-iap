package com.taymay.library.app

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.taymay.library.app.ui.theme.AppTheme
import taymay.iap.frameworks.DialogRemoveAd
import taymay.iap.frameworks.utils.setupIAP

/*
var prop = "show_intro"
var a = getDataRemote("show_intro", prop, false) == true
elog("$prop=", a)
assert(a)

prop = "ad_on_intro_on_top"
var b = getDataRemote("ad_on_intro_on_top", "ad_on_intro_on_top", true)
elog("$prop=", b)
assert(b == false)

prop = "open_app_show_intro_after"
var c = getDataRemote("open_app_show_intro_after", "open_app_show_intro_after", 1)
elog("$prop=", c)
assert(c == 2)

prop = "demo"
var d = getDataRemote("demo", "demo", 1)
elog("$prop=", d)
assert(d == 1)
*/

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        enableEdgeToEdge()

//        checkRemoveAd("remove_ad", false) { isSuccess ->
//            elog("checkRemoveAd", "remove_ad", isSuccess)
//        }

        setupIAP(this.application, true, "remove_ad")
        val key = "remove_ad"
        DialogRemoveAd(this).showDialogRemoveAd(key, MainActivity::class.java)


//
//        val listItemSubscriptionContent = listOf(
//            ItemSubscriptionContent(
//                R.drawable.ic_launcher_foreground,
//                "This is a content 1",
//                "This is a content 1 description"
//            ), ItemSubscriptionContent(
//                R.drawable.ic_launcher_foreground,
//                "This is a content 2",
//                "This is a content 2 description"
//            ), ItemSubscriptionContent(
//                R.drawable.ic_launcher_foreground,
//                "This is a content 3",
//                "This is a content 3 description"
//            )
//        )
//
//        goToIAPActivity(
//            listItemSubscriptionContent,
//            key,
//            MainActivity::class.java.name,
//            isSub = false,
//            title = "Unlock Pro Version",
//            buttonTitle = "Continue"
//        )

        setContent {
            AppTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    Greeting(
                        name = "Android", modifier = Modifier.padding(innerPadding)
                    )
                }
            }
        }


    }
}

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Hello $name!", modifier = modifier
    )
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    AppTheme {
        Greeting("Android")
    }
}