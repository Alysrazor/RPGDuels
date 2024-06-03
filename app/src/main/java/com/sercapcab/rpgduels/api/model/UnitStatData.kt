package com.sercapcab.rpgduels.api.model

import com.google.gson.annotations.SerializedName
import com.sercapcab.rpgduels.game.entity.unit.Stat.*
import com.sercapcab.rpgduels.game.entity.unit.UnitStat
import java.util.UUID

data class UnitStatData(
    @SerializedName("uuid") val uuid: UUID,
    @SerializedName("strength") var strength: Int,
    @SerializedName("dexterity") var dexterity: Int,
    @SerializedName("constitution") var constitution: Int,
    @SerializedName("intelligence") var intelligence: Int,
    @SerializedName("wisdom") var wisdom: Int,
    @SerializedName("charisma") var charisma: Int,
    @SerializedName("comment") var comment: String,
) {
    fun toUnitStat(): UnitStat {
        return UnitStat(
            uuid = uuid,
            stats = mapOf(
                STAT_STRENGTH to strength,
                STAT_DEXTERITY to dexterity,
                STAT_CONSTITUTION to constitution,
                STAT_INTELLIGENCE to intelligence,
                STAT_WISDOM to wisdom,
                STAT_CHARISMA to charisma
            )
        )
    }
}
