package com.sercapcab.rpgduels.ui.screen

import android.app.Activity
import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.sercapcab.rpgduels.R
import com.sercapcab.rpgduels.RPGDuelsText
import com.sercapcab.rpgduels.account
import com.sercapcab.rpgduels.api.RetrofitSingleton
import com.sercapcab.rpgduels.api.service.AccountAPIService
import com.sercapcab.rpgduels.api.service.AuthAPIService
import com.sercapcab.rpgduels.datastore.PreferencesManager
import com.sercapcab.rpgduels.ui.navigation.NavScreens
import com.sercapcab.rpgduels.ui.theme.RPGDuelsTheme
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.launch
import okhttp3.Credentials
import java.net.SocketTimeoutException

@Composable
fun SplashScreen(navController: NavController) {
    val scope = rememberCoroutineScope()
    val preferencesManager = PreferencesManager(LocalContext.current)
    val dataLoaded = rememberSaveable {
        mutableStateOf(false)
    }

    val showDialog = rememberSaveable {
        mutableStateOf(false)
    }
    val context = LocalContext.current

    RPGDuelsTheme {
        Log.d("SplashScreen", "Splash Screen Initialized")
        Box(
            modifier = Modifier.fillMaxSize(),
            content = {

                Image(
                    painter = painterResource(id = R.drawable.pantalla_principal),
                    contentDescription = "",
                    contentScale = ContentScale.FillBounds,
                    modifier = Modifier.matchParentSize()
                )
                Column(
                    modifier = Modifier,
                    horizontalAlignment = Alignment.CenterHorizontally,
                    content = {
                        if (!showDialog.value) {
                            RPGDuelsText()
                            Spacer(modifier = Modifier.height(50.dp))
                            CircularProgressIndicator()
                            Spacer(modifier = Modifier.height(16.dp))
                            Text(
                                text = "Loading Data...",
                                style = TextStyle(
                                    fontFamily = fontFamily,
                                    fontSize = 28.sp
                                )
                            )
                        } else
                            RPGDuelsText()
                    }
                )

                LaunchedEffect(Unit) {
                    Log.d("SplashScreen", "isAPIAlive started")
                    val apiRequest = flow {
                        emit(isAPIResponding())
                    }

                    apiRequest.collect { success ->
                        dataLoaded.value = success

                        if (success) {
                            if (preferencesManager.getCredentials().toList().isNotEmpty()) {
                                CoroutineScope(Dispatchers.IO).launch {
                                    val credentials = Credentials.basic(preferencesManager.getCredentials().first, preferencesManager.getCredentials().second)
                                    try {
                                        val retrofit = RetrofitSingleton.getRetrofitInstance()
                                        val service = retrofit?.create(AccountAPIService::class.java)
                                        val call = service?.getAccountByUsername(preferencesManager.getCredentials().first, authHeader = credentials)
                                        val response = call?.execute()

                                        if (response!!.isSuccessful) {
                                            account = response.body()!!.toAccount()
                                            Log.d("SplashScreen", "Account loaded: ${account?.username} ${account?.email}")
                                        }
                                    } catch(ex: SocketTimeoutException) {
                                        Log.d("SplashScreen", "Api is not responding")
                                    }
                                }.join()

                                if (account != null)
                                {
                                    scope.launch {
                                        navController.navigate(NavScreens.GameMenuScreen.route)
                                    }
                                } else
                                    navController.navigate(NavScreens.LoginScreen.route)
                            }
                        }
                        else {
                            showDialog.value = true
                            Log.d("SplashScreen", "API Not responding")
                        }
                    }
                }

                if (showDialog.value) {
                    ErrorAlertDialog(
                        onDismissRequest = {
                            val activity = context as Activity
                            activity.finish()
                        },
                        dialogTitle = R.string.string_error_text,
                        dialogText = R.string.string_error_api_not_responding,
                    )
                }
            }
        )
    }
}

private suspend fun isAPIResponding(): Boolean {
    var isOk = false

    CoroutineScope(Dispatchers.IO).launch {
        try {
            val retrofit = RetrofitSingleton.getRetrofitInstance()

            val service = retrofit?.create(AuthAPIService::class.java)

            val call = service?.getPing()

            val response = call?.execute()

            if (response!!.isSuccessful) {
                Log.d("SplashScreen", "Api is responding")
                isOk = true
            }

        } catch (ex: SocketTimeoutException) {
            Log.d("SplashScreen", "Api is not responding")
            isOk = false
        }
    }.join()

    return isOk
}

@Preview(showSystemUi = false)
@Composable
fun SplashScreenPreview() {
    SplashScreen(navController = NavController(LocalContext.current))
}