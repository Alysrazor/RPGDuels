package com.sercapcab.rpgduels.ui.screen.game

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.sercapcab.rpgduels.R
import com.sercapcab.rpgduels.ui.screen.IconButtonElement

class GameMenuUI {
    companion object {
        /**
         * @return un [BoxScope] con el contenido visual de la pantalla principal del juego
         */
        @Composable
        fun homeContent(): @Composable BoxScope.() -> Unit {
            return  {
                Image(
                    painter = painterResource(id = R.drawable.game_screen),
                    contentDescription = "Imagen de fondo de la pantalla principal del juego",
                    contentScale = ContentScale.FillBounds,
                    modifier = Modifier.fillMaxSize()
                )

                Row(
                    modifier = Modifier
                        .padding(top = 50.dp, start = 5.dp)
                        .height(175.dp)
                        .fillMaxWidth(),
                    content = {
                        /*ImageButton(
                            painter = painterResource(id = R.drawable.tablon_anuncios),
                            onClick = { /*TODO*/ },
                            textId = R.string.announce_string,
                            textColor = Color.White,
                            textSize = 36.sp
                        )
                        Spacer(modifier = Modifier.padding(start = 40.dp))
                        ImageButton(
                            painter = painterResource(id = R.drawable.mailbox_image),
                            onClick = { /*TODO*/ },
                            textId = R.string.mailbox_string,
                            textColor = Color.White,
                            textSize = 36.sp
                        )*/
                    }
                )

            }
        }

        /**
         * @return un [BoxScope] con el contenido visual de la pantalla del selector de nivel de mazmorra
         */
        @Composable
        fun dungeonsContent(): @Composable BoxScope.() -> Unit {
            return {
                Image(
                    painter = painterResource(id = R.drawable.rpgduelsimagestart),
                    contentDescription = "Imagen de fondo de la selección del nivel de mazmorra",
                    contentScale = ContentScale.FillBounds,
                    modifier = Modifier.fillMaxSize()
                )

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    content = {
                        IconButtonElement(
                            onClick = { /*TODO*/ },
                            drawableId = R.drawable.home_icon,
                            stringId = R.string.home_menu
                        )
                    }
                )
            }
        }

        /**
         * @return un [BoxScope] con el contenido visual de la pantalla del selector de personajes
         */
        @Composable
        fun charactersContent(): @Composable BoxScope.() -> Unit {
            return {
                Image(
                    painter = painterResource(id = R.drawable.character_selector),
                    contentDescription = "Imagen de fondo de la selección de personajes",
                    contentScale = ContentScale.FillBounds,
                    modifier = Modifier.fillMaxSize()
                )

                Column(
                    modifier = Modifier.fillMaxSize(),
                    content = {
                        /* TODO Cargar y añadir los personajes del jugador */
                    }
                )
            }
        }

        /**
         * @return un [BoxScope] con el contenido visual de la pantalla del inventario
         */
        @Composable
        fun inventoryContent(): @Composable BoxScope.() -> Unit {
            return {
                Image(
                    painter = painterResource(id = R.drawable.inventory_background),
                    contentDescription = "Imagen de fondo del inventario",
                    contentScale = ContentScale.FillBounds,
                    modifier = Modifier.fillMaxSize()
                )

                Column(
                    modifier = Modifier.fillMaxSize(),
                    content = {
                        /* TODO cargar el inventario del jugador */
                    }
                )
            }
        }

        /**
         * @return un [BoxScope] con el contenido visual de la pantalla de la tienda
         */
        @Composable
        fun shopContent(): @Composable BoxScope.() -> Unit {
            return {
                Image(
                    painter = painterResource(id = R.drawable.inventory_background),
                    contentDescription = "Imagen de fondo de la tienda",
                    contentScale = ContentScale.FillBounds,
                    modifier = Modifier.fillMaxSize()
                )

                Column(
                    modifier = Modifier.fillMaxSize(),
                    content = {
                        /* TODO Cargar la tienda */
                    }
                )
            }
        }
    }
}

