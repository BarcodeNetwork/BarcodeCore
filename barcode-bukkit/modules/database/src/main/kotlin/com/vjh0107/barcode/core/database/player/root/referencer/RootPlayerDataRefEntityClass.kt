package com.vjh0107.barcode.core.database.player.root.referencer

import com.vjh0107.barcode.core.database.player.api.referencer.AbstractRefEntityClass
import com.vjh0107.barcode.framework.database.player.multiprofile.ProfileID

abstract class RootPlayerDataRefEntityClass<out E : RootPlayerDataRefEntity>(
    rootPlayerDataRefTable: RootPlayerDataRefTable,
    entityType: Class<E>? = null
) : AbstractRefEntityClass<E, ProfileID>(rootPlayerDataRefTable, entityType) {
    /**
     * profileID 로 entity 를 구합니다.
     * 만약에 null 이면, 새로 생성합니다.
     */
    fun findByIdOrNew(profileID: ProfileID): E {
        return findById(profileID) ?: new(profileID) {}
    }
}