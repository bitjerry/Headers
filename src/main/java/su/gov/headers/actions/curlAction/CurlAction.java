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
package su.gov.headers.actions.curlAction;

import com.intellij.openapi.diagnostic.Logger;
import com.intellij.openapi.util.text.StringUtil;
import org.openjdk.nashorn.api.scripting.JSObject;
import su.gov.headers.HeadersBundle;
import su.gov.headers.curl.CurlObject;
import su.gov.headers.curl.CurlParserAdapter;
import su.gov.headers.transform.TransformScriptAction;
import su.gov.headers.transform.TransformScriptModel;
import su.gov.headers.utils.NotificationUtils;

import javax.script.Compilable;
import javax.script.CompiledScript;
import javax.script.ScriptContext;
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
        } catch (Exception e) {
            LOGGER.error("Cannot parse curl command.", e);
            NotificationUtils.error(HeadersBundle.message("error.transform.failure.parse.curl.command"));
            return null;
        }

        TransformScriptModel scriptModel = getTransformModel();
        CompiledScript script = scriptModel.getCompiledScript();
        try {
            if (script == null) {
                String scriptContent = scriptModel.getScriptContent();
                LOGGER.debug("Compiling script content: " + scriptContent);
                if (StringUtil.isEmpty(scriptContent)) {
                    NotificationUtils.error(HeadersBundle.message("warning.transform.script.empty"));
                    return null;
                }
                script = ((Compilable) TransformScriptModel.ENGINE).compile(scriptContent);
                scriptModel.setCompiledScript(script);
            }
        } catch (ScriptException e) {
            LOGGER.error("A compile error occurred while compiling the script.", e);
            NotificationUtils.error(HeadersBundle.message("error.transform.script.compile"));
            return null;
        }

        try{
            ScriptContext context = TransformScriptModel.createScriptContext();
            script.eval(context);
            JSObject function = (JSObject) context.getAttribute("transform", ScriptContext.ENGINE_SCOPE);
            if (function == null) {
                NotificationUtils.error(HeadersBundle.message("error.transform.no.transform.function"));
                return null;
            }
            Object result = function.call(null, curlObject.toJSObject());
            return result.toString();
        }
        catch (Exception e){
            LOGGER.error("An error occurred while running the script.", e);
            NotificationUtils.error(HeadersBundle.message("error.transform.script.runtime"));
            return null;
        }

    }
}
