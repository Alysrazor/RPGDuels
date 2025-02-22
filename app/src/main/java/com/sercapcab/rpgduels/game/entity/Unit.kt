package com.sercapcab.rpgduels.game.entity

import com.sercapcab.rpgduels.Since
import com.sercapcab.rpgduels.Works
import com.sercapcab.rpgduels.api.serializer.UUIDSerializer
import com.sercapcab.rpgduels.game.entity.unit.DefenseType
import com.sercapcab.rpgduels.game.entity.unit.PowerType
import com.sercapcab.rpgduels.game.entity.unit.Stat
import com.sercapcab.rpgduels.game.entity.unit.UnitClass
import com.sercapcab.rpgduels.game.entity.unit.UnitDefense
import com.sercapcab.rpgduels.game.entity.unit.UnitStat
import kotlinx.serialization.Serializable
import java.util.UUID

const val MAX_LEVEL = 10u

@Serializable
@Since(version = "1.0")
sealed class Unit {
    // Properties
    @Serializable(with = UUIDSerializer::class)
    protected abstract val uuid: UUID
    protected abstract var name: String
    protected abstract var level: UInt
    protected abstract val unitClass: UnitClass
    protected abstract var unitDefense: UnitDefense
    protected abstract var unitStat: UnitStat
    protected abstract var spells: Set<Spell>
    protected abstract var powerType: PowerType

    // Atributos de clase
    private var maxHealth: UInt = 0u
    private var currentHealth: UInt = 0u

    private var maxPower: UInt = 0u
    private var currentPower: UInt = 0u

    /// Properties Getters and Setters
    /**
     * @return el [UUID] de la unidad
     */
    fun getUnitUUID(): UUID = this.uuid

    /**
     * @return el nombre de la unidad
     */
    fun getUnitName(): String = this.name

    /**
     * Establece el nombre de la unidad
     *
     * @param newName el nuevo nombre de la unidad
     */
    fun setUnitName(newName: String) {
        this.name = newName
    }

    /**
     * @return el nivel de la unidad
     */
    fun getUnitLevel(): UInt = this.level

    /**
     * Establece el nivel de la unidad
     *
     * @param newLevel el nuevo nivel de la unidad
     */
    fun setUnitLevel(newLevel: UInt) {
        if (newLevel > MAX_LEVEL)
            this.level = MAX_LEVEL
        else
            this.level = newLevel
    }

    /**
     * @return la clase de la unidad
     */
    fun getUnitUnitClass(): UnitClass = this.unitClass

    /**
     * @return la armadura de la unidad
     */
    fun getUnitArmor(): Int = this.unitDefense.defenses.getValue(DefenseType.Armor)

    /**
     * Establece la armadura de la unidad
     *
     * @param newArmor la nueva armadura de la unidad
     */
    fun setArmor(newArmor: Int) {
        if (newArmor < 0)
            this.unitDefense.defenses = mapOf(
                DefenseType.Armor to 0,
                DefenseType.MagicResistance to this.getUnitMagicResistance()
            )
        else {
            this.unitDefense.defenses = mapOf(
                DefenseType.Armor to newArmor,
                DefenseType.MagicResistance to this.getUnitMagicResistance()
            )
        }
    }

    /**
     * @return la resistencia mágica de la unidad
     */
    fun getUnitMagicResistance(): Int = this.unitDefense.defenses.getValue(DefenseType.MagicResistance)

    /**
     * Establece la resistencia mágica de la unidad
     *
     * @param newMagicResistance la nueva resistencia mágica de la unidad
     */
    fun setMagicResistance(newMagicResistance: Int) {
        if (newMagicResistance < 0)
        {
            this.unitDefense.defenses = mapOf(
                DefenseType.Armor to this.getUnitArmor(),
                DefenseType.MagicResistance to 0
            )
        }
        else {
            this.unitDefense.defenses = mapOf(
                DefenseType.Armor to this.getUnitArmor(),
                DefenseType.MagicResistance to newMagicResistance
            )
        }
    }

    /**
     * @return las estadísticas de la unidad
     */
    fun getUnitStats(): Map<Stat, Int> = this.unitStat.stats

    /**
     * Establece las estadísticas de la unidad
     *
     * @param newStats las nuevas estadísticas de la unidad
     */
    fun setStats(newStats: Map<Stat, Int>) {
        newStats.map {
            require(it.value in 1..30) {"Las estadísticas tienen que estar entre 1 y 30"}
        }

        this.unitStat.stats = newStats
    }

    /**
     * Actualiza las estadísticas de la unidad con los valores proporcionados en el mapa `updatedStats`
     * para aquellas estadísticas que coincidan en ambos mapas.
     *
     * Verifica que los valores en `updatedStats` estén dentro del rango válido de 1 a 30.
     *
     * @param updatedStats El mapa de estadísticas actualizadas que se utilizará para actualizar las estadísticas de la unidad.
     * @throws IllegalArgumentException Si alguna de las estadísticas en `updatedStats` está fuera del rango válido (1 a 30).
     */
    @Works
    fun updateStats(updatedStats: Map<Stat, Int>) {
        val mutableStats = this.unitStat.stats.toMutableMap()

        updatedStats.forEach { (stat, value) ->
            require(value in 1..30) { "Las estadísticas deben estar entre 1 y 30" }

            if (mutableStats.containsKey(stat))
                mutableStats[stat] = value
        }

        this.unitStat.stats = mutableStats

        if (updatedStats.containsKey(Stat.STAT_CONSTITUTION))
            updateHealthOnLevelOrConstitutionChange(this.level)
        else if (updatedStats.containsKey(Stat.STAT_INTELLIGENCE))
            updatePowerOnIntelligenceChangeOrLevelUp()
    }

    /**
     * @return las hechizos de la unidad
     */
    fun getUnitSpells(): Set<Spell> = this.spells

    /**
     * Establece las hechizos de la unidad
     *
     * @param newSpells las nuevas hechizos de la unidad
     */
    @JvmName("setUnitSpells")
    fun setSpells(newSpells: Set<Spell>) {
        this.spells = newSpells
    }

    /**
     * @return el poder que usa la unidad
     */
    fun getUnitPowerType(): PowerType = this.powerType

    /**
     * @return obtiene el poder máximo de la unidad
     */
    fun getMaxPower(): UInt = this.maxPower

    /**
     * @return obtiene el poder actual de la unidad
     */
    fun getCurrentPower(): UInt = this.currentPower

    /**
     * Establece la nueva cantidad de poder de la unidad
     *
     * @param newMaxPower la nueva cantidad de poder
     */
    fun setMaxPower(newMaxPower: UInt) {
        this.maxPower = if (newMaxPower == 0u) {
            this.currentPower = 1u
            1u
        }
        else {
            this.currentPower = newMaxPower
            newMaxPower
        }
    }

    /// Propiedades de la clase

    /**
     * @return la vida máxima de la unidad
     */
    fun getMaxHealth(): UInt = this.maxHealth

    /**
     * Establece el valor máximo de salud de la unidad con el nuevo valor proporcionado.
     * El nuevo valor debe estar en el rango válido de 1 a 4294967295.
     *
     * @param newMaxHealth El nuevo valor máximo de salud que se va a establecer.
     * @throws IllegalArgumentException Si el nuevo valor no está dentro del rango válido (1 a 4294967295).
     */
    @Works
    fun setMaxHealth(newMaxHealth: UInt) {
        require(newMaxHealth in 1u..UInt.MAX_VALUE) { "Debe estar en el rango de 1 a 4294967295" }

        this.maxHealth = newMaxHealth
        this.currentHealth = newMaxHealth
    }

    /**
     * @return la vida actual de la unidad
     */
    fun getHealth(): UInt = this.currentHealth

    /**
     * Establece la vida de la unidad a una cantidad determinada, el valor mínimo que puede ser es de 1.
     *
     * @param newHealth La nueva vida actual para la unidad.
     */
    fun setNewHealth(newHealth: UInt) {
        currentHealth = if (newHealth == 0u)
            1u
        else if (newHealth > this.getMaxHealth())
            maxHealth
        else
            newHealth
    }

    /**
     * Establece las estadísticas de la unidad con las nuevas estadísticas proporcionadas.
     * Cada estadística debe estar en el rango válido de 1 a 30, de lo contrario se lanzará una excepción.
     *
     * @param newStats Las nuevas estadísticas en un [MutableMap]
     * @throws IllegalArgumentException Si alguna de las estadísticas está fuera del rango válido (1 a 30).
     */
    @Works
    fun setNewStats(newStats: Map<Stat, Int>) {
        newStats.map {
            require(it.value in 1..30) {"Las estadísticas tienen que estar entre 1 y 30"}
        }

        this.unitStat.stats = newStats
    }


    /**
     * Actualiza los puntos de salud máximos y actuales de la unidad cuando cambia de nivel.
     * Calcula los puntos de salud máximos basados en el nivel y las estadísticas de la unidad.
     *
     * @param level El nuevo nivel de la unidad.
     */
    @Works
    protected fun updateHealthOnLevelOrConstitutionChange(level: UInt) {
        val firstLevelHP: UInt = when (this.unitClass) {
            UnitClass.CLASS_BARBARIAN -> (12u + Stat.getModifierChart(
                this.unitStat.stats.getValue(
                    Stat.STAT_CONSTITUTION
                )
            ).toUInt())
            UnitClass.CLASS_FIGHTER, UnitClass.CLASS_PALADIN, UnitClass.CLASS_RANGER -> (10u + Stat.getModifierChart(
                this.unitStat.stats.getValue(Stat.STAT_CONSTITUTION)
            ).toUInt())
            UnitClass.CLASS_BARD, UnitClass.CLASS_CLERIC, UnitClass.CLASS_DRUID,
            UnitClass.CLASS_MONK, UnitClass.CLASS_ROGUE, UnitClass.CLASS_WARLOCK ->
                (8u + Stat.getModifierChart(this.unitStat.stats.getValue(Stat.STAT_CONSTITUTION)).toUInt())
            UnitClass.CLASS_SORCERER, UnitClass.CLASS_WIZARD -> (6u + Stat.getModifierChart(
                this.unitStat.stats.getValue(
                    Stat.STAT_CONSTITUTION
                )
            ).toUInt())
        }

        val levelUpHealthPoints: UInt = when (this.unitClass) {
            UnitClass.CLASS_BARBARIAN -> (7u + Stat.getModifierChart(
                this.unitStat.stats.getValue(
                    Stat.STAT_CONSTITUTION
                )
            ).toUInt())
            UnitClass.CLASS_FIGHTER, UnitClass.CLASS_PALADIN, UnitClass.CLASS_RANGER -> (6u + Stat.getModifierChart(
                this.unitStat.stats.getValue(Stat.STAT_CONSTITUTION)
            ).toUInt())
            UnitClass.CLASS_BARD, UnitClass.CLASS_CLERIC, UnitClass.CLASS_DRUID, UnitClass.CLASS_MONK, UnitClass.CLASS_ROGUE, UnitClass.CLASS_WARLOCK -> (5u + Stat.getModifierChart(
                this.unitStat.stats.getValue(Stat.STAT_CONSTITUTION)
            ).toUInt())
            UnitClass.CLASS_SORCERER, UnitClass.CLASS_WIZARD -> (4u + Stat.getModifierChart(
                this.unitStat.stats.getValue(
                    Stat.STAT_CONSTITUTION
                )
            ).toUInt())
        }

        this.maxHealth = if (level.toInt() == 1) {
            firstLevelHP
        }
        else{
            firstLevelHP + (levelUpHealthPoints * (level - 1u))
        }

        this.currentHealth = maxHealth
    }

    /**
     * Actualiza el poder de la unidad cuando cambia la inteligencia.
     */
    protected fun updatePowerOnIntelligenceChangeOrLevelUp() {
        if (!this.unitStat.stats.containsKey(Stat.STAT_INTELLIGENCE))
            return

        this.maxPower = 10u + (this.level * 5u) + (this.unitStat.stats.getValue(Stat.STAT_INTELLIGENCE).toUInt() * 2u) + (Stat.getModifierChart(this.unitStat.stats.getValue(Stat.STAT_INTELLIGENCE)).toUInt() * 10u)
        this.currentPower = this.maxPower
    }

    /**
     * Establece el poder inicial de la unidad y establece su cantidad de poder.
     */
    protected fun setInitialsPowerAmount() {
        when (this.unitClass) {
            UnitClass.CLASS_BARBARIAN, UnitClass.CLASS_FIGHTER -> this.powerType = PowerType.RAGE
            UnitClass.CLASS_ROGUE, UnitClass.CLASS_MONK -> this.powerType = PowerType.ENERGY
            UnitClass.CLASS_BARD, UnitClass.CLASS_CLERIC, UnitClass.CLASS_DRUID,
            UnitClass.CLASS_PALADIN, UnitClass.CLASS_RANGER, UnitClass.CLASS_SORCERER,
            UnitClass.CLASS_WIZARD, UnitClass.CLASS_WARLOCK -> this.powerType =
                PowerType.MANA
        }

        when (this.powerType) {
            PowerType.RAGE, PowerType.ENERGY -> this.maxPower = 100u
            PowerType.MANA -> {
                this.maxPower = 10u + (this.level * 5u) + (this.unitStat.stats.getValue(Stat.STAT_INTELLIGENCE).toUInt() * 2u) + (Stat.getModifierChart(this.unitStat.stats.getValue(Stat.STAT_INTELLIGENCE)).toUInt() * 10u)
                this.currentPower = this.maxPower
            }
            else -> maxPower
        }
    }
}