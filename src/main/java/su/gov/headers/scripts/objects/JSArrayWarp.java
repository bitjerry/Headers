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
import su.gov.headers.scripts.Scope;

public class JSArrayWarp extends JSObjectWarp {

    protected JSArrayWarp(Scriptable jsObject) {
        super(jsObject);
    }


    public static JSArrayWarp newObject()  {
        try {
            Context ct = Context.enter();
            return new JSArrayWarp(ct.newObject(Scope.shareScope, "Array"));
        } finally {
            Context.exit();
        }
    }

    public void append(Object o){
        Double length = (Double) get("length");
        jsObject.put(length.intValue(), jsObject, o);
    }
}
