/**
 *
 * @Time: 2021/12/28 21:38
 * @author Mr.lin
 * @File: Cookies.java
 */
package su.gov.headers.actions;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.intellij.openapi.diagnostic.Logger;
import su.gov.headers.HeadersBundle;
import su.gov.headers.utils.NotificationUtils;
import su.gov.headers.transform.TransformAction;

import java.util.LinkedHashMap;

public class CookieAction extends TransformAction {

    private static final Logger LOGGER = Logger.getInstance(CookieAction.class);

    @Override
    public String transform(String text) {
        LinkedHashMap<String, String> cookieMap = new LinkedHashMap<>();
        String[] cookieItems = text.split("\\s*;\\s*");
        for (String cookieItem : cookieItems){
            String[] cookieKV = cookieItem.split("\\s*=\\s*", 2);
            if (cookieKV.length != 2){
                break;
            }
            cookieMap.put(cookieKV[0], cookieKV[1]);
        }
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            return objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(cookieMap);
        }
        catch (JsonProcessingException e){
            LOGGER.error("Transform request cookie failure", e);
            NotificationUtils.error(HeadersBundle.message("error.transform.cookie"));
        }
        return null;
    }
}
