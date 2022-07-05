package com.github.burkclik.asplugin.actions.projectbased

import com.github.burkclik.asplugin.actions.base.ProjectBasedGradleAction

class ProjectBasedKtlintCheckAction : ProjectBasedGradleAction() {
    override fun getActionName(): String = TASK_NAME

    companion object {
        const val TASK_NAME = "./gradlew ktlintCheck --continue"
    }
}