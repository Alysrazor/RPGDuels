package com.sercapcab.rpgduels.game.entity

import android.util.Log
import com.sercapcab.rpgduels.Since
import com.sercapcab.rpgduels.api.serializer.UUIDSerializer
import com.sercapcab.rpgduels.game.entity.exception.NotEnoughPowerException
import com.sercapcab.rpgduels.game.entity.unit.Stat
import kotlinx.serialization.Serializable
import org.jetbrains.annotations.ApiStatus
import java.util.UUID

/**
 * Enumeración que representa las distintas escuelas de magia en el juego.
 *
 * Cada escuela de magia tiene un nombre asociado que describe su tipo de daño o efecto.
 *
 * @property schoolName El nombre de la escuela de magia.
 */
@ApiStatus.Experimental
@Since(version = "1.0")
@Serializable
enum class SpellSchool(private val id: Int, private val schoolName: String) {
    SCHOOL_ACID(1, "Ácido"),
    SCHOOL_BLUDGEONING(2,"Contundente"),
    SCHOOL_COLD(3,"Frío"),
    SCHOOL_FIRE(4, "Fuego"),
    SCHOOL_FORCE(5, "Fuerza"),
    SCHOOL_LIGHTNING(6, "Rayo"),
    SCHOOL_NECROTIC(7,"Necrótico"),
    SCHOOL_PIERCING( 8,"Perforante"),
    SCHOOL_PHYSIC(9, "Físico"),
    SCHOOL_POISON(10, "Veneno"),
    SCHOOL_RADIANT(11, "Radiante"),
    SCHOOL_SLASHING(12, "Cortante"),
    SCHOOL_THUNDER(13, "Trueno");

    companion object {
        /**
         * Comprueba si el hechizo es de tipo mágico.
         *
         * @param spellSchool la escuela de magia del hechizo
         *
         * @return - true si es mágico.
         *         - false si no es mágico.
         */
        fun isMagicDamage(spellSchool: SpellSchool): Boolean {
            return when (spellSchool) {
                SCHOOL_ACID, SCHOOL_COLD, SCHOOL_FIRE, SCHOOL_FORCE,
                    SCHOOL_LIGHTNING, SCHOOL_NECROTIC, SCHOOL_POISON, SCHOOL_RADIANT,
                    SCHOOL_THUNDER -> true
                else -> false
            }
        }
    }
}

/**
 * Clase de datos que representa la información de un hechizo en el juego.
 *
 * @property uuid El identificador único del hechizo, generado automáticamente si no se proporciona.
 * @property name El nombre del hechizo.
 * @property spellSchool La escuela de magia a la que pertenece el hechizo.
 */
@Since(version = "1.0")
@Serializable
data class Spell(
    @Serializable(with = UUIDSerializer::class)
    val uuid: UUID,
    val name: String,
    val description: String,
    val spellSchool: SpellSchool,
    val baseDamage: Int,
    val basePowerCost: Int,
    val statModifier: Stat,
    val statMultiplier: Double
) {
    companion object {
        fun castSpell(caster: Unit, spell: Spell, target: Unit) {
            val spellDamage = spell.getSpellDamage(caster)
            val spellPowerCost = spell.basePowerCost

            try {
                caster.updatePower(-spellPowerCost)
                target.takeDamage(spellDamage)
            } catch(ex: NotEnoughPowerException) {
                Log.d("Spell", "$caster tried to cast $spell but not enough power.")
            }
        }
    }
    /**
     * Calcula el daño en base al daño base del hechizo más el modificador del lanzador
     *
     * @return el daño que hace el hechizo
     */
    fun getSpellDamage(caster: Unit): Int {
        val spellBonusStat = this.statModifier
        val unitStatModifier = Stat.getModifierChartForStat(caster.getUnitStats().getValue(spellBonusStat))

        Log.d("Spell", "Daño de ${this.name}: ${this.baseDamage + Stat.getModifierChartForStat(unitStatModifier) * ((100 + unitStatModifier) / 100)}")
        return this.baseDamage + Stat.getModifierChartForStat(unitStatModifier) * ((100.0 + unitStatModifier) / 100.0).toInt()
    }
}