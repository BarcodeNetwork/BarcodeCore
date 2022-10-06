package com.vjh0107.barcode.buildscripts.bukkitexecutor

import org.gradle.api.file.DirectoryProperty
import org.gradle.api.provider.Property
import org.gradle.api.tasks.Input
import org.gradle.api.tasks.TaskProvider
import org.gradle.api.tasks.bundling.AbstractArchiveTask

abstract class BukkitExecutorExtension {
    /**
     * 실행 시킬까요?
     */
    abstract val enabled: Property<Boolean>

    /**
     * 버킷 파일 이름 ex) paperclip.jar, paper-1.18.2-402.jar
     */
    abstract val bukkitFileName: Property<String>

    /**
     * 버킷 폴더 디렉토리
     */
    abstract val bukkitDir: DirectoryProperty

    /**
     * 어느 AbstractArchiveTask 을 의존할 지
     */
    abstract val archiveTask: Property<AbstractArchiveTask>
}