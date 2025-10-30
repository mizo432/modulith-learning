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

  /**
   * 指定された Base64 エンコード形式の文字列をデコードし、{@code Long} 型の値に変換します。
   *
   * @param base64String デコード対象となる Base64 エンコード形式の文字列
   * @return デコードされた {@code Long} 型の値
   */
  public static Long decodeFromBase64(String base64String) {
    byte[] decodedBytes = java.util.Base64.getUrlDecoder().decode(base64String);
    ByteBuffer buffer = ByteBuffer.wrap(decodedBytes);
    if (decodedBytes.length != Long.BYTES) {
      throw new IllegalArgumentException(
          "デコードされたバイト配列の長さが不正です。期待値: " + Long.BYTES + "バイト、実際: " + decodedBytes.length + "バイト");
    }
    return buffer.getLong();
  }
}
