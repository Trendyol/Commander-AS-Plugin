package com.github.burkclik.asplugin.actions

import com.github.burkclik.asplugin.util.ConfigFileReader
import com.github.burkclik.asplugin.util.getModuleName
import com.github.burkclik.asplugin.util.getModuleTerminalCommand
import com.intellij.openapi.actionSystem.ActionGroup
import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.actionSystem.CommonDataKeys
import com.intellij.openapi.project.Project
import com.intellij.util.containers.map2Array

class ModuleLevelDynamicActionGroup : ActionGroup() {

    override fun getChildren(e: AnActionEvent?): Array<AnAction> {
        val event = e ?: return emptyArray()
        val project = e.project ?: return emptyArray()
        return runCatching { readActions(project) }
            .onFailure { it.printStackTrace() }
            .getOrNull()
            .orEmpty()
            .map2Array { createModuleLevelGradleTaskAction(event, splitTaskName(it)) }
    }

    private fun readActions(project: Project): List<String> {
        return ConfigFileReader()
            .readConfigFile(project, "config/Commander/module-tasks.txt")
    }

    private fun createModuleLevelGradleTaskAction(event: AnActionEvent, task: Pair<String, String>): GradleTaskAction {
        val (taskName, commands) = task
        val path: String = event.getData(CommonDataKeys.VIRTUAL_FILE)?.path.orEmpty()
        val rootPath: String = event.getData(CommonDataKeys.PROJECT)?.basePath.orEmpty().split("/").last()
        val gradleTerminalCommand =
            getModuleTerminalCommand(rootPath = rootPath, modulePath = path, gradleTaskName = commands)
        val moduleName = getModuleName(rootPath, path)
        return GradleTaskAction(
            tabName = moduleName,
            taskName = taskName,
            gradleTerminalCommand = gradleTerminalCommand
        )
    }

    private fun splitTaskName(task: String): Pair<String, String> {
        val splittedTask = task.split(DELIMITER)
        val taskName = splittedTask.first()
        val commands = splittedTask.filterIndexed { index, _ -> index > 0 }.joinToString(",")
        return taskName to "{$commands}"
    }

    companion object {
        const val DELIMITER = " "
    }
}