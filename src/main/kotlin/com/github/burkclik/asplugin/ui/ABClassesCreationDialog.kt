package com.github.burkclik.asplugin.ui

import com.github.burkclik.asplugin.util.createTerminalInstance
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.actionSystem.CommonDataKeys
import com.intellij.openapi.ui.DialogPanel
import com.intellij.openapi.ui.DialogWrapper
import com.intellij.ui.components.JBTextField
import com.intellij.ui.dsl.builder.AlignX
import com.intellij.ui.dsl.builder.TopGap
import com.intellij.ui.dsl.builder.panel
import java.awt.Dimension
import java.nio.file.Files
import java.nio.file.Path
import javax.swing.JComponent

class ABClassesCreationDialog(val event: AnActionEvent, val scriptPath: Path) :
    DialogWrapper(event.project, null, true, IdeModalityType.MODELESS, false) {

    private val prefixTextField: JBTextField = JBTextField()

    private val path: String = event.getData(CommonDataKeys.VIRTUAL_FILE)?.path.orEmpty()

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
                label = "Enter the AB Class name prefix: ",
                placeholder = "OrderBrowsingHistoryProductsSliderAB",
                hint = "Example: FitAsExpectedService the output directory:" +
                        "<br>FitAsExpectedServiceAB</br>" +
                        "<br>FitAsExpectedServiceABDecider</br>" +
                        "<br>FitAsExpectedServiceABDeciderTest</br>"
            )
            row {
                comment(getFirstLine(scriptPath))
            }.topGap(TopGap.MEDIUM)
            row {
                button("Create") {
                    val shellCommand = "python3 $scriptPath -Pfile_name ${prefixTextField.text} -Poutput_dir $path"
                    createTerminalInstance(e = event, shellCommand)
                    this@ABClassesCreationDialog.close(0)
                }.align(align = AlignX.CENTER)
            }.topGap(TopGap.SMALL)
        }

        dialogPanel.add(dialogContent)
        return dialogPanel
    }

    private fun getFirstLine(script: Path): String {
        return Files.lines(script)
            .findFirst()
            .orElse("")
    }

    companion object {
        private const val MINIMUM_WIDTH = 600
        private const val MINIMUM_HEIGHT = 220
    }
}