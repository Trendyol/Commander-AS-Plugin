package com.github.burkclik.asplugin.actions

import java.nio.file.Path

open class TaskModel(
    open val type: TaskType
)

data class GradleTask(
    val name: String,
    override val type: TaskType = TaskType.GRADLE,
): TaskModel(type = type)

data class ScriptTask(
    val path: Path,
    override val type: TaskType = TaskType.SCRIPT
): TaskModel(type = type)

enum class TaskType {
    GRADLE, SCRIPT
}
