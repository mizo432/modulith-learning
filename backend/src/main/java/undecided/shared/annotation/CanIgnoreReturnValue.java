package undecided.shared.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * このアノテーションは、メソッドまたはコンストラクタの戻り値を無視できることを示すために使用できます。
 *
 * <p>メソッド、コンストラクタ、型に適用できます。
 */
@Documented
@Target({ElementType.METHOD, ElementType.CONSTRUCTOR, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface CanIgnoreReturnValue {}
