/**
 * Description:
 *
 * @ProjectName Header
 * @Title ImportAction
 * @Author Mr.lin
 * @Date 2023-12-20 23:34
 * @Version V1.0.0
 * @Copyright © 2023 by Mr.lin. All rights reserved.
 */
package su.gov.headers.ui.scriptsEditor.scriptsListActions;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.intellij.icons.AllIcons;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.LangDataKeys;
import com.intellij.openapi.diagnostic.Logger;
import com.intellij.openapi.fileChooser.FileChooser;
import com.intellij.openapi.fileChooser.FileChooserDescriptor;
import com.intellij.openapi.ui.Messages;
import com.intellij.openapi.vfs.VirtualFile;
import org.jetbrains.annotations.NotNull;
import su.gov.headers.HeadersBundle;
import su.gov.headers.Website;
import su.gov.headers.transform.TransformScriptModel;
import su.gov.headers.ui.scriptsEditor.ScriptsListPanel;

public class ImportAction extends AnAction {

    private final ScriptsListPanel scriptsListPanel;
    private final static Logger LOGGER = Logger.getInstance(ImportAction.class);

    public ImportAction(ScriptsListPanel scriptsListPanel) {
        super(HeadersBundle.message("settings.scripts.import.title"), HeadersBundle.message("settings.scripts.import.description"), AllIcons.ToolbarDecorator.Import);
        this.scriptsListPanel = scriptsListPanel;
    }

    @Override
    public void actionPerformed(@NotNull AnActionEvent event) {
        FileChooserDescriptor descriptor = new FileChooserDescriptor(true, false, false, false, true, false);
        descriptor.setDescription(HeadersBundle.message("settings.scripts.import.load.description"));
        descriptor.setTitle(HeadersBundle.message("settings.scripts.import.load.title"));
        descriptor.putUserData(
                LangDataKeys.MODULE_CONTEXT,
                LangDataKeys.MODULE.getData(event.getDataContext())
        );

        VirtualFile file = FileChooser.chooseFile(descriptor, scriptsListPanel, null, null);
        if (file == null) {
            return;
        }
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            TransformScriptModel[] models = objectMapper.readValue(file.getInputStream(), TransformScriptModel[].class);
            for (TransformScriptModel model : models) {
                scriptsListPanel.addItem(model);
            }
            Messages.showInfoMessage(scriptsListPanel, HeadersBundle.message("settings.scripts.import.success.message"), HeadersBundle.message("settings.scripts.import.success.title"));
        } catch (JsonParseException ex) {
            LOGGER.error(ex);
            Messages.showErrorDialog(scriptsListPanel, HeadersBundle.message("settings.scripts.import.failure.message.invalid.json", Website.ISSUES), HeadersBundle.message("settings.scripts.import.failure.title"));
        } catch (JsonMappingException ex) {
            LOGGER.error(ex);
            Messages.showErrorDialog(scriptsListPanel, HeadersBundle.message("settings.scripts.import.failure.message.invalid.scripts", Website.ISSUES), HeadersBundle.message("settings.scripts.import.failure.title"));
        } catch (Exception ex) {
            LOGGER.error(ex);
            Messages.showErrorDialog(scriptsListPanel, HeadersBundle.message("settings.scripts.import.failure.message", Website.ISSUES), HeadersBundle.message("settings.scripts.import.failure.title"));
        }
    }

}
