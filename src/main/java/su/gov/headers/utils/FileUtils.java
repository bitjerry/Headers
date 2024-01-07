/**
 * Description:
 *
 * @ProjectName Header
 * @Title FileUtils
 * @Author Mr.lin
 * @Date 2023-12-25 16:07
 * @Version V1.0.0
 * @Copyright © 2023 by Mr.lin. All rights reserved.
 */
package su.gov.headers.utils;

import java.io.InputStream;

public class FileUtils {

    public final String filePath;

    public  FileUtils(String filePath) {
        this.filePath = filePath;
    }

    public String read(){
        return "file content";
    }

    public void write(){

    }

    public InputStream inputStream(){
        return null;
    }
}
