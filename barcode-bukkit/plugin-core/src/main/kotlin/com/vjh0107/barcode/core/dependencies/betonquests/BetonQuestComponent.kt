package com.vjh0107.barcode.core.dependencies.betonquests

import com.vjh0107.barcode.core.dependencies.betonquests.conditions.IsStationsDuring
import com.vjh0107.barcode.core.dependencies.betonquests.conditions.PlayerInConversation
import com.vjh0107.barcode.core.dependencies.betonquests.conditions.bigdoors.IsDoorBusy
import com.vjh0107.barcode.core.dependencies.betonquests.conditions.bigdoors.IsDoorOpened
import com.vjh0107.barcode.core.dependencies.betonquests.events.*
import com.vjh0107.barcode.core.dependencies.betonquests.events.advancement.UpdatePlayerAdvancementEvent
import com.vjh0107.barcode.core.dependencies.betonquests.events.mmo.BarcodeClassSet
import com.vjh0107.barcode.core.dependencies.betonquests.events.mmo.OpenMMOItemsStation
import com.vjh0107.barcode.core.dependencies.betonquests.events.mythicmobs.MythicMobsKillInSpawnerEvent
import com.vjh0107.barcode.core.dependencies.betonquests.events.mythicmobs.MythicMobsSpawnerSpawn
import com.vjh0107.barcode.core.dependencies.betonquests.variables.MMOItemAmount
import com.vjh0107.barcode.core.dependencies.betonquests.variables.mythicmobs.MythicMobsInSpawnerAmount
import com.vjh0107.barcode.core.dependencies.betonquests.variables.mythicmobs.MythicMobsSpawnerCooldown
import com.vjh0107.barcode.core.dependencies.betonquests.events.meister.GiveMeisterPoint
import com.vjh0107.barcode.core.dependencies.betonquests.events.meister.OpenMeisterSkill
import com.vjh0107.barcode.framework.component.BarcodeComponent
import com.vjh0107.barcode.framework.component.BarcodeRegistrable
import com.vjh0107.barcode.framework.component.handler.impl.registrable.Registrar
import org.betonquest.betonquest.BetonQuest
import org.betonquest.betonquest.api.Condition
import org.betonquest.betonquest.api.QuestEvent
import org.betonquest.betonquest.api.Variable

@BarcodeComponent
class BetonQuestComponent : BarcodeRegistrable {
    override val id: String = "BetonQuestRegistrar"

    @Registrar
    fun registerBarcodeCoreIntegration() {
        registerEvents("OpenEnhanceTable", OpenEnhanceTable::class.java)
        registerEvents("BarcodeGPS", GPSStartEvent::class.java)
        registerEvents("BarcodeCutscene", BarcodeCutscene::class.java)
        registerEvents("BarcodeClassSet", BarcodeClassSet::class.java)

        registerConditions("Barcode_PlayerInConversation", PlayerInConversation::class.java)
    }

    @Registrar("BarcodeAdvancement")
    fun registerBarcodeAdvancementIntegration() {
        registerEvents("Barcode_UpdatePlayerAdvancement", UpdatePlayerAdvancementEvent::class.java)
    }

    @Registrar("MythicMobs")
    fun registerMythicMobsIntegration() {
        registerEvents("Barcode_MMobsKillInSpawner", MythicMobsKillInSpawnerEvent::class.java)
        registerEvents("Barcode_MMobsSpawnerSpawn", MythicMobsSpawnerSpawn::class.java)
        registerVariable("Barcode_MMobsInSpawnerAmount", MythicMobsInSpawnerAmount::class.java)
        registerVariable("Barcode_MMobsSpawnerCooldown", MythicMobsSpawnerCooldown::class.java)
    }

    @Registrar("BigDoors")
    fun registerBigDoorsIntegration() {
        registerEvents("Barcode_setDoorState", SetDoorState::class.java)
        registerConditions("Barcode_isDoorOpened", IsDoorOpened::class.java)
        registerConditions("Barcode_isDoorBusy", IsDoorBusy::class.java)
    }

    @Registrar("MMOItems")
    fun registerMMOItemsIntegration() {
        registerConditions("IsStationsDuring", IsStationsDuring::class.java)
        registerEvents("OpenMMOItemsStation", OpenMMOItemsStation::class.java)
        registerVariable("Barcode_MMOItemAmount", MMOItemAmount::class.java)
    }

    companion object {
        private fun <T : QuestEvent> registerEvents(name: String, eventClass: Class<T>) {
            BetonQuest.getInstance().registerEvents(name, eventClass)
        }

        private fun <T : Variable> registerVariable(name: String, variableClass: Class<T>) {
            BetonQuest.getInstance().registerVariable(name, variableClass)
        }

        private fun <T : Condition> registerConditions(name: String, conditionClass: Class<T>) {
            BetonQuest.getInstance().registerConditions(name, conditionClass)
        }

        fun registerMeisterIntegration() {
            registerEvents("BarcodeMeisterSkill", OpenMeisterSkill::class.java)
            registerEvents("BarcodeMeisterPoint", GiveMeisterPoint::class.java)
        }
    }
}


