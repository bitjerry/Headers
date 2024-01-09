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

import org.mozilla.javascript.Context;
import org.mozilla.javascript.Scriptable;
import org.mozilla.javascript.Undefined;
import su.gov.headers.scripts.Scope;

public class JSObjectWarp {

    protected final Scriptable jsObject;


    protected JSObjectWarp(Scriptable jsObject) {
        this.jsObject = jsObject;
    }

    public static JSObjectWarp newObject() {
        try {
            Context ct = Context.enter();
            return new JSObjectWarp(ct.newObject(Scope.shareScope));
        } finally {
            Context.exit();
        }
    }

    public Object get(String key) {
        return jsObject.get(key, jsObject);
    }

    public void set(String key, Object value) {
        jsObject.put(key, jsObject, value);
    }

    public void remove(String key) {
        jsObject.delete(key);
    }

    public Scriptable getObject() {
        return jsObject;
    }


    public static boolean isEmpty(Object object) {
        return object == null || object instanceof Undefined;
    }

}
