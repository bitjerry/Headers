package su.gov.headers.ui;

import com.intellij.openapi.Disposable;
import com.intellij.ui.JBSplitter;
import com.intellij.uiDesigner.core.GridConstraints;
import com.intellij.uiDesigner.core.GridLayoutManager;
import org.jetbrains.annotations.NotNull;
import su.gov.headers.transform.TransformScriptModel;
import su.gov.headers.ui.scriptsEditor.ScriptsEditorPanel;
import su.gov.headers.ui.scriptsEditor.ScriptsListPanel;

import javax.swing.*;
import java.awt.*;
import java.util.List;


public class SettingsPanel implements Disposable {

    private JPanel mainPanel;

    private JBSplitter templateSplitter;

    private ScriptsListPanel scriptsListPanel;

    private ScriptsEditorPanel scriptsEditorPanel;

    public ScriptsListPanel getScriptsListPanel() {
        return scriptsListPanel;
    }

    public ScriptsEditorPanel getScriptsEditorPanel() {
        return scriptsEditorPanel;
    }

    private void createUIComponents() {
        this.scriptsListPanel = new ScriptsListPanel(this);
        this.scriptsEditorPanel = new ScriptsEditorPanel(this);
        templateSplitter = new JBSplitter(false, 0.2f);
        templateSplitter.setFirstComponent(this.scriptsListPanel);
        templateSplitter.setSecondComponent(this.scriptsEditorPanel.getPanel());
    }

    public void setTransformModels(@NotNull List<TransformScriptModel> models) {
        this.scriptsListPanel.setItems(models);
    }

    public List<TransformScriptModel> getTransformModels() {
        return this.scriptsListPanel.getItems();
    }

    public JPanel getMainPanel() {
        return mainPanel;
    }

    @Override
    public void dispose() {
        scriptsEditorPanel.dispose();
    }

}
