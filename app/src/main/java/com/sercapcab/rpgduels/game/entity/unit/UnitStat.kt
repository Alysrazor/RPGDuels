package com.sercapcab.rpgduels.game.entity.unit

import com.sercapcab.rpgduels.Since
import com.sercapcab.rpgduels.Works
import com.sercapcab.rpgduels.api.serializer.UUIDSerializer
import com.sercapcab.rpgduels.game.entity.unit.Stat.STAT_CHARISMA
import com.sercapcab.rpgduels.game.entity.unit.Stat.STAT_CONSTITUTION
import com.sercapcab.rpgduels.game.entity.unit.Stat.STAT_DEXTERITY
import com.sercapcab.rpgduels.game.entity.unit.Stat.STAT_INTELLIGENCE
import com.sercapcab.rpgduels.game.entity.unit.Stat.STAT_STRENGTH
import com.sercapcab.rpgduels.game.entity.unit.Stat.STAT_WISDOM
import kotlinx.serialization.Serializable
import org.jetbrains.annotations.ApiStatus
import java.util.Locale
import java.util.UUID
import kotlin.math.floor

/**
 * Enumerador de las estadísticas
 *
 * @property statId la id de la estadística
 * @property statName el nombre de la estadística
 */
@ApiStatus.Experimental
@Since("1.0")
@Serializable
enum class Stat(private val statId: Int, val statName: String) {
    STAT_STRENGTH(1, "Fuerza"),
    STAT_DEXTERITY(2, "Destreza"),
    STAT_CONSTITUTION(3, "Constitución"),
    STAT_INTELLIGENCE(4, "Inteligencia"),
    STAT_WISDOM(5, "Sabiduría"),
    STAT_CHARISMA(6, "Carisma");

    companion object {
        /**
         * Calcula el modificador de habilidad basado en el valor de clase proporcionado.
         * El modificador de habilidad se utiliza para varias acciones en el juego, como tiradas de habilidades,
         * salvación, acciones de combate, etc.
         *
         * @param classValue El valor de clase que determina el modificador de habilidad.
         * @return El modificador de habilidad calculado.
         *
         * Ejemplos:
         * - getModifierChart(1) devuelve -5
         * - getModifierChart(2) devuelve -4
         * - getModifierChart(3) devuelve -4
         */
        @Works
        fun getModifierChart(classValue: Int): Int {
            require(classValue in 1..30) { "El valor de clase debe estar entre 1 y 30" }

            return floor((classValue - 10) / 2.0).toInt()
        }

        @Works
        fun getModifierChartForStat(statValue: Int): Int {
            return floor((statValue - 10) / 2.0).toInt()
        }

        /**
         * Muestra la información de las estadísticas más legible.
         *
         * @return un [String] con la información
         */
        @Works
        fun statsInfo(stats: Map<Stat, Int>): String {
            var statsInfo = ""

            stats.map {
                statsInfo += String.format(Locale.getDefault(),"%s: %d%n", it.key.statName, it.value)
            }

            return statsInfo
        }
    }
}

/**
 * Las stats de la unidad
 */
@Serializable
@Since(version = "1.0")
data class UnitStat(
    @Serializable(with = UUIDSerializer::class)
    val uuid: UUID,
    var stats: Map<Stat, Int> = mapOf(
        STAT_STRENGTH to 10,
        STAT_DEXTERITY to 10,
        STAT_CONSTITUTION to 10,
        STAT_INTELLIGENCE to 10,
        STAT_WISDOM to 10,
        STAT_CHARISMA to 10
    )
)
