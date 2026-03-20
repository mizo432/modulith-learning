package undecided.shared.common.io;

import java.io.File;
import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;

@UtilityClass
@Slf4j
public class Files2 {

  /**
   * Javaランタイム環境によって使用されるデフォルトの一時ディレクトリ用の システムプロパティキーを表します。
   *
   * <p>この文字列の値は、一時ファイルをプログラムの実行中に保存できる ディレクトリパスを取得するために利用できます。
   */
  public static final String JAVA_IO_TMPDIR = "java.io.tmpdir";

  /**
   * 現在のJavaランタイムのデフォルトの一時ディレクトリを取得します。
   *
   * @return システムの一時ディレクトリを表す {@code File} オブジェクト
   */
  public static File tmpDir() {
    return new File(System.getProperty(JAVA_IO_TMPDIR));
  }
}
