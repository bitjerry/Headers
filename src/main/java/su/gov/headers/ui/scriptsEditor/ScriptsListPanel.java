/**
 * Description:
 *
 * @ProjectName Header
 * @Title TemplatesListPanel
 * @Author Mr.lin
 * @Date 2023-12-20 23:02
 * @Version V1.0.0
 * @Copyright © 2023 by Mr.lin. All rights reserved.
 */
package su.gov.headers.ui.scriptsEditor;

import com.intellij.openapi.actionSystem.DefaultActionGroup;
import com.intellij.openapi.ui.InputValidatorEx;
import com.intellij.openapi.ui.Messages;
import com.intellij.openapi.util.text.StringUtil;
import com.intellij.ui.AddEditDeleteListPanel;
import com.intellij.ui.ToolbarDecorator;
import org.jetbrains.annotations.Nullable;
import su.gov.headers.setting.SettingsPersistentState;
import su.gov.headers.transform.TransformScriptModel;
import su.gov.headers.ui.SettingsPanel;
import su.gov.headers.ui.scriptsEditor.scriptsListActions.ExportAction;
import su.gov.headers.ui.scriptsEditor.scriptsListActions.ImportAction;

import java.util.ArrayList;
import java.util.List;

public class ScriptsListPanel extends AddEditDeleteListPanel<TransformScriptModel> {

    private final SettingsPanel bindSettingsPanel;

    public ScriptsListPanel(SettingsPanel bindSettingsPanel) {
        super(null, SettingsPersistentState.getInstance().getTransformModels());
        this.bindSettingsPanel = bindSettingsPanel;
        myList.addListSelectionListener(e -> {
            if (e.getValueIsAdjusting()) {
                return;
            }
            if (myListModel.isEmpty()) {
                bindSettingsPanel.getScriptsEditorPanel().disable();
                return;
            }
            processSelectedItem(
                    model -> bindSettingsPanel.getScriptsEditorPanel().setEditorPanel(model)
            );
        });
    }

    public void setItem(TransformScriptModel model) {
        myListModel.addElement(model);
    }

    public void setItems(List<TransformScriptModel> transformScriptModels) {
        myListModel.clear();
        for (TransformScriptModel template : transformScriptModels) {
            myListModel.addElement(template.copy());
        }
    }


    public List<TransformScriptModel> getItems() {
        ArrayList<TransformScriptModel> list = new ArrayList<>();
        for (int i = 0; i < myListModel.size(); i++) {
            list.add(myListModel.getElementAt(i));
        }
        return list;
    }

    @FunctionalInterface
    public interface TransformScriptModelProcessor {

        void process(TransformScriptModel transformModel);
    }

    public void processSelectedItem(TransformScriptModelProcessor processor) {
        TransformScriptModel transformScriptModel = getSelectedItem();
        if (transformScriptModel == null) {
            bindSettingsPanel.getScriptsEditorPanel().disable();
        } else {
            bindSettingsPanel.getScriptsEditorPanel().enable();
            processor.process(transformScriptModel);
        }
    }

    @Nullable
    public TransformScriptModel getSelectedItem() {
        int index = myList.getSelectedIndex();
        if (index == -1) {
            return null;
        }
        return myListModel.get(index);
    }

    public List<TransformScriptModel> getSelectedItems() {
        List<TransformScriptModel> list = new ArrayList<>();
        int[] ids = myList.getSelectedIndices();
        for (int i = 0; i < ids.length; i++) {
            list.add(getItems().get(i));
        }
        return list;
    }

    @Override
    protected void customizeDecorator(ToolbarDecorator decorator) {
        super.customizeDecorator(decorator);
        DefaultActionGroup group = new DefaultActionGroup();
        group.addSeparator();
        group.add(new ImportAction(this));
        group.add(new ExportAction(this));
        decorator.setActionGroup(group);
    }

    @Override
    protected @Nullable TransformScriptModel findItemToAdd() {
        return showEditDialog(null);
    }


    @Override
    protected @Nullable TransformScriptModel editSelectedItem(TransformScriptModel item) {
        return showEditDialog(item);
    }

    private TransformScriptModel showEditDialog(TransformScriptModel model) {
        if (model == null) {
            model = new TransformScriptModel();
        }
        String input = Messages.showInputDialog(
                this,
                "Set new template name:",
                "JavaScript Template",
                Messages.getQuestionIcon(),
                model.getName(),
                new InputValidatorEx() {
                    @Override
                    public boolean checkInput(String inputString) {
                        return !StringUtil.isEmpty(inputString);
                    }

                    @Override
                    public boolean canClose(String inputString) {
                        return !StringUtil.isEmpty(inputString);
                    }

                    @Override
                    public String getErrorText(String inputString) {
                        if (!checkInput(inputString)) {
                            return "Template name cannot be empty";
                        }
                        return null;
                    }
                }
        );
        if (input != null) {
            model.setName(input);
            return model;
        }
        return null;
    }

}
