package com.example.demo.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface SensitiveSql {
    /**
     * 需要掩码的参数索引（从 0 开始）
     * 不指定则对所有参数进行掩码
     */
    int[] value() default {};
}
