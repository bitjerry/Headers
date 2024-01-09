/**
 * Description:
 *
 * @ProjectName Header
 * @Title JSUtils
 * @Author Mr.lin
 * @Date 2024-01-09 12:13
 * @Version V1.0.0
 * @Copyright © 2024 by Mr.lin. All rights reserved.
 */
package su.gov.headers.scripts;

import org.mozilla.javascript.Context;
import javax.script.ScriptException;

public class Script {

    private final String script;

    private org.mozilla.javascript.Script compiledScript = null;

    public Script(String script) {
        this.script = script;
    }

    public void compile() throws ScriptException {
        if (compiledScript == null) {

            try {
                Context ct = Context.enter();
                compiledScript = ct.compileString(script, null, 1, null);
            } finally {
                Context.exit();
            }
        }
    }

    public Object eval() {
        if (compiledScript != null) {
            try {
                Context ct = Context.enter();
                return compiledScript.exec(ct, Scope.shareScope);
            } finally {
                Context.exit();
            }
        }
        try {
            Context ct = Context.enter();
            return ct.evaluateString(Scope.shareScope, script, null, 1, null);
        } finally {
            Context.exit();
        }
    }

    public Object eval(Scope scope) {
        if (compiledScript != null) {
            try {
                Context ct = Context.enter();
                return compiledScript.exec(ct, scope.scope);
            } finally {
                Context.exit();
            }
        }
        try {
            Context ct = Context.enter();
            return ct.evaluateString(scope.scope, script, null, 1, null);
        } finally {
            Context.exit();
        }
    }

}
