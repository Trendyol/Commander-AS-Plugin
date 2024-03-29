package com.github.burkclik.asplugin.services

import com.github.burkclik.asplugin.MyBundle
import com.intellij.openapi.components.Service

@Service
class MyApplicationService {

    init {
        println(MyBundle.message("applicationService"))
    }
}
