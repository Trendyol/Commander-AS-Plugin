package com.github.burkclik.asplugin.util

fun getModuleTerminalCommand(modulePath: String, gradleTaskName: String): String {
    val subString = modulePath.substringAfter("Trendyol_v2")
    val formattedModulePath = subString
        .split("/")
        .plus(gradleTaskName)
        .joinToString(":")
    return "./gradlew $formattedModulePath"
}