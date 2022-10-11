package com.vjh0107.barcode.framework.component.handler

enum class HandlerPriority(val slot: Int) {
    /**
     * repository 를 먼저 로드합니다. repository 를 bean 에 등록합니다.
     */
    REPOSITORY(0),

    /**
     * 예를 들어, service 를 repository 가 로드되기 전에 사용할 수 있으니, 우선순위를 정립합니다.
     * 또한, 다른 컴포넌트 핸들러들의 생성자 주입을 위해 먼저 로드합니다.
     */
    PROVIDER(1),

    /**
     * 앞에서 bean 들에 대한 처리를 다 하였으니 이로써 컴포넌트들은 생성자주입을 받을 수 있게 되었습니다.
     */
    DEFAULT(2),
    HIGH(3),
    HIGHEST(4)
}
