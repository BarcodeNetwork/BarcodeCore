package com.vjh0107.barcode.core.playerclass.models.statmap.impl

import com.vjh0107.barcode.core.playerclass.models.statmap.AbstractClassStatMap

object SorcererClassStatMapImpl : AbstractClassStatMap("sorcerer".uppercase()) {
    override fun getLevelStat(level: Int): Stats {
        return Stats().apply {
            when (level) {
                in 0..9 -> addStatEasily1(4.0, 0.25, 5.0)
                in 10..19 -> addStatEasily1(6.0, 0.25, 5.0)
                in 20..29 -> addStatEasily1(8.0, 0.25, 6.0)
                in 30..39 -> addStatEasily1(10.0, 0.25, 6.0)
                in 40..49 -> addStatEasily1(12.0, 0.5, 7.0)
                in 50..59 -> addStatEasily1(14.0, 0.5, 7.0)
                in 60..69 -> addStatEasily1(16.0, 0.5, 8.0)
                in 70..79 -> addStatEasily1(18.0, 0.5, 8.0)
                in 80..89 -> addStatEasily1(20.0, 1.0, 9.0)
                in 90..99 -> addStatEasily1(25.0, 1.0, 9.0)
                in 100..109 -> addStatEasily1(30.0, 1.0, 10.0)
                in 110..119 -> addStatEasily1(35.0, 2.0, 10.0)
                in 120..129 -> addStatEasily1(40.0, 2.0, 11.0)
                in 130..139 -> addStatEasily1(45.0, 2.0, 11.0)
                in 140..149 -> addStatEasily1(50.0, 2.0, 12.0)
                in 150..159 -> addStatEasily1(55.0, 3.0, 12.0)
                in 160..169 -> addStatEasily1(60.0, 3.0, 13.0)
                in 170..179 -> addStatEasily1(68.0, 4.0, 13.0)
                in 180..189 -> addStatEasily1(76.0, 4.0, 15.0)
                in 190..199 -> addStatEasily1(84.0, 5.0, 15.0)
                else -> {}
            }
        }
    }
}