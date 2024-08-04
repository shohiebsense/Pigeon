package com.shohieb.pigeon

import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.google.gson.Gson
import com.shohieb.pigeon.cache.SharedPreferencesManager
import com.shohieb.pigeon.cache.appGson
import com.shohieb.pigeon.model.MonthlyTransactionPageParameter
import com.shohieb.pigeon.page.monthlist.MonthListPage
import com.shohieb.pigeon.page.monthlytransaction.MonthlyTransactionPage
import com.shohieb.pigeon.ui.theme.PigeonTheme
import java.net.URLDecoder
import java.net.URLEncoder
import java.nio.charset.StandardCharsets

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        val sharedPreferencesManager = SharedPreferencesManager(this)

        setContent {
            PigeonTheme {
                val navController: NavHostController = rememberNavController()
                NavHost(
                    navController = navController,
                    startDestination = MonthListPage.NAME,
                    modifier = Modifier
                        .fillMaxSize()
                ) {

                    composable(route = MonthListPage.NAME) {
                        MonthListPage(sharedPreferencesManager) { monthlyTransactionPageParameter ->
                            Log.e("shohiebsense", "parameter ${monthlyTransactionPageParameter}")
                            val monthlyTransactionPageParameterJson = appGson.toJson(monthlyTransactionPageParameter)
                            navController.navigateEncoded(MonthlyTransactionPage.NAME, monthlyTransactionPageParameterJson)
                        }
                    }

                    composable(route = "${MonthlyTransactionPage.NAME}/{data}") { backStackEntry ->
                        val dataJson = backStackEntry.getData()
                        val parameter = appGson.fromJson(dataJson, MonthlyTransactionPageParameter::class.java)
                        MonthlyTransactionPage(sharedPreferencesManager, parameter, onBackPressed = {
                            navController.popBackStack()
                        })
                    }
                }
            }
        }
    }
}


fun NavHostController.navigateEncoded(pageName: String, data: String) {
    val encoded = URLEncoder.encode(data, StandardCharsets.UTF_8.toString())
    navigate("$pageName/${encoded}")
}

fun NavBackStackEntry.getData() : String {
    val encodedUserJson = arguments?.getString("data")
    val userJson = encodedUserJson?.let { URLDecoder.decode(it, StandardCharsets.UTF_8.toString()) }
    return userJson ?: "{}"
}