/**
 * Description:
 *
 * @ProjectName Headers
 * @Title CustomCurlAction
 * @Author Mr.lin
 * @Date 2023-12-24 20:14
 * @Version V1.0.0
 * @Copyright © 2023 by Mr.lin. All rights reserved.
 */
package su.gov.headers.actions;

import com.intellij.openapi.diagnostic.Logger;
import com.intellij.openapi.util.text.StringUtil;
import su.gov.headers.HeadersBundle;
import su.gov.headers.curl.CurlObject;
import su.gov.headers.curl.CurlParserAdapter;
import su.gov.headers.scripts.Scope;
import su.gov.headers.scripts.Script;
import su.gov.headers.transform.TransformScriptAction;
import su.gov.headers.transform.TransformScriptModel;
import su.gov.headers.utils.NotificationUtils;

import javax.script.ScriptException;

public class CurlAction extends TransformScriptAction {

    private final static Logger LOGGER = Logger.getInstance(CurlAction.class);

    public CurlAction(TransformScriptModel model) {
        super(model);
    }

    @Override
    public String transform(String curlCommand) {
        CurlObject curlObject;
        try {
            curlObject = CurlParserAdapter.parse(curlCommand);
        } catch (Throwable e) {
            LOGGER.error("Cannot parse curl command.", e);
            NotificationUtils.error(HeadersBundle.message("error.transform.failure.parse.curl.command"), project);
            return null;
        }

        TransformScriptModel scriptModel = getTransformModel();
        Script script = scriptModel.getScript();
        try {
            if (script == null) {
                String scriptContent = scriptModel.getScriptContent();
                LOGGER.debug("Compiling script content: " + scriptContent);
                if (StringUtil.isEmpty(scriptContent)) {
                    NotificationUtils.error(HeadersBundle.message("warning.transform.script.empty"), project);
                    return null;
                }
                script = new Script(scriptContent);
                script.compile();
                scriptModel.setScript(script);
            }
        } catch (ScriptException e) {
            LOGGER.error("A compile error occurred while compiling the script.", e);
            NotificationUtils.error(HeadersBundle.message("error.transform.script.compile"), project);
            return null;
        }

        try (Scope scope = Scope.enter()) {
            script.eval(scope);
            String result = scope.call("transform", null, curlObject.getRoot().getObject());
            if (result == null) {
                NotificationUtils.error(HeadersBundle.message("error.transform.no.transform.function"), project);
                return null;
            }
            return result;
        } catch (Exception e) {
            LOGGER.error("An error occurred while running the script.", e);
            NotificationUtils.error(HeadersBundle.message("error.transform.script.runtime"), project);
            return null;
        }
    }
}
