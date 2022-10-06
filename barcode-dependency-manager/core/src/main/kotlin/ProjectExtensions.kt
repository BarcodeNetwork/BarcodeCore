import org.gradle.api.Project
import org.gradle.api.file.ConfigurableFileCollection

fun Project.jar(jarDir: String): ConfigurableFileCollection {
    val dir = "${rootDir.path}/dependencies/$jarDir"
    return files(dir)
}