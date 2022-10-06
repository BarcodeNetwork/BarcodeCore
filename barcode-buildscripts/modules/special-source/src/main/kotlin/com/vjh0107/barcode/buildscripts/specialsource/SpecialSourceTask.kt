package com.vjh0107.barcode.buildscripts.specialsource

import org.gradle.api.DefaultTask
import org.gradle.api.provider.Property
import org.gradle.api.tasks.*
import org.gradle.api.tasks.bundling.AbstractArchiveTask
import java.io.File
import java.nio.file.Files

abstract class SpecialSourceTask : DefaultTask() {
    @get:Input
    abstract val version: Property<String>

    @get:Input
    abstract val archiveTask: Property<AbstractArchiveTask>

    private fun createTempFile(): File {
        return Files.createTempFile(null, ".jar").toFile()
    }

    @TaskAction
    fun execute() {
        if (!isEnabled) {
            println("[SpecialSourceTask] obfuscateFromSpecialSource 를 건너뜁니다.")
            return
        }
        val task = archiveTask.orNull ?: throw NullPointerException("archive task 를 명시해주세요. ex) shadowJar, jar")
        val version = version.orNull ?: throw NullPointerException("버전을 명시해주세요.")

        val archiveFile = task.archiveFile.get().asFile

        val obfOutput = createTempFile()
        RemappingStrategyFactory
            .createMojangToObf(version)
            .executeRemap(project, archiveFile, obfOutput)

        val spigotOutput = createTempFile()
        RemappingStrategyFactory
            .createObfToSpigot(version)
            .executeRemap(project, obfOutput, spigotOutput)

        obfOutput.delete()

        val outputTo = task.archiveFile.get().asFile
        spigotOutput.copyTo(outputTo, true)
        spigotOutput.delete()
        println("[SpecialSourceTask] 성공적으로 task obfuscateFromSpecialSource 를 실행하였습니다.")
    }
}
