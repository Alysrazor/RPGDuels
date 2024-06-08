package com.sercapcab.rpgduels.game.combat

import android.util.Log
import androidx.compose.ui.graphics.Color
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sercapcab.rpgduels.game.entity.Character
import com.sercapcab.rpgduels.game.entity.Spell
import com.sercapcab.rpgduels.game.entity.SpellSchool
import com.sercapcab.rpgduels.game.entity.unit.UnitClass
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

private const val LIMIT_OF_TURNS = 50

enum class Turn {
    PLAYER,
    ENEMY
}

class CombatViewModel : ViewModel() {
    private val uiState = MutableStateFlow(CombatUiState())
    private val _imageFilter = MutableStateFlow(Color.Transparent)
    val uiStateFlow: StateFlow<CombatUiState> = uiState.asStateFlow()
    val imageFilter: StateFlow<Color> = _imageFilter.asStateFlow()

    private var player: Character? = null
    private var enemy: Character? = null

    fun startCombat(player: Character, enemy: Character) {
        this.player = player
        this.enemy = enemy

        uiState.value = CombatUiState(
            playerHealth = player.getHealth(),
            playerPower = player.getCurrentPower(),
            playerAIHealth = enemy.getHealth(),
            playerAIPower = enemy.getCurrentPower(),
            playerSpells = player.getUnitSpells(),
            turnNo = 1,
            whosTurn = Turn.PLAYER,
            buttonsEnabled = true,
            whoWon = null
        )
    }

    fun performAction(spell: Spell) {
        val (currentPlayer, currentEnemy) = when (uiState.value.whosTurn) {
            Turn.PLAYER -> player to enemy
            Turn.ENEMY -> enemy to player
        }

        val spellColor = when (spell.spellSchool) {
            SpellSchool.SCHOOL_COLD -> Color.Cyan.copy(alpha = 0.5f)
            SpellSchool.SCHOOL_ACID -> Color.Green.copy(alpha = 0.5f)
            SpellSchool.SCHOOL_BLUDGEONING, SpellSchool.SCHOOL_SLASHING, SpellSchool.SCHOOL_PIERCING, SpellSchool.SCHOOL_FORCE -> Color.Red.copy(
                alpha = 0.5f
            )

            SpellSchool.SCHOOL_LIGHTNING -> Color.White.copy(alpha = 0.5f)
            SpellSchool.SCHOOL_NECROTIC -> Color.Black.copy(alpha = 0.5f)
            SpellSchool.SCHOOL_PHYSIC -> Color.Magenta.copy(alpha = 0.5f)
            SpellSchool.SCHOOL_POISON -> Color.Gray.copy(alpha = 0.5f)
            SpellSchool.SCHOOL_RADIANT, SpellSchool.SCHOOL_FIRE -> Color.Yellow.copy(alpha = 0.5f)
            SpellSchool.SCHOOL_THUNDER -> Color.Blue.copy(alpha = 0.5f)
        }

        val damage = getSpellDamage(
            spell = spell,
            spellType = spell.spellSchool,
            spellCaster = currentPlayer!!,
            target = currentEnemy!!
        )

        when (uiState.value.whosTurn) {
            Turn.PLAYER ->
                handlePlayerTurn(spell, damage)

            Turn.ENEMY ->
                handleEnemyTurn(spell, damage)
        }

        _imageFilter.value = spellColor

        Log.d(
            "CombatViewModel",
            String.format(
                "Vida Personaje: %d%n" +
                        "Poder Personaje: %d%n" +
                        "Vida Enemigo: %d%n" +
                        "Poder Enemigo: %d%n" +
                        "Turno :%d%n" +
                        "Whos Turn: %s%n" +
                        "Daño: %d%n",
                uiState.value.playerHealth,
                uiState.value.playerPower,
                uiState.value.playerAIHealth,
                uiState.value.playerAIPower,
                uiState.value.turnNo,
                uiState.value.whosTurn.name,
                damage
            )
        )
    }

    private fun handlePlayerTurn(spell: Spell, damage: Int) {
        if (spell.name == "Atacar" || spell.name == "Ataque con daga")
            updatePowers(player!!)
        else
            player!!.updatePower(-spell.basePowerCost)

        enemy!!.updateHealth(damage)

        uiState.value = uiState.value.copy(
            playerPower = player!!.getCurrentPower(),
            playerAIHealth = enemy!!.getHealth(),
            buttonsEnabled = false
        )

        viewModelScope.launch {
            resetImageFilter()
            checkVictory()

            if (uiState.value.whoWon != null)
                return@launch
            uiState.value = uiState.value.copy(
                whosTurn = Turn.ENEMY,
            )
            delay(750)
            enemy!!.getUnitSpells()?.let { spells ->
                performAction(spells.filter { spell -> spell.basePowerCost <= enemy!!.getCurrentPower() }
                    .random())
            }
        }
    }

    private fun updatePowers(character: Character) {
        when (character.getUnitUnitClass()) {
            UnitClass.CLASS_FIGHTER -> {
                val amount = (character.getMaxPower() * 0.35).toInt()
                if (amount > character.getMaxPower() - character.getCurrentPower())
                    character.updatePower(character.getMaxPower() - character.getCurrentPower())
                else
                    character.updatePower(amount)
            }
            UnitClass.CLASS_PALADIN -> {
                val amount = (character.getMaxPower() * 0.15).toInt()
                if (amount > character.getMaxPower() - character.getCurrentPower())
                    character.updatePower(character.getMaxPower() - character.getCurrentPower())
                else
                    character.updatePower(amount)
            }
            UnitClass.CLASS_ROGUE -> {
                val amount = (character.getMaxPower() * 0.45).toInt()
                if (amount > character.getMaxPower() - character.getCurrentPower())
                    character.updatePower(character.getMaxPower() - character.getCurrentPower())
                else
                    character.updatePower(amount)
            }
            UnitClass.CLASS_WIZARD -> {
                val amount = (character.getMaxPower() * 0.20).toInt()
                if (amount > character.getMaxPower() - character.getCurrentPower())
                    character.updatePower(character.getMaxPower() - character.getCurrentPower())
                else
                    character.updatePower(amount)
            }
            else -> {}
        }
    }

    private fun handleEnemyTurn(spell: Spell, damage: Int) {
        player!!.updateHealth(damage)
        if (spell.name == "Atacar" || spell.name == "Ataque con daga")
            updatePowers(enemy!!)
        else
            enemy!!.updatePower(-spell.basePowerCost)

        uiState.value = uiState.value.copy(
            playerHealth = player!!.getHealth(),
            playerAIPower = enemy!!.getCurrentPower(),
        )

        viewModelScope.launch {
            resetImageFilter()
            checkVictory()

            if (uiState.value.whoWon != null)
                return@launch

            uiState.value = uiState.value.copy(
                whosTurn = Turn.PLAYER,
                turnNo = uiState.value.turnNo.inc(),
                buttonsEnabled = true
            )
        }
    }

    private fun checkVictory() {
        if (uiState.value.turnNo > LIMIT_OF_TURNS)
            uiState.value = uiState.value.copy(
                whoWon = enemy
            )
        else if (player != null && player!!.getHealth() <= 0) {
            uiState.value = uiState.value.copy(
                whoWon = enemy
            )
        } else if (enemy != null && enemy!!.getHealth() <= 0) {
            uiState.value = uiState.value.copy(
                whoWon = player
            )
        }
    }

    private suspend fun resetImageFilter() {
        viewModelScope.launch {
            delay(350)
            _imageFilter.value = Color.Transparent
        }.join()
    }
}

data class CombatUiState(
    val playerHealth: Int = 0,
    val playerPower: Int = 0,
    val playerAIHealth: Int = 0,
    val playerAIPower: Int = 0,
    val playerSpells: Set<Spell>? = emptySet(),
    val turnNo: Int = 1,
    val whosTurn: Turn = Turn.PLAYER,
    val buttonsEnabled: Boolean = true,
    val whoWon: Character? = null,
)