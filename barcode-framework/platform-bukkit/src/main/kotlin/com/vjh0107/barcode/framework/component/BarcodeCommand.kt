package com.vjh0107.barcode.framework.component

import dev.jorel.commandapi.CommandAPICommand

interface BarcodeCommand : IBarcodeComponent {
    val command: CommandAPICommand
}