package com.sercapcab.rpgduels.ui.screen

import android.app.Activity
import androidx.annotation.StringRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.paddingFromBaseline
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.sercapcab.rpgduels.R
import com.sercapcab.rpgduels.ui.navigation.NavScreens

@Composable
fun MenuScreen(navController: NavController) {
    val finishApp = LocalContext.current as? Activity
    Box(modifier = Modifier.fillMaxSize()) {
        Image(
            painterResource(id = R.drawable.pantalla_principal),
            contentDescription = "",
            contentScale = ContentScale.FillBounds,
            modifier = Modifier.matchParentSize(),
        )

        Column(
            modifier = Modifier,
            horizontalAlignment = Alignment.CenterHorizontally,
            content = {
                RPGDuelsText()
                Spacer(modifier = Modifier.padding(bottom = 60.dp))
                Column(
                    modifier = Modifier,
                    horizontalAlignment = Alignment.CenterHorizontally,
                    content = {
                        ButtonWithVerticalSpacer(
                            textResId = R.string.start_game,
                            onClick = {
                                navController.navigate(NavScreens.GameMenuScreen.route)
                            }
                        )
                        ButtonWithVerticalSpacer(textResId = R.string.menu_options) {

                        }
                        ButtonWithVerticalSpacer(textResId = R.string.exit,
                            onClick = {
                                finishApp?.finish()
                            })
                    }
                )
            }
        )
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

@Preview(showSystemUi = false)
@Composable
fun MenuScreenPreview() {
    MenuScreen(navController = NavController(LocalContext.current))
}