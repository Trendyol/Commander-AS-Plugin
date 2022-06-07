package com.github.burkclik.asplugin.util

import com.intellij.notification.NotificationGroupManager
import com.intellij.notification.NotificationType
import com.intellij.openapi.project.Project

object Util {

    fun showNotification(project: Project?, message: String) {
        NotificationGroupManager.getInstance().getNotificationGroup("Custom Notification Group")
                .createNotification(message, NotificationType.ERROR)
                .notify(project)
    }
}