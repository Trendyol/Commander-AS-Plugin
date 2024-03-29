package com.github.burkclik.asplugin.actions

import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent
import org.jetbrains.plugins.terminal.TerminalToolWindowManager
import java.io.IOException

class GradleTaskAction(
    val tabName: String,
    taskName: String,
    val gradleTerminalCommand: String,
) : AnAction(taskName) {
    override fun actionPerformed(e: AnActionEvent) {
        val project = e.project ?: return
        val terminalView = TerminalToolWindowManager.getInstance(project)
        try {
            val terminalWidget = terminalView.createLocalShellWidget(e.project?.basePath, tabName)
            terminalWidget.executeCommand(gradleTerminalCommand)
        } catch (err: IOException) {
            err.printStackTrace()
        }
    }
}