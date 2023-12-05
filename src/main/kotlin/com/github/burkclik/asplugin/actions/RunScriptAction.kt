package com.github.burkclik.asplugin.actions

import com.github.burkclik.asplugin.ui.ABClassesCreationDialog
import com.github.burkclik.asplugin.ui.Deneme
import com.github.burkclik.asplugin.ui.ScriptData
import com.github.burkclik.asplugin.util.getPackageName
import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.actionSystem.CommonDataKeys
import com.intellij.openapi.project.Project
import com.intellij.openapi.ui.Messages
import com.intellij.util.containers.orNull
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json
import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.Paths

class RunScriptAction(private val scriptPath: Path) : AnAction(scriptPath.fileName.toString()) {
    override fun actionPerformed(e: AnActionEvent) {
        //getFirstLine(scriptPath)

        //val obj = Json.decodeFromString<ScriptData>("""{"title":"Create AB Classes", flag:[{"name":"-PFlag_name", "placeholder":"placeholder", "label":"label", "hint":"hint"}]}""")
        val obj = Json.decodeFromString<Deneme>("""{"text":"Deneme"}""")

        Messages.showInfoMessage(obj.text, "Script Path")
//        ABClassesCreationDialog(
//            event = e,
//            scriptPath = scriptPath
//        ).show()
    }
}