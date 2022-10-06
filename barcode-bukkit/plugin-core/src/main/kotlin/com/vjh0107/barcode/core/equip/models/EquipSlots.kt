package com.vjh0107.barcode.core.equip.models


enum class EquipSlots(val kor: String, val customModelData: Int, val slot: Int){
    ARTIFACT("아티팩트", 2, 9),
    GLOVES("장갑", 5, 10),
    PENDANT("목걸이", 1, 11),
    RING("반지", 7, 12),
    EARRING("귀걸이", 12, 13),
    DECO("치장", 3, 15),
    TITLE("칭호", 14, 14),
    PET("펫", 6, 17),
    RIDING("라이딩", 13, 16);

    fun getDisplayName(): String {
        return "§6${this.kor}"
    }

    fun getLore(): List<String> {
        return listOf("§7${this.kor} 종류 아이템 장착 슬롯입니다")
    }
}

fun getEquipSlot(slot: Int): EquipSlots? {
    return EquipSlots.values().firstOrNull { it.slot == slot }
}
