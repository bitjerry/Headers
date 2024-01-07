/**
 * Description:
 *
 * @ProjectName Headers
 * @Title TransformScriptAction
 * @Author Mr.lin
 * @Date 2023-12-24 22:54
 * @Version V1.0.0
 * @Copyright © 2023 by Mr.lin. All rights reserved.
 */
package su.gov.headers.transform;

import com.intellij.openapi.diagnostic.Logger;
import com.intellij.openapi.util.text.StringUtil;
import org.jetbrains.annotations.NotNull;
import su.gov.headers.HeadersBundle;
import su.gov.headers.utils.NotificationUtils;
import su.gov.headers.utils.StringUtils;

public abstract class TransformScriptAction extends TransformAction {
    private final TransformScriptModel transformScriptModel;

    private static final Logger LOGGER = Logger.getInstance(TransformScriptAction.class);

    public TransformScriptAction(@NotNull TransformScriptModel model) {
        this.transformScriptModel = model;
        getTemplatePresentation().setText(model.getName());
    }

    public String getId() {
        return transformScriptModel.getId();
    }

    public TransformScriptModel getTransformModel() {
        return transformScriptModel;
    }


    @Override
    public void writeTransformedCode(String code){
        code = transform(code);
        if (code == null){
            return;
        }
        if (StringUtil.isEmpty(code)) {
            NotificationUtils.warning(HeadersBundle.message("warning.transform.result.empty"));
            return;
        }

        code = StringUtils.convertToLF(code);
        if (StringUtil.isEmpty(code)){
            NotificationUtils.warning(HeadersBundle.message("warning.transform.result.empty"));
            return;
        }
        if (transformScriptModel.getKeepIndent()){
            int startColumn = editor.getCaretModel().getPrimaryCaret().getSelectionStartPosition().column;
            String padding = startColumn > 0 ? String.format("%"+startColumn+"s", " ") : "";
            code = code.replace("\n", "\n"+padding);
        }
        replaceSelectedCode(code);
        if (transformScriptModel.getAutoReformat()){
            reformatSelectedCode(code);
        }
    }

}
