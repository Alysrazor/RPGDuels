package com.sercapcab.rpgduels

import android.os.Bundle
import android.util.Patterns
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.sercapcab.rpgduels.ui.navigation.AppNavigation
import com.sercapcab.rpgduels.ui.theme.RPGDuelsTheme

class RPGDuelsMain : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            RPGDuelsTheme {
                AppNavigation()
            }
        }
    }
}

fun CharSequence?.isValidEmail(): Boolean {
    return !isNullOrEmpty() &&
            Patterns.EMAIL_ADDRESS.matcher(this).matches()
}

@Preview(showSystemUi = false)
@Composable
fun FirstScreenPreview() {
    AppNavigation()
}
