/**
 * Description:
 *
 * @ProjectName Header
 * @Title Scope
 * @Author Mr.lin
 * @Date 2024-01-09 18:19
 * @Version V1.0.0
 * @Copyright © 2024 by Mr.lin. All rights reserved.
 */
package su.gov.headers.scripts;

import org.openjdk.nashorn.api.scripting.JSObject;
import su.gov.headers.scripts.objects.JSObjectWarp;

import javax.script.ScriptContext;
import javax.script.SimpleScriptContext;
import java.io.Closeable;

public class Scope implements Closeable {

    protected final ScriptContext context;

    private Scope() {
        this.context = new SimpleScriptContext();
        context.setBindings(Script.ENGINE.createBindings(), ScriptContext.ENGINE_SCOPE);
    }

    public static Scope enter() {
        return new Scope();
    }

    public Object getAttribute(String key) {
        return context.getAttribute(key, ScriptContext.ENGINE_SCOPE);
    }

    public void setAttribute(String key, Object value) {
        context.setAttribute(key, value, ScriptContext.ENGINE_SCOPE);
    }

    public Object removeAttribute(String key) {
        return context.removeAttribute(key, ScriptContext.ENGINE_SCOPE);
    }

    public String call(String function, Object thiz, Object... args) {
        JSObject func = (JSObject) getAttribute(function);
        if (JSObjectWarp.isEmpty(func)) {
            return null;
        }
        return String.valueOf(func.call(thiz, args));
    }

    @Override
    public void close() {
//        context.getBindings(ScriptContext.ENGINE_SCOPE).clear();
    }

}
