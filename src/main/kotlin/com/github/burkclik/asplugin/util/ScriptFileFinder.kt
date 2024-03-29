package com.github.burkclik.asplugin.util

import com.github.burkclik.asplugin.actions.ScriptTask
import com.github.burkclik.asplugin.actions.TaskModel
import com.github.burkclik.asplugin.actions.TaskType
import com.intellij.openapi.project.Project
import com.intellij.openapi.ui.Messages
import java.io.File
import java.nio.file.*
import java.nio.file.attribute.BasicFileAttributes

class ScriptFileFinder {

    fun fileNames(project: Project, prefix: String): MutableList<TaskModel> {
        val scriptFolderPath = Paths.get(getModuleTasksConfigAbsolutePath(project).toUri())
        val files = mutableListOf<TaskModel>()

        try {
            Files.walkFileTree(
                scriptFolderPath,
                setOf(FileVisitOption.FOLLOW_LINKS),
                Int.MAX_VALUE,
                object : SimpleFileVisitor<Path>() {
                    override fun visitFile(file: Path?, attrs: BasicFileAttributes?): FileVisitResult {
                        if (file != null && file.toString().contains(prefix)) {
                            files.add(ScriptTask(file, TaskType.SCRIPT))
                        }
                        return FileVisitResult.CONTINUE
                    }
                }
            )
        } catch (ex: Exception) {
            ex.printStackTrace()
            Messages.showErrorDialog("Error while getting Python files: ${ex.message}", "Error")
        }

        return files
    }

    private fun getModuleTasksConfigAbsolutePath(project: Project): Path {
        return "scripts/".replace('/', File.separatorChar)
            .let { project.basePath?.let(Paths::get)?.resolve(it) ?: Paths.get(it) }
    }
}