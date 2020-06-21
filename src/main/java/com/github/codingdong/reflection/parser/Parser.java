package com.github.codingdong.reflection.parser;

import java.lang.annotation.*;

/**
 * <p>Description: Parser 注解</p>
 *
 * @author dbx
 * @date 2020/6/21 9:38
 * @since JDK1.8
 */

@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE})
@Documented
public @interface Parser {

    String aliasName() default "";

}
