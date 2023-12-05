package com.github.burkclik.asplugin.ui

import com.github.burkclik.asplugin.util.ConfigFileWriter
import com.intellij.openapi.options.Configurable
import com.intellij.openapi.project.Project
import com.intellij.ui.components.JBCheckBox
import com.intellij.ui.components.JBTextField
import com.intellij.ui.dsl.builder.TopGap
import com.intellij.ui.dsl.builder.panel
import javax.swing.JComponent
import javax.swing.event.DocumentEvent
import javax.swing.event.DocumentListener

class ProjectSettingsConfigurable(private val project: Project) : Configurable, DocumentListener {

    private val command: JBTextField = JBTextField()
    private val commandName: JBTextField = JBTextField()
    private val rootCheckbox: JBCheckBox = JBCheckBox()
    private val moduleCheckbox: JBCheckBox = JBCheckBox()

    private var modified = false

    override fun createComponent(): JComponent {
        val panel = panel {
            createTextField(
                textFieldComponent = commandName,
                label = "Command Name: ",
                placeholder = "projectHealth",
                listener = this@ProjectSettingsConfigurable
            )
            createTextField(
                textFieldComponent = command,
                label = "Command: ",
                placeholder = "projectHealth -Pdependency.analysis.autoapply=true",
                listener = this@ProjectSettingsConfigurable,
                hint = "If you add a flag, please add it after to task!"
            )
            row("Command Type:") {
                cell(rootCheckbox.apply { text = "Root" })
                cell(moduleCheckbox.apply { text = "Module" })
            }.topGap(TopGap.MEDIUM)
        }

        return panel
    }

    override fun isModified(): Boolean = modified && (rootCheckbox.isSelected || moduleCheckbox.isSelected)

    override fun apply() {
        writeToFile()
        modified = false
    }

    override fun getDisplayName(): String = PLUGIN_NAME

    override fun insertUpdate(e: DocumentEvent?) {
        modified = command.text.isNotEmpty() && commandName.text.isNotEmpty()
    }

    override fun removeUpdate(e: DocumentEvent?) {
        modified = true
    }

    override fun changedUpdate(e: DocumentEvent?) {
        modified = true
    }

    private fun writeToFile() {
        if (rootCheckbox.isSelected && moduleCheckbox.isSelected) {
            writeFile("config/Commander/module-tasks.txt")
            writeFile("config/Commander/root-level-tasks.txt")
        } else if (rootCheckbox.isSelected) {
            writeFile("config/Commander/root-level-tasks.txt")
        } else {
            writeFile("config/Commander/module-tasks.txt")
        }
    }

    private fun writeFile(path: String) {
        val fullCommand = "${commandName.text} ${command.text}"
        ConfigFileWriter().appendToTextFile(project, path, fullCommand)
    }

    companion object {
        private const val PLUGIN_NAME = "Commander"
    }
}