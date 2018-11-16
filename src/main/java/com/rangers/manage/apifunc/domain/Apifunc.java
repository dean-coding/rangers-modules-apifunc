package com.rangers.manage.apifunc.domain;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * API功能标识
 * @author fuhw
 * @date 2017年12月12日
 */
@Target({ ElementType.METHOD })
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Apifunc {

	ApifuncEnum name();//api名称

	int accessLimit() default 10000;//访问限制

	String version() default "v1";

	boolean disabled() default false;

}
