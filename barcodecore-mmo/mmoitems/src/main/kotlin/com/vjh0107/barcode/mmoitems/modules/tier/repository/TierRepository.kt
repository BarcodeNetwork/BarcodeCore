package com.vjh0107.barcode.mmoitems.modules.tier.repository

import com.vjh0107.barcode.mmoitems.modules.tier.ItemTier

interface TierRepository {
    /**
     * 티어를 등록합니다.
     *
     * @param tier 티어 객체
     */
    fun register(tier: ItemTier)

    /**
     * 존재하는 티어인가요?
     *
     * @param id 티어 아이디
     */
    fun exists(id: String): Boolean

    /**
     * 티어를 가져옵니다.
     *
     * @param id 티어 아이디
     * @throws NullPointerException
     */
    fun get(id: String): ItemTier

    /**
     * 모든 티어를 가져옵니다.
     */
    fun getAll(): Collection<ItemTier>
}