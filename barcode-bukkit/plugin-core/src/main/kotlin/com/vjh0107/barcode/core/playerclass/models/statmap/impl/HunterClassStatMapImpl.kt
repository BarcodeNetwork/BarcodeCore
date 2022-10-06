package com.vjh0107.barcode.core.playerclass.models.statmap.impl

import com.vjh0107.barcode.core.playerclass.models.statmap.AbstractClassStatMap

object HunterClassStatMapImpl : AbstractClassStatMap("hunter".uppercase()) {
    override fun getLevelStat(level: Int): Stats {
        return Stats().apply {
            when (level) {
                in 0..9 -> addStatEasily1(4.0, 0.25, 3.0)
                in 10..19 -> addStatEasily1(6.0, 0.25, 3.0)
                in 20..29 -> addStatEasily1(8.0, 0.25, 4.0)
                in 30..39 -> addStatEasily1(10.0, 0.5, 4.0)
                in 40..49 -> addStatEasily1(12.0, 0.5, 5.0)
                in 50..59 -> addStatEasily1(14.0, 0.5, 5.0)
                in 60..69 -> addStatEasily1(18.0, 0.5, 5.0)
                in 70..79 -> addStatEasily1(22.0, 1.0, 6.0)
                in 80..89 -> addStatEasily1(26.0, 1.0, 6.0)
                in 90..99 -> addStatEasily1(30.0, 1.0, 7.0)
                in 100..109 -> addStatEasily1(34.0, 1.0, 7.0)
                in 110..119 -> addStatEasily1(40.0, 2.0, 8.0)
                in 120..129 -> addStatEasily1(45.0, 2.0, 8.0)
                in 130..139 -> addStatEasily1(51.0, 2.0, 9.0)
                in 140..149 -> addStatEasily1(56.0, 2.0, 9.0)
                in 150..159 -> addStatEasily1(64.0, 2.0, 10.0)
                in 160..169 -> addStatEasily1(70.0, 4.0, 10.0)
                in 170..179 -> addStatEasily1(76.0, 5.0, 11.0)
                in 180..189 -> addStatEasily1(84.0, 6.0, 11.0)
                in 190..199 -> addStatEasily1(90.0, 6.0, 12.0)
                else -> {}
            }
        }
    }
}