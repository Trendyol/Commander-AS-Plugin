package com.github.burkclik.asplugin.actions

import com.github.burkclik.asplugin.util.ConfigFileReader
import com.intellij.openapi.actionSystem.ActionGroup
import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.project.Project
import com.intellij.util.containers.map2Array

class RootLevelDynamicActionGroup : ActionGroup() {

    override fun getChildren(e: AnActionEvent?): Array<AnAction> {
        val project = e?.project ?: return emptyArray()
        return runCatching { readActions(project) }
            .onFailure { it.printStackTrace() }
            .getOrNull()
            .orEmpty()
            .map2Array { taskName -> createRootGradleTaskAction(splitTaskName(taskName)) }
    }

    private fun createRootGradleTaskAction(taskName: List<String>) = GradleTaskAction(
        tabName = "root",
        taskName = taskName.first(),
        gradleTerminalCommand = "./gradlew ${taskName.last()}"
    )

    private fun readActions(project: Project): List<String> {
        return ConfigFileReader()
            .readConfigFile(project, "config/Commander/root-level-tasks.txt")
    }

    private fun splitTaskName(task: String): List<String> {
        return task.split(" ", limit = MAX_SUBSTRING)
    }

    companion object {
        private const val MAX_SUBSTRING = 2
    }
}

