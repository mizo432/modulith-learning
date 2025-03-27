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

  /**
   * アノテーションが付けられたメソッドに対する説明や理由を指定します。
   *
   * @return メソッドに関する説明や意図した使用制限事項
   */
  String value() default "";

}
