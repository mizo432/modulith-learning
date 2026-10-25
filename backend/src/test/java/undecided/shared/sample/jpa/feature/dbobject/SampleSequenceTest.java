package undecided.shared.sample.jpa.feature.dbobject;

import static org.assertj.core.api.Assertions.assertThat;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

@Tag("small")
@DisplayName("SampleSequenceのテスト")
class SampleSequenceTest {

  @Nested
  @DisplayName("コンストラクタのテスト")
  class ConstructorTest {

    @Test
    @DisplayName("有効な引数でSampleSequenceが正常に作成される")
    void shouldCreateSampleSequenceWithValidArgs() {
      // Arrange
      String sequenceName = "seq_sample_order";
      String description = "サンプル注文番号用のシーケンス";

      // Act
      SampleSequence sampleSequence = new SampleSequence(sequenceName, description);

      // Assert
      assertThat(sampleSequence).isNotNull();
      assertThat(sampleSequence.getSequenceName()).isEqualTo(sequenceName);
      assertThat(sampleSequence.getDescription()).isEqualTo(description);
    }

    @Test
    @DisplayName("descriptionがnullでもSampleSequenceが作成される")
    void shouldCreateSampleSequenceWithNullDescription() {
      // Arrange
      String sequenceName = "seq_sample_party";

      // Act
      SampleSequence sampleSequence = new SampleSequence(sequenceName, null);

      // Assert
      assertThat(sampleSequence).isNotNull();
      assertThat(sampleSequence.getSequenceName()).isEqualTo(sequenceName);
      assertThat(sampleSequence.getDescription()).isNull();
    }

    @Test
    @DisplayName("複数のSampleSequenceが異なる値で作成される")
    void shouldCreateMultipleSampleSequences() {
      // Act
      SampleSequence seq1 = new SampleSequence("seq_order", "注文用シーケンス");
      SampleSequence seq2 = new SampleSequence("seq_party", "パーティ用シーケンス");

      // Assert
      assertThat(seq1.getSequenceName()).isNotEqualTo(seq2.getSequenceName());
      assertThat(seq1.getDescription()).isNotEqualTo(seq2.getDescription());
    }
  }

  @Nested
  @DisplayName("アノテーションのテスト")
  class AnnotationTest {

    @Test
    @DisplayName("@Entityアノテーションが付与されている")
    void shouldHaveEntityAnnotation() {
      // Arrange & Act
      Entity entity = SampleSequence.class.getAnnotation(Entity.class);

      // Assert
      assertThat(entity).isNotNull();
    }

    @Test
    @DisplayName("@Table.nameはsample_sequence_demoに設定されている")
    void shouldTableNameBeSet() {
      // Arrange & Act
      Table table = SampleSequence.class.getAnnotation(Table.class);

      // Assert
      assertThat(table).isNotNull();
      assertThat(table.name()).isEqualTo("sample_sequence_demo");
    }

    @Test
    @DisplayName("@SequenceGeneratorアノテーションが付与されている")
    void shouldHaveSequenceGeneratorAnnotation() {
      // Arrange & Act
      SequenceGenerator sequenceGenerator =
          SampleSequence.class.getAnnotation(SequenceGenerator.class);

      // Assert
      assertThat(sequenceGenerator).isNotNull();
      assertThat(sequenceGenerator.name()).isEqualTo("sampleOrderSeq");
      assertThat(sequenceGenerator.sequenceName()).isEqualTo("seq_sample_order");
      assertThat(sequenceGenerator.schema()).isEqualTo("undecided_as");
      assertThat(sequenceGenerator.allocationSize()).isEqualTo(1);
    }

    @Test
    @DisplayName("@GeneratedValueアノテーションがidフィールドに付与されている")
    void shouldHaveGeneratedValueAnnotation() {
      // Arrange & Act
      GeneratedValue generatedValue =
          SampleSequence.class.getDeclaredField("id").getAnnotation(GeneratedValue.class);

      // Assert
      assertThat(generatedValue).isNotNull();
    }
  }

  @Nested
  @DisplayName("セッターのテスト")
  class SetterTest {

    @Test
    @DisplayName("idが設定できる")
    void shouldSetId() {
      // Arrange
      SampleSequence sampleSequence = new SampleSequence("seq_test", "テスト");

      // Act
      sampleSequence.setId(1L);

      // Assert
      assertThat(sampleSequence.getId()).isEqualTo(1L);
    }

    @Test
    @DisplayName("sequenceNameが設定できる")
    void shouldSetSequenceName() {
      // Arrange
      SampleSequence sampleSequence = new SampleSequence("seq_test", "テスト");

      // Act
      sampleSequence.setSequenceName("seq_new");

      // Assert
      assertThat(sampleSequence.getSequenceName()).isEqualTo("seq_new");
    }

    @Test
    @DisplayName("descriptionが設定できる")
    void shouldSetDescription() {
      // Arrange
      SampleSequence sampleSequence = new SampleSequence("seq_test", "テスト");

      // Act
      sampleSequence.setDescription("新しい説明");

      // Assert
      assertThat(sampleSequence.getDescription()).isEqualTo("新しい説明");
    }
  }
}
