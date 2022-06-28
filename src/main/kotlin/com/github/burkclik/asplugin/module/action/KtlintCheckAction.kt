package com.github.burkclik.asplugin.module.action

import com.github.burkclik.asplugin.module.base.GradleAction

class KtlintCheckAction : GradleAction() {

    override fun getActionName(): String = TASK_NAME

    companion object {
        const val TASK_NAME = "ktlintCheck"
    }
}