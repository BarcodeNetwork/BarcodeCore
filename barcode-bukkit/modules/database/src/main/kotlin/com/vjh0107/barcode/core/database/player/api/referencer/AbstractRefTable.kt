package com.vjh0107.barcode.core.database.player.api.referencer

import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.dao.id.IdTable
import org.jetbrains.exposed.sql.Column
import org.jetbrains.exposed.sql.ReferenceOption

/**
 * Root 테이블을 참조하는 자식 테이블의 추상 클래스이다.
 *
 * @param name 테이블 이름
 * @param columnName 참조하는 칼럼 이름 (키)
 * @param referencingColumn 참조되는 칼럼
 * @param onDelete 참조하는 칼럼이 삭제되었을 때의 옵션, 기본값은 참조하는 테이블의 값(로우)이 삭제되었을 때 삭제된다.
 * @param onUpdate 참조하는 칼럼이 업데이트 되었을 때의 옵션이다.
 *                 일반적으로 참조되고 있는 키가 업데이트되지 않기 때문에, 기본값은 업데이트를 제한한다.
 * @param constraintName 외래키 제약조건 이름
 */
abstract class AbstractRefTable<T : Comparable<T>>(
    name: String = "",
    columnName: String,
    referencingColumn: Column<T>,
    onDelete: ReferenceOption = ReferenceOption.CASCADE,
    onUpdate: ReferenceOption = ReferenceOption.RESTRICT,
    constraintName: String? = null
) : IdTable<T>(name) {
    final override val id: Column<EntityID<T>> = reference(
        columnName,
        referencingColumn,
        onDelete,
        onUpdate,
        constraintName
    ).entityId().uniqueIndex()
    final override val primaryKey = PrimaryKey(id)
}