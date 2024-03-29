package com.github.burkclik.asplugin.actions

import com.github.burkclik.asplugin.ui.ABClassesCreationDialog
import com.github.burkclik.asplugin.ui.CreateApiImplModuleDialog
import com.github.burkclik.asplugin.ui.CreateLibraryModuleDialog
import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent
import java.nio.file.Path

class RunScriptAction(private val scriptPath: Path, private val actionName: String) : AnAction(actionName) {
    override fun actionPerformed(e: AnActionEvent) {
        when {
            actionName.contains(AB_CLASS_PREFIX) -> {
                ABClassesCreationDialog(event = e, scriptPath = scriptPath).show()
            }

            actionName.contains(LIBRARY_MODULE_PREFIX) -> {
                CreateLibraryModuleDialog(event = e, scriptPath = scriptPath).show()
            }

            actionName.contains(API_IMPL_MODULE_PREFIX) -> {
                CreateApiImplModuleDialog(event = e, scriptPath = scriptPath).show()
            }
        }
    }

    companion object {
        private const val AB_CLASS_PREFIX = "ab class"
        private const val LIBRARY_MODULE_PREFIX = "library"
        private const val API_IMPL_MODULE_PREFIX = "api impl"
    }
}