/**
 * Description:
 *
 * @ProjectName Header
 * @Title CurlObject
 * @Author Mr.lin
 * @Date 2023-12-26 21:57
 * @Version V1.0.0
 * @Copyright © 2023 by Mr.lin. All rights reserved.
 */
package su.gov.headers.curl;

import su.gov.headers.scripts.objects.JSArrayWarp;
import su.gov.headers.scripts.objects.JSObjectWarp;

public class CurlObject {

    private final JSObjectWarp rootObject;

    private JSObjectWarp params;

    private JSObjectWarp headers;

    private JSObjectWarp cookies;

    private Form form;

    public static class Form {

        private final JSArrayWarp jsArrayWarp;

        public Form(JSArrayWarp jsArrayWarp) {
            this.jsArrayWarp = jsArrayWarp;
        }

        public JSObjectWarp get() {
            JSObjectWarp jsObjectWarp = JSObjectWarp.newObject();
            jsArrayWarp.append(jsObjectWarp.getObject());
            return jsObjectWarp;
        }
    }

    public CurlObject() {
        rootObject = JSObjectWarp.newObject();
    }

    public String getMethod() {
        return (String) rootObject.get("method");
    }

    public void setMethod(String method) {
        rootObject.set("method", method);
    }

    public String getUrl() {
        return (String) rootObject.get("url");
    }

    public void setUrl(String url) {
        rootObject.set("url", url);
    }

    public JSObjectWarp getParams() {
        if (params != null) {
            return params;
        }
        params = JSObjectWarp.newObject();
        rootObject.set("params", params.getObject());
        return params;
    }

    public JSObjectWarp getHeaders() {
        if (headers != null) {
            return headers;
        }
        headers = JSObjectWarp.newObject();
        rootObject.set("headers", headers.getObject());
        return headers;
    }

    public JSObjectWarp getCookies() {
        if (cookies != null) {
            return cookies;
        }
        cookies = JSObjectWarp.newObject();
        rootObject.set("cookies", cookies.getObject());
        return cookies;
    }

    public Object getData() {
        return rootObject.get("data");
    }

    public void setData(String data) {
        rootObject.set("data", data);
    }

    public Form getForm() {
        if (form != null) {
            return form;
        }
        JSArrayWarp jsArrayWarp = JSArrayWarp.newObject();
        form = new Form(jsArrayWarp);
        rootObject.set("form", jsArrayWarp.getObject());
        return form;
    }


    public JSObjectWarp getRoot() {
        return rootObject;
    }

}
