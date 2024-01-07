/**
 * Description:
 *
 * @ProjectName Header
 * @Title Curl
 * @Author Mr.lin
 * @Date 2023-12-11 1:33
 * @Version V1.0.0
 * @Copyright © 2023 by Mr.lin. All rights reserved.
 */
package su.gov.headers.actions.curlAction;


import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.DefaultActionGroup;
import org.jetbrains.annotations.NotNull;
import su.gov.headers.setting.SettingsPersistentState;
import su.gov.headers.transform.TransformScriptModel;

import java.util.List;

public class CurlGroup extends DefaultActionGroup {

    @NotNull
    @Override
    public AnAction[] getChildren(AnActionEvent e) {
        List<TransformScriptModel> models = SettingsPersistentState.getInstance().getTransformModels();
        CurlAction[] actions = new CurlAction[models.size()];
        for (int i=0; i<models.size(); i++){
            actions[i] = new CurlAction(models.get(i));
        }
        return actions;
    }
}
