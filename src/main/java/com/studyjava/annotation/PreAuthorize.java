package com.studyjava.annotation;

import java.lang.annotation.*;

/**
 * 权限校验注解
 */
@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface PreAuthorize {
    /**
     * 验证权限字符串
     * @return String
     */
    String value() default "";
}
