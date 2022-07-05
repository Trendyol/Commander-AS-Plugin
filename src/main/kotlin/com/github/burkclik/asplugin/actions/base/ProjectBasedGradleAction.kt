package com.github.burkclik.asplugin.actions.base

import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent
import org.jetbrains.plugins.terminal.TerminalView
import java.io.IOException

abstract class ProjectBasedGradleAction : AnAction() {

    abstract fun getActionName(): String

    override fun actionPerformed(e: AnActionEvent) {

        // terminal
        val terminalView = e.project?.let { TerminalView.getInstance(it) }
        val terminalTabTitle = getActionName()
        // endregion

        try {
            val terminalWidget = terminalView?.createLocalShellWidget(e.project?.basePath, terminalTabTitle)
            terminalWidget?.executeCommand(getActionName())
        } catch (err: IOException) {
            err.printStackTrace()
        }
    }
}