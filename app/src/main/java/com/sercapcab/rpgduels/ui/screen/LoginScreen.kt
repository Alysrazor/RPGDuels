package com.sercapcab.rpgduels.ui.screen

import android.app.Activity
import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Snackbar
import androidx.compose.material3.SnackbarData
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.sercapcab.rpgduels.R
import com.sercapcab.rpgduels.api.RetrofitSingleton
import com.sercapcab.rpgduels.api.model.LoginDto
import com.sercapcab.rpgduels.api.service.AuthAPIService
import com.sercapcab.rpgduels.ui.navigation.NavScreens
import com.sercapcab.rpgduels.ui.theme.RPGDuelsTheme
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.net.HttpURLConnection
import java.net.SocketTimeoutException

private enum class LoginContent {
    Login,
    SignUp,
    SignIn
}

@Composable
fun LoginScreen(navController: NavController) {
    val finishApp = LocalContext.current as? Activity
    val currentLoginContent = rememberSaveable { mutableStateOf(LoginContent.Login) }

    RPGDuelsTheme {
        Log.d("LoginScreen", "Login Screen Initialized")
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
                    modifier = Modifier.fillMaxSize(),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    content = {
                        RPGDuelsText()
                        Spacer(modifier = Modifier.height(30.dp))
                        Column(
                            modifier = Modifier
                                .padding(start = 50.dp, end = 50.dp)
                                .fillMaxWidth()
                                .height(295.dp)
                                .background(Color.Transparent)
                                .border(5.dp, borderBrush, RectangleShape),

                            horizontalAlignment = Alignment.CenterHorizontally,
                            content = {
                                when (currentLoginContent.value) {
                                    LoginContent.Login -> {
                                        SingUpSingInButtons(finishApp, currentLoginContent)
                                    }

                                    LoginContent.SignUp -> {
                                        SignUpContent(finishApp, currentLoginContent, navController)
                                    }

                                    LoginContent.SignIn -> {
                                        SignInContent(finishApp, navController)
                                    }
                                }
                            }
                        )
                    }
                )
            }
        )
    }
}

@Composable
private fun SingUpSingInButtons(context: Activity?, loginStatus: MutableState<LoginContent>) {
    Spacer(modifier = Modifier.padding(top = 40.dp))
    ButtonWithVerticalSpacer(
        textResId = R.string.sign_in_text,
        onClick = {
            loginStatus.value = LoginContent.SignIn
        }
    )
    ButtonWithVerticalSpacer(
        textResId = R.string.sign_up_text,
        onClick = {
            loginStatus.value = LoginContent.SignUp
        }
    )
    ButtonWithVerticalSpacer(
        textResId = R.string.exit,
        onClick = {
            context?.finish()
        }
    )
}

@Composable
private fun SignUpContent(
    context: Activity?,
    loginStatus: MutableState<LoginContent>,
    navController: NavController
) {
    editText(
        labelText = stringResource(id = R.string.username_text),
    )
    editText(
        labelText = stringResource(id = R.string.email_text),
    )
    editText(
        labelText = stringResource(id = R.string.password_text),
        password = true
    )
    Spacer(modifier = Modifier.padding(top = 20.dp))
    Row(
        modifier = Modifier.fillMaxSize(),
        content = {
            ButtonWithHorizontalSpacer(
                textResId = R.string.sign_up_text,
                onClick = { TODO("Hacer el funcionamiento del botón") }
            )

            ButtonWithHorizontalSpacer(
                textResId = R.string.exit,
                onClick = { context?.finish() }
            )
        }
    )
}

@Composable
private fun SignInContent(
    context: Activity?,
    navController: NavController,
) {
    val isValidUser = rememberSaveable { mutableStateOf(false) }
    val showInvalidUser = rememberSaveable { mutableStateOf(false) }

    val user = editText(
        labelText = stringResource(id = R.string.username_text),
    )
    val password = editText(
        labelText = stringResource(id = R.string.password_text),
        password = true
    )
    Spacer(modifier = Modifier.padding(top = 20.dp))
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        content = {
            ButtonWithVerticalSpacer(
                textResId = R.string.sign_in_text,
                onClick = {
                    if (user.isEmpty() || password.isEmpty()) {
                        showInvalidUser.value = true
                        return@ButtonWithVerticalSpacer
                    }

                    CoroutineScope(Dispatchers.IO).launch {
                        Log.d("LoginScreen", "SignIn Start")

                        isValidUser.value = isValidUser(LoginDto(user, password))
                        Log.d("LoginScreen", "isValidUser: $isValidUser")

                        withContext(Dispatchers.Main) {
                            if (isValidUser.value) {
                                Log.d("LoginScreen", "Valid User")
                                navController.navigate(NavScreens.GameMenuScreen.route)
                            } else {
                                showInvalidUser.value = true
                                Log.d("LoginScreen", "Invalid User")
                            }
                        }

                        Log.d("LoginScreen", "Show Invalid User ${showInvalidUser.value}")
                        Log.d("LoginScreen", "SignIn End")
                    }
                }
            )

            ButtonWithVerticalSpacer(
                textResId = R.string.exit,
                onClick = { context?.finish() }
            )

            if (showInvalidUser.value) {
                ErrorAlertDialog(
                    onDismissRequest = { showInvalidUser.value = false },
                    dialogTitle = R.string.error_text,
                    dialogText = R.string.error_wrong_credentials
                )
            }
        }
    )
}

private fun isValidUser(user: LoginDto): Boolean {
    try {
        val retrofit = RetrofitSingleton.getRetrofitInstance()
        val service = retrofit?.create(AuthAPIService::class.java)
        val call = service?.signIn(user)
        val response = call?.execute()

        if (response?.isSuccessful == true) {
            if (response.code() != HttpURLConnection.HTTP_UNAUTHORIZED)
                return true
        } else
            Log.d("LoginScreen", "Response Not Successful: ${response?.code()}")
    } catch (ex: SocketTimeoutException) {
        Log.d("LoginScreen", "Socket Timeout Exception: $ex")
    }

    return false
}

@Preview(showSystemUi = false)
@Composable
fun LoginScreenPreview() {
    LoginScreen(navController = NavController(LocalContext.current))
}