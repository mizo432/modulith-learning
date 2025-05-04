package undecided.erp.common.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 呼ぶべきではないメソッドに付与する
 */
@Retention(RetentionPolicy.CLASS)
@Target({ElementType.METHOD})
public @interface DoNotCall {

  String value() default "";

}