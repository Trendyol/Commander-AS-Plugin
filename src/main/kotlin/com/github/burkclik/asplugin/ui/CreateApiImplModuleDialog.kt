package com.github.burkclik.asplugin.ui

import com.github.burkclik.asplugin.util.createTerminalInstance
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.actionSystem.CommonDataKeys
import com.intellij.openapi.ui.DialogPanel
import com.intellij.openapi.ui.DialogWrapper
import com.intellij.ui.components.JBTextField
import com.intellij.ui.dsl.builder.AlignX
import com.intellij.ui.dsl.builder.TopGap
import com.intellij.ui.dsl.builder.bind
import com.intellij.ui.dsl.builder.panel
import java.awt.Dimension
import java.nio.file.Path
import javax.swing.JComponent

class CreateApiImplModuleDialog(val event: AnActionEvent, val scriptPath: Path) :
    DialogWrapper(event.project, null, true, IdeModalityType.MODELESS, false) {

    private val modulePath: JBTextField = JBTextField()

    init {
        title = "Create Api Impl Module"
        init()
    }

    override fun createCenterPanel(): JComponent = createDialogPanel()

    private fun createDialogPanel(): DialogPanel {
        val dialogPanel = DialogPanel()
        dialogPanel.minimumSize = Dimension(400, 180)
        dialogPanel.preferredSize = Dimension(540, 180)

        var radioButtonValue = "n"

        val dialogContent = panel {
            createTextField(
                textFieldComponent = modulePath,
                label = "Feature/Domain path",
                hint = "Example ':common:login', ':mlbs:meal:cart-domain'):"
            )
            buttonsGroup {
                row("Would you like to create `:testing` module too? ") {
                    radioButton("Yes", "y").component.apply {
                        addActionListener { radioButtonValue = "y" }
                    }
                    radioButton("No", "n").component.apply {
                        addActionListener { radioButtonValue = "n" }
                    }
                }
            }.bind({ radioButtonValue }, { radioButtonValue = it })
            row {
                button("Create Api-Impl Module") {
                    val childDialog = ApiImplApprovalDialogPanel(event, modulePath.text, scriptPath, radioButtonValue)
                    childDialog.show()
                    childDialog.closeDialogListener = {
                        this@CreateApiImplModuleDialog.close(0)
                    }
                }.align(AlignX.CENTER)
            }.topGap(TopGap.SMALL)
        }
        dialogPanel.add(dialogContent)
        return dialogPanel
    }
}

class ApiImplApprovalDialogPanel(
    private val event: AnActionEvent,
    modulePath: String,
    scriptPath: Path,
    isTestingModuleCreate: String
) :
    DialogWrapper(event.project, null, true, IdeModalityType.MODELESS, false) {

    var closeDialogListener: (() -> Unit)? = null

    private val path: String = event.getData(CommonDataKeys.VIRTUAL_FILE)?.path.orEmpty().split("scripts/").first()
    private val getModuleName = modulePath.replace(":", "/")
    private val shellCommand =
        "python3 $scriptPath -Pmodule_name $modulePath -Papproval y -PapprovalTestModule $isTestingModuleCreate"

    init {
        title = "Approval Dialog"
        init()
    }

    override fun createCenterPanel(): JComponent = approvalDialogPanel()

    private fun approvalDialogPanel(): DialogPanel {
        val dialogPanel = DialogPanel()
        dialogPanel.minimumSize = Dimension(650, 120)
        dialogPanel.preferredSize = Dimension(650, 200)

        val dialogContent = panel {
            row {
                text(
                    "<b>Module location:</b> <br><b>API module:</b>$path${getModuleName}/api</br>" +
                            "<br><b>Implementation module:</b>$path${getModuleName}/impl</br>"
                )
            }
            row {
                button("Yes") {
                    this@ApiImplApprovalDialogPanel.close(0)
                    createTerminalInstance(event, shellCommand)
                    closeDialogListener?.invoke()
                }
                button("No") {
                    this@ApiImplApprovalDialogPanel.close(0)
                }
            }
        }
        dialogPanel.add(dialogContent)
        return dialogPanel
    }
}