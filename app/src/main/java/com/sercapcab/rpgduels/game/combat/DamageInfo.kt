package com.sercapcab.rpgduels.game.combat

import com.sercapcab.rpgduels.Since
import com.sercapcab.rpgduels.game.entity.Unit
import com.sercapcab.rpgduels.game.entity.unit.UnitDefense
import com.sercapcab.rpgduels.game.entity.SpellSchool

/**
 * Hace daño a la unidad seleccionada.
 *
 * @param damage el daño.
 * @param spellType la escuela del hechizo, por defecto es [SpellSchool.SCHOOL_SLASHING].
 * @param target la unidad objetivo.
 *
 * @return el daño que recibe la unidad.
 */
@Since(version = "1.0")
fun dealDamage(damage: Int, spellType: SpellSchool = SpellSchool.SCHOOL_SLASHING, target: Unit): Int {
    val targetArmor = target.getUnitArmor()
    val targetRM = target.getUnitMagicResistance()

    return if (SpellSchool.isMagicDamage(spellType))
        damage * UnitDefense.calculateDamageReduction(targetRM).toInt()
    else damage * UnitDefense.calculateDamageReduction(targetArmor).toInt()
}
