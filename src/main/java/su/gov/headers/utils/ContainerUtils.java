/**
 * Description:
 *
 * @ProjectName Header
 * @Title ContainerUtils
 * @Author Mr.lin
 * @Date 2023-12-26 11:53
 * @Version V1.0.0
 * @Copyright © 2023 by Mr.lin. All rights reserved.
 */
package su.gov.headers.utils;

import java.awt.*;

public class ContainerUtils {

    public static void enableComponents(Container container, boolean enable) {
        Component[] components = container.getComponents();
        for (Component component : components) {
            component.setEnabled(enable);
            if (component instanceof Container) {
                enableComponents((Container) component, enable);
            }
        }
    }
}
