package com.sercapcab.rpgduels.api.model

import com.google.gson.annotations.SerializedName
import com.sercapcab.rpgduels.game.entity.Creature
import com.sercapcab.rpgduels.game.entity.unit.DefenseType
import com.sercapcab.rpgduels.game.entity.unit.PowerType
import com.sercapcab.rpgduels.game.entity.unit.UnitClass
import com.sercapcab.rpgduels.game.entity.unit.UnitDefense
import com.sercapcab.rpgduels.game.entity.unit.UnitStat
import java.util.UUID

data class CreatureData(
    @SerializedName("uuid") val uuid: UUID,
    @SerializedName("name") var name: String,
    @SerializedName("level") var level: Int,
    @SerializedName("unitArmor") var unitArmor: Int,
    @SerializedName("unitMagicResistance") var unitMagicResistance: Int,
    @SerializedName("unitClass") var unitClass: UnitClass,
    @SerializedName("unitStats") var unitStat: UnitStatData,
    @SerializedName("spells") var spells: Set<SpellData>
) {
    fun toCreature(): Creature {
        return Creature(
            uuid = uuid,
            name = name,
            level = level,
            unitClass = unitClass,
            unitStat = unitStat.toUnitStat(),
            unitDefense = UnitDefense(
                defenses = mapOf(
                    DefenseType.Armor to unitArmor,
                    DefenseType.MagicResistance to unitMagicResistance
                )
            ),
            spells = spells.map { it.toSpell() }.toSet(),
        )
    }
}
