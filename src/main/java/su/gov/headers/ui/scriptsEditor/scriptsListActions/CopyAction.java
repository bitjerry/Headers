/**
 * Description:
 *
 * @ProjectName Header
 * @Title CopyAction
 * @Author Mr.lin
 * @Date 2024-01-22 22:53
 * @Version V1.0.0
 * @Copyright © 2024 by Mr.lin. All rights reserved.
 */
package su.gov.headers.ui.scriptsEditor.scriptsListActions;

import com.intellij.icons.AllIcons;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import org.jetbrains.annotations.NotNull;
import su.gov.headers.HeadersBundle;
import su.gov.headers.ui.scriptsEditor.ScriptsListPanel;

public class CopyAction extends AnAction {

    private final ScriptsListPanel scriptsListPanel;

    public CopyAction(ScriptsListPanel scriptsListPanel) {
        super(HeadersBundle.message("settings.scripts.copy.title"), HeadersBundle.message("settings.scripts.copy.description"), AllIcons.Actions.Copy);
        this.scriptsListPanel = scriptsListPanel;
    }

    @Override
    public void actionPerformed(@NotNull AnActionEvent e) {
        scriptsListPanel.processSelectedItem(model->scriptsListPanel.addItem(model.clone()));
    }

    @Override
    public void update(AnActionEvent e) {
        e.getPresentation().setEnabled(!scriptsListPanel.getSelectedItems().isEmpty());
    }
}
