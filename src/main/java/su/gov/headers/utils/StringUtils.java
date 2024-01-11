/**
 * Description:
 *
 * @ProjectName Header
 * @Title StringUtils
 * @Author Mr.lin
 * @Date 2024-01-05 23:27
 * @Version V1.0.0
 * @Copyright © 2024 by Mr.lin. All rights reserved.
 */
package su.gov.headers.utils;

public class StringUtils {

    public static String convertToLF(String s) {
        return s.replaceAll("\r\n?", "\n");
    }
}
