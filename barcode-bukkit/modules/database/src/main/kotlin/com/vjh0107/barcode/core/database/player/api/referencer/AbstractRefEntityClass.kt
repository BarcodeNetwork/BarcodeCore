package com.vjh0107.barcode.core.database.player.api.referencer

import org.jetbrains.exposed.dao.EntityClass

/**
 * 추상 레퍼런싱 엔티티 클래스
 *
 * @param E 레퍼런싱 엔티티
 * @param T 키 타입
 * @param table 레퍼런싱 테이블
 * @param entityType 최종 엔티티 타입
 */
abstract class AbstractRefEntityClass<out E : AbstractRefEntity<T>, T : Comparable<T>>(
    table: AbstractRefTable<T>,
    entityType: Class<E>? = null
) : EntityClass<T, E>(table, entityType)