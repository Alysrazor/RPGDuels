package com.sercapcab.rpgduels.game.combat

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.sercapcab.rpgduels.R
import com.sercapcab.rpgduels.game.entity.Character
import com.sercapcab.rpgduels.game.entity.Spell
import com.sercapcab.rpgduels.ui.screen.TextComposable
import com.sercapcab.rpgduels.ui.screen.fontFamily
import kotlinx.coroutines.Runnable
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.runBlocking
import java.util.Locale

private const val TAG = "Combat"
private const val LIMIT_OF_TURNS = 50

enum class Turn {
    PLAYER,
    ENEMY
}

sealed class CombatState {
    data object Idle : CombatState()
    data object Running : CombatState()
    data object Ended : CombatState()
    data class Updated(val player: Character, val playerAI: Character, val turnNo: Int): CombatState()
    data class Finished(val winner: Character): CombatState()
}

class Combat(
    private val player: Character,
    private val enemy: Character,
    private val onUpdate: (CombatState) -> Unit
): Runnable {
    private val combatEnded = false
    val turnNo = 1
    val whosTurn = Turn.PLAYER
    val winner: Character? = null
    
    override fun run() {
        onUpdate(CombatState.Running)

        while (!isSomeoneDead()) {
            when (whosTurn) {
                Turn.PLAYER -> {

                }
                Turn.ENEMY -> TODO()
            }
        }
    }

    fun playerAction(
        player: Character,
        spell: Spell
    ) = runBlocking {
        Spell.castSpell(player, spell, enemy).also {
            onUpdate(CombatState.Updated(player, enemy, turnNo))
        }
    }

    private fun isSomeoneDead(): Boolean {
        return player.getHealth() <= 0 || enemy.getHealth() <= 0
    }
}
