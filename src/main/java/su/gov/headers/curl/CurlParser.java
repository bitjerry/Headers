/**
 * Description:
 *
 * @ProjectName Header
 * @Title CurlParser
 * @Author Mr.lin
 * @Date 2023-12-26 23:47
 * @Version V1.0.0
 * @Copyright © 2023 by Mr.lin. All rights reserved.
 */
package su.gov.headers.curl;

import org.openjdk.nashorn.api.scripting.JSObject;
import org.openjdk.nashorn.api.scripting.ScriptObjectMirror;
import org.openjdk.nashorn.internal.runtime.Undefined;
import su.gov.headers.curl.annotation.CurlOptionHandler;
import su.gov.headers.curl.annotation.PreParser;
import su.gov.headers.utils.RegexUtils;

import javax.script.ScriptException;
import java.net.*;
import java.nio.charset.StandardCharsets;
import java.util.regex.Matcher;

public class CurlParser{

    private boolean forceGet;
    private final CurlObject curlObject;

    public CurlParser(CurlObject curlObject) {
        this.curlObject = curlObject;
        curlObject.setMember("method", "GET");
    }

    @PreParser
    @CurlOptionHandler(options = {"-G", "--get"})
    public void getHandler() {
        forceGet = true;
    }

    @PreParser
    @CurlOptionHandler(regex = "^[A-Za-z]+:/{2,3}.*")
    public void urlHandler(String url) throws URISyntaxException, ScriptException {
        URI uri = new URI(url);
        String query = uri.getQuery();
        if (query != null) {
            parameterHandler(query);
        }
        StringBuilder builder = new StringBuilder();
        builder.append(uri.getScheme()).append("://").append(uri.getHost()).append(uri.getRawPath());
        if (uri.getPort() != -1) {
            builder.append(":").append(uri.getPort());
        }
        curlObject.setMember("url", builder.toString());
    }

    @CurlOptionHandler(options = {"-A", "--user-agent"})
    public void userAgentHandler(String ua) throws ScriptException {
        curlObject.getJSObject("headers").setMember("user-agent", ua);
    }


    @CurlOptionHandler(options = {"-H", "--header"})
    public void headersHandler(String headers) throws ScriptException {
        Matcher matcher = RegexUtils.COLON_PATTERN.matcher(headers);
        if (!matcher.find()) {
            return;
        }
        String key = matcher.group(1).toLowerCase();
        String value = matcher.group(2);
        if (key.equals("cookie")) {
            cookiesHandler(value);
        } else {
            curlObject.getJSObject("headers").setMember(key, value);
        }
    }

    @CurlOptionHandler(options = {"-c", "--cookie"})
    public void cookiesHandler(String cookie) throws ScriptException {
        ScriptObjectMirror headers = curlObject.getJSObject("headers");
        Object cookies = headers.getMember("cookie");
        if (cookies instanceof Undefined) {
            headers.setMember("cookie", cookie);
        } else {
            headers.setMember("cookie", cookies + "; " + cookie);
        }
    }

    @CurlOptionHandler(options = {"-e", "--referer"})
    public void refererHandler(String referer) throws ScriptException {
        curlObject.getJSObject("headers").setMember("referer", referer);
    }

    @CurlOptionHandler(options = {"-X", "--request"})
    public void methodHandler(String method) {
        if (forceGet) return;
        curlObject.setMember("method", method);
    }

    @CurlOptionHandler(options = {"-u", "--user"})
    public void authHandler(String auth) throws ScriptException {
        curlObject.getJSObject("headers").setMember("authorization", auth);
    }

    @CurlOptionHandler(options = {"-d", "--data", "--data-ascii", "--data-raw", "--data-binary"})
    public void dataHandler(String data) throws ScriptException {
        if (forceGet) {
            parameterHandler(data);
            return;
        }

        Object method = curlObject.getMember("method");
        if ("GET".equals(method) || "HEAD".equals(method)) {
            methodHandler("POST");
        }
        JSObject headers = curlObject.getJSObject("headers");
        if (headers.getMember("content-type") instanceof Undefined) {
            headers.setMember("content-type", "application/x-www-form-urlencoded");
        }
        Object body = curlObject.getMember("body");
        if (body instanceof Undefined) {
            curlObject.setMember("body", data);
        } else {
            curlObject.setMember("body", body + "&" + data);
        }
    }

    @CurlOptionHandler(options = "--data-urlencode")
    public void dataUrlencodeHandler(String data) throws ScriptException {
        data = URLEncoder.encode(data, StandardCharsets.UTF_8);
        dataHandler(data);
    }


    @CurlOptionHandler(options = {"-F", "--form"})
    public void formHandler(String data) throws ScriptException {
        Object method = curlObject.getMember("method");
        if ("GET".equals(method) || "HEAD".equals(method)) {
            methodHandler("POST");
        }
        curlObject.getJSObject("headers").removeMember("content-type");
        ScriptObjectMirror jsArray = curlObject.getJSArray("form");
        ScriptObjectMirror jsObject = curlObject.createJSObject();
        Object length = jsArray.getMember("length");
        final Matcher matcher = RegexUtils.SEMICOLON_PATTERN.matcher(data);
        if (matcher.find()) {
            String[] kvPair = matcher.group().split("=", 2);
            if (kvPair.length != 2) return;
            jsArray.setMember(String.valueOf(length), jsObject);
            jsObject.setMember("name", kvPair[0].trim());
            jsObject.setMember("value", kvPair[1].trim());
        }
        while (matcher.find()) {
            String[] kvPair = matcher.group().split("=", 2);
            if (kvPair.length != 2) continue;
            jsObject.setMember(kvPair[0].trim(), kvPair[1].trim());
        }
    }

    @CurlOptionHandler(options = "--compressed")
    public void compressedHandler() throws ScriptException {
        ScriptObjectMirror headers = curlObject.getJSObject("headers");
        if (headers.getMember("accept-encoding") instanceof Undefined) {
            curlObject.getJSObject("headers").setMember("accept-encoding", "gzip, deflate, br");
        }
    }

    @CurlOptionHandler(options = {"-I", "--head"})
    public void headHandler() {
        methodHandler("HEAD");
    }


    public void parameterHandler(String parameter) throws ScriptException {
        ScriptObjectMirror paramsObject = curlObject.getJSObject("params");
        String[] params = parameter.split("&");
        for (String param : params) {
            String[] kv = param.split("=", 2);
            if (kv.length != 2) continue;
            paramsObject.setMember(kv[0], kv[1]);
        }

    }

}
