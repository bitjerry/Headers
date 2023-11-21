/**
 * @Time: 2021/12/28 18:38
 * @Author: Mr.lin
 */
package control;

import com.intellij.codeInsight.actions.ReformatCodeProcessor;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.CommonDataKeys;
import com.intellij.openapi.command.WriteCommandAction;
import com.intellij.openapi.diagnostic.Logger;
import com.intellij.openapi.editor.Caret;
import com.intellij.openapi.editor.Document;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.editor.SelectionModel;
import com.intellij.openapi.project.Project;
import com.intellij.psi.PsiDocumentManager;
import com.intellij.psi.PsiFile;
import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;

import java.awt.*;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;

public abstract class Controller extends AnAction {
    private Project project;
    private Editor editor;
    private Document document;

    private static final @NonNls Logger LOG = Logger.getInstance(Controller.class);

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
    public void actionPerformed(@NotNull AnActionEvent event) {
        Caret primaryCaret = editor.getCaretModel().getPrimaryCaret();
        int start = primaryCaret.getSelectionStart();
        int end = primaryCaret.getSelectionEnd();
        String text;
        if (end - start > 0) {
            text = primaryCaret.getSelectedText();
        } else {
            text = getClipboardText();
        }
        if (text == null) {
            return;
        }
//        String padding = genPadding(primaryCaret.getSelectionStartPosition().column);
        try {
            String result = parse(text).replaceAll("\\r\\n?", "\n");
            writeMessages(start, end, result);
        } catch (Exception e) {
            LOG.error("Oops!!! There was a fucking error in the format-string", e);
        }

    }

//    public static String genPadding(int pads){
//        return pads > 0 ? String.format("%"+pads+"s", " ") : "";
//    }

    public void writeMessages(int start, int end, String result) {
        WriteCommandAction.runWriteCommandAction(project, () -> {
            if (result != null) {
                document.replaceString(start, end, result);
                PsiFile psiFile = PsiDocumentManager.getInstance(project).getPsiFile(document);
                if (psiFile != null) {
                    SelectionModel selectionModel = editor.getSelectionModel();
                    selectionModel.setSelection(start, start+result.length());
                    new ReformatCodeProcessor(psiFile, selectionModel).run();
                    PsiDocumentManager.getInstance(project).commitDocument(document);
                }
            }
        });
    }

    public static String getClipboardText() {
        String text = null;
        Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
        Transferable contents = clipboard.getContents(null);
        if (contents != null && contents.isDataFlavorSupported(DataFlavor.stringFlavor)) {
            try {
                text = (String) contents.getTransferData(DataFlavor.stringFlavor);
            } catch (Exception e) {
                LOG.error("Oops!!! There was a fucking error in the Clipboard", e);
            }
        }
        return text;
    }

    /**
     * Format text
     * @param text Get test to format
     * @return Formatted text
     */
    public abstract String parse(String text) throws Exception;
}