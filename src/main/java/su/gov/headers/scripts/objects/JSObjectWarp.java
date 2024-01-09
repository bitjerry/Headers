/**
 * Description:
 *
 * @ProjectName Header
 * @Title JSObjectWarp
 * @Author Mr.lin
 * @Date 2024-01-09 12:09
 * @Version V1.0.0
 * @Copyright © 2024 by Mr.lin. All rights reserved.
 */
package su.gov.headers.scripts.objects;

import org.openjdk.nashorn.api.scripting.ScriptObjectMirror;
import org.openjdk.nashorn.internal.runtime.Undefined;
import su.gov.headers.scripts.Script;

import javax.script.ScriptException;

public class JSObjectWarp {

    private final ScriptObjectMirror jsObject;

    private final static ScriptObjectMirror objConstructor;

    static {
        try {
            objConstructor = (ScriptObjectMirror) Script.ENGINE.eval("Object");
        } catch (ScriptException e) {
            throw new RuntimeException(e);
        }
    }

    protected JSObjectWarp(ScriptObjectMirror jsObject) {
        this.jsObject = jsObject;
    }

    public static JSObjectWarp newObject() {
        return new JSObjectWarp((ScriptObjectMirror) objConstructor.newObject());
    }

    public Object get(String key) {
        return jsObject.getMember(key);
    }

    public void set(String key, Object value) {
        jsObject.setMember(key, value);
    }

    public void remove(String key) {
        jsObject.removeMember(key);
    }

    public ScriptObjectMirror getObject() {
        return jsObject;
    }


    public static boolean isEmpty(Object object) {
        return object == null || object instanceof Undefined;
    }

}
