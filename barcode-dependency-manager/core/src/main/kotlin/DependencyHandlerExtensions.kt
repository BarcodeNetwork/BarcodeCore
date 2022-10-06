import org.gradle.api.Action
import org.gradle.api.artifacts.Dependency
import org.gradle.api.artifacts.ExternalModuleDependency
import org.gradle.api.artifacts.dsl.DependencyHandler
import org.gradle.kotlin.dsl.accessors.runtime.addDependencyTo
import com.vjh0107.dependency.impl.project

private val transitive = Action<ExternalModuleDependency> { isTransitive = false }

/**
 * 의존 대상의 의존성을 제외하고 의존한다.
 */
fun DependencyHandler.compileOnlyTransitively(dependencyNotation: String): ExternalModuleDependency {
    return addDependencyTo(
        this,
        "compileOnly",
        dependencyNotation,
        configurationAction = transitive
    )
}

fun DependencyHandler.compileOnlyModule(module: BarcodeModule): Dependency? {
    return add("compileOnly", project(module.path))
}

fun DependencyHandler.compileOnlyModuleAll(module: DependencySet<BarcodeModule>) {
    module.getDependencies().forEach {
        add("compileOnly", project(it.path))
    }
}

fun DependencyHandler.implementationModule(module: BarcodeModule): Dependency? {
    return add("implementation", project(module.path))
}

fun DependencyHandler.testImplementationModule(module: BarcodeModule): Dependency? {
    return add("testImplementation", project(module.path))
}

fun DependencyHandler.implementationAll(dependencySet: DependencySet<String>) {
    dependencySet.getDependencies().forEach {
        add("implementation", it)
    }
}

fun DependencyHandler.compileOnlyAll(dependencySet: DependencySet<String>, isTransitive: Boolean = false) {
    dependencySet.getDependencies().forEach {
        if (isTransitive) {
            compileOnlyTransitively(it)
        } else {
            add("compileOnly", it)
        }
    }
}

fun DependencyHandler.testImplementationAll(dependencySet: DependencySet<String>) {
    dependencySet.getDependencies().forEach {
        add("testImplementation", it)
    }
}