package undecided.erp.common.io;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mockStatic;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

@DisplayName("Files2のテスト")
class Files2Test {

  @Nested
  @DisplayName("buildDirメソッドのテスト")
  class BuildDirTests {

    @Test
    @DisplayName("正常ケース: buildディレクトリが存在する場合")
    void shouldReturnBuildDirectoryWhenItExists() throws Exception {
      // Given
      String projectDir = System.getProperty(Files2.PROJECT_DIRECTORY_KEY);
      Path buildPath = Paths.get(projectDir, "build");
      Files.createDirectories(buildPath);

      // When
      File result = Files2.buildDir();

      // Then
      assertThat(result).isNotNull();
      assertThat(result).exists();
      assertThat(result.getAbsolutePath()).isEqualTo(buildPath.toFile().getAbsolutePath());

      // Cleanup
      Files.deleteIfExists(buildPath);
    }

    @Test
    @DisplayName("正常ケース: buildディレクトリが存在しない場合")
    void shouldCreateBuildDirectoryWhenItDoesNotExist() throws Exception {
      // Given
      String projectDir = System.getProperty(Files2.PROJECT_DIRECTORY_KEY);
      Path buildPath = Paths.get(projectDir, "build");
      Files.deleteIfExists(buildPath);

      // When
      File result = Files2.buildDir();

      // Then
      assertThat(result).isNotNull();
      assertThat(result).exists();
      assertThat(result.getAbsolutePath()).isEqualTo(buildPath.toFile().getAbsolutePath());

      // Cleanup
      Files.deleteIfExists(buildPath);
    }

    @Test
    @DisplayName("エラーケース: user.dir プロパティがnullの場合")
    void shouldReturnNullWhenUserDirIsNull() {
      try (var mockedSystem = mockStatic(System.class)) {
        // Given
        mockedSystem.when(() -> System.getProperty(Files2.PROJECT_DIRECTORY_KEY)).thenReturn(null);

        // When
        File result = Files2.buildDir();

        // Then
        assertThat(result).isNull();
      }
    }

    @Test
    @DisplayName("エラーケース: user.dir プロパティが空文字の場合")
    void shouldThrowExceptionWhenUserDirIsEmpty() {
      try (var mockedSystem = mockStatic(System.class)) {
        // Given
        mockedSystem.when(() -> System.getProperty(Files2.PROJECT_DIRECTORY_KEY)).thenReturn("");

        // When
        File result = Files2.buildDir();

        // Then
        assertThat(result).isNull();
      }
    }

    @Test
    @DisplayName("例外ケース: ディレクトリ作成中にエラーが発生した場合")
    void shouldReturnNullWhenExceptionOccursDuringDirectoryCreation() {
      try (var mockedSystem = mockStatic(System.class)) {
        // Given
        String projectDir = System.getProperty(Files2.PROJECT_DIRECTORY_KEY);
        mockedSystem.when(() -> System.getProperty(Files2.PROJECT_DIRECTORY_KEY))
            .thenReturn(projectDir);
        Path invalidPath = Paths.get("/invalid_path/build");
        try (var mockedPaths = mockStatic(Paths.class)) {
          mockedPaths.when(() -> Paths.get(projectDir, "build")).thenReturn(invalidPath);

          // When
          File result = Files2.buildDir();

          // Then
          assertThat(result).isNull();
        }
      }
    }
  }

  @Nested
  @DisplayName("tmpDirメソッドのテスト")
  class TmpDirTests {

    @Test
    @DisplayName("正常ケース: tmpディレクトリパスを取得する")
    void shouldReturnTmpDirectory() {
      // Given
      String tmpDirPath = System.getProperty(Files2.JAVA_IO_TMPDIR);

      // When
      File result = Files2.tmpDir();

      // Then
      assertThat(result).isNotNull();
      assertThat(result.exists()).isTrue();
      assertThat(result.getAbsolutePath()).isEqualTo(new File(tmpDirPath).getAbsolutePath());
    }

    @Test
    @DisplayName("エラーケース: JAVA_IO_TMPDIR プロパティがnullの場合")
    void shouldReturnNullWhenTmpDirIsNull() {
      try (var mockedSystem = mockStatic(System.class)) {
        // Given
        mockedSystem.when(() -> System.getProperty(Files2.JAVA_IO_TMPDIR)).thenReturn(null);

        // When
        File result = Files2.tmpDir();

        // Then
        assertThat(result).isNull();
      }
    }

    @Test
    @DisplayName("エラーケース: JAVA_IO_TMPDIR プロパティが空文字の場合")
    void shouldThrowExceptionWhenTmpDirIsEmpty() {
      try (var mockedSystem = mockStatic(System.class)) {
        // Given
        mockedSystem.when(() -> System.getProperty(Files2.JAVA_IO_TMPDIR)).thenReturn("");

        // When
        File result = Files2.tmpDir();

        // Then
        assertThat(result).isNotNull();
        assertThat(result.exists()).isFalse();
      }
    }
  }
}
