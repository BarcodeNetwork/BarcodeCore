package com.vjh0107.barcode.core.dependencies.magicspells

import com.nisovin.magicspells.util.managers.ConditionManager
import com.vjh0107.barcode.core.dependencies.magicspells.conditions.*
import com.vjh0107.barcode.framework.component.BarcodeRegistrable
import com.vjh0107.barcode.core.skill.PreventMagicSpellsTargetCitizens
import com.vjh0107.barcode.core.skill.damager.listeners.PainSpellTagParser
import com.vjh0107.barcode.framework.AbstractBarcodePlugin
import com.vjh0107.barcode.framework.component.BarcodeComponent

@BarcodeComponent
class MagicSpellsComponent(val plugin: AbstractBarcodePlugin) : BarcodeRegistrable {
    override val id: String = "MagicSpellsRegistrar"

    override fun register() {
        plugin.run {
            registerListener(PainSpellTagParser())
            registerListener(PreventMagicSpellsTargetCitizens())
        }
        //conditions 는 정적 필드이기 때문에 객체를 하나 더 생성해서 박아준다.
        ConditionManager().run {
            addCondition("vjh0107.offhandIsSet", OffHandIsSetCondition::class.java)
            addCondition("vjh0107.Placeholder", PlaceholderAPICondition::class.java)
            addCondition("vjh0107.UnderIsAir", UnderIsAirCondition::class.java)
            addCondition("vjh0107.mainhandIsSet", MainHandIsSetCondition::class.java)
            addCondition("vjh0107.mmocoreLevel", MMOCoreLevelRequiredCondition::class.java)
        }
    }

}