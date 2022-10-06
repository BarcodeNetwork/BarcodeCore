package com.vjh0107.barcode.advancement.config

import com.vjh0107.barcode.advancement.builders.AdvancementDisplayBuilder
import com.vjh0107.barcode.advancement.config.meta.MetaFactory.isFinishedMetaOf
import com.vjh0107.barcode.advancement.services.ConditionManager.Companion.evaluateCondition
import eu.endercentral.crazy_advancements.NameKey
import eu.endercentral.crazy_advancements.advancement.AdvancementDisplay
import org.bukkit.configuration.ConfigurationSection
import org.bukkit.configuration.file.YamlConfiguration
import org.bukkit.entity.Player

class AdvancementFile(private val config: YamlConfiguration) {
    val index: Int = config.getInt("index")
    val isEnabled: Boolean = config.getBoolean("enabled", true)

    var components: LinkedHashMap<String, AdvancementComponent> = linkedMapOf()

    init {
        config.getConfigurationSection("components")!!.getKeys(false).forEach { key ->
            val section = config.getConfigurationSection("components.$key") ?: return@forEach
            components[key] = AdvancementComponent(section)
        }
    }

    fun getPlayerAdvancementComponents(player: Player): LinkedHashSet<AdvancementComponent> {
        //Collection.filter() 을 쓰지 않는 이유로, Linked Collection 을 넘겨줘야 하기 때문이다.
        //순서대로 처리되어 있어야 parent 를 처리할 수 있다.
        val linkedHashSet: LinkedHashSet<AdvancementComponent> = linkedSetOf()
        components.forEach { (_, component) ->
            if (evaluateCondition(player, component.conditions)) {
                linkedHashSet.add(component)
            }
        }
        return linkedHashSet
    }

    //index 를 참조하기 때문에 inner class 이다.
    inner class AdvancementComponent(config: ConfigurationSection) {
        val key = config.getString("key") ?: config.name
        val parentComponentKey: String? = config.getString("parent-component-key")

        private val icon = config.getString("icon")
        private val title = config.getString("title")
        private val description = config.getString("description")
        private val optionalIcons = config.getConfigurationSection("optional-icons")
        private val optionalTitles = config.getConfigurationSection("optional-titles")
        private val optionalDescriptions = config.getConfigurationSection("optional-descriptions")
        private val optionalIsFinished = config.getConfigurationSection("optional-finishes")

        private val advancementFrame = config.getString("advancement-frame")
        private val backgroundTexture = config.getString("background-texture")
        private val xCoord: Float = config.getDouble("x-coord").toFloat()
        private val yCoord: Float = config.getDouble("y-coord").toFloat()
        private val isFinished = config.getBoolean("isFinished", false)

        val conditions: ConfigurationSection? = config.getConfigurationSection("conditions")
        val index: Int get() = this@AdvancementFile.index

        fun isFinished(player: Player): Boolean {
            return isFinishedMetaOf(isFinished, optionalIsFinished).getResult(player)
        }

        private fun getAdvancementFrame(): AdvancementDisplay.AdvancementFrame {
            AdvancementDisplay.AdvancementFrame.values().forEach {
                if (it.name == advancementFrame?.uppercase()) {
                    return it
                }
            }
            return AdvancementDisplay.AdvancementFrame.TASK
        }

        fun getNameKey(): NameKey {
            return NameKey(index.toString(), key)
        }

        fun getParentNameKey(): NameKey {
            return NameKey(index.toString(), parentComponentKey)
        }

        fun getDisplay(player: Player): AdvancementDisplay {
            return AdvancementDisplayBuilder(player)
                .icon(icon, optionalIcons)
                .title(title, optionalTitles)
                .description(description, optionalDescriptions)
                .advancementFrame(getAdvancementFrame())
                .xCoord(xCoord)
                .yCoord(yCoord)
                .backgroundTexture(backgroundTexture)
                .build()
        }
    }
}