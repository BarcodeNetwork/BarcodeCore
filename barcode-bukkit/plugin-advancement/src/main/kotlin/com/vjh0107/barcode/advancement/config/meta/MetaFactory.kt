package com.vjh0107.barcode.advancement.config.meta

import com.vjh0107.barcode.advancement.config.meta.impl.DescriptionMeta
import com.vjh0107.barcode.advancement.config.meta.impl.IconMeta
import com.vjh0107.barcode.advancement.config.meta.impl.IsFinishedMeta
import com.vjh0107.barcode.advancement.config.meta.impl.TitleMeta
import org.bukkit.configuration.ConfigurationSection

object MetaFactory {
    fun itemIconMetaOf(expression: String, conditions: ConfigurationSection?) : ItemMeta {
        return IconMeta(expression, conditions)
    }

    fun isFinishedMetaOf(isFinished: Boolean, conditions: ConfigurationSection?) : BooleanMeta {
        return IsFinishedMeta(isFinished, conditions)
    }

    fun descriptionMetaOf(description: List<String>, configurationSection: ConfigurationSection?) : TextComponentMeta {
        return DescriptionMeta(description, configurationSection)
    }

    fun titleMetaOf(title: String, optionalTitles: ConfigurationSection?) : TextComponentMeta {
        return TitleMeta(title, optionalTitles)
    }
}