/**
 *
 * @Time: 2021/12/28 18:38
 * @Author: Mr.lin
 */
package control;

import com.intellij.openapi.actionSystem.*;
import com.intellij.openapi.editor.*;
import com.intellij.openapi.command.WriteCommandAction;
import com.intellij.openapi.project.Project;
import org.jetbrains.annotations.NotNull;

import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;

public abstract class Controller extends AnAction {
    private Project project;
    private Editor editor;
    private Document document;

    @Override
    public void update(@NotNull AnActionEvent e) {
        project = e.getRequiredData(CommonDataKeys.PROJECT);
        editor = e.getRequiredData(CommonDataKeys.EDITOR);
        document = editor.getDocument();
        e.getPresentation().setEnabledAndVisible(
                        project != null &&
                        editor != null &&
                        document != null
                );
    }
    @Override
    public void actionPerformed(@NotNull AnActionEvent e) {
        Caret primaryCaret = editor.getCaretModel().getPrimaryCaret();
        int start = primaryCaret.getSelectionStart();
        int end = primaryCaret.getSelectionEnd();
        String text;
        if (end - start > 0) {
            text = primaryCaret.getSelectedText();
        } else {
            text = getClipboardText();
        }
        if (text != null) {
            String padding = genPadding(primaryCaret.getSelectionStartPosition().column);
            String result = result(text, padding);
            writeMessages(start, end, result);
        }
    }

    public static String genPadding(int pads){
        return pads > 0 ? String.format("%"+pads+"s", " ") : "";
    }

    public void writeMessages(int start, int end, String result){
        WriteCommandAction.runWriteCommandAction(project, () ->{
            if (result != null){
                document.replaceString(start, end, result);
            }
        });
    }

    public static String getClipboardText(){
        String text = null;
        Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
        Transferable contents = clipboard.getContents(null);
        if (contents != null && contents.isDataFlavorSupported(DataFlavor.stringFlavor)) {
            try {
                text = (String) contents.getTransferData(DataFlavor.stringFlavor);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return text;
    }

    /**
     * Format text
     * @param text Get test to format
     * @param padding Set padding characters
     * @return Formatted text
     */
    public abstract String result(String text, String padding);
}