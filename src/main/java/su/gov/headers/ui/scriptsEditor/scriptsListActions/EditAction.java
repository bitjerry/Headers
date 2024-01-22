/**
 * Description:
 *
 * @ProjectName Header
 * @Title EditAction
 * @Author Mr.lin
 * @Date 2024-01-23 0:49
 * @Version V1.0.0
 * @Copyright © 2024 by Mr.lin. All rights reserved.
 */
package su.gov.headers.ui.scriptsEditor.scriptsListActions;

import com.intellij.ui.DoubleClickListener;
import org.jetbrains.annotations.NotNull;
import su.gov.headers.ui.scriptsEditor.ScriptsListPanel;

import java.awt.event.MouseEvent;

public class EditAction extends DoubleClickListener {

    private final ScriptsListPanel scriptsListPanel;

    public EditAction(ScriptsListPanel scriptsListPanel){
        this.scriptsListPanel = scriptsListPanel;
    }

    @Override
    protected boolean onDoubleClick(@NotNull MouseEvent event) {
        scriptsListPanel.processSelectedItem(scriptsListPanel::showEditDialog);
        return true;
    }
}
