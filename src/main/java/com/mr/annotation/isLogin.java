package com.mr.annotation;

import java.lang.annotation.*;

@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.PARAMETER,ElementType.METHOD})
@Documented
public @interface isLogin {
    String description() default"";
}
