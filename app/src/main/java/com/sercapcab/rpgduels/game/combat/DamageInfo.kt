package com.sercapcab.rpgduels.game.combat

import android.util.Log
import com.sercapcab.rpgduels.Since
import com.sercapcab.rpgduels.game.entity.Spell
import com.sercapcab.rpgduels.game.entity.Unit
import com.sercapcab.rpgduels.game.entity.unit.UnitDefense
import com.sercapcab.rpgduels.game.entity.SpellSchool
import kotlin.math.floor
import kotlin.math.round

private const val TAG = "DamageInfo"

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
    val spellDamage = spell.getSpellDamage(spellCaster)
    val damageReduction = if (SpellSchool.isMagicDamage(spellType)) {
        UnitDefense.calculateDamageReduction(targetRM)
    }
    else {
        UnitDefense.calculateDamageReduction(targetArmor)
    }

    Log.d(TAG, "${spell.name}: $spellDamage")
    Log.d(TAG, "Target Armor: $targetArmor")
    Log.d(TAG, "Target RM: $targetRM")
    Log.d(TAG, "Damage Reduction: $damageReduction")

    return round(spellDamage.toDouble() * damageReduction).toInt()
}

fun damageUnit(damage: Int, target: Unit) {
    target.takeDamage(damage)
}
