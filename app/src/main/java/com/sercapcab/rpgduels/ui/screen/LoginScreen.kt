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
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
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
import com.sercapcab.rpgduels.api.model.RegisterDto
import com.sercapcab.rpgduels.api.service.AccountAPIService
import com.sercapcab.rpgduels.api.service.AuthAPIService
import com.sercapcab.rpgduels.isValidEmail
import com.sercapcab.rpgduels.ui.navigation.NavScreens
import com.sercapcab.rpgduels.ui.theme.RPGDuelsTheme
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
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
                                        SignUpContent(
                                            currentLoginContent,
                                            navController
                                        )
                                    }

                                    LoginContent.SignIn -> {
                                        SignInContent(
                                            currentLoginContent,
                                            navController
                                        )
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
        textResId = R.string.string_sign_in_text,
        onClick = {
            loginStatus.value = LoginContent.SignIn
        }
    )
    ButtonWithVerticalSpacer(
        textResId = R.string.string_sign_up_text,
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
    loginStatus: MutableState<LoginContent>,
    navController: NavController
) {
    val showInvalidEmail = rememberSaveable { mutableStateOf(false) }
    val showInvalidInputs = rememberSaveable { mutableStateOf(false) }
    val usernameOrEmailAlreadyInUse = rememberSaveable { mutableStateOf(false) }
    val user = editText(
        labelText = stringResource(id = R.string.string_username_text),
    )
    val email = editText(
        labelText = stringResource(id = R.string.string_email_text),
    )
    val password = editText(
        labelText = stringResource(id = R.string.string_password_text),
        password = true
    )
    Spacer(modifier = Modifier.padding(top = 20.dp))
    Row(
        modifier = Modifier.fillMaxSize(),
        content = {
            ButtonWithHorizontalSpacer(
                textResId = R.string.string_sign_up_text,
                onClick = {
                    if (!email.isValidEmail()) {
                        showInvalidEmail.value = true
                        return@ButtonWithHorizontalSpacer
                    } else if (user.isEmpty() || password.isEmpty()) {
                        showInvalidInputs.value = true
                        return@ButtonWithHorizontalSpacer
                    }

                    CoroutineScope(Dispatchers.IO).launch {
                        val validAccount = createAccount(
                            RegisterDto(username = user, email = email, password = password)
                        )

                        withContext(Dispatchers.Main) {
                            if (validAccount)
                                navController.navigate(NavScreens.GameMenuScreen.route)
                            else
                                usernameOrEmailAlreadyInUse.value = true
                        }
                    }
                }
            )

            ButtonWithHorizontalSpacer(
                textResId = R.string.exit,
                onClick = { loginStatus.value = LoginContent.Login }
            )

            if (showInvalidEmail.value) {
                ErrorAlertDialog(
                    onDismissRequest = { showInvalidEmail.value = false },
                    dialogTitle = R.string.string_error_text,
                    dialogText = R.string.string_error_wrong_email_pattern
                )
            }

            if (showInvalidInputs.value) {
                ErrorAlertDialog(
                    onDismissRequest = { showInvalidInputs.value = false },
                    dialogTitle = R.string.string_error_text,
                    dialogText = R.string.string_error_wrong_credentials
                )
            }

            if (usernameOrEmailAlreadyInUse.value) {
                ErrorAlertDialog(
                    onDismissRequest = { usernameOrEmailAlreadyInUse.value = false },
                    dialogTitle = R.string.string_error_text,
                    dialogText = R.string.string_account_already_exists
                )
            }
        }
    )
}

@Composable
private fun SignInContent(
    loginStatus: MutableState<LoginContent>,
    navController: NavController,
) {
    val isValidUser = rememberSaveable { mutableStateOf(false) }
    val showInvalidUser = rememberSaveable { mutableStateOf(false) }

    val user = editText(
        labelText = stringResource(id = R.string.string_username_text),
    )
    val password = editText(
        labelText = stringResource(id = R.string.string_password_text),
        password = true
    )
    Spacer(modifier = Modifier.padding(top = 20.dp))
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        content = {
            ButtonWithVerticalSpacer(
                textResId = R.string.string_sign_in_text,
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
                onClick = { loginStatus.value = LoginContent.Login }
            )

            if (showInvalidUser.value) {
                ErrorAlertDialog(
                    onDismissRequest = { showInvalidUser.value = false },
                    dialogTitle = R.string.string_error_text,
                    dialogText = R.string.string_error_wrong_credentials
                )
            }
        }
    )
}

/**
 * Comprueba si un usuario existe.
 *
 * @param user El usuario a comprobar.
 * @return - true si el usuario existe.
 * - false si el usuario no existe.
 */
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

/**
 * Crea un nuevo jugador.
 *
 * @param account El usuario a crear.
 * @return - true si el usuario se ha creado correctamente.
 * - false si el usuario o email ya está en uso.
 */
private fun createAccount(account: RegisterDto): Boolean {
    try {
        val retrofit = RetrofitSingleton.getRetrofitInstance()
        val service = retrofit?.create(AuthAPIService::class.java)
        val call = service?.signUp(account)
        val response = call?.execute()

        if (response?.isSuccessful == true) {
            Log.d("LoginScreen", "Response Successful: ${response.code()}")
            return true
        } else {
            Log.d("LoginScreen", "Response Not Successful: ${response?.code()}")
            if (response?.code() == HttpURLConnection.HTTP_BAD_REQUEST)
                return false
        }
    } catch (ex: SocketTimeoutException) {
        Log.d("LoginScreen", "Socket Timeout Exception: $ex")
        return false
    }

    return false
}

@Preview(showSystemUi = false)
@Composable
fun LoginScreenPreview() {
    LoginScreen(navController = NavController(LocalContext.current))
}