package com.sercapcab.rpgduels.game.entity

import com.sercapcab.rpgduels.Since
import com.sercapcab.rpgduels.api.serializer.UUIDSerializer
import com.sercapcab.rpgduels.game.entity.unit.PowerType
import com.sercapcab.rpgduels.game.entity.unit.UnitClass
import com.sercapcab.rpgduels.game.entity.unit.UnitDefense
import com.sercapcab.rpgduels.game.entity.unit.UnitStat
import kotlinx.serialization.Serializable
import java.util.UUID

@Serializable
@Since(version = "1.0")
class Creature(
    @Serializable(with = UUIDSerializer::class)
    @get:JvmName("getCreatureUUID")
    override val uuid: UUID,

    @get:JvmName("getCreatureName")
    override var name: String,

    @get:JvmName("getCreatureLevel")
    override var level: Int,

    @get:JvmName("getCreatureUnitClass")
    override val unitClass: UnitClass,

    @get:JvmName("getCreatureStats")
    override var unitStat: UnitStat,

    @get:JvmName("getCreatureDefenses")
    override var unitDefense: UnitDefense,

    @get:JvmName("getCreatureSpells")
    override var spells: Set<Spell>,

    @get:JvmName("getCreaturePowerType")
    override var powerType: PowerType = PowerType.NONE
): Unit() {
    init{
        if (this.level > MAX_LEVEL)
            this.level = MAX_LEVEL

        updateHealthOnLevelOrConstitutionChange(this.level)
        updatePowerOnIntelligenceChangeOrLevelUp()
    }
}
