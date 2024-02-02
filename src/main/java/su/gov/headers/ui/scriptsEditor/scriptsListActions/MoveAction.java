/**
 * Description:
 *
 * @ProjectName Header
 * @Title MoveAction
 * @Author Mr.lin
 * @Date 2024-01-22 22:54
 * @Version V1.0.0
 * @Copyright © 2024 by Mr.lin. All rights reserved.
 */
package su.gov.headers.ui.scriptsEditor.scriptsListActions;


import org.jetbrains.annotations.NotNull;
import su.gov.headers.transform.TransformScriptModel;
import su.gov.headers.ui.scriptsEditor.ScriptsListPanel;

import javax.swing.event.MouseInputAdapter;
import java.awt.*;
import java.awt.event.MouseEvent;

public class MoveAction extends MouseInputAdapter {
    private final ScriptsListPanel scriptsListPanel;
    private boolean mouseDragging = false;
    private int dragSourceIndex;

    public MoveAction(ScriptsListPanel scriptsListPanel) {
        this.scriptsListPanel = scriptsListPanel;
    }

    public void installOn(@NotNull Component component) {
        component.addMouseListener(this);
        component.addMouseMotionListener(this);
    }

    @Override
    public void mousePressed(MouseEvent e) {
        dragSourceIndex = scriptsListPanel.getSelectedItemIndex();
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        if (mouseDragging) {
            int dragTargetIndex = scriptsListPanel.getSelectedItemIndex();
            TransformScriptModel model = scriptsListPanel.getItem(dragSourceIndex);
            scriptsListPanel.removeItem(dragSourceIndex);
            scriptsListPanel.insertItem(dragTargetIndex, model);
            scriptsListPanel.setSelectionInterval(dragTargetIndex, dragTargetIndex);
        }

        mouseDragging = false;
    }

    @Override
    public void mouseDragged(MouseEvent e) {
        mouseDragging = true;
    }

}
