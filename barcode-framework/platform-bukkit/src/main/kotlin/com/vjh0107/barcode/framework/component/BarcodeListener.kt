package com.vjh0107.barcode.framework.component

import org.bukkit.event.Listener

/**
 * 자동으로 등록되는 BarcodeComponent 리스너입니다.
 * BarcodeComponentHandler 에서 handle 합니다
 */
interface BarcodeListener : Listener, IBarcodeComponent