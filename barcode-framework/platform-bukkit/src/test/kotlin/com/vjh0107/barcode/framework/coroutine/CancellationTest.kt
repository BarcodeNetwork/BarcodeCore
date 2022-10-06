package com.vjh0107.barcode.framework.coroutine

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.junit.jupiter.api.Test

class CancellationTest {
    @Test
    fun cancellationTest() {
        val job = CoroutineScope(Dispatchers.Default).launch {
            println("3 secs...")
            delay(3000)
            println("complete")
        }
        Thread.sleep(1000)
        job.cancel()
        Thread.sleep(3000)
    }
}