/**
 * Description:
 *
 * @ProjectName Header
 * @Title Tools
 * @Author Mr.lin
 * @Date 2023-12-25 18:50
 * @Version V1.0.0
 * @Copyright © 2023 by Mr.lin. All rights reserved.
 */
package su.gov.headers.utils;

import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.regex.Pattern;

public class RegexUtils {
    public final static Pattern SEMICOLON_PATTERN = Pattern.compile("(?:\"(?:[^\\\\\"]|\\\\.)*\"|[^;\"]+)+");

    public final static Pattern COLON_PATTERN = Pattern.compile("(\\S+?):\\s*(.*)");

//    static {
//        String escape_pattern = "(?:\"(?:[^\\\\\"]|\\\\.)*\"|[^%s\"]+)+";
//        SEMICOLON_PATTERN = Pattern.compile(String.format(escape_pattern, ";"));
//        EQUAL_SIGN_PATTERN = Pattern.compile(String.format(escape_pattern, "="));
//    }
}
