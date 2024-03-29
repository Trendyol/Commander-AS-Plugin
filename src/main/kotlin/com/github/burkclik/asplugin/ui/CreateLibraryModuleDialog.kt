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
import java.nio.file.Path
import javax.swing.JComponent

class CreateLibraryModuleDialog(val event: AnActionEvent, val scriptPath: Path) :
    DialogWrapper(event.project, null, true, IdeModalityType.MODELESS, false) {

    private val modulePath: JBTextField = JBTextField()
    private val packagePath: JBTextField = JBTextField()

    init {
        title = "Create Library Module"
        init()
    }

    override fun createCenterPanel(): JComponent = createDialogPanel()

    private fun createDialogPanel(): DialogPanel {
        val dialogPanel = DialogPanel()
        dialogPanel.minimumSize = Dimension(400, 180)
        dialogPanel.preferredSize = Dimension(400, 180)

        val dialogContent = panel {
            createTextField(
                textFieldComponent = modulePath,
                label = "Module Name",
                hint = "Example ':common:login'"
            )
            createTextField(
                textFieldComponent = packagePath,
                label = "Package name",
                hint = "Example 'com.trendyol.common.login'"
            )
            row {
                button("Create Library Module") {
                    val childDialog = ApprovalDialogPanel(event, modulePath.text, packagePath.text, scriptPath)
                    childDialog.show()
                    childDialog.closeDialogListener = {
                        this@CreateLibraryModuleDialog.close(0)
                    }
                }.align(AlignX.CENTER)
            }.topGap(TopGap.SMALL)
        }
        dialogPanel.add(dialogContent)
        return dialogPanel
    }
}

class ApprovalDialogPanel(private val event: AnActionEvent, modulePath: String, packagePath: String, scriptPath: Path) :
    DialogWrapper(event.project, null, true, IdeModalityType.MODELESS, false) {

    var closeDialogListener: (() -> Unit)? = null

    private val path: String = event.getData(CommonDataKeys.VIRTUAL_FILE)?.path.orEmpty().split("/scripts/").first()
    private val getModuleName = modulePath.replace(":", "/")
    private val shellCommand = "python3 $scriptPath -Pmodule_name $modulePath -Ppackage_name $packagePath -Papproval y"

    init {
        title = "Approval Dialog"
        init()
    }

    override fun createCenterPanel(): JComponent = approvalDialogPanel()

    private fun approvalDialogPanel(): DialogPanel {
        val dialogPanel = DialogPanel()
        dialogPanel.minimumSize = Dimension(650, 120)
        dialogPanel.preferredSize = Dimension(650, 120)

        val dialogContent = panel {
            row {
                text("Module location: $path${getModuleName} <br>Do you approve?</br>")
            }
            row {
                button("Yes") {
                    this@ApprovalDialogPanel.close(0)
                    createTerminalInstance(event, shellCommand)
                    closeDialogListener?.invoke()
                }
                button("No") {
                    this@ApprovalDialogPanel.close(0)
                }
            }
        }
        dialogPanel.add(dialogContent)
        return dialogPanel
    }
}