package com.vjh0107.barcode.mmoitems.modules.tier.service.impl

import com.vjh0107.barcode.framework.utils.uncheckedNonnullCast
import com.vjh0107.barcode.mmoitems.modules.tier.repository.TierRepository
import com.vjh0107.barcode.mmoitems.modules.tier.service.TierService
import net.Indyuce.mmoitems.ItemStats
import net.Indyuce.mmoitems.api.ItemTier
import net.Indyuce.mmoitems.api.item.mmoitem.MMOItem
import net.Indyuce.mmoitems.stat.data.StringData
import org.koin.core.annotation.Single

@Single(binds = [TierService::class])
class TierServiceImpl(private val repository: TierRepository) : TierService {
    override fun findTier(item: MMOItem): ItemTier? {
        if (!item.hasData(ItemStats.TIER)) {
            return null
        }
        val tierId = item.getData(ItemStats.TIER).uncheckedNonnullCast<StringData>().string ?: return null
        return repository.get(tierId)
    }
}