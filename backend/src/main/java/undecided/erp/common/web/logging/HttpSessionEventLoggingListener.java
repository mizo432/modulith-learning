package undecided.erp.common.web.logging;

import jakarta.servlet.http.HttpSession;
import jakarta.servlet.http.HttpSessionActivationListener;
import jakarta.servlet.http.HttpSessionAttributeListener;
import jakarta.servlet.http.HttpSessionBindingEvent;
import jakarta.servlet.http.HttpSessionEvent;
import jakarta.servlet.http.HttpSessionListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * HttpSessionEventLoggingListener は、HttpSession に関連するライフサイクルイベントおよび属性変更イベントのロギングを提供するクラスです。
 * <p>
 * このクラスは、HttpSessionListener、HttpSessionAttributeListener、HttpSessionActivationListener
 * インターフェースを実装しています。
 * セッションの作成、破棄、アクティベーション、パッシベーション、属性の追加・変更・削除などのイベントを監視し、それらをデバッグログまたはトレースログに記録します。
 * <p>
 * 主に以下のイベントを処理します: 1. セッションのライフサイクルイベント: - セッションの作成と破棄 - セッションのアクティベーションとパッシベーション 2.
 * セッション属性の変更イベント: - 属性の追加、削除、置換
 * <p>
 * フィールド: - logger: イベントの情報をロギングするための Logger インスタンス。
 * <p>
 * 利用用途: アプリケーション内のセッション操作をトレースし、デバッグ情報を収集するために使用されます。
 */
public class HttpSessionEventLoggingListener implements HttpSessionListener,
    HttpSessionAttributeListener, HttpSessionActivationListener {

  private static final Logger logger = LoggerFactory.getLogger(
      HttpSessionEventLoggingListener.class);

  public HttpSessionEventLoggingListener() {
  }

  /**
   * セッションが非アクティブ状態に遷移した際に呼び出されるメソッドです。
   * <p>
   * このメソッドは、セッションの非アクティブ化イベントに関する情報をデバッグログに記録します。
   *
   * @param se セッション非アクティブ化イベント情報を格納した HttpSessionEvent オブジェクト -
   * このオブジェクトにはイベントがトリガーされたセッション情報が含まれます。
   */
  public void sessionWillPassivate(HttpSessionEvent se) {
    if (logger.isDebugEnabled()) {
      HttpSession session = se.getSession();
      logger.debug("SESSIONID#{} sessionWillPassivate : {}", session.getId(), se.getSource());
    }

  }

  /**
   * セッションがアクティブ状態に遷移した際に呼び出されるメソッドです。
   * <p>
   * このメソッドは、セッションの活性化イベントに関する情報をデバッグログに記録します。
   *
   * @param se セッション活性化イベント情報を格納した HttpSessionEvent オブジェクト - このオブジェクトにはイベントがトリガーされたセッション情報が含まれます。
   */
  public void sessionDidActivate(HttpSessionEvent se) {
    if (logger.isDebugEnabled()) {
      HttpSession session = se.getSession();
      logger.debug("SESSIONID#{} sessionDidActivate : {}", session.getId(), se.getSource());
    }

  }

  /**
   * セッションに属性が追加された際に呼び出されるメソッドです。
   * <p>
   * このメソッドは、セッションにおける属性追加イベントをデバッグログに記録します。
   *
   * @param se 属性追加イベント情報を格納した HttpSessionBindingEvent オブジェクト -
   * イベントがトリガーされたセッション情報や、追加された属性名および値が含まれます。
   */
  public void attributeAdded(HttpSessionBindingEvent se) {
    if (logger.isDebugEnabled()) {
      HttpSession session = se.getSession();
      logger.debug("SESSIONID#{} attributeAdded : {}={}", session.getId(), se.getName(),
          se.getValue());
    }

  }

  /**
   * セッションから属性が削除された際に呼び出されるメソッドです。
   * <p>
   * このメソッドは、セッションにおける属性削除イベントをデバッグログに記録します。
   *
   * @param se 属性削除イベント情報を格納した HttpSessionBindingEvent オブジェクト -
   * イベントがトリガーされたセッション情報や、削除された属性名および値が含まれます。
   */
  public void attributeRemoved(HttpSessionBindingEvent se) {
    if (logger.isDebugEnabled()) {
      HttpSession session = se.getSession();
      logger.debug("SESSIONID#{} attributeRemoved : {}={}", session.getId(), se.getName(),
          se.getValue());
    }

  }

  /**
   * セッション属性が置き換えられた際に呼び出されるメソッドです。
   * <p>
   * このメソッドは、セッションにおける属性置換イベントをトレースログに記録します。
   *
   * @param se 属性置換イベント情報を格納した HttpSessionBindingEvent オブジェクト -
   * イベントがトリガーされたセッション情報や、置き換えられた属性名および値が含まれます。
   */
  public void attributeReplaced(HttpSessionBindingEvent se) {
    if (logger.isTraceEnabled()) {
      HttpSession session = se.getSession();
      logger.trace("SESSIONID#{} attributeReplaced : {}={}", session.getId(), se.getName(),
          se.getValue());
    }

  }

  /**
   * セッションが作成された際に呼び出されるメソッドです。
   * <p>
   * セッションの作成イベントの情報をデバッグログに記録します。
   *
   * @param se セッションイベント情報を格納した HttpSessionEvent オブジェクト
   */
  public void sessionCreated(HttpSessionEvent se) {
    if (logger.isDebugEnabled()) {
      HttpSession session = se.getSession();
      logger.debug("SESSIONID#{} sessionCreated : {}", session.getId(), se.getSource());
    }

  }

  /**
   * セッションが破棄された際に呼び出されるメソッドです。
   * <p>
   * セッションの破棄イベントの情報をデバッグログに記録します。
   *
   * @param se セッションイベント情報を格納した HttpSessionEvent オブジェクト
   */
  public void sessionDestroyed(HttpSessionEvent se) {
    if (logger.isDebugEnabled()) {
      HttpSession session = se.getSession();
      logger.debug("SESSIONID#{} sessionDestroyed : {}", session.getId(), se.getSource());
    }

  }
}
