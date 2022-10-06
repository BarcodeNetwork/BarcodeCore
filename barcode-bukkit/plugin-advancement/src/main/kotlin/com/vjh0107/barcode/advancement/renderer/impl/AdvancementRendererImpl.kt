package com.vjh0107.barcode.advancement.renderer.impl

import com.vjh0107.barcode.advancement.BarcodeAdvancementPlugin
import com.vjh0107.barcode.advancement.config.AdvancementFile
import com.vjh0107.barcode.advancement.renderer.AdvancementRenderer
import eu.endercentral.crazy_advancements.NameKey
import eu.endercentral.crazy_advancements.advancement.Advancement
import eu.endercentral.crazy_advancements.manager.AdvancementManager
import org.bukkit.entity.Player

class AdvancementRendererImpl(
    private val player: Player
) : AdvancementRenderer {
    private val advancementManager: AdvancementManager = AdvancementManager(NameKey("barcode"), player)

    override fun loadAsynchronously(delay: Long) {
        BarcodeAdvancementPlugin.runAsyncTaskLater(delay) {
            //advancement 초기화
            val advancementList = advancementManager.advancements.toTypedArray()
            advancementManager.removeAdvancement(*advancementList)
            //root advancement component 검사 후 child advancement component 추가
            applyPlayerAdvancementComponents()
        }
    }

    private fun applyPlayerAdvancementComponents() {
        BarcodeAdvancementPlugin.instance.advancementManager.getComponentsAll().values.forEach { advancement ->
            val playerComponents = advancement.getPlayerAdvancementComponents(player)
            playerComponents.forEach { component ->
                applyPlayerAdvancementComponent(component)
            }
        }
    }

    private fun applyPlayerAdvancementComponent(component: AdvancementFile.AdvancementComponent) {
        // parent component 가 없으면, 해당 컴포넌트가 parent 이다.
        val advancement = if (component.parentComponentKey == null) {
            Advancement(null, component.getNameKey(), component.getDisplay(player))
        } else {
            // parent 가 없으면, 추가하지않는다.
            val parent: Advancement = advancementManager.getAdvancement(component.getParentNameKey()) ?: return
            Advancement(parent, component.getNameKey(), component.getDisplay(player))
        }

        if (component.isFinished(player)) {
            advancement.getProgress(player).criteriaProgress = 100
        }

        advancementManager.addAdvancement(advancement)
    }
}