package com.github.chinacat.retry;

import java.lang.annotation.*;

/**
 * @author s.c.gao
 */
@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Retry {

    int times() default 3;

}
