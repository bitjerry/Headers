/**
 *
 * @Time: 2022/1/10 20:39
 * @Author: Mr.lin
 * @File: FormData.java
 */
package actions;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import control.Controller;

import java.util.HashMap;
import java.util.LinkedHashMap;

public class FormData extends Controller {
    private static void parseFormData(String formData, HashMap<String, String> formDataMap){
        String[] formDataItems = formData.split("\\s*&\\s*");
        for (String formDataItem : formDataItems){
            String[] formDataKV = formDataItem.split("\\s*=\\s*", 2);
            if (formDataKV.length != 2){
                return;
            }
            formDataMap.put(formDataKV[0], formDataKV[1]);
        }

    }
    @Override
    public String parse(String text) throws JsonProcessingException {
        LinkedHashMap<String, String> formDataMap = new LinkedHashMap<>();
        ObjectMapper objectMapper = new ObjectMapper();
        parseFormData(text, formDataMap);
        return objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(formDataMap);
    }
}
