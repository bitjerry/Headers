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
import org.apache.tools.ant.types.Commandline;
import su.gov.headers.curl.annotation.CurlOptionHandler;
import su.gov.headers.curl.annotation.Parser;
import su.gov.headers.curl.annotation.PostParser;
import su.gov.headers.curl.annotation.PreParser;
import su.gov.headers.transform.TransformScriptModel;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

public class CurlParserAdapter {

    private static final Map<Class<?>, Map<String, Method>> handlerMap = new HashMap<>(){
        {
            put(Parser.class, new HashMap<>());
            put(PreParser.class, new HashMap<>());
            put(PostParser.class, new HashMap<>());
        }
    };

    private static final Map<Class<?>, Map<Pattern, Method>> regexHandlerMap = new HashMap<>(){
        {
            put(Parser.class, new HashMap<>());
            put(PreParser.class, new HashMap<>());
            put(PostParser.class, new HashMap<>());
        }
    };

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

    private static void handlerCurlOptions(Class<?> parserAnnotation, String[] tokens, CurlParser parser) throws Exception {
        Map<String, Method> handlers = handlerMap.get(parserAnnotation);
        Map<Pattern, Method> regexHandlers = regexHandlerMap.get(parserAnnotation);
        if (handlers.isEmpty() && regexHandlers.isEmpty()) {
            return;
        }
        for (int i=1;i< tokens.length;i++) {
            Method handle = handlers.get(tokens[i]);
            if (handle != null) {
                int parameterCount = handle.getParameterCount();
                handle.invoke(parser, (Object[]) Arrays.copyOfRange(tokens, i + 1, i + parameterCount + 1));
                i += parameterCount;
            }
            else {
                for (Map.Entry<Pattern, Method> entry : regexHandlers.entrySet()) {
                    if (entry.getKey().matcher(tokens[i]).matches()) {
                        entry.getValue().invoke(parser, tokens[i]);
                        break;
                    }
                }
            }
        }

    }

    public static CurlObject parse(String curlCommand) throws Exception {
        CurlObject curlObject = new CurlObject(TransformScriptModel.ENGINE);
        curlObject.initRootObject();
        CurlParser parser = new CurlParser(curlObject);
        String[] tokens = Commandline.translateCommandline(curlCommand);
        handlerCurlOptions(PreParser.class, tokens, parser);
        handlerCurlOptions(Parser.class, tokens, parser);
        handlerCurlOptions(PostParser.class, tokens, parser);
        return curlObject;
    }
}
