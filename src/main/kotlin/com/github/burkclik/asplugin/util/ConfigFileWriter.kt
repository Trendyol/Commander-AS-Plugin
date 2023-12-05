package com.github.burkclik.asplugin.util

import com.intellij.openapi.project.Project
import com.intellij.openapi.vfs.VirtualFileManager
import java.io.BufferedReader
import java.io.BufferedWriter
import java.io.File
import java.io.FileReader
import java.io.FileWriter
import java.io.IOException
import java.nio.file.Path
import java.nio.file.Paths

class ConfigFileWriter {

    fun appendToTextFile(project: Project, path: String ,content: String) {
        val moduleTasksPath = getModuleTasksConfigAbsolutePath(project, path)
        val filePath = File(moduleTasksPath.toString())
        try {
            val fileWriter = FileWriter(filePath, true)
            val bufferedWriter = BufferedWriter(fileWriter)
            val existingData = readExistingData(filePath.toString())

            if (existingData.isNotEmpty()) {
                bufferedWriter.newLine()
            }

            bufferedWriter.append(content)
            bufferedWriter.close()
            VirtualFileManager.getInstance().syncRefresh()
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }

    private fun readExistingData(filePath: String): String {
        val content = StringBuilder()
        try {
            val fileReader = FileReader(filePath)
            val bufferedReader = BufferedReader(fileReader)

            var line: String?
            while (bufferedReader.readLine().also { line = it } != null) {
                content.append(line)
                content.append("\n")
            }

            bufferedReader.close()
        } catch (e: IOException) {
            e.printStackTrace()
        }

        return content.toString()
    }

    private fun getModuleTasksConfigAbsolutePath(project: Project, relativeConfigPath: String): Path {
        return relativeConfigPath.replace('/', File.separatorChar)
            .let { project.basePath?.let(Paths::get)?.resolve(it) ?: Paths.get(it) }
    }
}