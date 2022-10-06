package com.vjh0107.barcode.core.playerclass.models

import com.vjh0107.barcode.core.playerclass.models.statmap.AbstractClassStatMap
import com.vjh0107.barcode.core.playerclass.models.statmap.impl.BladeClassStatMapImpl
import com.vjh0107.barcode.core.playerclass.models.statmap.impl.HunterClassStatMapImpl
import com.vjh0107.barcode.core.playerclass.models.statmap.impl.PaladinClassStatMapImpl
import com.vjh0107.barcode.core.playerclass.models.statmap.impl.SorcererClassStatMapImpl

enum class BarcodeClasses(val displayName: String, vararg val subClasses: SubClasses) {
    WARRIOR("전사", SubClasses.PALADIN),
    ASSASSIN("암살자", SubClasses.BLADE),
    ARCHER("궁수", SubClasses.HUNTER),
    MAGICIAN("마법사", SubClasses.SORCERER);

    enum class SubClasses(val displayName: String, val rootClass: BarcodeClasses, val statMap: AbstractClassStatMap) {
        PALADIN("팔라딘", WARRIOR, PaladinClassStatMapImpl),
        BLADE("블레이드", ASSASSIN, BladeClassStatMapImpl),
        HUNTER("헌터", ARCHER, HunterClassStatMapImpl),
        SORCERER("소서러", MAGICIAN, SorcererClassStatMapImpl);
    }
}