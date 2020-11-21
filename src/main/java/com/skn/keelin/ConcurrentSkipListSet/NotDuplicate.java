package com.skn.keelin.ConcurrentSkipListSet;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.METHOD;

/**
 * 防止重复提交注解
 */
@Target({METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface NotDuplicate {
}
