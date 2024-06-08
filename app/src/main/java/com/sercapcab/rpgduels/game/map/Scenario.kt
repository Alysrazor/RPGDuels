package com.sercapcab.rpgduels.game.map

import android.app.Activity
import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.google.gson.Gson
import com.sercapcab.rpgduels.R
import com.sercapcab.rpgduels.game.combat.CombatViewModel
import com.sercapcab.rpgduels.game.combat.Turn
import com.sercapcab.rpgduels.game.entity.Account
import com.sercapcab.rpgduels.game.entity.Character
import com.sercapcab.rpgduels.game.entity.Spell
import com.sercapcab.rpgduels.game.entity.SpellSchool
import com.sercapcab.rpgduels.game.entity.unit.DefenseType
import com.sercapcab.rpgduels.game.entity.unit.PowerType
import com.sercapcab.rpgduels.game.entity.unit.Stat
import com.sercapcab.rpgduels.game.entity.unit.UnitClass
import com.sercapcab.rpgduels.game.entity.unit.UnitDefense
import com.sercapcab.rpgduels.game.entity.unit.UnitStat
import com.sercapcab.rpgduels.ui.screen.MyAlertDialog
import com.sercapcab.rpgduels.ui.screen.SpellButton
import com.sercapcab.rpgduels.ui.screen.TextComposable
import com.sercapcab.rpgduels.ui.screen.fontFamily
import java.util.Locale
import java.util.UUID

private const val TAG = "Scenario"

@Composable
fun Scenario(
    data: String?,
    combatViewModel: CombatViewModel = viewModel(),
    navController: NavController,
) {
    val characters = Gson().fromJson(data, Array<Character>::class.java)

    val player = characters?.getOrNull(0)
    val playerAI = characters?.getOrNull(1)

    LaunchedEffect(Unit) {
        combatViewModel.startCombat(player!!, playerAI!!)
        Log.d(TAG, player.toString())
        Log.d(TAG, playerAI.toString())
    }
    val activity = LocalContext.current as? Activity
    val uiState by combatViewModel.uiStateFlow.collectAsState()
    val imageFilter by combatViewModel.imageFilter.collectAsState()

    // Layout del Escenario
    Box(
        modifier = Modifier.fillMaxSize(),
    ) {
        Image(
            painter = painterResource(id = R.drawable.pantalla_principal),
            contentDescription = "Imagen del Escenario",
            contentScale = ContentScale.FillBounds,
            modifier = Modifier.fillMaxSize()
        )
        Column(
            modifier = Modifier.fillMaxSize(),
        ) {
            // Display Turn
            DisplayTurnUI(
                modifier = Modifier
                    .weight(0.75f)
                    .padding(start = 10.dp, top = 20.dp, end = 10.dp),
                whosTurn = uiState.whosTurn,
                turnNo = uiState.turnNo
            )
            Row(
                modifier = Modifier
                    .weight(1f)
                    .padding(start = 10.dp, end = 10.dp)
            ) {
                // Display Characters
                val showImageFilterPlayer =
                    if (uiState.whosTurn == Turn.ENEMY) imageFilter else Color.Transparent
                val showImageFilterEnemy =
                    if (uiState.whosTurn == Turn.PLAYER) imageFilter else Color.Transparent
                DisplayCharacterUI(
                    modifier = Modifier
                        .weight(1f)
                        .padding(start = 10.dp, end = 10.dp),
                    character = player!!,
                    characterHealth = uiState.playerHealth,
                    characterPower = uiState.playerPower,
                    imageFilter = showImageFilterPlayer
                )
                Spacer(modifier = Modifier.width(150.dp))
                DisplayCharacterUI(
                    modifier = Modifier
                        .weight(1f)
                        .padding(start = 10.dp, end = 10.dp),
                    character = playerAI!!,
                    characterHealth = uiState.playerAIHealth,
                    characterPower = uiState.playerAIPower,
                    isReversed = true,
                    imageFilter = showImageFilterEnemy
                )
            }

            uiState.playerSpells?.let {
                DisplayCharacterSpellsUI(
                    modifier = Modifier
                        .weight(0.75f)
                        .padding(start = 10.dp, end = 10.dp, bottom = 10.dp)
                        .fillMaxWidth(),
                    characterSpells = it.toList(),
                    buttonsEnabled = uiState.buttonsEnabled,
                    combatViewModel = combatViewModel
                )
            }

            if (uiState.whoWon != null) {
                Log.d(TAG, uiState.whoWon.toString())
                MyAlertDialog(
                    onDismissRequest = { navController.popBackStack() },
                    dialogTitle = when (uiState.whoWon!!.getUnitName()) {
                        player?.getUnitName() -> R.string.string_victory
                        playerAI?.getUnitName() -> R.string.string_defeat
                        else -> {
                            R.string.string_error_text
                        }
                    },
                    dialogText = when (uiState.whoWon!!.getUnitName()) {
                        player?.getUnitName() -> R.string.string_victory_text
                        playerAI?.getUnitName() -> R.string.string_defeat_text
                        else -> {
                            R.string.string_error_text
                        }
                    }
                )
            }
        }
    }
}

@Composable
private fun DisplayTurnUI(
    modifier: Modifier = Modifier,
    whosTurn: Turn,
    turnNo: Int,
) {
    Row(
        modifier = modifier,
    ) {
        TextComposable(
            textId = when (whosTurn) {
                Turn.PLAYER -> R.string.player_turn
                Turn.ENEMY -> R.string.enemy_turn
            },
            modifier = Modifier
                .weight(1f),
            textStyle = TextStyle(
                color = when (whosTurn) {
                    Turn.PLAYER -> Color.Blue
                    Turn.ENEMY -> Color.Red
                },
                fontFamily = fontFamily,
                fontSize = 32.sp
            )
        )

        Text(
            modifier = Modifier
                .weight(1f),
            text = String.format(
                Locale.getDefault(),
                "%s: %d",
                stringResource(id = R.string.turn_string),
                turnNo
            ),
            textAlign = TextAlign.Center,
            style = TextStyle(
                color = Color.White,
                fontFamily = fontFamily,
                fontSize = 32.sp
            )
        )
    }
}

@Composable
private fun DisplayCharacterUI(
    modifier: Modifier = Modifier,
    character: Character,
    characterHealth: Int,
    characterPower: Int,
    isReversed: Boolean = false,
    imageFilter: Color = Color.Transparent,
) {
    Row(
        modifier = modifier
    ) {
        Column(
            modifier = Modifier.weight(1f)
        ) {
            Image(
                painter = painterResource(id = loadCharacterModel(character = character)),
                contentDescription = "Imagen del personaje del jugador",
                modifier = if (isReversed) {
                    Modifier
                        .weight(0.3f)
                        .graphicsLayer(scaleX = -1f)
                } else {
                    Modifier
                        .weight(0.3f)
                },
                colorFilter = ColorFilter.tint(imageFilter, blendMode = BlendMode.SrcAtop)
            )
            Column(
                modifier = Modifier.weight(0.33f)
            ) {
                val powerColor: Color = when (character.getUnitPowerType()) {
                    PowerType.NONE -> Color.Transparent
                    PowerType.RAGE -> Color.Red
                    PowerType.ENERGY -> Color.Yellow
                    PowerType.MANA -> Color.Blue
                }
                val healthPercentage = characterHealth.toFloat() / character.getMaxHealth()
                val powerPercentage = characterPower.toFloat() / character.getMaxPower()

                Box(
                    modifier = Modifier
                        .width(136.dp)
                        .height(15.dp)
                ) {
                    LinearProgressIndicator(
                        progress = { healthPercentage },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(15.dp),
                        color = Color.Green,
                        trackColor = Color.Transparent
                    )
                    // Box transparente para contener el texto
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(Color.Transparent) // Fondo transparente
                    ) {
                        Text(
                            text = "$characterHealth / ${character.getMaxHealth()}",
                            modifier = Modifier.align(Alignment.Center),
                            style = TextStyle(
                                color = Color.Black,
                                fontSize = 10.sp
                            )
                        )
                    }
                }
                Box(
                    modifier = Modifier
                        .width(136.dp)
                        .height(15.dp)
                ) {
                    LinearProgressIndicator(
                        progress = { powerPercentage },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(15.dp),
                        color = powerColor,
                        trackColor = Color.Transparent
                    )
                    // Box transparente para contener el texto
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(Color.Transparent) // Fondo transparente
                    ) {
                        Text(
                            text = "$characterPower / ${character.getMaxPower()}",
                            modifier = Modifier.align(Alignment.Center),
                            style = TextStyle(
                                color = Color.White,
                                fontSize = 10.sp
                            )
                        )
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun DisplayCharacterSpellsUI(
    modifier: Modifier = Modifier,
    characterSpells: List<Spell>,
    buttonsEnabled: Boolean,
    combatViewModel: CombatViewModel,
) {
    FlowRow(
        modifier = modifier,
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        characterSpells.forEach { spell ->
            SpellButton(
                text = spell.name,
                enabled = buttonsEnabled && combatViewModel.uiStateFlow.value.playerPower >= spell.basePowerCost,
                onClick = {
                    combatViewModel.performAction(spell)
                },
            )
        }
    }
}

@Composable
fun FistIcon(modifier: Modifier = Modifier) {
    Icon(
        painter = painterResource(id = R.drawable.fist_icon),
        contentDescription = "Fist Icon",
        modifier = modifier
    )
}

private fun loadCharacterModel(character: Character): Int {
    return when (character.getUnitUnitClass()) {
        UnitClass.CLASS_FIGHTER -> R.drawable.character_warrior
        UnitClass.CLASS_WIZARD -> R.drawable.character_mage
        UnitClass.CLASS_PALADIN -> R.drawable.character_paladin
        UnitClass.CLASS_ROGUE -> R.drawable.character_rogue
        else -> -1
    }
}

@Preview(showSystemUi = false)
@Composable
fun ScenarioPreview() {
    val accountPlayer = Account(
        accountUuid = UUID.randomUUID(),
        username = "Alysrazor",
        email = "alysrazor@rpgduels.es",
        accountLocked = false,
        activeCharacter = null
    )

    val accountPlayerAI = Account(
        accountUuid = UUID.randomUUID(),
        username = "RPG Duels",
        email = "rpgduels@rpgduels.es",
        accountLocked = false,
        activeCharacter = null
    )

    val characterPlayer = Character(
        uuid = UUID.randomUUID(),
        name = "Alysrazor",
        10,
        UnitClass.CLASS_WIZARD,
        unitDefense = UnitDefense(
            defenses = mapOf(
                DefenseType.Armor to 10,
                DefenseType.MagicResistance to 10
            )
        ),
        unitStat = UnitStat(
            uuid = UUID.randomUUID(),
            stats = mapOf(
                Stat.STAT_STRENGTH to 10,
                Stat.STAT_DEXTERITY to 13,
                Stat.STAT_CONSTITUTION to 15,
                Stat.STAT_INTELLIGENCE to 18,
                Stat.STAT_WISDOM to 12,
                Stat.STAT_CHARISMA to 14
            )
        ),
        spells = setOf(
            Spell(
                uuid = UUID.randomUUID(),
                name = "Atacar",
                description = "Ataca al objetivo con el arma",
                spellSchool = SpellSchool.SCHOOL_SLASHING,
                baseDamage = 1,
                basePowerCost = 0,
                statModifier = Stat.STAT_STRENGTH,
                statMultiplier = 1.0
            ),
            Spell(
                uuid = UUID.randomUUID(),
                name = "Bola de Fuego",
                description = "Ataca al objetivo con una bola de fuego",
                spellSchool = SpellSchool.SCHOOL_FIRE,
                baseDamage = 20,
                basePowerCost = 15,
                statModifier = Stat.STAT_INTELLIGENCE,
                statMultiplier = 1.15
            ),
            Spell(
                uuid = UUID.randomUUID(),
                name = "Rayo de escarcha",
                description = "Ataca al objetivo con un rayo de escarcha",
                spellSchool = SpellSchool.SCHOOL_COLD,
                baseDamage = 8,
                basePowerCost = 5,
                statModifier = Stat.STAT_INTELLIGENCE,
                statMultiplier = 1.04
            ),
            Spell(
                uuid = UUID.randomUUID(),
                name = "Telekinesis",
                description = "Ataca al objetivo con telekinesis",
                spellSchool = SpellSchool.SCHOOL_FORCE,
                baseDamage = 30,
                basePowerCost = 20,
                statModifier = Stat.STAT_INTELLIGENCE,
                statMultiplier = 1.2
            ),
            Spell(
                uuid = UUID.randomUUID(),
                name = "Meteoro",
                description = "Ataca al objetivo con un meteoro",
                spellSchool = SpellSchool.SCHOOL_FIRE,
                baseDamage = 40,
                basePowerCost = 30,
                statModifier = Stat.STAT_INTELLIGENCE,
                statMultiplier = 1.5
            )
        ),
        powerType = PowerType.MANA,
        account = accountPlayer
    )

    val characterPlayerAI = Character(
        uuid = UUID.randomUUID(),
        name = "Player",
        10,
        UnitClass.CLASS_PALADIN,
        unitDefense = UnitDefense(
            defenses = mapOf(
                DefenseType.Armor to 10,
                DefenseType.MagicResistance to 10
            )
        ),
        unitStat = UnitStat(
            uuid = UUID.randomUUID(),
            stats = mapOf(
                Stat.STAT_STRENGTH to 18,
                Stat.STAT_DEXTERITY to 14,
                Stat.STAT_CONSTITUTION to 16,
                Stat.STAT_INTELLIGENCE to 8,
                Stat.STAT_WISDOM to 12,
                Stat.STAT_CHARISMA to 14
            )
        ),
        spells = setOf(
            Spell(
                uuid = UUID.randomUUID(),
                name = "Atacar",
                description = "Ataca al objetivo con el arma",
                spellSchool = SpellSchool.SCHOOL_SLASHING,
                baseDamage = 1,
                basePowerCost = 0,
                statModifier = Stat.STAT_STRENGTH,
                statMultiplier = 1.0
            ),
        ),
        powerType = PowerType.MANA,
        account = accountPlayerAI
    )

    accountPlayer.activeCharacter = characterPlayer
    accountPlayer.activeCharacter = characterPlayerAI

    val navController = NavController(LocalContext.current)
    Scenario(
        data = Gson().toJson(arrayOf(characterPlayer, characterPlayerAI)),
        navController = navController
    )
}