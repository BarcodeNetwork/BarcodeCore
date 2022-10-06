package com.vjh0107.barcode.core.dependencies.magicspells.conditions

import com.nisovin.magicspells.castmodifiers.Condition
import com.vjh0107.barcode.framework.utils.underIsAir
import org.bukkit.Location
import org.bukkit.entity.LivingEntity

class UnderIsAirCondition : Condition() {
    private var blockCount: Int = 1

    override fun initialize(p0: String?): Boolean {
        if (p0 == null) return false
        this.blockCount = p0.toInt()
        return true
    }



    override fun check(p0: LivingEntity?): Boolean {
        return p0.underIsAir(blockCount)
    }
    override fun check(p0: LivingEntity?, p1: LivingEntity?): Boolean {
        return p1.underIsAir(blockCount)
    }

    override fun check(p0: LivingEntity?, p1: Location?): Boolean {
        return false
    }
}