import com.vjh0107.barcode.buildscripts.common.BarcodeTaskContainerScope
import com.vjh0107.barcode.buildscripts.resourcegenerator.BukkitResourcePropertyExtension
import org.gradle.kotlin.dsl.getByType

fun BarcodeTaskContainerScope.bukkitResource(scope: BukkitResourcePropertyExtension.() -> Unit) {
    scope(this.project.extensions.getByType())
}