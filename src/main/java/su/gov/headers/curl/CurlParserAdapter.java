/**
 * Description:
 *
 * @ProjectName Header
 * @Title CurlParser
 * @Author Mr.lin
 * @Date 2023-12-26 21:58
 * @Version V1.0.0
 * @Copyright © 2023 by Mr.lin. All rights reserved.
 */
package su.gov.headers.curl;

import com.intellij.openapi.diagnostic.Logger;
import com.intellij.openapi.util.text.StringUtil;
import su.gov.headers.curl.annotation.CurlOptionHandler;
import su.gov.headers.curl.annotation.Parser;
import su.gov.headers.curl.annotation.PostParser;
import su.gov.headers.curl.annotation.PreParser;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CurlParserAdapter {

    private static final Map<Class<? extends Annotation>, Map<String, Method>> handlerMap = new HashMap<>(){
        {
            put(Parser.class, new HashMap<>());
            put(PreParser.class, new HashMap<>());
            put(PostParser.class, new HashMap<>());
        }
    };

    private static final Map<Class<? extends Annotation>, Map<Pattern, Method>> regexHandlerMap = new HashMap<>(){
        {
            put(Parser.class, new HashMap<>());
            put(PreParser.class, new HashMap<>());
            put(PostParser.class, new HashMap<>());
        }
    };

    public static final Pattern COMMAND_PATTERN = Pattern.compile("'((?:[^\\\\']|\\\\.)*)'|\"((?:[^\\\\\"]|\\\\.)*)\"|([^\\s\"']+)");
    private final static Logger LOGGER = Logger.getInstance(CurlParserAdapter.class);

    static {
        Method[] methods = CurlParser.class.getDeclaredMethods();
        for (Method method : methods) {
            CurlOptionHandler handlerAnnotation = method.getAnnotation(CurlOptionHandler.class);
            if (null == handlerAnnotation) {
                continue;
            }

            method.setAccessible(true);
            Class<?> parserAnnotation = Parser.class;
            if (method.isAnnotationPresent(PreParser.class)) {
                parserAnnotation =  PreParser.class;
            }
            else if(method.isAnnotationPresent(PostParser.class)){
                parserAnnotation =  PostParser.class;
            }
            String regex = handlerAnnotation.regex();
            if (!StringUtil.isEmpty(regex)){
                regexHandlerMap.get(parserAnnotation).put(Pattern.compile(regex), method);
            }
            else {
                for (String option : handlerAnnotation.options()){
                    handlerMap.get(parserAnnotation).put(option, method);
                }
            }
        }
    }

    private static void handlerCurlOptions(Class<? extends Annotation> parserAnnotation, List<String> tokens, CurlParser parser) throws Exception {
        Map<String, Method> handlers = handlerMap.get(parserAnnotation);
        Map<Pattern, Method> regexHandlers = regexHandlerMap.get(parserAnnotation);
        if (handlers.isEmpty() && regexHandlers.isEmpty()) {
            return;
        }
        for (int i=1;i< tokens.size();i++) {
            Method handle = handlers.get(tokens.get(i));
            if (handle != null) {
                int parameterCount = handle.getParameterCount();
                handle.invoke(parser, tokens.subList(i + 1, i + parameterCount + 1).toArray());
                i += parameterCount;
            }
            else {
                for (Map.Entry<Pattern, Method> entry : regexHandlers.entrySet()) {
                    if (entry.getKey().matcher(tokens.get(i)).matches()) {
                        entry.getValue().invoke(parser, tokens.get(i));
                        break;
                    }
                }
            }
        }

    }


    public static CurlObject parse(String curlCommand) throws Exception {
        CurlObject curlObject = new CurlObject();
        CurlParser parser = new CurlParser(curlObject);
        Matcher matcher = COMMAND_PATTERN.matcher(curlCommand);
        List<String> tokens = new ArrayList<>();
        while (matcher.find()) {
            for (int i = 1; i <= matcher.groupCount(); i++){
                String capturedGroup = matcher.group(i);
                if (capturedGroup != null){
                    tokens.add(capturedGroup);
                    break;
                }
            }

        }
        handlerCurlOptions(PreParser.class, tokens, parser);
        handlerCurlOptions(Parser.class, tokens, parser);
        handlerCurlOptions(PostParser.class, tokens, parser);
        return curlObject;
    }
}
