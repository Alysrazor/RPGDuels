package com.sercapcab.rpgduels.ui.screen.game

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.sercapcab.rpgduels.R
import com.sercapcab.rpgduels.ui.theme.RPGDuelsTheme
import com.sercapcab.rpgduels.ui.screen.IconButtonElement

enum class ContentToShow {
    Main,
    Dungeons,
    Characters,
    Inventory,
    Shop
}

@Composable
fun GameMenuScreen(navController: NavController) {
    RPGDuelsTheme {
        Column(
            modifier = Modifier
                .fillMaxSize(),
            content = {
                var currentContent by rememberSaveable {
                    mutableStateOf(ContentToShow.Main)
                }

                val modifierContent = Modifier
                    .weight(1.0f, false)
                    .fillMaxSize()

                Box(
                    modifier = Modifier
                        .weight(1.0f, false)
                        .fillMaxSize()
                )
                {
                    when (currentContent) {
                        ContentToShow.Main -> ShowContent(
                            modifier = modifierContent,
                            content = GameMenuUI.homeContent()
                        )

                        ContentToShow.Dungeons -> ShowContent(
                            modifier = modifierContent,
                            content = GameMenuUI.dungeonsContent()
                        )

                        ContentToShow.Characters -> ShowContent(
                            modifier = modifierContent,
                            content = GameMenuUI.charactersContent()
                        )

                        ContentToShow.Inventory -> ShowContent(
                            modifier = modifierContent,
                            content = GameMenuUI.inventoryContent()
                        )

                        ContentToShow.Shop -> ShowContent(
                            modifier = modifierContent,
                            content = GameMenuUI.shopContent()
                        )
                    }
                }

                BottomBar(modifier = Modifier.weight(0.15f),
                    onContentSelected = { contentToShow -> currentContent = contentToShow })
            }
        )
    }
}

/// Barra de Arriba
/**
 * Top bar component for the application.
 *
 * @param modifier [Modifier] to be applied to the top bar.
 */
@Composable
fun TopBar(modifier: Modifier) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .background(Color.Gray.copy(alpha = 0.2f)),
        content = {
            Box(
                modifier = Modifier
                    .background(color = Color.Transparent)
                    .border(
                        width = 2.dp,
                        color = Color(0xFF546E3D),
                        shape = MaterialTheme.shapes.medium
                    )
                    .padding(5.dp),
                contentAlignment = Alignment.CenterStart
            ) {
                Text(
                    text = stringResource(id = R.string.shop_string),
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onBackground
                )
            }
        }
    )
}

/// Contenido principal de la pantalla

/**
 * Cambia el contenido de la pantalla principal sin tocar las barras.
 *
 * @param modifier los modificadores.
 * @param content el contenido de [Box]
 */
@Composable
fun ShowContent(
    modifier: Modifier = Modifier,
    content: @Composable (BoxScope.() -> Unit)
) {
    Box(
        modifier = modifier,
    ) {
        content()
    }
}


/// Barra de abajo
/**
 * Bottom bar component for the application.
 *
 * @param modifier [Modifier] to be applied to the bottom bar.
 * @param onContentSelected cambia el contenido de la pantalla al pulsar un botón
 */
@Composable
fun BottomBar(modifier: Modifier, onContentSelected: (ContentToShow) -> Unit) {
    BottomAppBar(
        containerColor = Color.Gray.copy(alpha = 0.25f),
        tonalElevation = 8.dp,
        modifier = modifier
    ) {
        Row(
            content = {
                IconButtonElement(
                    onClick = { onContentSelected(ContentToShow.Main) },
                    drawableId = R.drawable.home_icon,
                    stringId = R.string.home_menu
                )

                IconButtonElement(
                    onClick = { onContentSelected(ContentToShow.Dungeons) },
                    drawableId = R.drawable.dungeon_icon,
                    stringId = R.string.dungeon_string
                )

                IconButtonElement(
                    onClick = { onContentSelected(ContentToShow.Characters) },
                    drawableId = R.drawable.character_icon,
                    stringId = R.string.character_menu
                )

                IconButtonElement(
                    onClick = { onContentSelected(ContentToShow.Inventory) },
                    drawableId = R.drawable.inventory_icon,
                    stringId = R.string.inventory_string
                )

                IconButtonElement(
                    onClick = { onContentSelected(ContentToShow.Shop) },
                    drawableId = R.drawable.shop_icon,
                    stringId = R.string.shop_string
                )
            }
        )
    }
}

@Preview(
    showSystemUi = false,
    showBackground = false
)
@Composable
fun GameScreenPreview() {
    // Esta variable es solo para hacer la preview. No se debe usar para otros fines dentro de esta función
    val navController: NavController = NavController(LocalContext.current)
    GameMenuScreen(navController = navController)
}