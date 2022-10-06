package com.vjh0107.barcode.framework.nbt.test

import com.vjh0107.barcode.framework.nbt.data.ItemTag
import com.vjh0107.barcode.framework.utils.print
import io.kotest.core.spec.style.AnnotationSpec

class ItemTagTest : AnnotationSpec() {
    private val itemTagList = mutableListOf<ItemTag>()
    @Before
    fun init() {
        with(itemTagList) {
            add(ItemTag("skillId2", "SkillID2"))
            add(ItemTag("SkillLevel1", 1))
            add(ItemTag("isEnabled", false))
            add(ItemTag("skillId", "SkillID1"))
            add(ItemTag("skills", listOf("skillname1", "skillname2", "skillname3")))
        }
    }
    @Test
    fun compressTagTest() {
        ItemTag.compressTags(itemTagList).print()
    }

    @Test
    fun decompressTagTest() {
        ItemTag.decompressTags(ItemTag.compressTags(itemTagList)).print("decompressTagTest: ")
    }

    @Test
    fun toJsonArrayTest() {
        ItemTag.toJsonArray(itemTagList.last()).print()
    }

    @Test
    fun getStringListFromTagTest() {
        ItemTag.getStringListFromTag(itemTagList.last()).print()
    }

    @Test
    fun fromStringListTest() {
        ItemTag.fromStringList("pathA", listOf("asdfasdf", "asdfasdf2")).print()
    }

    @After
    fun tearDown() {
        itemTagList.clear()
    }
}