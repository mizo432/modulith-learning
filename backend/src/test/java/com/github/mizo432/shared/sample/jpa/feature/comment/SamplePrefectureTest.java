package com.github.mizo432.shared.sample.jpa.feature.comment;

import static org.assertj.core.api.Assertions.assertThat;

import jakarta.persistence.Table;
import java.lang.reflect.Field;
import java.util.Arrays;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

@Tag("small")
@DisplayName("SamplePrefectureの単体テスト")
class SamplePrefectureTest {

  @Nested
  @DisplayName("コンストラクタのテスト")
  class ConstructorTest {

    @Test
    @DisplayName("3引数コンストラクタでエンティティが作成される")
    void shouldCreateEntityWithThreeArguments() {
      // Arrange
      Long id = 1L;
      String prefectureCode = "13";
      String prefectureName = "東京都";

      // Act
      SamplePrefecture prefecture = new SamplePrefecture(id, prefectureCode, prefectureName);

      // Assert
      assertThat(prefecture).isNotNull();
      assertThat(prefecture.getId()).isEqualTo(id);
      assertThat(prefecture.getPrefectureCode()).isEqualTo(prefectureCode);
      assertThat(prefecture.getPrefectureName()).isEqualTo(prefectureName);
      assertThat(prefecture.getRegion()).isNull();
    }

    @Test
    @DisplayName("4引数コンストラクタでエンティティが作成される")
    void shouldCreateEntityWithFourArguments() {
      // Arrange
      Long id = 1L;
      String prefectureCode = "13";
      String prefectureName = "東京都";
      String region = "関東";

      // Act
      SamplePrefecture prefecture =
          new SamplePrefecture(id, prefectureCode, prefectureName, region);

      // Assert
      assertThat(prefecture).isNotNull();
      assertThat(prefecture.getId()).isEqualTo(id);
      assertThat(prefecture.getPrefectureCode()).isEqualTo(prefectureCode);
      assertThat(prefecture.getPrefectureName()).isEqualTo(prefectureName);
      assertThat(prefecture.getRegion()).isEqualTo(region);
    }
  }

  @Nested
  @DisplayName("getter/setterのテスト")
  class GetterSetterTest {

    @Test
    @DisplayName("都道府県名が設定・取得できる")
    void shouldSetAndGetPrefectureName() {
      // Arrange
      SamplePrefecture prefecture = new SamplePrefecture(1L, "13", "東京都");

      // Act
      prefecture.setPrefectureName("大阪府");

      // Assert
      assertThat(prefecture.getPrefectureName()).isEqualTo("大阪府");
    }

    @Test
    @DisplayName("地方区分が設定・取得できる")
    void shouldSetAndGetRegion() {
      // Arrange
      SamplePrefecture prefecture = new SamplePrefecture(1L, "13", "東京都");

      // Act
      prefecture.setRegion("関西");

      // Assert
      assertThat(prefecture.getRegion()).isEqualTo("関西");
    }
  }

  @Nested
  @DisplayName("テーブルコメントのテスト")
  class TableCommentTest {

    @Test
    @DisplayName("@Table.commentが設定されている")
    void shouldHaveTableComment() {
      // Arrange & Act
      Table table = SamplePrefecture.class.getAnnotation(Table.class);

      // Assert
      assertThat(table).isNotNull();
      assertThat(table.comment()).isEqualTo("都道府県;対照表");
      assertThat(table.name()).isEqualTo("sample_prefectures");
      assertThat(table.schema()).isEqualTo("undecided_ef");
    }
  }

  @Nested
  @DisplayName("カラムコメントのテスト")
  class ColumnCommentTest {

    @Test
    @DisplayName("全カラムにcommentが設定されている")
    void shouldHaveColumnComments() throws NoSuchFieldException {
      // Arrange
      SamplePrefecture prefecture = new SamplePrefecture(1L, "13", "東京都");

      // Act & Assert
      assertThat(prefecture).isNotNull();

      // Verify that all fields have @Column annotations with comments
      Field[] fields = SamplePrefecture.class.getDeclaredFields();
      long fieldsWithComment =
          Arrays.stream(fields)
              .filter(f -> f.isAnnotationPresent(jakarta.persistence.Column.class))
              .filter(f -> f.getAnnotation(jakarta.persistence.Column.class).comment().length() > 0)
              .count();

      // We expect 4 fields with comments: id, prefectureCode, prefectureName, region
      assertThat(fieldsWithComment).isEqualTo(4);
    }
  }
}
