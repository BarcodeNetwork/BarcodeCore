package com.vjh0107.barcode.framework.serialization

/**
 * 직렬화/역직렬화를 조금 더 쉽게 해줄 수 있습니다.
 * 해당 인터페이스를 구현할 경우 <reified T : SerializableData> T.serialize 과 T.deserialize 을 사용할 수 있습니다.
 * 구문을 조금 더 간결하고 세련되게 해줍니다.
 *
 * @see serialize
 * @see deserialize
 */
interface SerializableData