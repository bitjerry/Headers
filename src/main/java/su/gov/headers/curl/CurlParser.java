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

import su.gov.headers.curl.annotation.CurlOptionHandler;
import su.gov.headers.curl.annotation.PreParser;
import su.gov.headers.scripts.objects.JSObjectWarp;

import java.net.URI;
import java.net.URISyntaxException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CurlParser {

    private boolean forceGet;
    private final CurlObject curlObject;

    private final static Pattern SEMICOLON_PATTERN = Pattern.compile("(?:\"(?:[^\\\\\"]|\\\\.)*\"|[^;\"]+)+");

    private final static Pattern COLON_PATTERN = Pattern.compile("(\\S+?):\\s*(.*)");

    public CurlParser(CurlObject curlObject) {
        this.curlObject = curlObject;
        curlObject.setMethod("GET");
    }

    @PreParser
    @CurlOptionHandler(options = {"-G", "--get"})
    public void getHandler() {
        forceGet = true;
    }

    @PreParser
    @CurlOptionHandler(regex = "^[A-Za-z]+:/{2,3}.*")
    public void urlHandler(String url) throws URISyntaxException {
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
        curlObject.setUrl(builder.toString());
    }

    @CurlOptionHandler(options = {"-A", "--user-agent"})
    public void userAgentHandler(String ua) {
        curlObject.getHeaders().set("user-agent", ua);
    }


    @CurlOptionHandler(options = {"-H", "--header"})
    public void headersHandler(String headers) {
        Matcher matcher = COLON_PATTERN.matcher(headers);
        if (!matcher.find()) {
            return;
        }
        String key = matcher.group(1).toLowerCase();
        String value = matcher.group(2);
        if (key.equals("cookie")) {
            cookiesHandler(value);
        } else {
            curlObject.getHeaders().set(key, value);
        }
    }

    @CurlOptionHandler(options = {"-c", "--cookie"})
    public void cookiesHandler(String cookie) {
        JSObjectWarp headers = curlObject.getHeaders();
        Object cookies = headers.get("cookie");
        if (JSObjectWarp.isEmpty(cookies)) {
            headers.set("cookie", cookie);
        } else {
            headers.set("cookie", cookies + "; " + cookie);
        }
    }

    @CurlOptionHandler(options = {"-e", "--referer"})
    public void refererHandler(String referer) {
        curlObject.getHeaders().set("referer", referer);
    }

    @CurlOptionHandler(options = {"-X", "--request"})
    public void methodHandler(String method) {
        if (forceGet) return;
        curlObject.setMethod(method);
    }

    @CurlOptionHandler(options = {"-u", "--user"})
    public void authHandler(String auth) {
        curlObject.getHeaders().set("authorization", auth);
    }

    @CurlOptionHandler(options = {"-d", "--data", "--data-ascii", "--data-raw", "--data-binary"})
    public void dataHandler(String data) {
        if (forceGet) {
            parameterHandler(data);
            return;
        }
        Object method = curlObject.getMethod();
        if ("GET".equals(method) || "HEAD".equals(method)) {
            methodHandler("POST");
        }
        JSObjectWarp headers = curlObject.getHeaders();
        if (JSObjectWarp.isEmpty(headers.get("content-type"))) {
            headers.set("content-type", "application/x-www-form-urlencoded");
        }
        Object body = curlObject.getData();
        if (JSObjectWarp.isEmpty(body)) {
            curlObject.setData(data);
        } else {
            curlObject.setData(body + "&" + data);
        }
    }

    @CurlOptionHandler(options = "--data-urlencode")
    public void dataUrlencodeHandler(String data) {
        data = URLEncoder.encode(data, StandardCharsets.UTF_8);
        dataHandler(data);
    }


    @CurlOptionHandler(options = {"-F", "--form"})
    public void formHandler(String data) {
        Object method = curlObject.getMethod();
        if ("GET".equals(method) || "HEAD".equals(method)) {
            methodHandler("POST");
        }
        curlObject.getHeaders().remove("content-type");
        CurlObject.Form form = curlObject.getForm();
        final Matcher matcher = SEMICOLON_PATTERN.matcher(data);
        if (matcher.find()) {
            String[] kvPair = matcher.group().split("=", 2);
            if (kvPair.length != 2) return;
            JSObjectWarp formItem = form.get();
            formItem.set("name", kvPair[0].trim());
            formItem.set("value", kvPair[1].trim());
        }
        while (matcher.find()) {
            String[] kvPair = matcher.group().split("=", 2);
            if (kvPair.length != 2) continue;
            form.get().set(kvPair[0].trim(), kvPair[1].trim());
        }
    }

    @CurlOptionHandler(options = "--compressed")
    public void compressedHandler() {
        JSObjectWarp headers = curlObject.getHeaders();
        if (JSObjectWarp.isEmpty(headers.get("accept-encoding"))) {
            headers.set("accept-encoding", "gzip, deflate, br");
        }
    }

    @CurlOptionHandler(options = {"-I", "--head"})
    public void headHandler() {
        methodHandler("HEAD");
    }


    public void parameterHandler(String parameter) {
        JSObjectWarp paramsObject = curlObject.getParams();
        String[] params = parameter.split("&");
        for (String param : params) {
            String[] kv = param.split("=", 2);
            if (kv.length != 2) continue;
            paramsObject.set(kv[0], kv[1]);
        }

    }

}
