package com.sercapcab.rpgduels.game.entity.unit

import com.sercapcab.rpgduels.Since
import kotlinx.serialization.Serializable
import org.jetbrains.annotations.ApiStatus

/**
 * Enumerador que define las clases existentes
 *
 * @property id identificador único de la clase
 * @property className el nombre de la clase
 */
@ApiStatus.Experimental
@Since(version = "1.0")
@Serializable
enum class UnitClass(val id: Int, private val className: String) {
    CLASS_BARBARIAN(1, "Bárbaro"),
    CLASS_BARD(2, "Bardo"),
    CLASS_CLERIC(3, "Clérigo"),
    CLASS_DRUID(4, "Druida"),
    CLASS_FIGHTER(5, "Luchador"),
    CLASS_MONK(6, "Monje"),
    CLASS_PALADIN(7, "Paladín"),
    CLASS_RANGER(8, "Explorador"),
    CLASS_ROGUE(9, "Pícaro"),
    CLASS_SORCERER(10, "Hechicero"),
    CLASS_WARLOCK(11, "Brujo"),
    CLASS_WIZARD(12, "Mago");

    companion object {
        /**
         * Obtiene la estadísitca principal de una clase.
         *
         * Se entiende por estadísitica principal aquella que beneficia principalmente a la clase.
         *
         * @param unitClassType la clase de la unidad
         *
         * @return el [Stat] primario de la [UnitClass]
         */
        fun getPrimaryStat(unitClassType: UnitClass): Stat {
            return when (unitClassType) {
                CLASS_BARBARIAN, CLASS_FIGHTER, CLASS_PALADIN -> Stat.STAT_STRENGTH
                CLASS_MONK, CLASS_RANGER, CLASS_ROGUE -> Stat.STAT_DEXTERITY
                CLASS_BARD, CLASS_SORCERER, CLASS_WARLOCK -> Stat.STAT_CHARISMA
                CLASS_CLERIC, CLASS_DRUID -> Stat.STAT_WISDOM
                CLASS_WIZARD -> Stat.STAT_INTELLIGENCE
            }
        }

        /**
         *
         * @param classId el id de la clase
         * @return La clase en base al integer seleccionado. En caso de que no se encuentre
         * devolverá por defecto [CLASS_FIGHTER]
         */
        fun getUnitClassFromId(classId: Int): UnitClass {
            return UnitClass.entries.getOrElse(classId - 1) { CLASS_FIGHTER }
        }
    }
}
