/**
 *
 * @Time: 2021/12/28 21:38
 * @author Mr.lin
 * @File: Headers.java
 */
package actions;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import control.Controller;

import java.util.HashMap;
import java.util.LinkedHashMap;

public class Headers extends Controller {


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
    public String parse(String text) throws JsonProcessingException {
        LinkedHashMap<String, String> headers = new LinkedHashMap<>();
        parseHeader(text, headers);
        ObjectMapper objectMapper = new ObjectMapper();
        return objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(headers);
    }

}
