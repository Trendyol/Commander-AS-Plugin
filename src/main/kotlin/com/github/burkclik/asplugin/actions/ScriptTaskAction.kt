package com.github.burkclik.asplugin.actions

import com.intellij.openapi.actionSystem.ActionGroup
import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.project.Project
import com.intellij.openapi.ui.Messages
import com.intellij.util.containers.map2Array
import com.intellij.util.containers.orNull
import java.io.File
import java.nio.file.*
import java.nio.file.attribute.BasicFileAttributes
import kotlin.streams.toList

class ScriptTaskAction : ActionGroup() {

    override fun getChildren(e: AnActionEvent?): Array<AnAction> {
        val event = e ?: return emptyArray()
        val project = event.project ?: return emptyArray()
        return runCatching { fileNames(project) }
            .onFailure(::onError)
            .getOrNull()
            .orEmpty()
            .map2Array { RunScriptAction(it) }
    }

    private fun onError(throwable: Throwable) {
        throwable.printStackTrace()
        Messages.showErrorDialog(throwable.message.orEmpty(), "Error")
    }

    private fun fileNames(project: Project): List<Path> {
        val scriptFolderPath = Paths.get(getModuleTasksConfigAbsolutePath(project).toUri())
        val files = mutableListOf<Path>()

        try {
            Files.walkFileTree(
                scriptFolderPath,
                setOf(FileVisitOption.FOLLOW_LINKS),
                Int.MAX_VALUE,
                object : SimpleFileVisitor<Path>() {
                    override fun visitFile(file: Path?, attrs: BasicFileAttributes?): FileVisitResult {
                        if (file != null && file.toString().endsWith(PYTHON_FILE_EXT)) {
                            files.add(file)
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

    companion object {
        private const val PYTHON_FILE_EXT = ".py"
    }
}