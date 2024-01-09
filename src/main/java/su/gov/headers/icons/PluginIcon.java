/**
 * Description:
 *
 * @ProjectName Header
 * @Title PluginIcon
 * @Author Mr.lin
 * @Date 2024-01-04 19:17
 * @Version V1.0.0
 * @Copyright © 2024 by Mr.lin. All rights reserved.
 */
package su.gov.headers.icons;

import com.intellij.openapi.util.IconLoader;

import javax.swing.Icon;

public class PluginIcon {

    public static final Icon RED_16 = loadIcon("red_16.svg");
    public static final Icon GREEN_16 = loadIcon("green_16.svg");
    public static final Icon ORANGE_16 = loadIcon("orange_16.svg");


    private static Icon loadIcon(String iconName) {
        return IconLoader.getIcon("/icons/" + iconName, PluginIcon.class);
    }

}
