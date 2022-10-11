package com.vjh0107.barcode.rpg.skill

import org.bukkit.inventory.ItemStack

interface Skill {
    /**
     * 아이디와 이름입니다
     */
    val id: String
    val name: String

    /**
     * 아이콘과 설명입니다
     */
    fun getIcon(): ItemStack
    fun getLore(): List<String>

    /**
     * 패시브 스킬인가요?
     */
    fun isPassive(): Boolean

    fun getModifiers(): Map<String, LinearV>
}