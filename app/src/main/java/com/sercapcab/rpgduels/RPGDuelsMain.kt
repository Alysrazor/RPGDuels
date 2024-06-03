package com.sercapcab.rpgduels

import android.os.Bundle
import android.util.Patterns
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.annotation.StringRes
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.paddingFromBaseline
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.sercapcab.rpgduels.ui.navigation.AppNavigation
import com.sercapcab.rpgduels.ui.screen.fontFamily
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

@Composable
fun RPGDuelsText(modifier: Modifier = Modifier) {
    val alignBodyElementsData = arrayOf(
        R.string.title_screen_string_top,
        R.string.title_screen_string_bottom
    )

    LazyColumn(
        horizontalAlignment = Alignment.CenterHorizontally,
        contentPadding = PaddingValues(horizontal = 16.dp, vertical = 48.dp),
        modifier = modifier.fillMaxWidth(),
        content = {
            items(alignBodyElementsData) { item ->
                AlignTextElement(text = item)
            }
        }
    )
}

@Composable
fun AlignTextElement(
    @StringRes text: Int,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally,
        content = {
            Text(
                text = stringResource(id = text),
                modifier = Modifier.paddingFromBaseline(top = 24.dp, bottom = 8.dp),
                style = TextStyle(
                    fontFamily = fontFamily,
                    fontWeight = FontWeight.Normal,
                    fontSize = 96.sp,
                    letterSpacing = 10.sp,
                    color = Color.Black
                )
            )
        }
    )
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
