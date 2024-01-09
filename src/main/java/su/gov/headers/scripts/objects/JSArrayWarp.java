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
import su.gov.headers.scripts.Script;

import javax.script.ScriptException;

public class JSArrayWarp extends JSObjectWarp {

    private final static ScriptObjectMirror objConstructor;

    static {
        try {
            objConstructor = (ScriptObjectMirror) Script.ENGINE.eval("Array");
        } catch (ScriptException e) {
            throw new RuntimeException(e);
        }
    }

    protected JSArrayWarp(ScriptObjectMirror jsObject) {
        super(jsObject);
    }


    public static JSArrayWarp newObject()  {
        return new JSArrayWarp((ScriptObjectMirror) objConstructor.newObject());
    }

    public void append(Object o){
        Object length = get("length");
        set(String.valueOf(length), o);
    }
}
