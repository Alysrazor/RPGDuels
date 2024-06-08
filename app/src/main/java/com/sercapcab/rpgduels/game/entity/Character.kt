package com.sercapcab.rpgduels.game.entity

import com.sercapcab.rpgduels.Since
import com.sercapcab.rpgduels.api.model.CharacterData
import com.sercapcab.rpgduels.api.serializer.UUIDSerializer
import com.sercapcab.rpgduels.game.entity.unit.PowerType
import com.sercapcab.rpgduels.game.entity.unit.UnitClass
import com.sercapcab.rpgduels.game.entity.unit.UnitDefense
import com.sercapcab.rpgduels.game.entity.unit.UnitStat
import kotlinx.serialization.Serializable
import java.util.UUID

@Serializable
@Since(version = "1.0")
class Character(
    @get:JvmName(name = "getCharacterUUID")
    @Serializable(with = UUIDSerializer::class)
    override val uuid: UUID,

    @get:JvmName(name = "getCharacterName")
    override var name: String,

    @get:JvmName(name = "getCharacterLevel")
    override var level: Int,

    @get:JvmName(name = "getCharacterClass")
    override var unitClass: UnitClass,

    @get:JvmName(name = "getCharacterDefenses")
    override var unitDefense: UnitDefense,

    @get:JvmName(name = "getCharacterStats")
    override var unitStat: UnitStat,

    @get:JvmName(name = "getCharacterSpells")
    override var spells: Set<Spell>?,

    @get:JvmName(name = "getCharacterPowerType")
    override var powerType: PowerType,

    var characterXP: UInt = 0u,
    var xpToNextLevel: UInt = 0u,
    var account: Account,
): Unit() {
    init {
        setInitialsPowerAmount()
        updateHealthOnLevelOrConstitutionChange(this.level)
    }

    private fun levelUp() {
        this.level++
        updateHealthOnLevelOrConstitutionChange(this.level)
        updatePowerOnIntelligenceChangeOrLevelUp()
    }

    override fun toString(): String {
        return "Character(uuid=$uuid, name='$name', level=$level, unitClass=$unitClass, unitDefense=$unitDefense, unitStat=$unitStat, spells=$spells, powerType=$powerType, characterXP=$characterXP, xpToNextLevel=$xpToNextLevel, account=$account)"
    }
}
