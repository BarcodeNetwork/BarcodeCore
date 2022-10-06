package com.vjh0107.barcode.framework.coroutine.utils

import java.util.*
import java.util.concurrent.ConcurrentLinkedQueue
import kotlin.coroutines.AbstractCoroutineContextElement
import kotlin.coroutines.CoroutineContext

/**
 * 스피곳 타이밍에서는, runnable 을 참조하여 표기하지만,
 * 코틀린 코루틴에서는, 하나의 코루틴에 여러개의 runnable 객체가 있을 수 있기 때문에
 * [com.vjh0107.barcode.framework.coroutine.dispatchers.MainDispatcher] 를 통해 실행되는 것들이
 * 정상적으로 표기되기 위해서 필요하다.
 */
abstract class CoroutineTimings : AbstractCoroutineContextElement(CoroutineTimings), Runnable {
    /**
     * context 의 identifier 이다.
     */
    companion object Key : CoroutineContext.Key<CoroutineTimings>

    /**
     * 코루틴의 task 들을 저장한다.
     */
    var queue: Queue<Runnable> = ConcurrentLinkedQueue()

    /**
     * 분리된 다른 쓰레드에서 task 를 실행하기 위해 Runnable 을 구현한다.
     *
     * @see java.lang.Thread.run
     */
    override fun run() {
        queue.poll()?.run()
    }
}