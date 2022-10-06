import com.vjh0107.barcode.buildscripts.common.BarcodeTaskContainerScope
import com.vjh0107.barcode.buildscripts.specialsource.SpecialSourceExtension
import org.gradle.kotlin.dsl.getByType

fun BarcodeTaskContainerScope.specialSource(scope: SpecialSourceExtension.() -> Unit) {
    val extension = this.project.extensions.getByType<SpecialSourceExtension>()
    this.archiveTask?.let {
        extension.archiveTask.set(it.get())
    }
    scope(extension)
}