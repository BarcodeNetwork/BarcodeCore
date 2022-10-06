package com.vjh0107.barcode.buildscripts.specialsource

import com.vjh0107.barcode.buildscripts.specialsource.models.Mappings
import com.vjh0107.barcode.buildscripts.specialsource.models.Spigots
import com.vjh0107.barcode.buildscripts.specialsource.utils.createDependencyConfiguration
import net.md_5.specialsource.Jar
import net.md_5.specialsource.JarMapping
import net.md_5.specialsource.JarRemapper
import net.md_5.specialsource.provider.JarProvider
import net.md_5.specialsource.provider.JointProvider
import org.gradle.api.Project
import java.io.File
import java.io.OutputStream
import java.io.PrintStream

class RemappingStrategy(
    private val spigot: Spigots,
    private val mapping: Mappings,
    private val version: String,
    private val isReversed: Boolean
) {
    fun executeRemap(project: Project, input: File, output: File) {
        val mappingFile = project.createDependencyConfiguration(mapping, version).singleFile
        val spigotFiles = project.createDependencyConfiguration(spigot, version).files.toList()

        Jar.init(input).use { inputJar ->
            // 여기 참조 https://stackoverflow.com/questions/5936562/disable-system-err
            val printStream = PrintStream(object : OutputStream() {
                override fun write(b: Int) {}
            })

            System.setErr(printStream)
            Jar.init(spigotFiles).use { inheritanceJar ->
                val jarMapping = JarMapping()
                jarMapping.loadMappings(mappingFile.canonicalPath, isReversed, false, null, null)

                val provider = JointProvider()
                provider.add(JarProvider(inputJar))
                provider.add(JarProvider(inheritanceJar))
                jarMapping.setFallbackInheritanceProvider(provider)

                val mapper = JarRemapper(jarMapping)
                mapper.remapJar(inputJar, output)
            }
            System.setErr(printStream)
        }
    }
}