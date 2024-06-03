package com.sercapcab.rpgduels.game.map

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
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
import androidx.compose.runtime.MutableIntState
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.rememberCoroutineScope
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
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
import java.util.Locale
import java.util.UUID

private const val TAG = "Scenario"
private const val LIMIT_OF_TURNS = 50

enum class TurnTest {
    PLAYER, AI_PLAYER
}

@Composable
fun Scenario(
    player: Character,
    playerAI: Character,
) {
    // Variables Generales
    val scope = rememberCoroutineScope()

    // Variables de Jugadores
    val characterHealth = rememberSaveable { mutableIntStateOf(player.getHealth()) }
    val characterPower = rememberSaveable { mutableIntStateOf(player.getCurrentPower()) }
    val characterAIHealth = rememberSaveable { mutableIntStateOf(playerAI.getHealth()) }
    val characterAIPower = rememberSaveable { mutableIntStateOf(playerAI.getCurrentPower()) }
    val characterSpells = rememberSaveable { mutableStateOf(player.getUnitSpells()) }

    // Variables del Escenario
    val turnNo = rememberSaveable { mutableIntStateOf(1) }
    val whosTurn = rememberSaveable { mutableStateOf(TurnTest.PLAYER) }
    val whoWon = rememberSaveable { mutableStateOf<Character?>(null) }
    val buttonsEnabled by rememberSaveable { mutableStateOf(whosTurn.value == TurnTest.PLAYER) }

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
                    .weight(1f)
                    .padding(start = 10.dp, top = 20.dp, end = 10.dp),
                whosTurn = whosTurn,
                turnNo = turnNo
            )
            Row(
                modifier = Modifier
                    .weight(1f)
                    .padding(start = 10.dp, end = 10.dp)
            ) {
                // Display Characters
                DisplayCharacterUI(
                    modifier = Modifier
                        .weight(1f)
                        .padding(start = 10.dp, end = 10.dp),
                    character = player,
                    characterHealth = characterHealth,
                    characterPower = characterPower
                )
                Spacer(modifier = Modifier.width(150.dp))
                DisplayCharacterUI(
                    modifier = Modifier
                        .weight(1f)
                        .padding(start = 10.dp, end = 10.dp),
                    character = playerAI,
                    characterHealth = characterAIHealth,
                    characterPower = characterAIPower,
                    isReversed = true
                )
            }

            DisplayCharacterSpellsUI(
                modifier = Modifier
                    .weight(0.75f)
                    .padding(start = 10.dp, end = 10.dp, bottom = 10.dp),
                characterSpells = characterSpells,
                buttonsEnabled = buttonsEnabled,
                onClick = { spell ->
                    playerAction(
                        whosTurn,
                        turnNo,
                        player,
                        spell,
                        playerAI
                    )
                }
            )
        }
    }

    // Lógica del Escenario
    if (whosTurn.value == TurnTest.AI_PLAYER) {
        val spell = playerAI.getUnitSpells().random()

        playerAction(
            whosTurn,
            turnNo,
            playerAI,
            spell,
            player
        )
    }
}

fun playerAction(
    whosTurn: MutableState<TurnTest>,
    turnNo: MutableIntState,
    caster: Character,
    spell: Spell,
    enemy: Character
) {
    Spell.castSpell(caster, spell, enemy)

    when (whosTurn.value) {
        TurnTest.PLAYER -> {
            whosTurn.value = TurnTest.AI_PLAYER
        }
        TurnTest.AI_PLAYER -> {
            whosTurn.value = TurnTest.PLAYER
            turnNo.intValue++
        }
    }
}

@Composable
private fun DisplayTurnUI(
    modifier: Modifier = Modifier,
    whosTurn: MutableState<TurnTest>,
    turnNo: MutableIntState,
) {
    Row(
        modifier = modifier,
    ) {
        TextComposable(
            textId = when (whosTurn.value) {
                TurnTest.PLAYER -> R.string.player_turn
                TurnTest.AI_PLAYER -> R.string.enemy_turn
            },
            modifier = Modifier
                .weight(1f),
            textStyle = TextStyle(
                color = when (whosTurn.value) {
                    TurnTest.PLAYER -> Color.Blue
                    TurnTest.AI_PLAYER -> Color.Red
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
                turnNo.intValue
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
    characterHealth: MutableIntState,
    characterPower: MutableIntState,
    isReversed: Boolean = false,
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
                }
            )
            Column(
                modifier = Modifier.weight(0.33f)
            ) {
                Box(
                    contentAlignment = Alignment.Center,
                ) {
                    Box(
                        modifier = Modifier
                            .width(136.dp)
                            .height(15.dp)
                            .background(Color.Transparent)
                    )
                    Box(
                        modifier = Modifier
                            .width(136.dp)
                            .height(15.dp)
                            .background(Color.Green)
                    ) {
                        Text(
                            modifier = Modifier.fillMaxWidth(),
                            text = "${characterHealth.intValue} / ${character.getMaxHealth()}",
                            style = TextStyle(
                                color = Color.Black,
                                fontSize = 14.sp
                            ),
                            textAlign = TextAlign.Center
                        )
                    }
                }

                Box(
                    contentAlignment = Alignment.Center,
                ) {
                    val powerColor: Color = when (character.getUnitPowerType()) {
                        PowerType.NONE -> Color.Transparent
                        PowerType.RAGE -> Color.Red
                        PowerType.ENERGY -> Color.Yellow
                        PowerType.MANA -> Color.Blue
                    }
                    Box(
                        modifier = Modifier
                            .width(136.dp)
                            .height(15.dp)
                            .background(Color.Transparent)
                    )
                    Box(
                        modifier = Modifier
                            .width(136.dp)
                            .height(15.dp)
                            .background(powerColor)
                    ) {
                        Text(
                            modifier = Modifier.fillMaxWidth(),
                            text = "${characterPower.intValue} / ${character.getMaxPower()}",
                            style = TextStyle(
                                color = Color.White,
                                fontSize = 14.sp
                            ),
                            textAlign = TextAlign.Center
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun DisplayCharacterSpellsUI(
    modifier: Modifier = Modifier,
    characterSpells: MutableState<Set<Spell>>,
    buttonsEnabled: Boolean,
    onClick: (Spell) -> Unit
) {
    LazyVerticalGrid(
        modifier = modifier,
        columns = GridCells.Fixed(2),
        content = {
            items(characterSpells.value.size)  {spell ->
                SpellButton(
                    text = characterSpells.value.elementAt(spell).name,
                    onClick = {
                        onClick(characterSpells.value.elementAt(spell))
                    },
                    enabled = buttonsEnabled
                )
            }
        }
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

private fun handlePlayerAction(
    turnNo: MutableIntState,
    whosTurn: MutableState<TurnTest>,
    whoWon: MutableState<Character?>,
    player: Character,
    playerAI: Character,
    characterHealth: MutableIntState,
    characterAIHealth: MutableIntState
) {
    when {
        turnNo.intValue > LIMIT_OF_TURNS && whosTurn.value == TurnTest.AI_PLAYER -> whoWon.value =
            playerAI

        characterHealth.intValue <= 0 -> whoWon.value = playerAI
        characterAIHealth.intValue <= 0 -> whoWon.value = player
        else -> {
            Log.d(TAG, "Turn: $turnNo")
            when (whosTurn.value) {
                TurnTest.PLAYER -> {
                    Log.d(TAG, "Player Turn")
                    whosTurn.value = TurnTest.AI_PLAYER
                }

                TurnTest.AI_PLAYER -> {
                    Log.d(TAG, "AI Player Turn")
                    turnNo.intValue++
                    whosTurn.value = TurnTest.PLAYER
                }
            }
        }
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
                baseDamage = 5,
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
        spells = setOf(),
        powerType = PowerType.MANA,
        account = accountPlayerAI
    )

    accountPlayer.activeCharacter = characterPlayer
    accountPlayer.activeCharacter = characterPlayerAI



    Scenario(
        player = characterPlayer,
        playerAI = characterPlayerAI
    )
}
