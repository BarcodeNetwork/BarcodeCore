package com.vjh0107.barcode.framework.utils.formatters

import net.md_5.bungee.api.chat.TextComponent

/**
 * lore list 를 linebreak 가 있는 텍스트 컴포넌트로 변경한다.
 */
fun List<String>.toTextComponent(): TextComponent {
    return TextComponent().apply {
        this@toTextComponent.forEachIndexed { index, line ->
            var resultLine = line
            if ((index + 1) != this@toTextComponent.size) {
                resultLine += "\n"
            }
            this.addExtra(resultLine)
        }
    }
}
