import com.vjh0107.barcode.buildscripts.common.BarcodeTaskContainerScope
import org.gradle.api.Project

fun Project.barcodeTasks(scope: BarcodeTaskContainerScope.() -> Unit) {
    scope(BarcodeTaskContainerScope(this))
}