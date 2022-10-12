
# BarcodeNetwork Core
<img src="https://github.com/vjh0107/BarcodeCore/blob/master/BarcodeNetworkCore.png" width="300px">

# How to build?

barcode-buildscripts/initializers/spigot/install-spigot-dependencies.sh 를
통해 버전에 맞는 매핑된 스피곳을 로컬 메이븐에 추가한 후 빌드합니다.

# Usage of Build Plugins
GradlePluginPortal 에 publish 되어 있는 바코드 네트워크의 
빌드 플러그인들은 barcodeTasks 스코프 안에서 공통된 인자를 사용하여 실행될 수 있습니다.

```kotlin
barcodeTasks {
  // 바코드 네트워크의 빌드 플러그인들이 의존할 AbstractArchiveTask 
  archiveTask = tasks.shadowJar
  
  bukkitResource {
    //...
  }
  specialSource {
    //...
  }
  //...
}
```

### Bukkit Executor ![Generic badge](https://img.shields.io/badge/version-1.0.0-green.svg)

BukkitExecutor 플러그인은 AbstractArchiveTask 를 의존하여 빌드 후 자동으로 버킷이 실행되게 할 수 있습니다.
```kotlin
plugins {
   id("com.vjh0107.bukkit-executor") version "<version>"
}
```
```kotlin
barcodeTasks {
    bukkitExecutor {
        // 활성화여부를 결정할 수 있습니다.
        enabled.set(true)
        // 의존할 AbstractArchiveTask
        archiveTask.set(tasks.shadowJar)
        // 버킷이 있는 DirectoryProperty, 이 위에서 jvm 이 실행됩니다.
        bukkitDir.set(file("testBukkit/"))
        // 버킷의 jar 파일
        bukkitFileName.set("paper.jar")
    }
}
```

### Bukkit Resource Generator ![Generic badge](https://img.shields.io/badge/version-1.0.0-green.svg)
BukkitResourceGenerator 플러그인은 Bukkit 플러그인의 plugin.yml 을 자동으로 생성해줍니다.
```kotlin
plugins {
    id("com.vjh0107.bukkit-resource-generator") version "<version>"
}
```
```kotlin
barcodeTasks {
    bukkitResource {
        main = "com.vjh0107.barcode.framework.BarcodeFrameworkPlugin"
        name = "BarcodeFramework"
        apiVersion = "1.18"
        author = "vjh0107"
        softDepend = listOf(
            "Vault",
            "HolographicDisplays",
            "PlaceholderAPI"
        )
    }
}
```

### Special Source ![Generic badge](https://img.shields.io/badge/version-1.0.0-green.svg)
SpecialSource 플러그인은 [SpecialSource](https://github.com/md-5/SpecialSource) 의 Gradle 플러그인 입니다.
개발할때는 Mojang 의 name definition 을 사용하고, 빌드할때는 Spigot 의 난독화된 name definition 으로 다시 바꿔줍니다.
```kotlin
plugins {
    id("com.vjh0107.special-source") version "<version>"
}
```
```kotlin
barcodeTasks {
    specialSource {
        version.set("1.18.2")
        archiveTask.set(tasks.shadowJar)
        enabled.set(true)
    }
}
```

### KSP Extension ![Generic badge](https://img.shields.io/badge/version-1.0.0-green.svg)
KSPExtension 플러그인은 Google 에서 배포한 KSP 의 설정을 자동으로 해주며 확장함수를 제공합니다.
```kotlin
plugins {
    id("com.google.devtools.ksp") version "<ksp-version>"
    id("com.vjh0107.ksp-extension") version "<version>"
}
```
