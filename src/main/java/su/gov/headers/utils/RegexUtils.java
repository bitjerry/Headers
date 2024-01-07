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

import java.util.regex.Pattern;

public class RegexUtils {
    public final static Pattern SEMICOLON_PATTERN = Pattern.compile("(?:\"(?:[^\\\\\"]|\\\\.)*\"|[^;\"]+)+");

    public final static Pattern COLON_PATTERN = Pattern.compile("(\\S+?):\\s*(.*)");

}
