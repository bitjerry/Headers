/**
 * Description:
 *
 * @ProjectName Header
 * @Title EditorPanel
 * @Author Mr.lin
 * @Date 2023-12-20 21:05
 * @Version V1.0.0
 * @Copyright © 2023 by Mr.lin. All rights reserved.
 */
package su.gov.headers.ui.scriptsEditor;

import com.intellij.openapi.Disposable;
import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.command.CommandProcessor;
import com.intellij.openapi.diagnostic.Logger;
import com.intellij.openapi.editor.Document;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.editor.EditorFactory;
import com.intellij.openapi.editor.EditorSettings;
import com.intellij.openapi.editor.colors.EditorColors;
import com.intellij.openapi.editor.colors.EditorColorsScheme;
import com.intellij.openapi.editor.event.DocumentEvent;
import com.intellij.openapi.editor.event.DocumentListener;
import com.intellij.openapi.fileTypes.FileType;
import com.intellij.openapi.fileTypes.FileTypeManager;
import com.intellij.ui.JBSplitter;
import org.jetbrains.annotations.NotNull;
import su.gov.headers.HeadersBundle;
import su.gov.headers.scripts.Scope;
import su.gov.headers.scripts.Script;
import su.gov.headers.transform.TransformScriptModel;
import su.gov.headers.ui.SettingsPanel;
import su.gov.headers.utils.ContainerUtils;
import su.gov.headers.utils.ResourceUtils;
import su.gov.headers.utils.StringUtils;

import javax.swing.*;
import javax.swing.border.TitledBorder;

public class ScriptsEditorPanel implements Disposable {
    private final SettingsPanel bindSettingsPanel;
    private JCheckBox autoReformatCheckBox;
    private JCheckBox keepIndentCheckBox;
    private JButton testButton;
    private JBSplitter editorSplitter;
    private JPanel editorPanel;
    private JPanel editorSettingPanel;

    private Editor scriptEditor;

    private Editor previewEditor;

    private boolean disable;

    private static final Logger LOGGER = Logger.getInstance(ScriptsEditorPanel.class);

    private final static String TEST_CURL_OBJ = ResourceUtils.read("/scripts/test.js");

    public ScriptsEditorPanel(SettingsPanel bindSettingsPanel) {
        this.bindSettingsPanel = bindSettingsPanel;
        this.disable();
        this.addListeners();
        editorSettingPanel.setBorder(new TitledBorder(HeadersBundle.message("settings.scripts.control.title")));
        autoReformatCheckBox.setText(HeadersBundle.message("settings.scripts.control.auto.reformat"));
        autoReformatCheckBox.setToolTipText(HeadersBundle.message("settings.scripts.control.auto.reformat.tooltip"));
        keepIndentCheckBox.setText(HeadersBundle.message("settings.scripts.control.keep.indent"));
        keepIndentCheckBox.setToolTipText(HeadersBundle.message("settings.scripts.control.keep.indent.tooltip"));
        testButton.setText(HeadersBundle.message("settings.scripts.control.test"));
        testButton.setToolTipText(HeadersBundle.message("settings.scripts.control.test.tooltip"));
    }


    private void createUIComponents() {
        editorSplitter = new JBSplitter(true);
        editorSplitter.setFirstComponent(getScriptEditor());
        editorSplitter.setSecondComponent(getPreviewEditor());
    }


    @NotNull
    private JComponent getScriptEditor() {
        EditorFactory factory = EditorFactory.getInstance();
        Document document = factory.createDocument("");
        FileType fileType = FileTypeManager.getInstance().getFileTypeByExtension("js");
        scriptEditor = factory.createEditor(document, null, fileType, false);
        EditorSettings settings = scriptEditor.getSettings();
        settings.setAdditionalLinesCount(0);
        settings.setAdditionalColumnsCount(0);
        settings.setLineMarkerAreaShown(false);
        settings.setVirtualSpace(false);
        JComponent component = scriptEditor.getComponent();
        component.setBorder(new TitledBorder(HeadersBundle.message("settings.scripts.editor.title")));
        return component;
    }

    @NotNull
    private JComponent getPreviewEditor() {
        EditorFactory factory = EditorFactory.getInstance();
        Document document = factory.createDocument("");
        previewEditor = factory.createViewer(document, null);
        EditorColorsScheme colorsScheme = previewEditor.getColorsScheme();
        colorsScheme.setColor(EditorColors.CARET_ROW_COLOR, null);
        EditorSettings settings = previewEditor.getSettings();
        settings.setLineMarkerAreaShown(false);
        JComponent component = previewEditor.getComponent();
        component.setBorder(new TitledBorder(HeadersBundle.message("settings.scripts.preview.title")));
        return component;
    }

    private void addListeners() {
        autoReformatCheckBox.addActionListener(e ->
                bindSettingsPanel.getScriptsListPanel().processSelectedItem(
                        model -> model.setAutoReformat(autoReformatCheckBox.isSelected())
                )
        );
        keepIndentCheckBox.addActionListener(e ->
                bindSettingsPanel.getScriptsListPanel().processSelectedItem(
                        model -> model.setKeepIndent(keepIndentCheckBox.isSelected())
                )
        );
        testButton.addActionListener(e -> {
                    String testResult;
                    try (Scope scope = Scope.enter()) {
                        new Script(TEST_CURL_OBJ).eval(scope);
                        new Script(scriptEditor.getDocument().getText()).eval(scope);
                        testResult = scope.call("transform", null, scope.getAttribute("curlTestObj"));
                        if (testResult == null) {
                            testResult = "// Plugin error: " + HeadersBundle.message("error.transform.no.transform.function");
                        }
                    } catch (Exception ex) {
                        LOGGER.error("Error while executing curl test", ex);
                        testResult = "// Plugin error: \n" + ex;
                    }
                    setPreviewContent(testResult);
                }
        );
        scriptEditor.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void documentChanged(@NotNull DocumentEvent event) {
                bindSettingsPanel.getScriptsListPanel().processSelectedItem(model -> model.setScriptContent(event.getDocument().getText()));
            }
        });
    }

    public void setEditorPanel(@NotNull TransformScriptModel transformScriptModel) {
        autoReformatCheckBox.setSelected(transformScriptModel.getAutoReformat());
        keepIndentCheckBox.setSelected(transformScriptModel.getKeepIndent());
        setEditorContent(transformScriptModel.getScriptContent());
        setPreviewContent("");
    }

    public void setEditorContent(@NotNull String content) {
        ApplicationManager.getApplication().runWriteAction(
                () -> CommandProcessor.getInstance().runUndoTransparentAction(
                        () -> {
                            Document templateDocument = scriptEditor.getDocument();
                            templateDocument.replaceString(0, templateDocument.getTextLength(), StringUtils.convertToLF(content));
                        }
                )
        );
    }

    public void setPreviewContent(@NotNull String content) {
        ApplicationManager.getApplication().runWriteAction(
                () -> CommandProcessor.getInstance().runUndoTransparentAction(
                        () -> {
                            Document previewDocument = previewEditor.getDocument();
                            previewDocument.replaceString(0, previewDocument.getTextLength(), StringUtils.convertToLF(content));
                        }
                )
        );
    }


    public void disable() {
        if (this.disable) return;
        this.disable = true;
        scriptEditor.getSettings().setLineMarkerAreaShown(false);
        setEditorContent("");
        setPreviewContent("");
        ContainerUtils.enableComponents(getPanel(), false);
    }

    public void enable() {
        if (this.disable) {
            this.disable = false;
            scriptEditor.getSettings().setLineMarkerAreaShown(true);
            ContainerUtils.enableComponents(getPanel(), true);
        }
    }

    public JPanel getPanel() {
        return editorPanel;
    }


    @Override
    public void dispose() {
        if (!scriptEditor.isDisposed()) {
            EditorFactory.getInstance().releaseEditor(scriptEditor);
        }
        if (!previewEditor.isDisposed()) {
            EditorFactory.getInstance().releaseEditor(previewEditor);
        }

    }
}

