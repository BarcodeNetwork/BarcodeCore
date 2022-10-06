package com.vjh0107.barcode.framework.coroutine.service

/**
 * 서버 startup 과정에서만 사용된다. 서버가 실행중일때는 아무 영향을 주지 않는다.
 */
interface WakeUpBlockService {

    /**
     * 메인쓰레드 입니다.
     */
    var primaryThread: Thread?

    /**
     * blocking 되었을 경우 쓰레드가 살아있는지 확인하기 위해 스케줄러 관리 구현체를 호출한다.
     */
    fun ensureWakeup()

    /**
     * 서비스를 dispose 한다.
     */
    fun dispose()
}