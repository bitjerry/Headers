/**
 *
 * @Time: 2021/12/28 21:38
 * @author Mr.lin
 * @File: Cookies.java
 */
package actions;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import control.Controller;

import java.util.HashMap;
import java.util.LinkedHashMap;

public class Cookies extends Controller {

    private static void parseCookie(String cookie, HashMap<String, String> cookieMap){
        String[] cookieItems = cookie.split("\\s*;\\s*");
        for (String cookieItem : cookieItems){
            String[] cookieKV = cookieItem.split("\\s*=\\s*", 2);
            if (cookieKV.length != 2){
                return;
            }
            cookieMap.put(cookieKV[0], cookieKV[1]);
        }

    }

    @Override
    public String parse(String text) throws JsonProcessingException {
        LinkedHashMap<String, String> cookieMap = new LinkedHashMap<>();
        parseCookie(text, cookieMap);
        ObjectMapper objectMapper = new ObjectMapper();
        return objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(cookieMap);
    }
}
