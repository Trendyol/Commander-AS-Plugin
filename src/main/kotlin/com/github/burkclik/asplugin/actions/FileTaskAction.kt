package com.github.burkclik.asplugin.actions

import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.project.Project
import org.jetbrains.plugins.terminal.TerminalToolWindowManager

class FileTaskAction(
        taskName: String,
        private val command: String,
        private val filePaths: List<String>
) : AnAction(taskName) {
    override fun actionPerformed(e: AnActionEvent) {
        e.project?.let { project ->
            filePaths.forEach { filePath ->
                runTerminal(
                        project = project,
                        tabName = filePath,
                        command = "$command $filePath"
                )
            }
        }
    }

    private fun runTerminal(
            project: Project,
            tabName: String,
            command: String,
    ) {
        runCatching {
            TerminalToolWindowManager
                    .getInstance(project)
                    .createLocalShellWidget(project.basePath, tabName)
                    .executeCommand(command)
        }.onFailure { it.printStackTrace() }
    }
}