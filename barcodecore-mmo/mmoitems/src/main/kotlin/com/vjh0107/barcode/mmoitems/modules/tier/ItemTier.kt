package com.vjh0107.barcode.mmoitems.modules.tier

import com.vjh0107.barcode.framework.utils.formatters.parseColorCode
import com.vjh0107.barcode.mmoitems.utils.toInternalId
import net.Indyuce.mmoitems.MMOItems
import net.Indyuce.mmoitems.api.droptable.DropTable
import net.Indyuce.mmoitems.api.player.PlayerData
import net.Indyuce.mmoitems.api.util.NumericStatFormula
import net.Indyuce.mmoitems.comp.itemglow.TierColor
import org.bukkit.Bukkit
import org.bukkit.configuration.ConfigurationSection
import org.bukkit.inventory.ItemStack
import java.util.*
import java.util.concurrent.ThreadLocalRandom

/**
 * @param dropTable 분해하였을 때 드랍 테이블
 * @param modifierCapacity 스텟 capacity, 아이템이 티어의 NumericStatFormula 에 따라 능력치가 변동된다.
 * @param generationChance 등급이 뜰 확률
 */
data class ItemTier private constructor(
    val id: String,
    val name: String,
    val dropTable: DropTable? = null,
    val modifierCapacity: NumericStatFormula? = null,
    val generationChance: Double = 0.0
) {
    // item glow options
    var color: TierColor? = null
    var isHintEnabled = false

    /**
     * @return The formula for modifier capacity which can be then rolled to
     * generate a random amount of modifier capacity when generating a
     * random item
     */

    fun hasColor(): Boolean {
        return color != null
    }

    /**
     * @return 드롭 테이블을 아이템 List 로 구합니다.
     */
    fun getDeconstructedLoot(player: PlayerData): List<ItemStack> {
        return if (dropTable != null) dropTable.read(player, false) else listOf()
    }

    companion object {
        private val RANDOM = ThreadLocalRandom.current()
        private val GLOW = Bukkit.getPluginManager().getPlugin("GlowAPI") != null

        fun of(config: ConfigurationSection): ItemTier {
            val id = config.name.toInternalId()
            val name = config.getString("name")?.parseColorCode() ?: throw NullPointerException("$id 티어의 name 이 없습니다.")

            val dropTable = if (config.contains("deconstruct-item")) {
                DropTable(config.getConfigurationSection("deconstruct-item"))
            } else {
                null
            }

        }
    }

    /**
     * Load an ItemTier from the YML Configuration Itself
     *
     * @param config Configuration section to get all values from
     */
    init {
        try {

            // Is it defined?
            val glowSection = config.getConfigurationSection("item-glow")

            // Alr then lets read it
            if (glowSection != null) {

                // Does it hint?
                isHintEnabled = glowSection.getBoolean("hint")

                // Does it color?
                color = TierColor(config.getString("color", "WHITE"), GLOW)
            }
        } catch (exception: NoClassDefFoundError) {

            // No hints
            isHintEnabled = false
            color = null

            // Grrr but GlowAPI crashing shall not crash MMOItems tiers wtf
            MMOItems.print(
                null,
                "Could not load glow color for tier \$r{0}\$b;\$f {1}",
                "Tier Hints",
                id,
                exception.message
            )
        } catch (exception: IllegalAccessException) {
            isHintEnabled = false
            color = null
            MMOItems.print(
                null,
                "Could not load glow color for tier \$r{0}\$b;\$f {1}",
                "Tier Hints",
                id,
                exception.message
            )
        } catch (exception: NoSuchFieldException) {
            isHintEnabled = false
            color = null
            MMOItems.print(
                null,
                "Could not load glow color for tier \$r{0}\$b;\$f {1}",
                "Tier Hints",
                id,
                exception.message
            )
        } catch (exception: SecurityException) {
            isHintEnabled = false
            color = null
            MMOItems.print(
                null,
                "Could not load glow color for tier \$r{0}\$b;\$f {1}",
                "Tier Hints",
                id,
                exception.message
            )
        }

        // What are the chances?
        generationChance = config.getDouble("generation.chance")
        modifierCapacity =
            if (config.contains("generation.capacity")) NumericStatFormula(config.getConfigurationSection("generation.capacity")) else null
    }
}
