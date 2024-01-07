/**
 * Description:
 *
 * @ProjectName Header
 * @Title Template
 * @Author Mr.lin
 * @Date 2023-12-20 23:03
 * @Version V1.0.0
 * @Copyright © 2023 by Mr.lin. All rights reserved.
 */
package su.gov.headers.transform;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.intellij.util.xmlb.annotations.Transient;
import org.jetbrains.annotations.NotNull;
import org.mozilla.javascript.Context;
import org.mozilla.javascript.Scriptable;
import org.openjdk.nashorn.api.scripting.NashornScriptEngineFactory;
import su.gov.headers.utils.ResourceUtils;

import javax.script.*;
import java.util.UUID;

public class TransformScriptModel {

    private final static NashornScriptEngineFactory FACTORY = new NashornScriptEngineFactory();

    public final static ScriptEngine ENGINE = FACTORY.getScriptEngine("--optimistic-types=true", "--language=es6");

    private final static String TEMPLATE_SCRIPT = ResourceUtils.read("/scripts/template.js");

    private final String id;
    private String name;
    private String scriptContent;

    private boolean autoReformat;
    private boolean keepIndent;

    @JsonIgnore
    private CompiledScript compiledScript = null;

    public TransformScriptModel() {
        this(UUID.randomUUID().toString());
    }

    public TransformScriptModel(@NotNull String id) {
        this.id = id;
        this.name = "New Script";
        this.scriptContent = TEMPLATE_SCRIPT;
        this.autoReformat = true;
        this.keepIndent = true;
    }

    public TransformScriptModel(@NotNull String name, @NotNull String scriptContent) {
        this();
        setName(name);
        setScriptContent(scriptContent);
    }

    // getters and setters

    public String getId() {
        return id;
    }

    @Transient
    public CompiledScript getCompiledScript() {
        return compiledScript;
    }

    public void setCompiledScript(CompiledScript compiledScript) {
        this.compiledScript = compiledScript;
    }

    public void setAutoReformat(boolean autoReformat) {
        this.autoReformat = autoReformat;
    }

    public boolean getKeepIndent() {
        return keepIndent;
    }

    public void setKeepIndent(boolean keepIndent) {
        this.keepIndent = keepIndent;
    }

    public @NotNull String getName() {
        return name;
    }

    public void setName(@NotNull String name) {
        this.name = name;
    }

    public boolean getAutoReformat() {
        return autoReformat;
    }

    public @NotNull String getScriptContent() {
        return scriptContent;
    }

    public void setScriptContent(@NotNull String scriptContent) {
        setCompiledScript(null);
        this.scriptContent = scriptContent;
    }
    // end of getters and setters

    public static ScriptContext createScriptContext() {
        SimpleScriptContext context = new SimpleScriptContext();
        context.setBindings(ENGINE.createBindings(), ScriptContext.ENGINE_SCOPE);
        return context;
    }


    public TransformScriptModel copy() {
        TransformScriptModel model = new TransformScriptModel(this.id);
        model.setName(this.name);
        model.setScriptContent(this.scriptContent);
        model.setAutoReformat(this.autoReformat);
        model.setKeepIndent(this.keepIndent);
        return model;
    }


    @Override
    public String toString() {
        return name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        TransformScriptModel model = (TransformScriptModel) o;

        if (autoReformat != model.autoReformat) return false;
        if (keepIndent != model.keepIndent) return false;
        if (!id.equals(model.id)) return false;
        if (!name.equals(model.name)) return false;
        return scriptContent.equals(model.scriptContent);
    }

    @Override
    public int hashCode() {
        int result = id.hashCode();
        result = 31 * result + name.hashCode();
        result = 31 * result + scriptContent.hashCode();
        result = 31 * result + (autoReformat ? 1 : 0);
        result = 31 * result + (keepIndent ? 1 : 0);
        return result;
    }
}
