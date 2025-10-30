package undecided.erp.common.primitive;

import java.nio.ByteBuffer;
import lombok.experimental.UtilityClass;

/**
 * {@code Long} 型の値を処理するユーティリティクラスです。 このクラスでは、長整数値をエンコードやバイト配列への変換といった操作をサポートします。
 *
 * <p>このクラスのメソッドはすべて静的であり、インスタンスを生成せずに使用します。
 */
@UtilityClass
public class Longs {
  /**
   * 指定された {@code longValue} を Base64 エンコード形式の文字列に変換します。
   *
   * @param longValue エンコード対象となる {@code Long} 型の値
   * @return {@code longValue} を Base64 エンコードした文字列
   */
  public static String encodeToBase64(Long longValue) {
    return java.util.Base64.getUrlEncoder().encodeToString(Longs.toByteArray(longValue));
  }

  /**
   * 指定された {@code Long} 型の値を {@code byte[]} 配列に変換します。
   *
   * @param longValue 変換対象となる {@code Long} 型の値
   * @return 指定された値を表現する8バイトの {@code byte[]} 配列
   */
  static byte[] toByteArray(Long longValue) {
    ByteBuffer buffer = ByteBuffer.allocate(Long.BYTES);
    buffer.putLong(longValue);
    return buffer.array();
  }
}
