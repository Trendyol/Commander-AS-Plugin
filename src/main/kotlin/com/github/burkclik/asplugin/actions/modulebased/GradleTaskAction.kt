package com.github.burkclik.asplugin.actions.modulebased

import com.github.burkclik.asplugin.util.getModuleName
import com.github.burkclik.asplugin.util.getModuleTerminalCommand
import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.actionSystem.CommonDataKeys
import org.jetbrains.plugins.terminal.TerminalView
import java.io.File
import java.io.IOException
import java.nio.file.Paths

class GradleTaskAction constructor(
    private val task: String
) : AnAction(task) {


    override fun actionPerformed(event: AnActionEvent) {
        val path: String = event.getData(CommonDataKeys.VIRTUAL_FILE)?.path.orEmpty()
        val rootPath: String = event.getData(CommonDataKeys.PROJECT)?.basePath.orEmpty().split("/").last()
        val formattedPath = getModuleTerminalCommand(rootPath = rootPath, modulePath = path, gradleTaskName = task)

        // terminal
        val terminalView = event.project?.let { TerminalView.getInstance(it) }
        val terminalTabTitle = getModuleName(rootPath, path)
        // endregion

        try {
            val terminalWidget = terminalView?.createLocalShellWidget(event.project?.basePath, terminalTabTitle)
            terminalWidget?.executeCommand(formattedPath)
        } catch (err: IOException) {
            err.printStackTrace()
        }
    }
}