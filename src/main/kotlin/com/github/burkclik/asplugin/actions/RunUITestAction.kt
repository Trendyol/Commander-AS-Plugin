package com.github.burkclik.asplugin.actions

import com.github.burkclik.asplugin.path.getFullPathTerminalCommand
import com.intellij.notification.NotificationGroupManager
import com.intellij.notification.NotificationType
import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.actionSystem.CommonDataKeys
import com.intellij.openapi.editor.Editor
import com.intellij.openapi.project.Project
import com.intellij.openapi.vfs.VirtualFile
import org.jetbrains.android.sdk.AndroidSdkUtils
import java.io.IOException
import org.jetbrains.plugins.terminal.TerminalView

class RunUITestAction : AnAction() {

    override fun actionPerformed(event: AnActionEvent) {

        //region FilePath
        val editor: Editor? = event.getData(CommonDataKeys.EDITOR)
        val virtualFile: VirtualFile? = event.getData(CommonDataKeys.VIRTUAL_FILE)
        val fullPath = getFullPathTerminalCommand(
            absolutePath = virtualFile?.path.orEmpty(),
            functionName = editor?.selectionModel?.selectedText.orEmpty()
        )
        //endregion

        //region Terminal
        val terminalView = event.project?.let { TerminalView.getInstance(it) }
        val terminalTabTitle = "UI Test"
        //endregion

        val devices = event.project?.let { AndroidSdkUtils.getDebugBridge(it)?.devices }

        if (devices.isNullOrEmpty()) {
            showNotification(event.project, message = "No devices connected")
        } else {
            showNotification(event.project, message = fullPath)
            try {
                val shellTerminalWidget = terminalView?.createLocalShellWidget(event.project?.basePath, terminalTabTitle)
                shellTerminalWidget?.executeCommand(fullPath)
            } catch (err: IOException) {
                err.printStackTrace()
            }
            showNotification(event.project, message = "${devices.size} devices connected")
        }
    }

    private fun showNotification(project: Project?, message: String) {
        NotificationGroupManager.getInstance().getNotificationGroup("Custom Notification Group")
            .createNotification(message, NotificationType.ERROR)
            .notify(project)
    }
}