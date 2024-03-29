package com.github.burkclik.asplugin.actions

import com.github.burkclik.asplugin.util.ConfigFileReader
import com.github.burkclik.asplugin.util.ScriptFileFinder
import com.intellij.openapi.actionSystem.ActionGroup
import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.project.Project
import com.intellij.util.containers.map2Array

class RootLevelDynamicActionGroup : ActionGroup() {

    override fun getChildren(e: AnActionEvent?): Array<AnAction> {
        val project = e?.project ?: return emptyArray()
        return runCatching { mergeTasks(project) }
            .onFailure { it.printStackTrace() }
            .getOrNull()
            .orEmpty()
            .map2Array { task -> decideTaskAction(task) }
    }

    private fun decideTaskAction(taskModel: TaskModel): AnAction {
        return when (taskModel.type) {
            TaskType.GRADLE -> createRootGradleTaskAction(taskModel)
            TaskType.SCRIPT -> createRunScriptAction(taskModel)
        }
    }

    private fun createRunScriptAction(task: TaskModel): RunScriptAction {
        val scriptTask = task as ScriptTask
        val scriptPath = scriptTask.path
        val scriptName = scriptPath.fileName.toString()
            .removePrefix(COMMANDER_PREFIX)
            .replace("_", " ")
            .removeSuffix(PYTHON_SUFFIX)
        return RunScriptAction(scriptPath, scriptName)
    }

    private fun createRootGradleTaskAction(task: TaskModel): GradleTaskAction {
        val taskName = splitTaskName((task as GradleTask).name)
        return GradleTaskAction(
            tabName = "root",
            taskName = taskName.first(),
            gradleTerminalCommand = "./gradlew ${taskName.last()}"
        )
    }

    private fun readActions(project: Project): MutableList<TaskModel> {
        return ConfigFileReader()
            .readConfigFile(project, "config/Commander/root-level-tasks.txt")
            .map { GradleTask(name = it) }
            .toMutableList()
    }

    private fun splitTaskName(task: String): List<String> {
        return task.split(" ", limit = MAX_SUBSTRING)
    }

    private fun mergeTasks(project: Project): MutableList<TaskModel> {
        val scriptTasks = ScriptFileFinder().fileNames(project, COMMANDER_PREFIX)
        val gradleTasks = readActions(project)
        gradleTasks.addAll(scriptTasks)
        return gradleTasks
    }

    companion object {
        private const val MAX_SUBSTRING = 2
        private const val PYTHON_SUFFIX = ".py"
        private const val COMMANDER_PREFIX = "commander_root"
    }
}

