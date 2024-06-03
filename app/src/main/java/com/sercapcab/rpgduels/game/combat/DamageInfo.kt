package com.sercapcab.rpgduels.game.combat

import com.sercapcab.rpgduels.Since
import com.sercapcab.rpgduels.game.entity.Spell
import com.sercapcab.rpgduels.game.entity.Unit
import com.sercapcab.rpgduels.game.entity.unit.UnitDefense
import com.sercapcab.rpgduels.game.entity.SpellSchool

/**
 * Hace daño a la unidad seleccionada.
 *
 * @param spell El hechizo.
 * @param spellCaster El caster del hechizo.
 * @param spellType la escuela del hechizo, por defecto es [SpellSchool.SCHOOL_SLASHING].
 * @param target la unidad objetivo.
 *
 * @return el daño que recibe la unidad.
 */
@Since(version = "1.0")
fun getSpellDamage(spell: Spell, spellCaster: Unit, spellType: SpellSchool = SpellSchool.SCHOOL_SLASHING, target: Unit): Int {
    val targetArmor = target.getUnitArmor()
    val targetRM = target.getUnitMagicResistance()

    return if (SpellSchool.isMagicDamage(spellType))
        spell.getSpellDamage(spellCaster) * UnitDefense.calculateDamageReduction(targetRM).toInt()
    else spell.getSpellDamage(spellCaster) * UnitDefense.calculateDamageReduction(targetArmor).toInt()
}

fun damageUnit(damage: Int, target: Unit) {
    target.takeDamage(damage)
}
