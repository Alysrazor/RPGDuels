package com.sercapcab.rpgduels.api.model

import com.google.gson.annotations.SerializedName
import com.sercapcab.rpgduels.game.entity.unit.UnitClass
import java.util.UUID

data class Character(
    @SerializedName("uuid") val uuid: UUID,
    @SerializedName("name") var name: String,
    @SerializedName("level") var level: UInt,
    @SerializedName("unitArmor") var unitArmor: UInt,
    @SerializedName("unitMagicResistance") var unitMagicResistance: UInt,
    @SerializedName("unitClass") var unitClass: UnitClass,
    @SerializedName("characterStat") var characterStat: UnitStat,
    @SerializedName("unitModel") var unitModel: Int,
    @SerializedName("account") var account: Account
)
