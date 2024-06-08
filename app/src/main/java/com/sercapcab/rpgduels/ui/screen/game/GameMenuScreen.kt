package com.sercapcab.rpgduels.ui.screen.game

import android.content.Context
import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.google.gson.Gson
import com.sercapcab.rpgduels.R
import com.sercapcab.rpgduels.account
import com.sercapcab.rpgduels.api.RetrofitSingleton
import com.sercapcab.rpgduels.api.request.getCharacterByName
import com.sercapcab.rpgduels.api.request.getCharactersFromAccount
import com.sercapcab.rpgduels.api.service.CharacterAPIService
import com.sercapcab.rpgduels.datastore.PreferencesManager
import com.sercapcab.rpgduels.game.entity.Character
import com.sercapcab.rpgduels.game.entity.unit.UnitClass
import com.sercapcab.rpgduels.game.map.MapScreens
import com.sercapcab.rpgduels.ui.navigation.NavScreens
import com.sercapcab.rpgduels.ui.screen.ButtonWithVerticalSpacer
import com.sercapcab.rpgduels.ui.screen.ErrorAlertDialog
import com.sercapcab.rpgduels.ui.screen.IconButtonElement
import com.sercapcab.rpgduels.ui.screen.ImageButton
import com.sercapcab.rpgduels.ui.screen.MyButton
import com.sercapcab.rpgduels.ui.screen.fontFamily
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.Credentials
import java.net.SocketTimeoutException

enum class ContentToShow {
    Main,
    Dungeons,
    Characters,
}

@Composable
fun GameMenuScreen(
    navController: NavController,
    gameMenuViewModel: GameMenuViewModel = viewModel(),
) {
    val characterState by gameMenuViewModel.charactersState.collectAsState()
    val enemyCharacterState by gameMenuViewModel.enemyCharactersState.collectAsState()

    val preferencesManager = PreferencesManager(LocalContext.current)
    val scope = rememberCoroutineScope()

    LaunchedEffect(Unit) {
        var characters: List<Character>? = emptyList()
        var enemyCharacters: List<Character>? = emptyList()

        CoroutineScope(Dispatchers.IO).launch {
            characters = getCharactersFromAccount(
                username = account?.username ?: "",
                authCredentials = preferencesManager.getCredentials()
            )?.map { it.toCharacter() }
        }.join()

        scope.launch {
            Log.d("Characters", characters?.size.toString())

            characters?.let { gameMenuViewModel.updateCharacters(it) }
        }.join()

        CoroutineScope(Dispatchers.IO).launch {
            val alassya = getCharacterByName(
                "Alassya",
                authCredentials = preferencesManager.getCredentials()
            )
            val alysrazor = getCharacterByName(
                "Alysrazor",
                authCredentials = preferencesManager.getCredentials()
            )
            val lorgsa = getCharacterByName(
                "Lorgsa",
                authCredentials = preferencesManager.getCredentials()
            )
            val cordycept = getCharacterByName(
                "Cordycept",
                authCredentials = preferencesManager.getCredentials()
            )

            enemyCharacters =
                listOf(alassya, alysrazor, lorgsa, cordycept).map { it!!.toCharacter() }
        }.join()

        scope.launch {
            enemyCharacters?.let { gameMenuViewModel.updateEnemyCharacters(it) }
        }.join()
    }

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
                        content = { HomeContent() }
                    )

                    ContentToShow.Dungeons -> ShowContent(
                        modifier = modifierContent,
                        content = {
                            DungeonContent(enemyChars = enemyCharacterState, navController = navController)
                        }
                    )

                    ContentToShow.Characters -> ShowContent(
                        modifier = modifierContent,
                        content = { CharacterContent(
                            characters = characterState,
                            gameMenuViewModel = gameMenuViewModel
                        ) }
                    )
                }
            }

            BottomBar(modifier = Modifier.weight(0.15f),
                navController = navController,
                onContentSelected = { contentToShow -> currentContent = contentToShow })
        }
    )
}

@Composable
fun HomeContent() {
    Image(
        painter = painterResource(id = R.drawable.game_screen),
        contentDescription = "Imagen de fondo de la pantalla principal del juego",
        contentScale = ContentScale.FillBounds,
        modifier = Modifier.fillMaxSize()
    )

    Column(
        modifier = Modifier
            .padding(top = 50.dp, start = 5.dp)
            .fillMaxSize(),
        content = {
            Text(
                text = stringResource(id = R.string.string_version_title),
                color = Color.White,
                fontSize = 32.sp,
                fontFamily = fontFamily,
                modifier = Modifier.align(Alignment.CenterHorizontally)
            )
            Spacer(modifier = Modifier.height(20.dp))
            Text(
                text = stringResource(id = R.string.string_version_classes_text),
                color = Color.White,
                fontSize = 24.sp,
                fontFamily = fontFamily,
                modifier = Modifier
                    .padding(start = 20.dp, end = 20.dp)
                    .align(Alignment.CenterHorizontally)
            )
            Spacer(modifier = Modifier.height(20.dp))
            Text(
                text = stringResource(id = R.string.string_version_maps_text),
                color = Color.White,
                fontSize = 24.sp,
                fontFamily = fontFamily,
                modifier = Modifier
                    .padding(start = 20.dp, end = 20.dp)
                    .align(Alignment.CenterHorizontally)
            )
        }
    )
}

@Composable
fun DungeonContent(
    enemyChars: List<Character>,
    navController: NavController
) {
    var showDialog by remember { mutableStateOf(false) }
    val scope = rememberCoroutineScope()

    Image(
        painter = painterResource(id = R.drawable.dungeon_screen),
        contentDescription = "Imagen de fondo de la selección del nivel de mazmorra",
        contentScale = ContentScale.FillBounds,
        modifier = Modifier.fillMaxSize()
    )

    Column(
        modifier = Modifier
            .padding(top = 50.dp, start = 5.dp, end = 5.dp)
            .fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        content = {

            enemyChars.forEach {char ->
                ButtonWithVerticalSpacer(
                    text = char.getUnitName(),
                    onClick = {
                        if (account?.activeCharacter == null)
                            showDialog = true
                        else {
                            val character = account!!.activeCharacter
                            val serializedData = Gson().toJson(arrayOf(character, char))

                            navController.navigate(NavScreens.BattleScreen.route + "/$serializedData")
                        }
                    }
                )
            }
//            LazyVerticalGrid(
//                modifier = Modifier
//                    .padding(top = 20.dp, start = 20.dp, end = 20.dp)
//                    .weight(3.0f),
//                columns = GridCells.Fixed(1),
//            ) {
//                items(enemyChars.size) { index ->
//                    ButtonWithVerticalSpacer(
//                        text = enemyChars.elementAt(index).getUnitName(),
//                        onClick = {
//                            if (account?.activeCharacter == null)
//                                showDialog = true
//                            else {
//                                val character = account!!.activeCharacter
//                                val enemy = enemyChars.elementAt(index)
//                                val serializedData = Gson().toJson(arrayOf(character, enemy))
//
//                                navController.navigate(MapScreens.BattleScenario.route + "/$serializedData")
//                            }
//                        }
//                    )
//                    Spacer(modifier = Modifier.height(20.dp))
//                }
//            }
            //
            if (showDialog) {
                ErrorAlertDialog(
                    onDismissRequest = { showDialog = false },
                    dialogTitle = R.string.string_error_text,
                    dialogText = R.string.string_error_select_character
                )
            }
        }
    )
}

@Composable
fun CharacterContent(
    characters: List<Character>,
    gameMenuViewModel: GameMenuViewModel,
) {
    val character = gameMenuViewModel.selectedCharacterState.collectAsState()
    val selectedButton = gameMenuViewModel.selectedButtonState.collectAsState()

    Image(
        painter = painterResource(id = R.drawable.character_selector),
        contentDescription = "Imagen de fondo de la selección de personajes",
        contentScale = ContentScale.FillBounds,
        modifier = Modifier.fillMaxSize()
    )

    Column(
        modifier = Modifier.fillMaxSize(),
        content = {
            LazyVerticalGrid(
                modifier = Modifier
                    .padding(top = 20.dp)
                    .weight(3.0f),
                columns = GridCells.Fixed(2),
            ) {
                items(characters.size) { index ->
                    ImageButton(
                        painter = when (characters.elementAt(index).getUnitUnitClass()) {
                            UnitClass.CLASS_WIZARD -> painterResource(id = R.drawable.character_mage)
                            UnitClass.CLASS_FIGHTER -> painterResource(id = R.drawable.character_warrior)
                            UnitClass.CLASS_PALADIN -> painterResource(id = R.drawable.character_paladin)
                            UnitClass.CLASS_ROGUE -> painterResource(id = R.drawable.character_rogue)
                            else -> painterResource(id = R.drawable.character_mage)
                        },
                        onClick = {
                            gameMenuViewModel.selectCharacter(character = characters.elementAt(index))
                            gameMenuViewModel.selectButton(index)
                        },
                        text = characters.elementAt(index).getUnitName(),
                        textColor = Color.White,
                        textSize = 28.sp,
                        buttonIndex = index,
                        selectedButton = selectedButton.value == index
                    )
                }
            }
            MyButton(
                textId = R.string.string_select_character,
                modifier = Modifier
                    .align(Alignment.CenterHorizontally)
                    .padding(bottom = 20.dp)
                    .weight(0.33f),
                onClick = {
                    account!!.activeCharacter = gameMenuViewModel.selectedCharacterState.value
                    Log.d("Character", account!!.activeCharacter.toString())
                },
                enabled = character.value != null
                //enabled = character.value != null
            )
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
    content: @Composable (BoxScope.() -> Unit),
) {
    Box(
        modifier = modifier,
        content = { content() }
    )
}


/// Barra de abajo
/**
 * Bottom bar component for the application.
 *
 * @param modifier [Modifier] to be applied to the bottom bar.
 * @param onContentSelected cambia el contenido de la pantalla al pulsar un botón
 */
@Composable
fun BottomBar(
    modifier: Modifier,
    navController: NavController,
    onContentSelected: (ContentToShow) -> Unit,
) {
    val context = LocalContext.current
    val preferencesManager = PreferencesManager(context)

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
                    onClick = {
                        logOut(
                            preferencesManager = preferencesManager,
                            navController = navController
                        )
                    },
                    drawableId = R.drawable.logout_icon,
                    stringId = R.string.logout_string
                )
            }
        )
    }
}

fun logOut(
    preferencesManager: PreferencesManager,
    navController: NavController,
) {
    CoroutineScope(Dispatchers.Main).launch {
        preferencesManager.clearUserData()
        account = null
        navController.navigate(NavScreens.LoginScreen.route)
    }
}

suspend fun selectAICharacter(name: String, context: Context): Character? {
    val preferencesManager = PreferencesManager(context)
    val credentials = preferencesManager.getCredentials()
    return withContext(Dispatchers.IO) {
        try {
            val retrofitSingleton = RetrofitSingleton.getRetrofitInstance()
            val service = retrofitSingleton!!.create(CharacterAPIService::class.java)
            val call = service.getCharacterByName(
                authHeader = Credentials.basic(
                    credentials.first,
                    credentials.second
                ), name = name
            )
            val response = call.execute()
            if (response.isSuccessful) {
                response.body()!!.toCharacter()
            } else null

        } catch (ex: SocketTimeoutException) {
            Log.d("Error", ex.message.toString())
            null
        }
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