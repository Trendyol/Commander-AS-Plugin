package com.github.burkclik.asplugin.util

fun getModuleTerminalCommand(
    rootPath: String,
    modulePath: String,
    gradleTaskName: String
): String {
    val subString = modulePath.substringAfter(rootPath)
    val formattedModulePath = subString
        .split("/")
        .plus(gradleTaskName)
        .joinToString(":")
    return "./gradlew $formattedModulePath"
}