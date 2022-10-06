package com.vjh0107.barcode.core.playerclass.models.statmap

import com.vjh0107.barcode.core.stat.AbstractStatData
import net.Indyuce.mmocore.api.player.stats.StatType


abstract class AbstractClassStatMap(val id: String) : AbstractStatData() {

    //when 으로 구현
    abstract fun getLevelStat(level: Int): Stats

    fun calculateStats(newLevel: Int): Stats {
        val result = Stats()
        repeat(newLevel) {
            val getStatsByStatMap = this.getLevelStat(it)
            result.merge(getStatsByStatMap)
        }
        return result
    }

    companion object {
        /**
         * 현재 바코드 네트워크의 직업군 별 스텟 증가치는,
         * 최대 체력과 체력 재생, 최대마나가 레벨 구간 별로 다르며, 마나 재생은 등차수열이다.
         * 등차수열 스텟과 고정값 스텟은
         * @see net.Indyuce.mmocore.manager.AttributeManager
         * 에 plugins/MMOCore/classes/<classId>.yml에 작성 가능하도록 구현이 되어 있다.
         */
        fun Stats.addStatEasily1(maxHealth: Double, healthRegeneration: Double, maxMana: Double) {
            this.addStat(StatType.MAX_HEALTH, maxHealth)
            this.addStat(StatType.HEALTH_REGENERATION, healthRegeneration)
            this.addStat(StatType.MAX_MANA, maxMana)
        }
    }
}