package com.vjh0107.barcode.mmoitems.modules.tier.service

import net.Indyuce.mmoitems.api.ItemTier
import net.Indyuce.mmoitems.api.item.mmoitem.MMOItem

interface TierService {
    /**
     * 아이템 Tier 를 MMOItem 으로부터 가져옵니다.
     *
     * @param item 타겟 아이템
     */
    fun findTier(item: MMOItem): ItemTier?
}