package com.github.burkclik.asplugin.util

import com.intellij.openapi.project.Project
import java.io.File
import java.nio.file.Path
import java.nio.file.Paths

class ConfigFileReader constructor() {

    fun readConfigFile(project: Project, path: String): List<String> {
        val moduleTasksPath = getModuleTasksConfigAbsolutePath(
            project,
            path
        )

        return moduleTasksPath
            .toFile()
            .also { assertFileIsReadable(it) }
            .readLines()
    }

    private fun getModuleTasksConfigAbsolutePath(project: Project, relativeConfigPath: String): Path {
        return relativeConfigPath.replace('/', File.separatorChar)
            .let { project.basePath?.let(Paths::get)?.resolve(it) ?: Paths.get(it) }
    }

    private fun assertFileIsReadable(it: File) {
        require(it.canRead()) {
            "Application couldn't read file on: ${it.path}. Make sure it exists and it's readable by application"
        }
    }
}