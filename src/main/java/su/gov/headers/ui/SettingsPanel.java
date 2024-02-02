package su.gov.headers.ui;

import com.intellij.openapi.Disposable;
import com.intellij.ui.JBSplitter;
import com.intellij.util.ui.JBUI;
import org.jetbrains.annotations.NotNull;
import su.gov.headers.transform.TransformScriptModel;
import su.gov.headers.ui.scriptsEditor.ScriptsEditorPanel;
import su.gov.headers.ui.scriptsEditor.ScriptsListPanel;

import javax.swing.*;
import java.awt.*;
import java.util.List;


public class SettingsPanel implements Disposable {

    private final JPanel mainPanel;

    private final ScriptsListPanel scriptsListPanel;

    private final ScriptsEditorPanel scriptsEditorPanel;

    public ScriptsListPanel getScriptsListPanel() {
        return scriptsListPanel;
    }

    public ScriptsEditorPanel getScriptsEditorPanel() {
        return scriptsEditorPanel;
    }

    public SettingsPanel() {
        this.mainPanel = new JPanel(new BorderLayout());
        this.scriptsListPanel = new ScriptsListPanel(this);
        this.scriptsEditorPanel = new ScriptsEditorPanel(this);
        JBSplitter splitter = new JBSplitter(false, 0.2f);
        splitter.setFirstComponent(this.scriptsListPanel);
        splitter.setSecondComponent(this.scriptsEditorPanel.getPanel());
        this.mainPanel.add(splitter, BorderLayout.CENTER);
        this.mainPanel.setPreferredSize(JBUI.size(400, 300));
    }

    public void setTransformModels(@NotNull List<TransformScriptModel> models) {
        this.scriptsListPanel.addItems(models);
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
