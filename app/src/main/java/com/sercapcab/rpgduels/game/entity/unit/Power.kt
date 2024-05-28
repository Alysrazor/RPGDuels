package com.sercapcab.rpgduels.game.entity.unit

import com.sercapcab.rpgduels.Since
import kotlinx.serialization.Serializable

@Since(version = "1.0")
@Serializable
enum class PowerType(private val powerName: String) {
    NONE("None"),
    RAGE("Ira"),
    ENERGY("Energía"),
    MANA("Maná")
}
