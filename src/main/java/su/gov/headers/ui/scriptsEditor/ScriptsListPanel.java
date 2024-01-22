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
import com.intellij.ui.AddDeleteListPanel;
import com.intellij.ui.ToolbarDecorator;
import org.jetbrains.annotations.Nullable;
import su.gov.headers.HeadersBundle;
import su.gov.headers.setting.SettingsPersistentState;
import su.gov.headers.transform.TransformScriptModel;
import su.gov.headers.ui.SettingsPanel;
import su.gov.headers.ui.scriptsEditor.scriptsListActions.*;

import java.util.ArrayList;
import java.util.List;

public class ScriptsListPanel extends AddDeleteListPanel<TransformScriptModel> {

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
            processSelectedItem(model -> bindSettingsPanel.getScriptsEditorPanel().setEditorPanel(model));
        });
        new EditAction(this).installOn(myList);
        new MoveAction(this).installOn(myList);
    }

    public void addItem(TransformScriptModel model) {
        myListModel.addElement(model);

    }

    public void addItems(List<TransformScriptModel> transformScriptModels) {
        myListModel.clear();
        for (TransformScriptModel template : transformScriptModels) {
            myListModel.addElement(template.copy());
        }
    }

    public void insertItem(int index, TransformScriptModel model) {
        myListModel.insertElementAt(model, index);
    }

    public TransformScriptModel getItem(int index) {
        if (index < 0 || index >= myListModel.size()){
            return null;
        }
        return myListModel.get(index);
    }

    public List<TransformScriptModel> getItems() {
        ArrayList<TransformScriptModel> list = new ArrayList<>();
        for (int i = 0; i < myListModel.size(); i++) {
            list.add(myListModel.getElementAt(i));
        }
        return list;
    }

    public void removeItem(int index){
        myListModel.remove(index);
    }

    public void setSelectionInterval(int index0, int index1) {
        myList.getSelectionModel().setSelectionInterval(index0, index1);
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

    public int getSelectedItemIndex() {
        return myList.getSelectedIndex();
    }

    @Nullable
    public TransformScriptModel getSelectedItem() {
        return getItem(getSelectedItemIndex());
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
        group.add(new CopyAction(this));
        group.add(new ImportAction(this));
        group.add(new ExportAction(this));
        decorator.setActionGroup(group);
    }

    @Override
    protected @Nullable TransformScriptModel findItemToAdd() {
        return showEditDialog(null);
    }


    public TransformScriptModel showEditDialog(TransformScriptModel model) {
        if (model == null) {
            model = new TransformScriptModel();
        }
        String input = Messages.showInputDialog(
                this,
                HeadersBundle.message("settings.scripts.edit.dialog.messages"),
                HeadersBundle.message("settings.scripts.edit.dialog.title"),
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
                            return HeadersBundle.message("settings.scripts.edit.dialog.error");
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
