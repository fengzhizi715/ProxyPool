package com.cv4j.proxy.web.aop.annotation;

import java.lang.annotation.*;

/**
 * Created by tony on 2018/1/16.
 */
@Target({ ElementType.PARAMETER, ElementType.METHOD })
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface WebLog {

    String value() default "";
}
