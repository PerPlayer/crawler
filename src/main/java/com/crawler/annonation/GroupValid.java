package com.crawler.annonation;

import javax.validation.Valid;
import javax.validation.groups.Default;
import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Created by c_sunhaiwei on 2017/10/18.
 */

@Valid
public @interface GroupValid {
    Class value() default Default.class;

    Class<?>[] groups() default {};
}
