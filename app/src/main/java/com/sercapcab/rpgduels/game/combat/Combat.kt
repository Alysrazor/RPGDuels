package com.sercapcab.rpgduels.game.combat

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.sercapcab.rpgduels.R
import com.sercapcab.rpgduels.game.entity.Character
import com.sercapcab.rpgduels.ui.screen.TextComposable
import com.sercapcab.rpgduels.ui.screen.fontFamily
import kotlinx.coroutines.Runnable
import java.util.Locale

private const val TAG = "Combat"
private const val LIMIT_OF_TURNS = 50

enum class Turn {
    PLAYER,
    ENEMY
}

enum class GameResult {
    VICTORY,
    DEFEAT,
    IN_PROGRESS
}

class Combat(
    player: Character,
    enemy: Character
): Runnable {
    var turnNo = 1
    var whosTurn = Turn.PLAYER
    var gameResult = GameResult.IN_PROGRESS

    override fun run() {

    }

    @Composable
    fun DisplayTurn(
        modifier: Modifier = Modifier,
        turnNo: Int,
        whosTurn: Turn,
    ) {
        Row(
            modifier = Modifier,
            content = {
                val textId = when (whosTurn) {
                    Turn.PLAYER -> R.string.player_turn
                    Turn.ENEMY -> R.string.enemy_turn
                }

                TextComposable(
                    textId = textId,
                    modifier = Modifier
                        .weight(1f)
                        .padding(paddingValues = PaddingValues(all = 20.dp)),
                    textStyle = TextStyle(
                        color = Color.White, fontFamily = fontFamily, fontSize = 32.sp
                    )
                )

                Text(
                    text = String.format(
                        Locale.getDefault(),
                        "%s: %d",
                        stringResource(id = R.string.turn_string),
                        turnNo
                    ), modifier = Modifier.weight(1f), textAlign = TextAlign.Center, style = TextStyle(
                        color = Color.White, fontFamily = fontFamily, fontSize = 32.sp
                    )
                )
            }
        )
    }
}