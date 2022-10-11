package com.vjh0107.barcode.mmoitems.modules.tier.repository.impl

import com.vjh0107.barcode.framework.AbstractBarcodePlugin
import com.vjh0107.barcode.framework.component.BarcodeComponent
import com.vjh0107.barcode.framework.component.BarcodeRepository
import com.vjh0107.barcode.framework.component.Reloadable
import com.vjh0107.barcode.framework.koin.annotation.BarcodeSingleton
import com.vjh0107.barcode.mmoitems.modules.tier.ItemTier
import com.vjh0107.barcode.mmoitems.modules.tier.repository.TierRepository
import net.Indyuce.mmoitems.api.ConfigFile
import net.Indyuce.mmoitems.gui.edition.recipe.recipes.RecipeMakerGUI

@BarcodeComponent
@BarcodeSingleton(binds = [TierRepository::class])
class TierRepositoryImpl(private val plugin: AbstractBarcodePlugin) : TierRepository, BarcodeRepository, Reloadable {
    private val tiers = mutableMapOf<String, ItemTier>()

    override fun exists(id: String): Boolean {
        return tiers.containsKey(id)
    }

    override fun get(id: String): ItemTier {
        return tiers[id] ?: throw NullPointerException("$id 라는 아이디를 가진 티어가 존재하지 않습니다.")
    }

    override fun getAll(): Collection<ItemTier> {
        return tiers.values
    }

    override fun register(tier: ItemTier) {
        tiers[tier.id] = tier
    }

    override fun load() {
        plugin.logger.info("TierRepository 를 로드합니다.")
        val config = ConfigFile("item-tiers")
        config.config.getKeys(false).forEach { tier ->
            val tierSection = RecipeMakerGUI.getSection(config.config, tier);
            val itemTier = try {
                ItemTier.of(tierSection)
            } catch (exception: NullPointerException) {
                plugin.logger.severe("$tier 티어를 불러오는데에 오류가 발생하였습니다.")
                exception.printStackTrace()
                return@forEach
            }
            register(itemTier)
        }
    }

    override fun close() {
        plugin.logger.info("TierRepository 를 초기화합니다.")
        tiers.clear()
    }
}