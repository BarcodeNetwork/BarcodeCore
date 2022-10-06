package com.vjh0107.barcode.core.dependencies

import nl.pim16aap2.bigDoors.BigDoors
import nl.pim16aap2.bigDoors.Door

object BigDoorsAdapter {
    private val commander = BigDoors.get().commander

    private fun getDoor(doorID: Long): Door? {
        return commander.getDoor(null, doorID)
    }

    fun isDoorOpened(doorID: Long): Boolean {
        return getDoor(doorID)?.isOpen ?: throw NullPointerException("아이디 $doorID 의 문을 찾을 수 없습니다.")
    }

    fun isDoorBusy(doorID: Long): Boolean {
        return commander.isDoorBusy(doorID)
    }

    fun setDoorState(open: Boolean, doorID: Long) {
        if (isDoorBusy(doorID)) {
            return
        }
        if (isDoorOpened(doorID) == open) {
            return
        }
        BigDoors.get().toggleDoor(doorID)
    }
}