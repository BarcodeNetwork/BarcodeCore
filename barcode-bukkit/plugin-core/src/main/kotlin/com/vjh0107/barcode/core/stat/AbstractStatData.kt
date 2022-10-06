package com.vjh0107.barcode.core.stat

import com.vjh0107.barcode.framework.utils.nullToZero
import net.Indyuce.mmocore.api.player.stats.StatType

abstract class AbstractStatData {
    data class Stats(val statMap: HashMap<StatType, Double> = hashMapOf()) {
        constructor(stat: StatType, value: Double) : this(hashMapOf(Pair(stat, value)))

        fun merge(stats: Stats) {
            stats.statMap.forEach { (statType, value) ->
                this.statMap[statType] = this.statMap[statType].nullToZero() + value
            }
        }

        fun addStat(stat: StatType, value: Double) {
            statMap[stat] = value
        }

        fun removeStat(stat: StatType) {
            statMap.remove(stat)
        }

        fun get(stat: StatType) : Double{
            return statMap[stat].nullToZero()
        }
    }
}