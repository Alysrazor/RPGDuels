package com.sercapcab.rpgduels.api.model

import com.google.gson.annotations.SerializedName
import com.sercapcab.rpgduels.game.entity.unit.UnitClass
import com.sercapcab.rpgduels.game.entity.unit.UnitStat
import java.util.UUID

data class Creature(
    @SerializedName("uuid") val uuid: UUID,
    @SerializedName("name") var name: String,
    @SerializedName("level") var level: UInt,
    @SerializedName("unitArmor") var unitArmor: UInt,
    @SerializedName("unitMagicResistance") var unitMagicResistance: UInt,
    @SerializedName("unitClass") var unitClass: UnitClass,
    @SerializedName("unitStats") var unitStat: UnitStat,
    @SerializedName("unitModel") var unitModel: Int,
)
