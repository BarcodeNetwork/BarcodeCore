package com.vjh0107.barcode.cutscene.kotest

import be.seeseemelk.mockbukkit.MockBukkit
import io.kotest.core.annotation.AutoScan
import io.kotest.core.listeners.ProjectListener

@AutoScan
object MockedServerInitializer : ProjectListener {
    override suspend fun beforeProject() {
        println("MockedServerInitializer 가 정상적으로 작동하였습니다.")
        MockBukkit.mock().run {
            this.setPlayers(10)
        }
    }

    override suspend fun afterProject() {
        MockBukkit.unmock()
    }
}