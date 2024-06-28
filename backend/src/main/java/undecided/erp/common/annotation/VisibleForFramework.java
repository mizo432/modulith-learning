package undecided.erp.common.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * この注釈は、注釈が付けられたメソッドがフレームワークのみに可視であることを示すために使用されます。
 * <p>
 * この注釈が付けられたメソッドは、一般的な使用を目的としておらず、クライアントコードから直接呼び出すべきではありません。
 *
 * <p>この注釈は「CLASS」の保持ポリシーを持っています。つまり、コンパイル時には利用可能ですが、ランタイムでは利用できません</p>
 *
 * <p>使用法:</p>
 *
 * <pre>{@code
 *   @VisibleForFramework
 *   public void doSomethingOnlyForFramework() {
 *       // Implementation goes here
 *   }
 * }</pre>
 *
 * <p>@VisibleForFramework
 * で注釈されたメソッドは、通常、フレームワーク内部で使用され、内部の目的を果たします。それらはパッケージプライベートまたは保護された可視性を持ち、クライアントコードから直接呼び出すべきではありません。</p>
 */
@Retention(RetentionPolicy.CLASS)
@Target({ElementType.METHOD, ElementType.CONSTRUCTOR})
public @interface VisibleForFramework {

}
