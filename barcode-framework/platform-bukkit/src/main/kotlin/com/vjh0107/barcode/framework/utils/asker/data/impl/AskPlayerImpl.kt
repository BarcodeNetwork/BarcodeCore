package com.vjh0107.barcode.framework.utils.asker.data.impl

import com.vjh0107.barcode.framework.utils.asker.data.AskPlayer
import org.bukkit.entity.Player

data class AskPlayerImpl(override val player: Player, override var isAccepted: Boolean) : AskPlayer
