/**
 * Description:
 *
 * @ProjectName Header
 * @Title ReadResources
 * @Author Mr.lin
 * @Date 2023-12-23 10:21
 * @Version V1.0.0
 * @Copyright © 2023 by Mr.lin. All rights reserved.
 */
package su.gov.headers.utils;

import com.intellij.openapi.diagnostic.Logger;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;

public class ResourceUtils {

    private static final Logger LOG = Logger.getInstance(ResourceUtils.class);

    public static String read(String resourcePath) {
        if(!resourcePath.startsWith("/")){
            resourcePath = "/" + resourcePath;
        }
        StringBuilder content = new StringBuilder();
        try (InputStream inputStream = ResourceUtils.class.getResourceAsStream(resourcePath)) {
            if (inputStream != null) {
                InputStreamReader inputStreamReader = new InputStreamReader(inputStream, StandardCharsets.UTF_8);
                BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
                String line;
                while ((line = bufferedReader.readLine()) != null) {
                    content.append(line).append("\n");
                }
            }
        } catch (Exception e) {
            LOG.error(e);
        }
        return content.toString();
    }
}
