package su.gov.headers.setting;

import com.intellij.openapi.options.SearchableConfigurable;
import com.intellij.openapi.util.Disposer;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import su.gov.headers.ui.SettingsPanel;

import javax.swing.*;

public class SettingsConfigurable implements SearchableConfigurable {


    private SettingsPanel settingsPanel;
    private final SettingsPersistentState settingsState;


    public SettingsConfigurable() {
        settingsState = SettingsPersistentState.getInstance();
    }


    @Nullable
    @Override
    public JComponent createComponent() {
        if (settingsPanel == null) {
            settingsPanel = new SettingsPanel();
        }
        return settingsPanel.getMainPanel();
    }

    @Override
    public boolean isModified() {
        if(settingsPanel == null){
            return false;
        }
        return !settingsPanel.getTransformModels().equals(settingsState.getTransformModels());
    }

    @Override
    public @NotNull String getDisplayName() {
        return "Headers";
    }

    @Override
    public @NotNull String getHelpTopic() {
        return getDisplayName();
    }

    @Override
    public @NotNull String getId(){
        return getHelpTopic();
    }

    @Override
    public void reset() {
        settingsPanel.setTransformModels(settingsState.getTransformModels());
    }

    @Override
    public void disposeUIResources() {
        Disposer.dispose(settingsPanel);
    }


    @Override
    public void apply() {
        settingsState.setTransformModels(settingsPanel.getTransformModels());
    }
}