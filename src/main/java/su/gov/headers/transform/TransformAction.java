/**
 * @Time: 2021/12/28 18:38
 * @Author: Mr.lin
 */
package su.gov.headers.transform;

import com.intellij.openapi.actionSystem.*;
import com.intellij.openapi.command.CommandProcessor;
import com.intellij.openapi.command.WriteCommandAction;
import com.intellij.openapi.diagnostic.Logger;
import com.intellij.openapi.editor.Caret;
import com.intellij.openapi.editor.Document;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.util.text.StringUtil;
import com.intellij.psi.PsiDocumentManager;
import com.intellij.psi.PsiFile;
import com.intellij.psi.codeStyle.CodeStyleManager;
import org.jetbrains.annotations.NotNull;
import su.gov.headers.HeadersBundle;
import su.gov.headers.utils.NotificationUtils;
import su.gov.headers.utils.StringUtils;

import java.awt.*;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;

public abstract class TransformAction extends AnAction implements Transform {
    protected Project project;
    protected Editor editor;
    protected Document document;

    private static final Logger LOGGER = Logger.getInstance(TransformAction.class);

    @Override
    public void update(@NotNull AnActionEvent e) {
        project = e.getRequiredData(CommonDataKeys.PROJECT);
        editor = e.getRequiredData(CommonDataKeys.EDITOR);
        document = editor.getDocument();
    }

    @Override
    public void actionPerformed(@NotNull AnActionEvent event) {
        String code = readSelectedCode();
        if (StringUtil.isEmpty(code)) {
            NotificationUtils.warning(HeadersBundle.message("warning.read.result.empty"), project);
            return;
        }
        WriteCommandAction.runWriteCommandAction(project,
                () -> CommandProcessor.getInstance().runUndoTransparentAction(
                        () -> writeTransformedCode(code)
                )
        );

    }


    public String readSelectedCode() {
        Caret primaryCaret = editor.getCaretModel().getPrimaryCaret();
        String text = primaryCaret.getSelectedText();
        if (!StringUtil.isEmpty(text)) {
            return text;
        }
        try {
            Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
            Transferable contents = clipboard.getContents(null);
            if (contents != null && contents.isDataFlavorSupported(DataFlavor.stringFlavor)) {
                text = (String) contents.getTransferData(DataFlavor.stringFlavor);
            }
        } catch (Exception ex) {
            LOGGER.error("Oops!!! There was a fucking error on read the code", ex);
            NotificationUtils.error(HeadersBundle.message("error.read.code"), project);
        }
        return text;
    }

    public void writeTransformedCode(String code) {
        code = transform(code);
        if (code != null){
            if (StringUtil.isEmpty(code)) {
                NotificationUtils.warning(HeadersBundle.message("warning.transform.result.empty"), project);
            }
            else {
                code = StringUtils.convertToLF(code);
                replaceSelectedCode(code);
                reformatSelectedCode(code);
            }
        }
    }


    public void replaceSelectedCode(String code) {
        Caret primaryCaret = editor.getCaretModel().getPrimaryCaret();
        int start = primaryCaret.getSelectionStart();
        int end = primaryCaret.getSelectionEnd();
        document.replaceString(start, end, code);
    }

    public void reformatSelectedCode(String code) {
        int start = editor.getCaretModel().getPrimaryCaret().getSelectionStart();
        int end = start + code.length();
        PsiDocumentManager manager = PsiDocumentManager.getInstance(project);
        PsiFile psiFile = manager.getPsiFile(document);
        if (psiFile != null) {
            CodeStyleManager.getInstance(project).reformatText(psiFile, start, end);
        }
    }

}