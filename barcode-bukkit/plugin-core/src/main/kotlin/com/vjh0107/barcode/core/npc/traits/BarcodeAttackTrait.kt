package com.vjh0107.barcode.core.npc.traits

import net.citizensnpcs.api.persistence.Persist
import net.citizensnpcs.api.trait.Trait

class BarcodeAttackTrait : Trait("barcodeattack") {
    /**
     * Sentinel 이 사용할 미몹 스킬 이펙트 아이디
     */
    @Persist("attack_skill_ID")
    var attackSkillID: String = ""
}