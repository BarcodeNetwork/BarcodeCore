import com.vjh0107.barcode.buildscripts.bukkitexecutor.BukkitExecutorExtension
import com.vjh0107.barcode.buildscripts.common.BarcodeTaskContainerScope
import org.gradle.kotlin.dsl.getByType

fun BarcodeTaskContainerScope.bukkitExecutor(scope: BukkitExecutorExtension.() -> Unit) {
    val extension = this.project.extensions.getByType<BukkitExecutorExtension>()
    this.archiveTask?.let {
        extension.archiveTask.set(it.get())
    }
    scope(extension)
}