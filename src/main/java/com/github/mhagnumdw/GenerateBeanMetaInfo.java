package com.github.mhagnumdw;

import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

/**
 * Marks the classes to generate the metadata information.
 */
@Target(TYPE)
@Retention(RUNTIME)
public @interface GenerateBeanMetaInfo {

}
