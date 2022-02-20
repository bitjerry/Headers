/**
 *
 * @Time: 2021/12/28 21:38
 * @author Mr.lin
 * @File: Headers.java
 */
package actions;

import control.Controller;

public class Headers extends Controller {

    public static String getHeaders(String headers, String padding){
        return headers.replaceAll("\"","\\\\\"")
                .replaceAll("[\r\n ]*((?:\\\\\")?:?[\\s\\S]*?)[\r\n:\t] *\t?(.*)",  padding + "\"$1\": \"$2\",\n")
                .replaceAll("\\\\\",\"", "\\\\\\\"\"")
                .replaceAll(padding+"([\\s\\S]*),\n","$1")
                .replaceAll(",\n"+padding+"\"\": \"\"","");
    }

    @Override
    public String result(String text, String padding) {
        return getHeaders(text, padding);
    }

}
