extra["includeAll"] = fun(modulesDir: String) {
    file("${rootProject.projectDir.path}/${modulesDir.replace(":", "/")}/").listFiles()?.forEach { modulePath ->
        include("${modulesDir.replace("/", ":")}:${modulePath.name}")
    }
}

extra["includeAllExcept"] = fun(modulesDir: String, exclude: String) {
    file("${rootProject.projectDir.path}/${modulesDir.replace(":", "/")}/")
        .listFiles()?.filter { it.name != exclude }?.forEach { modulePath ->
            include("${modulesDir.replace("/", ":")}:${modulePath.name}")
        }
}