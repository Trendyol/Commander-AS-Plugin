package com.github.burkclik.asplugin.actions

import com.github.burkclik.asplugin.util.ConfigFileReader
import com.intellij.openapi.actionSystem.ActionGroup
import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.actionSystem.CommonDataKeys
import com.intellij.openapi.project.Project
import com.intellij.util.containers.map2Array

class FileLevelDynamicActionGroup : ActionGroup() {
    override fun getChildren(e: AnActionEvent?): Array<AnAction> {
        val event = e ?: return emptyArray()
        val project = e.project ?: return emptyArray()
        return runCatching { readActions(project) }
                .onFailure { it.printStackTrace() }
                .getOrNull()
                .orEmpty()
                .map2Array { task ->
                    val (taskName, command) = splitTaskName(task)
                    createFileLevelGradleTaskAction(event, taskName, command)
                }
    }

    private fun readActions(project: Project): List<String> {
        return ConfigFileReader()
                .readConfigFile(project, "config/Commander/file-tasks.txt")
    }

    private fun createFileLevelGradleTaskAction(
            event: AnActionEvent,
            taskName: String,
            command: String
    ): AnAction = FileTaskAction(
            taskName = taskName,
            command = command,
            filePaths = event.getSelectedPaths()
    )


    private fun AnActionEvent.getSelectedPaths(): List<String> {
        val basePath = project?.basePath.orEmpty()
        return getData(CommonDataKeys.VIRTUAL_FILE_ARRAY)?.map {
            it.path.removePrefix("$basePath/")
        }.orEmpty()
    }

    private fun splitTaskName(task: String): Pair<String, String> = task
            .split(" ", limit = MAX_SUBSTRING)
            .run { first() to last() }

    companion object {
        private const val MAX_SUBSTRING = 2
    }
}
