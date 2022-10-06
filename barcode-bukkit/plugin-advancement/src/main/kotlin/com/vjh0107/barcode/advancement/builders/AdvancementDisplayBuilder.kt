package com.vjh0107.barcode.advancement.builders

import com.vjh0107.barcode.advancement.config.meta.MetaFactory.descriptionMetaOf
import com.vjh0107.barcode.advancement.config.meta.MetaFactory.itemIconMetaOf
import com.vjh0107.barcode.advancement.config.meta.MetaFactory.titleMetaOf
import eu.endercentral.crazy_advancements.JSONMessage
import eu.endercentral.crazy_advancements.advancement.AdvancementDisplay
import eu.endercentral.crazy_advancements.advancement.AdvancementVisibility
import net.md_5.bungee.api.chat.TextComponent
import org.bukkit.Material
import org.bukkit.configuration.ConfigurationSection
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack

class AdvancementDisplayBuilder(val player: Player) {
    var icon: ItemStack = ItemStack(Material.BEDROCK)
    var title: TextComponent = TextComponent("타이틀을 설정해주세요.")
    var description: TextComponent = TextComponent("설명을 설정해주세요. 줄바꿈은 \\.n을 통해 가능합니다.")
    var advancementFrame: AdvancementDisplay.AdvancementFrame = AdvancementDisplay.AdvancementFrame.TASK
    var backgroundTexture: String? = null
    var advancementVisibility: AdvancementVisibility = AdvancementVisibility.ALWAYS
    var xCoord: Float = 0F
    var yCoord: Float = 0F

    fun icon(icon: String?, optionalIcons: ConfigurationSection?) : AdvancementDisplayBuilder {
        icon ?: return this
        this.icon = itemIconMetaOf(icon, optionalIcons).getResult(player)
        return this
    }
    fun title(string: String?, optionalTitles: ConfigurationSection?) : AdvancementDisplayBuilder {
        string ?: return this
        title = titleMetaOf(string, optionalTitles).getResult(player)
        return this
    }
    fun description(string: String?, optionalDescriptions: ConfigurationSection?) : AdvancementDisplayBuilder {
        string ?: return this
        val list = string.split("\n")
        description = descriptionMetaOf(list, optionalDescriptions).getResult(player)
        return this
    }
    fun advancementFrame(advancementFrame: AdvancementDisplay.AdvancementFrame?) : AdvancementDisplayBuilder {
        advancementFrame ?: return this
        this.advancementFrame = advancementFrame
        return this
    }
    fun backgroundTexture(texturePath: String?) : AdvancementDisplayBuilder {
        texturePath ?: return this
        backgroundTexture = texturePath
        return this
    }
    fun xCoord(float: Float?) : AdvancementDisplayBuilder {
        float ?: return this
        xCoord = float
        return this
    }
    fun yCoord(float: Float?) : AdvancementDisplayBuilder {
        float ?: return this
        yCoord = float
        return this
    }

    fun build() : AdvancementDisplay {
        return AdvancementDisplay(
            icon,
            title.toJSONMessage(),
            description.toJSONMessage(),
            advancementFrame,
            backgroundTexture,
            advancementVisibility
        ).apply {
            this.setCoordinates(xCoord, yCoord)
        }
    }

    private fun TextComponent.toJSONMessage() : JSONMessage {
        return JSONMessage(this)
    }
}