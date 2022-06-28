package com.github.burkclik.asplugin.module.base

import com.github.burkclik.asplugin.path.getModuleTerminalCommand
import com.github.burkclik.asplugin.util.Util
import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.actionSystem.CommonDataKeys
import org.jetbrains.plugins.terminal.TerminalView
import java.io.IOException

abstract class GradleAction : AnAction() {

    abstract fun getActionName(): String

    override fun actionPerformed(event: AnActionEvent) {
        val path: String = event.getData(CommonDataKeys.VIRTUAL_FILE)?.path.orEmpty()
        val formattedPath = getModuleTerminalCommand(modulePath = path, gradleTaskName = getActionName())

        // terminal
        val terminalView = event.project?.let { TerminalView.getInstance(it) }
        val terminalTabTitle = "Module ${getActionName()}"
        // endregion

        try {
            val terminalWidget = terminalView?.createLocalShellWidget(event.project?.basePath, terminalTabTitle)
            terminalWidget?.executeCommand(formattedPath)
            Util.showNotification(event.project, "Running Gradle Task ${getActionName()}")
        } catch (err: IOException) {
            err.printStackTrace()
        }
    }
}