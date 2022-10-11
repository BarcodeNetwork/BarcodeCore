import org.gradle.api.DefaultTask
import org.gradle.api.tasks.AbstractCopyTask
import org.gradle.api.tasks.bundling.AbstractArchiveTask

fun AbstractCopyTask.excludeKotlin() {
    this.exclude("**/kotlin/**")
}

fun AbstractCopyTask.excludeJetbrains() {
    this.exclude("**/org/jetbrains/**")
    this.exclude("**/org/intellij/**")
}

fun DefaultTask.dependShadowJar(module: BarcodeModule) {
    this.dependsOn("${module.path}:shadowJar")
}

fun DefaultTask.dependModuleTask(module: BarcodeModule, taskName: String) {
    this.dependsOn("${module.path}:$taskName")
}

fun AbstractArchiveTask.setBuildOutputDir(
    targetDir: String = "${project.rootDir.path}/build_outputs"
) {
    val file = project.file(targetDir)
    this.destinationDirectory.set(file)
}

fun AbstractCopyTask.includeAll(dependencySet: DependencySet<String>): AbstractCopyTask {
    include(dependencySet.getDependencies())
    return this
}