package com.github.burkclik.asplugin.actions.projectbased

import com.github.burkclik.asplugin.actions.base.ProjectBasedGradleAction

class ProjectBasedDetektCheckAction : ProjectBasedGradleAction() {
    override fun getActionName(): String = TASK_NAME

    companion object {
        const val TASK_NAME = "./gradlew detekt --continue"
    }
}