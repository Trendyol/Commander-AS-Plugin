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
import java.awt.Toolkit
import java.awt.datatransfer.StringSelection

class CopyReferenceAction : AnAction() {

    override fun actionPerformed(event: AnActionEvent) {
        val devices = event.project?.let { AndroidSdkUtils.getDebugBridge(it)?.devices }
        val editor: Editor? = event.getData(CommonDataKeys.EDITOR)
        val virtualFile: VirtualFile? = event.getData(CommonDataKeys.VIRTUAL_FILE)
        val fullPath = getFullPathTerminalCommand(
            absolutePath = virtualFile?.path.orEmpty(),
            functionName = editor?.selectionModel?.selectedText.orEmpty()
        )

        showNotification(event.project, fullPath)
        if (devices.isNullOrEmpty()) {
            showNotification(event.project, message = "No devices connected")
        }
        else {
            showNotification(event.project, message = "${devices.size} devices connected")
        }
        val stringSelection = StringSelection(fullPath)
        val clipBoard = Toolkit.getDefaultToolkit().systemClipboard
        clipBoard.setContents(stringSelection, stringSelection)
    }

    private fun showNotification(project: Project?, message: String) {
        NotificationGroupManager.getInstance().getNotificationGroup("Custom Notification Group")
            .createNotification(message, NotificationType.ERROR)
            .notify(project)
    }
}