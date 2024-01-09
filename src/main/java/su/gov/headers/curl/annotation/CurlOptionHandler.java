/**
 * Description:
 *
 * @ProjectName Header
 * @Title CurlParamHandler
 * @Author Mr.lin
 * @Date 2023-12-26 22:34
 * @Version V1.0.0
 * @Copyright © 2023 by Mr.lin. All rights reserved.
 */
package su.gov.headers.curl.annotation;

import java.lang.annotation.*;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface CurlOptionHandler {
    String[] options() default {};

    String regex() default "";
}
