package com.alibaba.excel.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author jipengfei
 * @author PC
 */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@Inherited
public @interface ExcelProperty {

	/**
	 * @return
	 */
	String[] value() default { "" };

	/**
	 * @return
	 */
	int index() default 99999;

	/**
	 *
	 * default @see com.alibaba.excel.util.TypeUtil if default is not meet you can
	 * set format if isDate = true , this optional
	 * 
	 * @return
	 */
	String format() default "";

	/**
	 * eg:"{'k1':'v1','k2':'v2'}"
	 * 
	 * @return
	 */
	String keyValue() default "";

	/**
	 * 是否将Long转换成时间
	 * 
	 * @return
	 */
	boolean date() default false;

	/**
	 * 是否缩小10000倍
	 * 
	 * @return
	 */
	boolean shrink() default false;

	/**
	 * 缩小的值。不填默认缩小10000
	 * 
	 * @return
	 */
	String shrinkValue() default "";

	/**
	 * 追加符号
	 * 
	 * @return
	 */
	String percent() default "";
}
