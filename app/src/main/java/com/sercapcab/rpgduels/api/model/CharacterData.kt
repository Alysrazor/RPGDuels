package com.sercapcab.rpgduels.api.model

import com.google.gson.annotations.SerializedName
import com.sercapcab.rpgduels.game.entity.Character
import com.sercapcab.rpgduels.game.entity.unit.DefenseType
import com.sercapcab.rpgduels.game.entity.unit.PowerType
import com.sercapcab.rpgduels.game.entity.unit.UnitClass
import com.sercapcab.rpgduels.game.entity.unit.UnitDefense
import java.util.UUID

data class CharacterData(
    @SerializedName("uuid") val uuid: UUID,
    @SerializedName("name") var name: String,
    @SerializedName("level") var level: Int,
    @SerializedName("unitArmor") var unitArmor: Int,
    @SerializedName("unitMagicResistance") var unitMagicResistance: Int,
    @SerializedName("unitClass") var unitClass: UnitClass,
    @SerializedName("characterStat") var characterStat: UnitStatData,
    @SerializedName("unitModel") var unitModel: Int,
    @SerializedName("characterSpells") var spellData: Set<SpellData>?,
    @SerializedName("account") var account: AccountData
) {
    fun toCharacter(): Character {
        return Character(
            uuid = uuid,
            name = name,
            level = level,
            unitClass = unitClass,
            unitDefense = UnitDefense(
                defenses = mapOf(
                    DefenseType.Armor to unitArmor,
                    DefenseType.MagicResistance to unitMagicResistance
                )
            ),
            unitStat = characterStat.toUnitStat(),
            spells = spellData?.map{ it.toSpell() }?.toSet(),
            powerType = PowerType.NONE,
            account = account.toAccount()
        )
    }
}
