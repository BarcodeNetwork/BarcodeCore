import org.gradle.internal.Cast.uncheckedCast

rootProject.name = "barcode-buildscripts"

apply(from = "scripts/module-management/module-includer.gradle.kts")

val includeAll = uncheckedCast<(String) -> Unit>(extra["includeAll"])
includeAll?.invoke("modules")

