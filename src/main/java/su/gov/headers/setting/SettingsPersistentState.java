/**
 * Description:
 *
 * @ProjectName Header
 * @Title TemplateStorage
 * @Author Mr.lin
 * @Date 2023-12-21 23:20
 * @Version V1.0.0
 * @Copyright © 2023 by Mr.lin. All rights reserved.
 */
package su.gov.headers.setting;

import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.components.PersistentStateComponent;
import com.intellij.openapi.components.State;
import com.intellij.openapi.components.Storage;
import com.intellij.util.xmlb.XmlSerializerUtil;
import com.intellij.util.xmlb.annotations.Transient;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import su.gov.headers.transform.TransformScriptModel;
import su.gov.headers.utils.ResourceUtils;

import java.util.ArrayList;
import java.util.List;

@State(name = "HeadersSettings", storages = {@Storage(value = "headersSettings.xml")})
public class SettingsPersistentState implements PersistentStateComponent<SettingsPersistentState> {

    private String version;

    private List<TransformScriptModel> transformScriptModels = new ArrayList<>() {{
        add(new TransformScriptModel("aiohttp", ResourceUtils.read("/scripts/example/aiohttp.js")));
        add(new TransformScriptModel("requests", ResourceUtils.read("/scripts/example/requests.js")));
        add(new TransformScriptModel("okhttp", ResourceUtils.read("/scripts/example/okhttp.js")));
    }};

    private static SettingsPersistentState state;

    @Transient
    public static SettingsPersistentState getInstance() {
        if (state == null) {
            state = ApplicationManager.getApplication().getService(SettingsPersistentState.class);
        }
        return state;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public List<TransformScriptModel> getTransformModels() {
        for (int i = transformScriptModels.size() - 1; i >= 0; i--) {
            if (transformScriptModels.get(i) == null) {
                transformScriptModels.remove(i);
            }
        }
        return transformScriptModels;
    }

    public void setTransformModels(List<TransformScriptModel> transformScriptModels) {
        this.transformScriptModels = transformScriptModels;
    }

    @Nullable
    @Override
    public SettingsPersistentState getState() {
        return this;
    }

    @Override
    public void loadState(@NotNull SettingsPersistentState state) {
        XmlSerializerUtil.copyBean(state, this);
    }
}
