//@Time: 2021/12/28 21:38
//@Author: Mr.lin
//@File: headers.java
package actions;

import control.Controller;

public class Headers extends Controller{

    public static String getHeaders(String headers, String padding){
        return headers.replaceAll("\"","\\\\\"")
                .replaceAll("[\r\n ]*((?:\\\\\")?:?[\\s\\S]*?)[\r\n:\t] *\t?(.*)",  padding + "\"$1\": \"$2\",\n")
                .replaceAll("\\\\\",\"", "\\\\\\\"\"")
                .replaceAll(padding+"([\\s\\S]*),\n","$1");
    }

    @Override
    public String result(String text, String padding) {
        return getHeaders(text, padding);
    }

}
