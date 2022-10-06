package com.vjh0107.barcode.framework.nbt.data

import com.vjh0107.barcode.framework.nbt.NBTItem
import com.vjh0107.barcode.framework.nbt.SupportedNBTTagValues
import com.vjh0107.barcode.framework.utils.cleanRound
import com.vjh0107.barcode.framework.utils.uncheckedNonnullCast
import kotlinx.serialization.json.*

data class ItemTag(internal val path: String, internal val value: Any) {
    companion object {
        /**
         * 경로에 있는 tag 를 구합니다. 없을 경우, null 을 반환합니다.
         */
        fun getTagAtPath(path: String, from: List<ItemTag>): ItemTag? {
            for (str in from) {
                if (path == str.path) {
                    return str
                }
            }
            return null
        }

        /**
         * 경로에 있는 tag 를 타입을 명시하여 구합니다. 없을 경우, null 을 반환합니다.
         */
        fun getTagAtPath(path: String, from: NBTItem, expect: SupportedNBTTagValues): ItemTag? {
            return if (!from.hasTag(path)) {
                null
            } else when (expect) {
                SupportedNBTTagValues.INTEGER -> ItemTag(path, from.getInt(path))
                SupportedNBTTagValues.STRING -> ItemTag(path, from.getString(path))
                SupportedNBTTagValues.DOUBLE -> ItemTag(path, from.getDouble(path))
                SupportedNBTTagValues.BOOLEAN -> ItemTag(path, from.getBoolean(path))
            }
        }

        /**
         * List 에 있는 ItemTag 를 가져옵니다.
         * @see getStringListFromTag 와 반대됨
         */
        fun fromStringList(path: String, list: List<String>): ItemTag {
            val array = buildJsonArray {
                for (string: String in list) {
                    add(string)
                }
            }
            return toItemTag(path, array)
        }

        /**
         * ItemTag 에 encode 되어 있는 StringList 를 가져옵니다.
         * @see fromStringList 와 반대됨
         */
        fun getStringListFromTag(tagThatContainsAnEncodedList: ItemTag): List<String> {
            return toJsonArray(tagThatContainsAnEncodedList).map { it.toString() }
        }

        /**
         * ItemTag 를 통해 JsonArray 를 구합니다.
         * @see toItemTag 와 반대됨
         */
        fun toJsonArray(itemTag: ItemTag): JsonArray {
            return Json.parseToJsonElement(itemTag.value.toString()).jsonArray
        }

        /**
         * JsonArray 를 통해 ItemTag 를 구합니다.
         * @see toJsonArray 와 반대됨
         */
        fun toItemTag(pathToAssign: String, someJsonArray: JsonArray): ItemTag {
            return ItemTag(pathToAssign, someJsonArray.toString())
        }

        /**
         * 아이템 태그들을 serialize 하려면 toString() 을 호출하면 됩니다.
         * Int, Double, String, Boolean, String List 만 지원합니다.
         * 무겁기 때문에, 자주 읽기와 쓰기를 하는 것에 적합하지 않습니다.
         *
         * @see decompressTags Boolean 타입과 Int 타입을 구별하기 위하여, decompressTags 를 사용해주세요. 확장자로 구별됩니다.
         */
        fun compressTags(tags: List<ItemTag>): JsonArray {
            return buildJsonArray array@{
                for (tag in tags) {
                    val jsonTag = buildJsonObject jsonTag@{
                        when (tag.value) {
                            is Int -> this@jsonTag.put(tag.path + compression_INTEGER, tag.value)
                            is Double -> this@jsonTag.put(tag.path + compression_DOUBLE, tag.value)
                            is String -> this@jsonTag.put(tag.path + compression_STRING, tag.value)
                            is Boolean -> this@jsonTag.put(tag.path + compression_BOOLEAN, tag.value)
                            is List<*> -> {
                                val jsonArray = buildJsonArray deepArray@{
                                    for (element in tag.value) {
                                        when (element) {
                                            is Number -> this@deepArray.add(
                                                cleanRound(element.uncheckedNonnullCast(), 3)
                                            )
                                            is String -> this@deepArray.add(element.uncheckedNonnullCast<String>())
                                            is Boolean -> this@deepArray.add(element.uncheckedNonnullCast<Boolean>())
                                        }
                                    }
                                }
                                this@jsonTag.put(tag.path + compression_SLIST, jsonArray)
                            }
                        }
                    }
                    this.add(jsonTag)
                }

            }
        }

        /**
         * 태그를 decompress 합니다.
         */
        fun decompressTags(compressedTags: JsonArray): List<ItemTag> {
            val returningList = mutableListOf<ItemTag>()
            compressedTags.forEach { compressedTag ->
                compressedTag.jsonObject.entries.forEach { (rawPath, element) ->
                    // 마지막 확장자를 구합니다.
                    val compressedExtension =
                        rawPath.substring(rawPath.length - compression_SLIST.length)
                    val path = rawPath.substring(0, rawPath.length - compression_SLIST.length)
                    // 엘리먼트로 원시 타입들이 왔을 때
                    val value: Any? = when (element) {
                        is JsonPrimitive -> when (compressedExtension) {
                            compression_BOOLEAN -> element.boolean
                            compression_DOUBLE -> element.double
                            compression_INTEGER -> element.int
                            compression_STRING -> element.content
                            compression_SLIST -> element.jsonArray
                            else -> null
                        }
                        is JsonArray -> element.map { it.toString() }
                        else -> null
                    }
                    if (value != null) {
                        returningList.add(ItemTag(path, value))
                    }
                }

            }
            return returningList
        }

        // tag 의 타입 확장자임. decompress 를 위해 길이가 같아야 함
        private const val compression_STRING = "_ñstr"
        private const val compression_DOUBLE = "_ñdbl"
        private const val compression_BOOLEAN = "_ñbol"
        private const val compression_INTEGER = "_ñint"
        private const val compression_SLIST = "_ñlst"
    }
}

