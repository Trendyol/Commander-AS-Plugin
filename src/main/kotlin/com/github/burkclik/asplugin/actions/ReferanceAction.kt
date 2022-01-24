package com.github.burkclik.asplugin.actions

import com.intellij.notification.NotificationDisplayType
import com.intellij.notification.NotificationGroup
import com.intellij.notification.NotificationType
import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.actionSystem.CommonDataKeys
import com.intellij.openapi.actionSystem.LangDataKeys
import com.intellij.openapi.editor.Editor
import com.intellij.openapi.project.Project
import com.intellij.openapi.vfs.VirtualFile
import com.intellij.psi.PsiElement

class ReferanceAction : AnAction() {

    override fun actionPerformed(e: AnActionEvent) {
        val editor: Editor? = e.getData(CommonDataKeys.EDITOR)
        val psiElement: PsiElement? = e.getData(LangDataKeys.PSI_ELEMENT)
        val virtualFile: VirtualFile? = e.getData(CommonDataKeys.VIRTUAL_FILE)

        val referencePath = convertToReference(
            absolutePath = virtualFile?.path.orEmpty(),
            functionName = editor?.selectionModel?.selectedText.orEmpty()
        )

        e.project?.showNotification(referencePath)
    }



    private fun Project.showNotification(message: String) {
        NotificationGroup("id", NotificationDisplayType.BALLOON)
            .createNotification(
                "Regression Test",
                message,
                NotificationType.WARNING,
                null
            ).notify(this)
    }

    private fun String?.orEmpty() = this ?: ""

    private fun convertToReference(absolutePath: String, functionName: String): String {
        return "com" + absolutePath
            .replace('/', '.')
            .substringAfter(".com")
            .substringBefore(".kt")
            .plus("#${functionName}")
    }
}