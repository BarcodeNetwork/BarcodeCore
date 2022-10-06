package com.vjh0107.barcode.advancement.config.placeholder

enum class Placeholders : Placeholder {
    OPTIONAL {
        override val prefix: String = "<optional."
        override val suffix: String = ">"
    }
}