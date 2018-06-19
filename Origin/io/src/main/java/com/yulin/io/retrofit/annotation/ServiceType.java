package com.yulin.io.retrofit.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;

import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Target;

/**
 * Created by liu_lei on 2017/6/20.
 * <p>
 * 指定实际执行请求的retrofit service类的class
 */

@Documented
@Target({ElementType.TYPE, ElementType.METHOD})
@Retention(RUNTIME)
public @interface ServiceType {

    Class<?> value();

}
