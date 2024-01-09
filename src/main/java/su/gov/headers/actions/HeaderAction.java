/**
 *
 * @Time: 2021/12/28 21:38
 * @author Mr.lin
 * @File: Headers.java
 */
package su.gov.headers.actions;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.intellij.openapi.diagnostic.Logger;
import su.gov.headers.HeadersBundle;
import su.gov.headers.utils.NotificationUtils;
import su.gov.headers.transform.TransformAction;

import java.util.HashMap;
import java.util.LinkedHashMap;

public class HeaderAction extends TransformAction {

    private static final Logger LOGGER = Logger.getInstance(HeaderAction.class);

    private static void parseHeader(String head, HashMap<String, String> headMap){
        String key;
        String[] valueParts;
        String[] headerParts = head.split("\\s", 2);
        if ( headerParts.length != 2 ){
            return;
        }
        String firstPart = headerParts[0];
        String lastPart = headerParts[1];
        if (lastPart.isEmpty()){
            return;
        }
        else if (firstPart.isEmpty()){
            parseHeader(lastPart, headMap);
            return;
        }
        if (firstPart.endsWith(":")) {
            key = firstPart.substring(0, firstPart.length() - 1);
        }
        else if (lastPart.startsWith(":")){
            key = firstPart;
            lastPart = lastPart.substring(1).trim();
        }
        else {
            key = firstPart;
        }

        valueParts = lastPart.split("\\s*?\n\\s*", 2);
        if ( valueParts.length == 2 ){
            headMap.put(key, valueParts[0].trim());
            parseHeader(valueParts[1], headMap);
        }
        else {
            headMap.put(key, lastPart.trim());
        }
    }

    @Override
    public String transform(String text) {
        LinkedHashMap<String, String> headers = new LinkedHashMap<>();
        parseHeader(text, headers);
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            return objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(headers);
        }catch (JsonProcessingException e){
            LOGGER.error("Transform request headers failure", e);
            NotificationUtils.error(HeadersBundle.message("error.transform.header", e.getMessage()), project);
        }
        return null;
    }

}
