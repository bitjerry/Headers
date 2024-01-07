/**
 * Description:
 *
 * @ProjectName Header
 * @Title CurlObject
 * @Author Mr.lin
 * @Date 2023-12-26 21:57
 * @Version V1.0.0
 * @Copyright © 2023 by Mr.lin. All rights reserved.
 */
package su.gov.headers.curl;

import com.intellij.openapi.diagnostic.Logger;
import org.openjdk.nashorn.api.scripting.ScriptObjectMirror;
import org.openjdk.nashorn.internal.runtime.Undefined;

import javax.script.ScriptContext;
import javax.script.ScriptEngine;
import javax.script.ScriptException;
import javax.script.SimpleScriptContext;

public class CurlObject  {
    private ScriptObjectMirror curlObject;
    private final ScriptEngine engine;

    private final static Logger LOGGER = Logger.getInstance(CurlObject.class);

    public CurlObject(ScriptEngine engine) {
        this.engine = engine;
    }

    public void initRootObject() throws ScriptException {
        this.curlObject = createJSObject();
    }

    public void setMember(String key, Object value){
        curlObject.setMember(key, value);
    }


    public Object getMember(String key){
        return curlObject.getMember(key);
    }


    public ScriptObjectMirror getJSObject(String key) throws ScriptException {
        Object object = curlObject.getMember(key);
        if (object instanceof Undefined){
            ScriptObjectMirror jsObject = createJSObject();
            curlObject.setMember(key, jsObject);
            return jsObject;
        }
        return (ScriptObjectMirror) object;
    }

    public ScriptObjectMirror getJSArray(String key) throws ScriptException {
        Object object = curlObject.getMember(key);
        if (object instanceof Undefined){
            ScriptObjectMirror jsObject = createJSArray();
            curlObject.setMember(key, jsObject);
            return jsObject;
        }
        return (ScriptObjectMirror) object;
    }

    public static boolean isEmpty(Object object){
        if (object == null){
            return true;
        } else if (object instanceof ScriptObjectMirror){
            return ((ScriptObjectMirror) object).isEmpty();
        } else return object instanceof String && ((String) object).isEmpty();
    }

    public ScriptObjectMirror createJSObject() throws ScriptException {
        ScriptObjectMirror objConstructor = (ScriptObjectMirror) engine.eval("Object");
        return (ScriptObjectMirror) objConstructor.newObject();

    }

    public ScriptObjectMirror createJSArray() throws ScriptException {
        ScriptObjectMirror objConstructor = (ScriptObjectMirror) engine.eval("Array");
        return (ScriptObjectMirror) objConstructor.newObject();
    }

    public ScriptObjectMirror toJSObject(){
        return curlObject;
    }

    @Override
    public String toString() {
        try {
            SimpleScriptContext context = new SimpleScriptContext();
            context.setAttribute("obj", curlObject, ScriptContext.ENGINE_SCOPE);
            return (String) engine.eval("JSON.stringify(obj,null,4)", context);
        } catch (ScriptException e) {
            LOGGER.error(e);
        }
        return "";
    }
}
