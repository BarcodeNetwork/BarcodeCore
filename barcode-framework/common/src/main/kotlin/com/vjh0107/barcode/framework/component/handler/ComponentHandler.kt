package com.vjh0107.barcode.framework.component.handler

interface ComponentHandler {
    /**
     * 컴포넌트 핸들러가 enable 되었을 때
     */
    fun onEnable()

    /**
     * 컴포넌트 핸들러가 reload 되었을 때
     */
    fun onReload()

    /**
     * 컴포넌트 핸들러가 disable 되었을 때
     */
    fun onDisable()
}