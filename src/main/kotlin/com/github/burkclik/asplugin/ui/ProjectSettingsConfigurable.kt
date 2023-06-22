package com.github.burkclik.asplugin.ui

import com.github.burkclik.asplugin.util.ConfigFileWriter
import com.intellij.openapi.options.Configurable
import com.intellij.openapi.project.Project
import com.intellij.ui.components.JBTextField
import com.intellij.ui.dsl.builder.TopGap
import com.intellij.ui.dsl.builder.panel
import com.intellij.ui.dsl.gridLayout.HorizontalAlign
import javax.swing.JComponent
import javax.swing.event.DocumentEvent
import javax.swing.event.DocumentListener

class ProjectSettingsConfigurable(private val project: Project) : Configurable, DocumentListener {

    private val command: JBTextField = JBTextField()
    private val commandName: JBTextField = JBTextField()

    private var modified = false

    override fun createComponent(): JComponent {
        val panel = panel {
            row("Command Name:") {
                cell(commandName.apply {
                    document.addDocumentListener(this@ProjectSettingsConfigurable)
                })
                    .horizontalAlign(HorizontalAlign.FILL)
                    .component.emptyText.setText("detektDebug")
            }

            row("Command:") {
                cell(command.apply {
                    document.addDocumentListener(this@ProjectSettingsConfigurable)
                })
                    .horizontalAlign(HorizontalAlign.FILL)
                    .component.emptyText.setText(":project-name:detektDebug")
            }.rowComment(
                "Buraya description ve örnek kod kısmını girebiliriz"
            )

            row("Command Type:") {
                checkBox("Root")
                checkBox("Module")
            }.topGap(TopGap.MEDIUM)
        }

        return panel
    }

    override fun isModified(): Boolean = modified

    override fun apply() {
        ConfigFileWriter().writeToTextFile(project, "config/Commander/module-tasks.txt", "${commandName.text} ${command.text}")
        modified = false
    }

    override fun getDisplayName(): String {
        return "Commander"
    }

    override fun insertUpdate(e: DocumentEvent?) {
        modified = command.text.isNotEmpty() && commandName.text.isNotEmpty()
    }

    override fun removeUpdate(e: DocumentEvent?) {
        modified = true
    }

    override fun changedUpdate(e: DocumentEvent?) {
        modified = true
    }
}