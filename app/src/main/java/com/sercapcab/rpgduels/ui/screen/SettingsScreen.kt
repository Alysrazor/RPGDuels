package com.sercapcab.rpgduels.ui.screen

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.sercapcab.rpgduels.R

@Composable
fun SettingsScreen(navController: NavController) {
    Box(
       modifier = Modifier.fillMaxSize()
    ) {
        Image(
            painter = painterResource(id = R.drawable.rpgduelsimagestart),
            contentDescription = "Imagen de los ajustes",
            contentScale = ContentScale.FillBounds,
            modifier = Modifier.matchParentSize()
        )

        Column(
            modifier = Modifier
                .padding(40.dp)
                .fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            content = {
                TextWithBorder(textId = R.string.settings_string,
                    border = BorderStroke(2.dp, borderBrush),
                    paddingValues = PaddingValues(8.dp),
                    textStyle = TextStyle(
                        fontFamily = fontFamily,
                        fontSize = 32.sp,
                        color = Color.White
                    )
                )

                Column(
                   modifier = Modifier
                       .padding(20.dp)
                       .fillMaxSize(),
                    content = {
                        Row(
                            modifier = Modifier
                                .weight(1f)
                                .padding(8.dp)
                                .fillMaxWidth(),
                            content = {
                                TextWithBorder(
                                    textId = R.string.mute_music_screen,
                                    border = BorderStroke(2.dp, borderBrush),
                                    paddingValues = PaddingValues(8.dp),
                                    textStyle = TextStyle(
                                        fontFamily = fontFamily,
                                        fontSize = 20.sp,
                                        color = Color.White
                                    )
                                )
                                Spacer(
                                    modifier = Modifier.padding(start = 140.dp)
                                )
                                CheckBoxWithBorder()
                            }
                        )
                    }
                )
            }
        )
    }
}

@Preview(showSystemUi = false)
@Composable
fun SettingsScreenPreview() {
    val navController: NavController = NavController(LocalContext.current)

    SettingsScreen(navController = navController)
}