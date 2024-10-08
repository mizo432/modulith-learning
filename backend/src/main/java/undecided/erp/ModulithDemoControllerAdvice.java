package undecided.erp;

import org.springframework.beans.propertyeditors.StringTrimmerEditor;
import org.springframework.validation.DataBinder;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.RestController;

/**
 * ModulithDemoControllerのためのコントローラーアドバイス。
 */
@ControllerAdvice(annotations = {RestController.class})
public class ModulithDemoControllerAdvice {

  /**
   * 指定されたリクエストのデータバインダーを初期化します。
   * <p>
   * このメソッドは {@code @InitBinder}
   * というアノテーションがついていて、ModulithDemoControllerクラスのコントローラアドバイスの一部として使用されます。このメソッドの目的は、Stringクラス用のカスタムエディターを登録することによってデータバインディングプロセスを設定することです。
   *
   * @param binder 初期化するWebDataBinderインスタンス
   * @see WebDataBinder
   * @see StringTrimmerEditor
   */
  @InitBinder
  public void initBinder(WebDataBinder binder) {
    // bind empty strings as null
    binder.registerCustomEditor(String.class, new StringTrimmerEditor(true));

  }

  /**
   * 指定された DataBinder インスタンスに直接フィールドアクセスを初期化します。
   *
   * <p>
   * このメソッドは {@code @InitBinder} で注釈されており、ModulithDemoController クラスの コントローラーアドバイスの一部です。その目的は、String
   * クラス用のカスタムエディターを登録することで データバインディングプロセスを構成することです。
   *
   * @param dataBinder 初期化する DataBinder インスタンス
   * @see InitBinder
   * @see WebDataBinder
   * @see StringTrimmerEditor
   */
  @InitBinder
  private void initDirectFieldAccess(DataBinder dataBinder) {
    dataBinder.initDirectFieldAccess();

  }
}
