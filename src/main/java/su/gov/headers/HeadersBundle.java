/**
 * Description:
 *
 * @ProjectName Header
 * @Title HeadersBundle
 * @Author Mr.lin
 * @Date 2023-12-25 0:47
 * @Version V1.0.0
 * @Copyright © 2023 by Mr.lin. All rights reserved.
 */
package su.gov.headers;

import com.intellij.AbstractBundle;
import com.intellij.DynamicBundle;
import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.PropertyKey;

import java.lang.invoke.MethodHandles;
import java.lang.reflect.Method;
import java.util.Locale;
import java.util.ResourceBundle;
import java.util.ResourceBundle.Control;

public final class HeadersBundle extends AbstractBundle {

    public static final String BUNDLE = "messages.HeadersBundle";

    public static final  HeadersBundle INSTANCE = new HeadersBundle();

    public HeadersBundle() {
        super(BUNDLE);
    }

    public static String message(@NotNull @PropertyKey(resourceBundle = BUNDLE) String key, Object ... params) {
        return INSTANCE.getMessage(key, params);
    }

    @Override
    protected ResourceBundle findBundle(@NotNull @NonNls String pathToBundle, @NotNull ClassLoader loader, @NotNull Control control) {
        return ResourceBundle.getBundle(pathToBundle, DynamicBundle.getLocale(), loader, control);
    }
}
