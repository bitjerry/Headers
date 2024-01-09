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

import org.mozilla.javascript.Context;
import org.mozilla.javascript.Function;
import org.mozilla.javascript.Scriptable;
import su.gov.headers.scripts.objects.JSObjectWarp;

import java.io.Closeable;

public class Scope implements Closeable {
    public static final Scriptable shareScope;

    protected final Context context;

    protected final Scriptable scope;

    static {
        try {
            Context ct = Context.enter();
            ct.setOptimizationLevel(-1);
            ct.setLanguageVersion(Context.VERSION_ES6);
            shareScope = ct.initStandardObjects();
        } finally {
            Context.exit();
        }
    }

    private Scope() {
        this.context = Context.enter();
        this.scope = this.context.newObject(shareScope);
    }

    public static Scope enter() {
        return new Scope();
    }

    public Object getAttribute(String key) {
        return scope.get(key, scope);
    }

    public void setAttribute(String key, Object value) {
        scope.put(key, scope, value);
    }

    public void removeAttribute(String key) {
        scope.delete(key);
    }

    public String call(String function, Object thiz, Object... args) {
        Function func = (Function) getAttribute(function);
        if (JSObjectWarp.isEmpty(func)) {
            return null;
        }
        return String.valueOf(func.call(context, scope, scope, args));
    }

    @Override
    public void close() {
        Context.exit();
    }

}
