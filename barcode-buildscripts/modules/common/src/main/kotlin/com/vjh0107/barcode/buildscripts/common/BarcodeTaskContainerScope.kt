package com.vjh0107.barcode.buildscripts.common

import org.gradle.api.Project
import org.gradle.api.tasks.TaskProvider
import org.gradle.api.tasks.bundling.AbstractArchiveTask

class BarcodeTaskContainerScope(val project: Project) {
    var archiveTask: TaskProvider<out AbstractArchiveTask>? = null
}