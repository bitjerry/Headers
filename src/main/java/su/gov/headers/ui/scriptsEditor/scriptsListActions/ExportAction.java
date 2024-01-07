/**
 * Description:
 *
 * @ProjectName Header
 * @Title ExportAction
 * @Author Mr.lin
 * @Date 2023-12-20 23:38
 * @Version V1.0.0
 * @Copyright © 2023 by Mr.lin. All rights reserved.
 */
package su.gov.headers.ui.scriptsEditor.scriptsListActions;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.intellij.icons.AllIcons;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.diagnostic.Logger;
import com.intellij.openapi.fileChooser.FileChooserFactory;
import com.intellij.openapi.fileChooser.FileSaverDescriptor;
import com.intellij.openapi.ui.Messages;
import com.intellij.openapi.vfs.VirtualFileWrapper;
import org.jetbrains.annotations.NotNull;
import su.gov.headers.HeadersBundle;
import su.gov.headers.Website;
import su.gov.headers.ui.scriptsEditor.ScriptsListPanel;

import java.io.*;

public class ExportAction extends AnAction {

    private final static Logger LOGGER = Logger.getInstance(ExportAction.class);

    private final ScriptsListPanel scriptsListPanel;

    public ExportAction(ScriptsListPanel templateList) {
        super(HeadersBundle.message("settings.scripts.export.title"), HeadersBundle.message("settings.scripts.export.description"), AllIcons.ToolbarDecorator.Export);
        this.scriptsListPanel = templateList;
    }

    @Override
    public void actionPerformed(@NotNull AnActionEvent event) {
        VirtualFileWrapper wrapper = FileChooserFactory.getInstance().createSaveFileDialog(
                new FileSaverDescriptor(
                        HeadersBundle.message("settings.scripts.export.save.title"),
                        HeadersBundle.message("settings.scripts.export.save.description"),
                        "json"
                ),
                scriptsListPanel
        ).save("headersConfig");

        if (wrapper != null) {
            try {
                ObjectMapper objectMapper = new ObjectMapper();
                objectMapper.writerWithDefaultPrettyPrinter().writeValue(wrapper.getFile(), scriptsListPanel.getSelectedItems());
                Messages.showInfoMessage(scriptsListPanel,
                        HeadersBundle.message("settings.scripts.export.success.message"),
                        HeadersBundle.message("settings.scripts.export.success.title"));
            } catch (Exception ex) {
                LOGGER.error(ex);
                Messages.showErrorDialog(
                        scriptsListPanel,
                        HeadersBundle.message("settings.scripts.export.failure.message", Website.ISSUES),
                        HeadersBundle.message("settings.scripts.export.failure.title")
                );
            }
        }
    }

    @Override
    public void update(AnActionEvent e) {
        e.getPresentation().setEnabled(!scriptsListPanel.getSelectedItems().isEmpty());
    }
}
