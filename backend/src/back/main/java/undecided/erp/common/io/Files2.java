package undecided.erp.common.io;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;

@UtilityClass
@Slf4j
public class Files2 {


  /**
   * Javaランタイム環境によって使用されるデフォルトの一時ディレクトリ用の システムプロパティキーを表します。
   * <p>
   * この文字列の値は、一時ファイルをプログラムの実行中に保存できる ディレクトリパスを取得するために利用できます。
   */
  public static final String JAVA_IO_TMPDIR = "java.io.tmpdir";
  /**
   * Javaランタイム環境の現在の作業ディレクトリを取得するために使用される システムプロパティキーを表します。
   * <p>
   * このキーに関連付けられた値は、プログラムが起動されたディレクトリの 絶対パスを示します。
   * <p>
   * このプロパティは、実行時にプロジェクトのルートディレクトリを特定するために よく使用されます。
   */
  public static final String PROJECT_DIRECTORY_KEY = "user.dir";


  /**
   * 現在のJavaランタイムのデフォルトの一時ディレクトリを取得します。
   *
   * @return システムの一時ディレクトリを表す {@code File} オブジェクト
   */
  public static File tmpDir() {
    return new File(System.getProperty(JAVA_IO_TMPDIR));
  }


  /**
   * プロジェクトのルートディレクトリ内にある「build」ディレクトリへのパスを取得します。
   * <p>
   * ディレクトリが存在しない場合は、作成を試みます。
   *
   * @return 「build」ディレクトリを表す {@code File} オブジェクト。 操作時にエラーが発生した場合は {@code null} を返します。
   */
  public static File buildDir() {
    try {
      // プロジェクトのルートディレクトリを取得
      String projectDir = System.getProperty(PROJECT_DIRECTORY_KEY);
      if (projectDir == null || projectDir.isEmpty()) {
        throw new IllegalStateException("user.dir プロパティが設定されていません");
      }

      // build ディレクトリのパスを生成
      Path buildPath = Paths.get(projectDir, "build");

      // 必要に応じてディレクトリの存在確認や作成
      if (!Files.exists(buildPath)) {
        Files.createDirectories(buildPath);
      }

      return buildPath.toFile();
    } catch (Exception e) {
      // 必要に応じてログを出力
      log.error("Build ディレクトリのパスを取得中にエラーが発生しました", e);
      return null;
    }
  }
}
