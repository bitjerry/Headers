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

import org.openjdk.nashorn.api.scripting.NashornScriptEngineFactory;

import javax.script.Compilable;
import javax.script.CompiledScript;
import javax.script.ScriptEngine;
import javax.script.ScriptException;

public class Script {

    private final static NashornScriptEngineFactory FACTORY = new NashornScriptEngineFactory();

    public final static ScriptEngine ENGINE = FACTORY.getScriptEngine("--optimistic-types=true", "--language=es6");

    private final String script;

    private CompiledScript compiledScript = null;

    public Script(String script) {
        this.script = script;
    }

    public void compile() throws ScriptException {
        if (compiledScript == null) {
            compiledScript = ((Compilable) ENGINE).compile(script);
        }
    }

    public Object eval() throws ScriptException {
        if (compiledScript != null) {
            return compiledScript.eval();
        }
        return ENGINE.eval(script);
    }

    public Object eval(Scope scope) throws ScriptException {
        if (compiledScript != null) {
            return compiledScript.eval(scope.context);
        }
        return ENGINE.eval(script, scope.context);
    }

}
