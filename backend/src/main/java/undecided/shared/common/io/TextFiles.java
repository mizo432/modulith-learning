package undecided.shared.common.io;

import java.io.InputStream;
import lombok.experimental.UtilityClass;

@UtilityClass
public class TextFiles {

  /**
   * UTF-8のバイトオーダーマーク (BOM) をバイト配列として表現します。
   *
   * <p>この定数は、UTF-8でエンコードされたテキストストリームの先頭に 付加される3バイトのシーケンスを含みます。このシーケンスはテキスト
   * ファイルのエンコーディングを示すためのマーカーとして使用されます。 ただし、RFC 3629によるとUTF-8のファイルでは使用は任意であり、
   * 推奨されていません。具体的なバイト値は以下の通りです: - 0xEF - 0xBB - 0xBF
   */
  private static final Byte[] UTF8_BOM = {(byte) 0xEF, (byte) 0xBB, (byte) 0xBF};

  public static boolean existsUtf8Bom(InputStream inputStream) {
    return inputStream.markSupported();
  }
}
