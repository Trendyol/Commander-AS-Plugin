package com.github.burkclik.asplugin.actions

import com.github.burkclik.asplugin.util.ScriptFileFinder
import com.intellij.openapi.actionSystem.ActionGroup
import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.ui.Messages
import com.intellij.util.containers.map2Array

class ScriptTaskAction : ActionGroup() {

    override fun getChildren(e: AnActionEvent?): Array<AnAction> {
        val event = e ?: return emptyArray()
        val project = event.project ?: return emptyArray()
        val scriptFiles = ScriptFileFinder().fileNames(project, COMMANDER_PREFIX)
        return runCatching { scriptFiles }
            .onFailure(::onError)
            .getOrNull()
            .orEmpty()
            .map2Array { createRunScriptAction(it) }
    }

    private fun onError(throwable: Throwable) {
        throwable.printStackTrace()
        Messages.showErrorDialog(throwable.message.orEmpty(), "Error")
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

    companion object {
        private const val PYTHON_SUFFIX = ".py"
        private const val COMMANDER_PREFIX = "commander_module"
    }
}