package com.github.burkclik.asplugin.services

import com.intellij.openapi.project.Project
import com.github.burkclik.asplugin.MyBundle

class MyProjectService(project: Project) {

    init {
        println(MyBundle.message("projectService", project.name))
    }
}
