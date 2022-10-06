package com.vjh0107.barcode.buildscripts.specialsource.utils

import com.vjh0107.barcode.buildscripts.specialsource.VersionedDependency
import org.gradle.api.Project
import org.gradle.api.artifacts.Configuration

/**
 * configuration container 에 추가되지 않는 configuration 을 만듭니다.
 */
fun Project.createDependencyConfiguration(versionedDependency: VersionedDependency, version: String): Configuration {
    return this.configurations.detachedConfiguration(this.dependencies.create(versionedDependency.path(version)))
}