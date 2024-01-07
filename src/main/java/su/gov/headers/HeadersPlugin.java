/**
 * Description:
 *
 * @ProjectName Header
 * @Title HeadersPlugin
 * @Author Mr.lin
 * @Date 2024-01-06 0:47
 * @Version V1.0.0
 * @Copyright © 2024 by Mr.lin. All rights reserved.
 */
package su.gov.headers;

import com.intellij.ide.BrowserUtil;
import com.intellij.ide.plugins.IdeaPluginDescriptor;
import com.intellij.ide.plugins.PluginManagerCore;
import com.intellij.notification.*;
import com.intellij.notification.impl.NotificationFullContent;
import com.intellij.openapi.extensions.PluginId;
import com.intellij.openapi.project.DumbAwareAction;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.startup.StartupActivity;
import org.jetbrains.annotations.NotNull;
import su.gov.headers.icons.PluginIcon;
import su.gov.headers.setting.SettingsPersistentState;

public class HeadersPlugin implements StartupActivity {
    public static final String PLUGIN_ID = "su.gov.Header";

    public static final IdeaPluginDescriptor descriptor = PluginManagerCore.getPlugin(PluginId.getId(PLUGIN_ID));

    private static String version;

    private static String name;

    public static @NotNull String getVersion() {
        if (version == null) {
            assert descriptor != null;
            version = descriptor.getVersion();
        }
        return version;
    }

    public static @NotNull String getName() {
        if (name == null) {
            assert descriptor != null;
            name = descriptor.getName();
        }
        return name;
    }

    static class WelcomeNotification extends Notification implements NotificationFullContent {

        public WelcomeNotification() {
            super("Headers Plugin Welcome", PluginIcon.GREEN_16, NotificationType.INFORMATION);
            setImportant(true);
            setTitle(HeadersBundle.message("welcome.title"));
            setContent(HeadersBundle.message("welcome.content"));
            addAction(DumbAwareAction.create(HeadersBundle.message("welcome.source.code"),
                    e -> BrowserUtil.open(Website.SOURCE)));
            addAction(DumbAwareAction.create(HeadersBundle.message("welcome.help"),
                    e -> BrowserUtil.open(Website.README)));
        }
    }

    @Override
    public void runActivity(@NotNull Project project) {
        SettingsPersistentState state = SettingsPersistentState.getInstance();
        if (!getVersion().equals(state.getVersion())) {
            state.setVersion(getVersion());
            new WelcomeNotification().notify(project);
        }
    }
}
