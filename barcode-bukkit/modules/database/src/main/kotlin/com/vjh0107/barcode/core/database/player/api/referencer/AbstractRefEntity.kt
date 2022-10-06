package com.vjh0107.barcode.core.database.player.api.referencer

import org.jetbrains.exposed.dao.Entity
import org.jetbrains.exposed.dao.id.EntityID

/**
 * 추상 레퍼런싱 엔티티
 */
abstract class AbstractRefEntity<T : Comparable<T>>(id: EntityID<T>) : Entity<T>(id)