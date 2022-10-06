package com.vjh0107.barcode.buildscripts.bukkitexecutor

import org.gradle.api.DefaultTask
import org.gradle.api.file.DirectoryProperty
import org.gradle.api.provider.Property
import org.gradle.api.tasks.*
import org.gradle.api.tasks.bundling.AbstractArchiveTask

abstract class BukkitExecutorTask : DefaultTask() {
    @get:Input
    abstract val bukkitFileName: Property<String>

    @get:InputDirectory
    @get:PathSensitive(PathSensitivity.RELATIVE)
    abstract val bukkitDir: DirectoryProperty

    @get:Input
    abstract val archiveTask: Property<AbstractArchiveTask>

    @TaskAction
    fun execute() {
        if (!isEnabled) {
            println("[BukkitExecutorTask] 버킷 실행을 건너뜁니다.")
            return
        }
        val bukkitFile = bukkitDir.get().asFile
        project.javaexec {
            // this.main = "-jar"
            this.mainClass.set("-jar")
            this.workingDir = bukkitFile
            this.args("../${bukkitFile.name}/${bukkitFileName.get()}")
        }
    }
}