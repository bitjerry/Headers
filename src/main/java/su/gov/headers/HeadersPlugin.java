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
import com.intellij.notification.Notification;
import com.intellij.notification.NotificationType;
import com.intellij.notification.impl.NotificationFullContent;
import com.intellij.openapi.actionSystem.ActionManager;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.Constraints;
import com.intellij.openapi.actionSystem.DefaultActionGroup;
import com.intellij.openapi.diagnostic.Logger;
import com.intellij.openapi.extensions.PluginId;
import com.intellij.openapi.project.DumbAwareAction;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.startup.StartupActivity;
import org.jetbrains.annotations.NotNull;
import su.gov.headers.actions.CurlAction;
import su.gov.headers.icons.PluginIcon;
import su.gov.headers.setting.SettingsPersistentState;
import su.gov.headers.transform.TransformScriptModel;

import java.util.List;
import java.util.Objects;

public class HeadersPlugin implements StartupActivity {

    private static final Logger LOGGER = Logger.getInstance(HeadersPlugin.class);
    public static final String PLUGIN_ID = "su.gov.Header";

    public static final PluginId ID = PluginId.getId(PLUGIN_ID);

    public @NotNull
    static final IdeaPluginDescriptor DESCRIPTOR = Objects.requireNonNull(PluginManagerCore.getPlugin(ID));

    private static DefaultActionGroup CURL_ACTION_GROUP;


    public static void registerActions(ActionManager manager, List<TransformScriptModel> models) {
        unRegisterActions(manager, models);
        LOGGER.debug("Registering " + models + "to group:" + CURL_ACTION_GROUP);
        for (int i = models.size() - 1; i >= 0; i--) {
            CurlAction action = new CurlAction(models.get(i));
            manager.registerAction(action.getId(), action, HeadersPlugin.ID);
            CURL_ACTION_GROUP.add(action, Constraints.FIRST);
        }
    }

    public static void unRegisterActions(ActionManager manager, List<TransformScriptModel> models) {
        LOGGER.debug("Unregistering " + models + "to group:" + CURL_ACTION_GROUP);
        for (TransformScriptModel model : models) {
            AnAction action = manager.getActionOrStub(model.getId());
            if (action != null) {
                CURL_ACTION_GROUP.remove(action);
                manager.unregisterAction(model.getId());
            }
        }

    }

    static class WelcomeNotification extends Notification implements NotificationFullContent {

        public WelcomeNotification() {
            super("Headers Plugin Welcome", HeadersBundle.message("welcome.title"), HeadersBundle.message("welcome.content"), NotificationType.INFORMATION);
            setImportant(true);
            setIcon(PluginIcon.GREEN_16);
            addAction(DumbAwareAction.create(HeadersBundle.message("welcome.source.code"),
                    e -> BrowserUtil.open(Website.SOURCE)));
            addAction(DumbAwareAction.create(HeadersBundle.message("welcome.help"),
                    e -> BrowserUtil.open(Website.README)));
        }
    }

    @Override
    public void runActivity(@NotNull Project project) {
        ActionManager manager = ActionManager.getInstance();
        CURL_ACTION_GROUP = (DefaultActionGroup) manager.getAction("Headers.Group.CurlGroup");
        SettingsPersistentState state = SettingsPersistentState.getInstance();
        registerActions(manager, state.getTransformModels());
        String version = state.getVersion();
        if (!DESCRIPTOR.getVersion().equals(version)) {
            state.setVersion(DESCRIPTOR.getVersion());
            new WelcomeNotification().notify(project);
        }
    }
}
