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
import taymay.firebase.utils.elog
import taymay.firebase.utils.getFirebaseRemoteData
import taymay.firebase.utils.setupUMP


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

        setupUMP(hashUMPTest = "F7EB8E91BAB6191FA8F87015557D41A5") {
            elog("setupUMP", it)
        }

        getFirebaseRemoteData(true, "ad_version", "fail") {
            elog("getFirebaseRemoteData", "callback", it)
        }

        getFirebaseRemoteData(true, "data_version", "fail") {
            elog("getFirebaseRemoteData", "callback", it)
        }

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