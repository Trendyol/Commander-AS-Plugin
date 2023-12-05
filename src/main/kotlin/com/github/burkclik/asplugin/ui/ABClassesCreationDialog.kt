package com.github.burkclik.asplugin.ui

import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.actionSystem.CommonDataKeys
import com.intellij.openapi.ui.DialogPanel
import com.intellij.openapi.ui.DialogWrapper
import com.intellij.ui.components.JBTextField
import com.intellij.ui.dsl.builder.AlignY
import com.intellij.ui.dsl.builder.TopGap
import com.intellij.ui.dsl.builder.panel
import io.ktor.utils.io.errors.*
import org.jetbrains.plugins.terminal.TerminalToolWindowManager
import java.awt.Dimension
import java.nio.file.Files
import java.nio.file.Path
import javax.swing.JComponent

class ABClassesCreationDialog(val event: AnActionEvent, val scriptPath: Path) :
    DialogWrapper(event.project, null, true, IdeModalityType.MODELESS, false) {

    private val prefixTextField: JBTextField = JBTextField()
    private val testNameTextField: JBTextField = JBTextField()

    //val obj = Json.decodeFromString<ScriptData>("""{"title":"Create AB Classes", flag:[{"name":"-PFlag_name", "placeholder":"placeholder", "label":"label", "hint":"hint"}]}""")

    init {
        title = "Create AB Classes"
        init()
    }

    override fun createCenterPanel(): JComponent = createDialogPanel()

    private fun createDialogPanel(): DialogPanel {
        val dialogPanel = DialogPanel()
        dialogPanel.minimumSize = Dimension(MINIMUM_WIDTH, MINIMUM_HEIGHT)
        dialogPanel.preferredSize = Dimension(MINIMUM_WIDTH, MINIMUM_HEIGHT)

        val dialogContent = panel {
            createTextField(
                textFieldComponent = prefixTextField,
                label = "Label: ",
                placeholder = "Placeholder",
                hint = "Hint"
            )
            row {
                button("Create") {
                    createTerminalInstance(e = event, createABClassParams(), scriptPath)
                    this@ABClassesCreationDialog.close(0)
                }.align(align = AlignY.CENTER)
            }.topGap(TopGap.SMALL)
        }

        dialogPanel.add(dialogContent)
        return dialogPanel
    }

    private fun createABClassParams(): ABClassParams {
        return ABClassParams(
            prefix = prefixTextField.text,
            abTestName = testNameTextField.text,
            packageName = getPackageName(event)
        )
    }

    private fun createTerminalInstance(e: AnActionEvent, abClassParams: ABClassParams, scriptPath: Path) {
        val project = e.project ?: return
        val terminalView = TerminalToolWindowManager.getInstance(project)
        try {
            val terminalWidget = terminalView.createLocalShellWidget(e.project?.basePath, "AB Classes")
            terminalWidget.executeCommand("python3 $scriptPath -Pfile_name ${abClassParams.prefix}")
        } catch (err: IOException) {
            err.printStackTrace()
        }
    }

    private fun getFirstLine(script: Path): String {
        return Files.lines(script)
            .findFirst()
            .orElse("")
    }

    private fun getPackageName(event: AnActionEvent): String {
        val path: String = event.getData(CommonDataKeys.VIRTUAL_FILE)?.path.orEmpty()
        val rootPath: String = event.getData(CommonDataKeys.PROJECT)?.basePath.orEmpty().split("/").last()
        return com.github.burkclik.asplugin.util.getPackageName(rootPath, path)
    }

    companion object {
        private const val MINIMUM_WIDTH = 400
        private const val MINIMUM_HEIGHT = 200
    }
}