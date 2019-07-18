package com.alibaba.excel.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter.DEFAULT;

/**
 * @author jipengfei
 */
/**
 * @author PC
 *
 */
/**
 * @author PC
 *
 */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@Inherited
public @interface ExcelProperty {

     /**
      * @return
      */
     String[] value() default {""};


     /**
      * @return
      */
     int index() default 99999;

     /**
      *
      * default @see com.alibaba.excel.util.TypeUtil
      * if default is not  meet you can set format
      * if isDate = true  , this optional 
      * @return
      */
     String format() default "";
     
     /**
      * eg:"{'k1':'v1','k2':'v2'}"
      * @return
      */
     String keyValue() default "";
     
     /**
      * default false
      * @return
      */
     boolean date() default false;
     
     boolean shrink() default false;
     
     String shrinkValue() default "";
}
