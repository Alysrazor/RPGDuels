package com.sercapcab.rpgduels.api.model

import com.google.gson.annotations.SerializedName
import com.sercapcab.rpgduels.game.entity.unit.Stat
import com.sercapcab.rpgduels.game.entity.SpellSchool
import java.util.UUID

data class Spell(
    @SerializedName("spellUuid") val spellUuid: UUID,
    @SerializedName("spellName") var spellName: String,
    @SerializedName("spellDescription") var spellDescription: String,
    @SerializedName("spellSchool") var spellSchool: SpellSchool,
    @SerializedName("baseDamage") var baseDamage: Int,
    @SerializedName("basePowerCost") var basePowerCost: Int,
    @SerializedName("statModifier") var statModifier: Stat,
    @SerializedName("statMultiplier") var statMultiplier: Double
)
