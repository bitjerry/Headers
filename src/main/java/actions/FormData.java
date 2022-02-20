/**
 *
 * @Time: 2022/1/10 20:39
 * @Author: Mr.lin
 * @File: FormData.java
 */
package actions;

import control.Controller;

public class FormData extends Controller {
    public static String getFormData(String formdata, String padding){
        return formdata.replaceAll("\"","\\\\\"")
                .replaceAll("[\r\n& ]*((?:\\\\\")?:?[\\s\\S]*?)[\r\n:=] *([^&\r\n]*)",  padding + "\"$1\": \"$2\",\n")
                .replaceAll("\\\\\",\"", "\\\\\\\"\"")
                .replaceAll(padding+"([\\s\\S]*),\n","$1")
                .replaceAll(",\n"+padding+"\"\": \"\"","");
    }

    @Override
    public String result(String text, String padding) {
        return getFormData(text,padding);
    }
}
