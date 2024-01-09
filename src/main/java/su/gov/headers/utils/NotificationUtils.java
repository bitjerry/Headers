/**
 * -*-coding: UTF-8 -*-
 *
 * @Time: 2022/1/11 14:31
 * @Author: Mr.lin
 * @File: HeadersPlugin.java
 */
package su.gov.headers.utils;

import com.intellij.ide.BrowserUtil;
import com.intellij.notification.NotificationGroup;
import com.intellij.notification.NotificationGroupManager;
import com.intellij.notification.NotificationType;
import com.intellij.openapi.project.DumbAwareAction;
import com.intellij.openapi.project.Project;
import su.gov.headers.HeadersBundle;
import su.gov.headers.HeadersPlugin;
import su.gov.headers.Website;
import su.gov.headers.icons.PluginIcon;


public class NotificationUtils {
    private static final NotificationGroup GROUP = NotificationGroupManager.getInstance().getNotificationGroup("Headers Plugin");

    public static void warning(String content, Project project) {
        GROUP.createNotification(content, NotificationType.WARNING)
                .setTitle(HeadersPlugin.getName())
                .setIcon(PluginIcon.ORANGE_16)
                .notify(project);
    }

    public static void error(String content, Project project) {
        GROUP.createNotification(content, NotificationType.ERROR)
                .setTitle(HeadersPlugin.getName())
                .setIcon(PluginIcon.RED_16)
                .addAction(DumbAwareAction.create(HeadersBundle.message("issues.feedback"),
                        e -> BrowserUtil.open(Website.ISSUES)))
                .notify(project);
    }

    public static void info(String content, Project project) {
        GROUP.createNotification(content, NotificationType.INFORMATION)
                .setTitle(HeadersPlugin.getName())
                .setIcon(PluginIcon.GREEN_16)
                .notify(project);
    }

}
