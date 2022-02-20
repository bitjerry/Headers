/**
 *
 * @Time: 2021/12/28 21:38
 * @author Mr.lin
 * @File: Cookies.java
 */
package actions;

import control.Controller;

public class Cookies extends Controller {

    public static String getCookies(String cookies, String padding){
        return cookies.replaceAll("\"","\\\\\"")
                .replaceAll("[\r\n; ]*([\\s\\S]*?)[=\t:] *([^;\r\n]*)",  padding + "\"$1\": \"$2\",\n")
                .replaceAll("\\\\\",\"", "\\\\\\\"\"")
                .replaceAll(padding + "([\\s\\S]*),\n","$1")
                .replaceAll(",\n"+padding+"\"\": \"\"","");
    }

    @Override
    public String result(String text, String padding) {
        return getCookies(text, padding);
    }
}
