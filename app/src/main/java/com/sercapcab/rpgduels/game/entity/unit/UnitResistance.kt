package com.sercapcab.rpgduels.game.entity.unit

import android.util.Log
import com.sercapcab.rpgduels.Since
import kotlinx.serialization.Serializable
import org.jetbrains.annotations.ApiStatus

private const val TAG = "UnitDefense"

@ApiStatus.Experimental
@Since(version = "1.0")
@Serializable
enum class DefenseType(private val defenseName: String) {
    Armor("Armadura"),
    MagicResistance("Resistencia Mágica")
}

@ApiStatus.Experimental
@Since(version = "1.0")
@Serializable
class UnitDefense(
    var defenses: Map<DefenseType, Int> = mapOf(
        DefenseType.Armor to 0,
        DefenseType.MagicResistance to 0
    )
) {
    companion object {
        /**
         * Calcula la reducción de daño a partir de la cantidad de armadura específica.
         *
         * @return la cantidad de daño reducida.
         */
        fun calculateDamageReduction(armorAmount: Int): Double {
            Log.d(TAG, "armorAmount: $armorAmount")
            Log.d(TAG, "calculateDamageReduction: ${100.0 / (100.0 + armorAmount.toDouble())}")
            return 100.0 / (100.0 + armorAmount.toDouble())
        }
    }
}
