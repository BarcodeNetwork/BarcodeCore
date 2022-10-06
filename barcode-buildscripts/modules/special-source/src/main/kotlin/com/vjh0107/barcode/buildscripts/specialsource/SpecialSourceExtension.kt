package com.vjh0107.barcode.buildscripts.specialsource

import org.gradle.api.provider.Property
import org.gradle.api.tasks.bundling.AbstractArchiveTask

abstract class SpecialSourceExtension {
    /**
     * 증분 컴파일을 정상적으로 지원하지 않기 때문에, 최종 컴파일 전에는 remapping task 를 비활성화 시킬 수 있어야 컴파일이 빠릅니다.
     */
    abstract val enabled: Property<Boolean>

    /**
     * 마인크래프트 버전 입니다.
     */
    abstract val version: Property<String>

    /**
     * AbstractArchiveTask 입니다.
     * ex) Jar, ShadowJar
     */
    abstract val archiveTask: Property<AbstractArchiveTask>
}