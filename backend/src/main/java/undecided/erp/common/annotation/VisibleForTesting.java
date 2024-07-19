package undecided.erp.common.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * このアノテーションは、メソッドがテスト目的で可視であることを示すために使用されます。
 * <p>
 * このアノテーションは、メソッドに適用することができます。
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD})
public @interface VisibleForTesting {

}
