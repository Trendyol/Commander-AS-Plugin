package com.github.burkclik.asplugin.util

fun getModuleTerminalCommand(
    rootPath: String,
    modulePath: String,
    gradleTaskName: String
): String {
    val moduleName = getModuleName(rootPath, modulePath)
    return "./gradlew $moduleName:$gradleTaskName"
}

fun getModuleName(rootPath: String, modulePath: String): String {
    val subString = modulePath.substringAfter(rootPath)
    return subString.split("/").joinToString(":") { it }
}

fun getPackageName(rootPath: String, modulePath: String): String {
    val subString = modulePath.substringAfter(rootPath)
    val isApiModule = subString.contains("api")
    val packageName = "com".plus(subString.split("/").joinToString(".") { it })

    return if (isApiModule) {
        packageName.replace(".api", "")
    } else {
        packageName
    }
}