package com.vjh0107.barcode.core.playerclass.models.statmap.impl

import com.vjh0107.barcode.core.playerclass.models.statmap.AbstractClassStatMap

object PaladinClassStatMapImpl : AbstractClassStatMap("paladin".uppercase()) {
    override fun getLevelStat(previousLevel: Int): Stats {
        return Stats().apply {
            when (previousLevel) {
                in 0..9 -> addStatEasily1(5.0, 0.25, 3.0)
                in 10..19 -> addStatEasily1(7.0, 0.25, 3.0)
                in 20..29 -> addStatEasily1(9.0, 0.25, 4.0)
                in 30..39 -> addStatEasily1(14.0, 0.5, 4.0)
                in 40..49 -> addStatEasily1(17.0, 0.75, 5.0)
                in 50..59 -> addStatEasily1(20.0, 1.0, 5.0)
                in 60..69 -> addStatEasily1(25.0, 1.0, 6.0)
                in 70..79 -> addStatEasily1(27.0, 2.0, 6.0)
                in 80..89 -> addStatEasily1(33.0, 2.0, 7.0)
                in 90..99 -> addStatEasily1(38.0, 2.0, 7.0)
                in 100..109 -> addStatEasily1(44.0, 2.0, 8.0)
                in 110..119 -> addStatEasily1(49.0, 3.0, 8.0)
                in 120..129 -> addStatEasily1(52.0, 3.0, 9.0)
                in 130..139 -> addStatEasily1(56.0, 4.0, 9.0)
                in 140..149 -> addStatEasily1(60.0, 4.0, 10.0)
                in 150..159 -> addStatEasily1(68.0, 5.0, 10.0)
                in 160..169 -> addStatEasily1(76.0, 5.0, 11.0)
                in 170..179 -> addStatEasily1(84.0, 6.0, 11.0)
                in 180..189 -> addStatEasily1(92.0, 7.0, 12.0)
                in 190..199 -> addStatEasily1(100.0, 8.0, 12.0)
                else -> {}
            }
        }
    }
}