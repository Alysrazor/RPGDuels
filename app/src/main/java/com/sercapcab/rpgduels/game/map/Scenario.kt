package com.sercapcab.rpgduels.game.map

import android.util.Log
import androidx.annotation.DrawableRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.sercapcab.rpgduels.R
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
import com.sercapcab.rpgduels.ui.screen.SpellButton
import com.sercapcab.rpgduels.ui.screen.TextComposable
import com.sercapcab.rpgduels.ui.screen.fontFamily
import com.sercapcab.rpgduels.ui.theme.RPGDuelsTheme
import java.util.Locale
import java.util.UUID

/**
 * Enumerador que define el turno del juego
 */
enum class Turn {
    PLAYER,
    AI_PLAYER
}

class Scenario(
    val player: Account,
    val playerAI: Account,
    private val limitOfTurns: Int = 50,
) {
    var turnNo = 1
    var whosTurn = Turn.PLAYER
    private var whoWon: Account? = null

    fun handlePlayerAction() {
        // Comprobaciones principales antes de poder avanzar al siguiente turno.
        when {
            whoWon != null -> return
            turnNo >= limitOfTurns && whosTurn == Turn.AI_PLAYER -> whoWon = playerAI
            player.activeCharacter?.getHealth()?.toInt() == 0 -> whoWon = playerAI
            playerAI.activeCharacter?.getHealth()?.toInt() == 0 -> whoWon = player
            else -> {
                Log.d("Scenario", "Turn: $turnNo")
                if (whosTurn == Turn.PLAYER)
                    Log.d("Scenario", "Player turn")
                else
                    Log.d("Scenario", "AI Player turn")
                endTurn()
            }
        }
    }

    private fun endTurn() {
        if (whosTurn == Turn.PLAYER)
            whosTurn = Turn.AI_PLAYER
        else {
            turnNo++
            whosTurn = Turn.PLAYER
        }
    }
}

@Composable
fun ScenarioComposable(
    scenario: Scenario,
    @DrawableRes mapBackground: Int,
) {
    val player = scenario.player
    val playerAI = scenario.playerAI

    val characterPlayer = player.activeCharacter!!
    val characterAIPlayer = playerAI.activeCharacter!!

    val turnNo by rememberSaveable { mutableIntStateOf(scenario.turnNo) }
    val whosTurn by rememberSaveable { mutableStateOf(scenario.whosTurn) }

    Box(
        modifier = Modifier.fillMaxSize(),
        content = {
            Image(
                painter = painterResource(id = mapBackground),
                contentDescription = "Imagen de fondo de la pantalla principal del juego",
                contentScale = ContentScale.FillBounds,
                modifier = Modifier.fillMaxSize()
            )

            Column(
                modifier = Modifier.fillMaxSize(),
                content = {
                    DisplayTurn(
                        modifier = Modifier
                            .weight(0.24f)
                            .padding(start = 10.dp, top = 20.dp, end = 10.dp),
                        turnNo = turnNo, whosTurn = whosTurn
                    )
                    Text(
                        text = "[PH] Acciones Turnos",
                        modifier = Modifier
                            .weight(0.65f)
                            .padding(start = 10.dp, end = 10.dp)
                            .fillMaxWidth()
                    )
                    Spacer(modifier = Modifier.weight(1f))
                    DisplayCharacters(
                        modifier = Modifier
                            .weight(1f)
                            .padding(start = 10.dp, end = 10.dp),
                        characterPlayer = characterPlayer,
                        playerCharacterModel = R.drawable.character_mage,
                        characterAIPlayer = characterAIPlayer,
                        aiPlayerCharacterModel = R.drawable.character_paladin
                    )
                    DisplayPlayerSpellButtons(
                        modifier = Modifier
                            .weight(0.75f)
                            .padding(start = 10.dp, end = 10.dp, bottom = 10.dp),
                        character = characterPlayer
                    )
                }
            )
        }
    )
}

@Composable
fun DisplayTurn(
    modifier: Modifier = Modifier,
    turnNo: Int,
    whosTurn: Turn
) {
    Row(
        modifier = modifier,
        content = {
            if (whosTurn == Turn.PLAYER) {
                TextComposable(
                    textId = R.string.player_turn,
                    modifier = Modifier
                        .weight(1f),
                    textStyle = TextStyle(
                        color = Color.Blue,
                        fontFamily = fontFamily,
                        fontSize = 32.sp
                    )
                )
            } else {
                TextComposable(
                    textId = R.string.enemy_turn,
                    modifier = Modifier
                        .weight(1f)
                        .padding(paddingValues = PaddingValues(all = 20.dp)),
                    textStyle = TextStyle(
                        color = Color.Red,
                        fontFamily = fontFamily,
                        fontSize = 32.sp
                    )
                )
            }

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
    )
}

@Composable
fun DisplayCharacters(
    characterPlayer: Character,
    @DrawableRes playerCharacterModel: Int,
    characterAIPlayer: Character,
    @DrawableRes aiPlayerCharacterModel: Int,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier,
        content = {
            Column(
                modifier = Modifier.weight(1f),
                content = {
                    Image(
                        painter = painterResource(id = playerCharacterModel),
                        contentDescription = "Imagen del personaje del jugador",
                        modifier = Modifier
                            .weight(0.3f)
                    )
                    DisplayHealthAndPowerBars(
                        modifier = Modifier.weight(0.33f),
                        character = characterPlayer
                    )
                }
            )

            Spacer(modifier = Modifier.width(150.dp))

            Column(
                modifier = Modifier.weight(1f),
                content = {
                    Image(
                        painter = painterResource(id = aiPlayerCharacterModel),
                        contentDescription = "Imagen del personaje del jugador",
                        modifier = Modifier
                            .weight(0.3f)
                            .graphicsLayer(scaleX = -1f)
                    )
                    DisplayHealthAndPowerBars(
                        modifier = Modifier.weight(0.33f),
                        character = characterAIPlayer
                    )
                }
            )
        }
    )
}

@Composable
fun DisplayPlayerSpellButtons(
    modifier: Modifier = Modifier,
    character: Character
) {
    val spells = character.getUnitSpells()

    LazyVerticalGrid(
        columns = GridCells.Fixed(2),
        modifier = modifier,
        content = {
            items(spells.size) { spell ->
                SpellButton(
                    text = spells.elementAt(spell).name,
                    onClick = {
                        Log.d("DisplayPlayerSpellButtons", "Spell: ${spells.elementAt(spell).name}")
                    }
                )
            }
        }
    )
}

@Composable
fun DisplayHealthAndPowerBars(
    modifier: Modifier = Modifier,
    character: Character
) {
    val characterHealth by rememberSaveable { mutableIntStateOf(character.getHealth().toInt()) }
    val characterMaxHealth by rememberSaveable { mutableIntStateOf(character.getMaxHealth().toInt()) }
    val characterPowerAmount by rememberSaveable { mutableIntStateOf(character.getCurrentPower().toInt()) }
    val characterMaxPower by rememberSaveable { mutableIntStateOf(character.getMaxPower().toInt()) }

    val powerColor: Color = when (character.getUnitPowerType()) {
        PowerType.NONE -> Color.Transparent
        PowerType.RAGE -> Color.Red
        PowerType.ENERGY -> Color.Yellow
        PowerType.MANA -> Color.Blue
    }

    Column(
        modifier = modifier,
        content = {
            Box(
                contentAlignment = Alignment.Center,
                content = {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(15.dp)
                            .background(Color.Transparent)
                    )
                    Box(modifier = Modifier
                        .fillMaxWidth()
                        .height(15.dp)
                        .background(Color.Green),
                        content = {
                            Text(
                                modifier = Modifier
                                    .fillMaxWidth(),
                                text = "$characterHealth / $characterMaxHealth",
                                style = TextStyle(
                                    color = Color.Black,
                                    fontSize = 14.sp
                                ),
                                textAlign = TextAlign.Center
                            )
                        }
                    )
                }
            )

            if (character.getUnitPowerType() != PowerType.NONE) {
                Box(
                    modifier = modifier,
                    content = {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(15.dp)
                                .background(Color.Transparent)
                        )
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(15.dp)
                                .background(powerColor),
                            content = {
                                Text(
                                    modifier = Modifier
                                        .fillMaxWidth(),
                                    text = "$characterPowerAmount / $characterMaxPower",
                                    style = TextStyle(
                                        color = Color.Black,
                                        fontSize = 14.sp
                                    ),
                                    textAlign = TextAlign.Center
                                )
                            }
                        )
                    }
                )
            }
        }
    )
}

@Preview(showSystemUi = false, showBackground = false)
@Composable
fun PreviewScenarioComposable() {
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
        10u,
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
                spellSchool = SpellSchool.SCHOOL_SLASHING,
                baseDamage = 5,
                basePowerCost = 0,
                statModifier = Stat.STAT_STRENGTH,
                statMultiplier = 1.0
            ),
            Spell(
                uuid = UUID.randomUUID(),
                name = "Bola de Fuego",
                spellSchool = SpellSchool.SCHOOL_FIRE,
                baseDamage = 20,
                basePowerCost = 15,
                statModifier = Stat.STAT_INTELLIGENCE,
                statMultiplier = 1.15
            ),
            Spell(
                uuid = UUID.randomUUID(),
                name = "Rayo de escarcha",
                spellSchool = SpellSchool.SCHOOL_COLD,
                baseDamage = 8,
                basePowerCost = 5,
                statModifier = Stat.STAT_INTELLIGENCE,
                statMultiplier = 1.04
            ),
            Spell(
                uuid = UUID.randomUUID(),
                name = "Telekinesis",
                spellSchool = SpellSchool.SCHOOL_FORCE,
                baseDamage = 30,
                basePowerCost = 20,
                statModifier = Stat.STAT_INTELLIGENCE,
                statMultiplier = 1.2
            ),
            Spell(
                uuid = UUID.randomUUID(),
                name = "Meteoro",
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
        10u,
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
        spells = setOf(),
        powerType = PowerType.MANA,
        account = accountPlayerAI
    )

    accountPlayer.activeCharacter = characterPlayer
    accountPlayer.activeCharacter = characterPlayerAI

    val scenario = Scenario(
        player = accountPlayer,
        playerAI = accountPlayerAI
    )

    RPGDuelsTheme {
        ScenarioComposable(
            scenario = scenario,
            mapBackground = R.drawable.game_screen,
        )
    }
}
