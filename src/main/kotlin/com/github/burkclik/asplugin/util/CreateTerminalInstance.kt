package com.github.burkclik.asplugin.util

import com.intellij.openapi.actionSystem.AnActionEvent
import org.jetbrains.plugins.terminal.TerminalToolWindowManager
import java.io.IOException

fun createTerminalInstance(e: AnActionEvent, shellCommand: String) {
    val project = e.project ?: return
    val terminalView = TerminalToolWindowManager.getInstance(project)
    try {
        val terminalWidget = terminalView.createLocalShellWidget(e.project?.basePath, "Create Module")
        terminalWidget.executeCommand(shellCommand)
    } catch (err: IOException) {
        err.printStackTrace()
    }
}