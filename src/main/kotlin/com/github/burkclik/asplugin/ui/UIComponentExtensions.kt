package com.github.burkclik.asplugin.ui

import com.intellij.ui.components.JBTextField
import com.intellij.ui.dsl.builder.Panel
import com.intellij.ui.dsl.builder.Row
import com.intellij.ui.dsl.gridLayout.HorizontalAlign
import javax.swing.event.DocumentListener

fun Panel.createTextField(
    textFieldComponent: JBTextField,
    label: String,
    placeholder: String = "",
    listener: DocumentListener? = null,
    hint: String = ""
): Row {
    return row(label) {
        cell(textFieldComponent.apply {
            listener?.let { documentListener ->
                document.addDocumentListener(documentListener)
            }
            emptyText.setText(placeholder)
        }).horizontalAlign(HorizontalAlign.FILL)
    }.rowComment(hint)
}