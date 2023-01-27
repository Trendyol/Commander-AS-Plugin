package com.github.burkclik.asplugin.actions

import com.github.burkclik.asplugin.actions.modulebased.GradleTaskAction
import com.intellij.openapi.actionSystem.ActionGroup
import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.project.Project
import com.intellij.util.containers.map2Array
import java.io.File
import java.nio.file.Path
import java.nio.file.Paths

class DynamicActionGroup : ActionGroup() {

    override fun getChildren(e: AnActionEvent?): Array<AnAction> {
        return readActions(e?.project)
    }

    private fun readActions(project: Project?): Array<AnAction> {
        project ?: return emptyArray()
        val moduleTasksPath = getModuleTasksConfigAbsolutePath(
            project,
            "config/Commander/module-tasks.txt"
        )

        val file = moduleTasksPath.toFile().also { assertFileIsReadable(it) }

        return file
            .readLines()
            .map2Array { GradleTaskAction(it) }
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