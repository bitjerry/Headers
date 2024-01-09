/**
 *
 * @Time: 2022/1/10 20:39
 * @Author: Mr.lin
 * @File: FormData.java
 */
package su.gov.headers.actions;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.intellij.openapi.diagnostic.Logger;
import su.gov.headers.HeadersBundle;
import su.gov.headers.utils.NotificationUtils;
import su.gov.headers.transform.TransformAction;

import java.util.LinkedHashMap;

public class FormDataAction extends TransformAction {

    private static final Logger LOGGER = Logger.getInstance(FormDataAction.class);

    @Override
    public String transform(String text) {
        LinkedHashMap<String, String> formDataMap = new LinkedHashMap<>();
        ObjectMapper objectMapper = new ObjectMapper();
        String[] formDataItems = text.split("\\s*&\\s*");
        for (String formDataItem : formDataItems){
            String[] formDataKV = formDataItem.split("\\s*=\\s*", 2);
            if (formDataKV.length != 2){
                break;
            }
            formDataMap.put(formDataKV[0], formDataKV[1]);
        }
        try{
            return objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(formDataMap);
        }catch (JsonProcessingException e){
            LOGGER.error("Transform request form data failure", e);
            NotificationUtils.error(HeadersBundle.message("error.transform.form.data", e.getMessage()), project);
        }
        return null;
    }
}
